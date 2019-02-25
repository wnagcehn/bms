package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.consumer.common.BmsMaterialImportTask;
import com.jiuyescm.bms.correct.service.IBmsProductsMaterialService;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("bmsPackmaterialImportWmsListener")
public class BmsPackmaterialImportWmsListener implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsPackmaterialImportWmsListener.class);
	
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private StorageClient storageClient;
	@Autowired private BmsMaterialImportTask bmsMaterialImportTaskCommon;
	@Autowired private IBizOutstockPackmaterialTempService bizOutstockPackmaterialTempService;
	@Autowired private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Autowired private IBmsProductsMaterialService bmsProductsMaterialService;
	
	
	private static final String REMARK = "导入数据不规范,请下载查看最后一列说明";
	private Map<String,WarehouseVo> wareHouseMap = null;
	private Map<String,CustomerVo> customerMap = null;
	private Map<String,PubMaterialInfoVo> materialMap = null;
	
	private List<String> readColumnNames = null;
	private List<Map<String,String>> materialGroup = null;
	private List<String> materialCommonList = null;
	
	private Map<Integer, String> errMap = null;
	private Map<Integer, String> errorMap = null;
	private Map<String,Integer> repeatMap = null;
	
	BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
	
	private String taskId;
	private int batchNum = 1000;

	TreeMap<Integer,String> originColumn = new TreeMap<Integer,String>(); //源生表头信息
	List<BizOutstockPackmaterialTempEntity> newList = new ArrayList<BizOutstockPackmaterialTempEntity>();
	
	List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	private int roNo = 1;
	
	//----------初始化基础数据
	public void initKeyValue(){
		wareHouseMap = bmsMaterialImportTaskCommon.queryAllWarehouse();
		customerMap  = bmsMaterialImportTaskCommon.queryAllCustomer();
		materialMap  = bmsMaterialImportTaskCommon.queryAllMaterial();
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			initKeyValue();
		} catch (Exception e) {
			logger.info("任务ID【{}】 -> 初始化仓库,商家,耗材数据异常",taskId);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 10, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
			
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("任务ID【{}】 -> 读取消息异常",taskId, e1);
			return;
		}
		
		try {
			handImportFile();		// 处理导入文件
		} catch (Exception e1) {
			logger.error("任务ID【{}】 -> 异步文件处理异常",taskId, e1);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 0, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.error("任务ID【{}】 -> 消息应答异常",taskId, e);
		}
		errMap = null;
		errorMap = null;
		logger.info("任务ID【{}】 -> MQ处理操作日志结束,耗时【{}】",taskId,end-start);
	}
	
	/**
	 * 异步处理Excel文件
	 * @param taskId 任务编号
	 */
	private void handImportFile() throws Exception {
		
		errMap = new HashMap<Integer, String>();
		errorMap = new HashMap<Integer, String>();
		repeatMap = new HashMap<String, Integer>();
		
		bmsMaterialImportTaskCommon.setTaskStatus(taskId, 0, FileAsynTaskStatusEnum.PROCESS.getCode());
		
		//----------查询任务
		taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
		logger.info("任务ID【{}】 -> 领取任务",taskId);
		if (null == taskEntity) {
			logger.error("任务ID【{}】 -> 任务不存在",taskId);
			return;
		}
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 5);
		
		//初始化哪些需要读取的列
		initColumnNames();
		logger.info(readColumnNames.size()+"");
		
		//----------读取excel
		logger.info("任务ID【{}】 -> 准备读取excel",taskId);
		
		XlsxWorkBook reader = null;
		long start = System.currentTimeMillis();
		byte[] bytes = storageClient.downloadFile(taskEntity.getOriginFilePath(), new DownloadByteArray());
		logger.info("任务ID【{}】 -> byte长度【{}】",taskId,bytes.length);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		try{	
			reader = new XlsxWorkBook(inputStream);
			Sheet sheet = reader.getSheets().get(0);
			//更新文件总行数
			BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 30,null, sheet.getRowCount(), null, null, null, null);
			bmsFileAsynTaskService.update(updateEntity);
			reader.readSheet(sheet.getSheetId(), new SheetReadCallBack() {

				@Override
				public void readTitle(List<String> columns) {
					//源生表头
					int a = 1;
					for (String column : columns) {
						originColumn.put(a, column);
						a++;
					}
					
					//----------校验表头
					logger.info("任务ID【{}】 -> 校验表头...",taskId);
					String[] str = {"出库日期", "仓库", "商家", "出库单号", "运单号"}; //必填列
					if(!checkTitle(columns,str)){
						logger.info("任务ID【{}】 -> 模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号",taskId);
						bmsMaterialImportTaskCommon.setTaskStatus(taskId, 38, FileAsynTaskStatusEnum.FAIL.getCode(), "模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号");
						return;
					}
					logger.info("任务ID【{}】 -> 表头校验完成，准备读取Excel内容……",taskId); 
					bmsMaterialImportTaskCommon.setTaskProcess(taskId, 50);
				}

				@Override
				public void read(DataRow dr) {
					//行错误信息
					String errorMsg="";					
					
					//组装往临时表存的数据，校验出错捕获加入errList
					List<BizOutstockPackmaterialTempEntity> tempList = null;
					try {
						tempList = loadTemp(dr, errorMsg);
					} catch (Exception e) {
						errorMap.put(dr.getRowNo(), e.getMessage());
						errMap.clear();
					}

					//组装好的数据存入全局List中
					if (null == tempList) {
						tempList = new ArrayList<BizOutstockPackmaterialTempEntity>();
					}
					for( int i = 0 ; i < tempList.size() ; i++){
						newList.add(tempList.get(i));
					}
					
					//1000条数据	
					if (newList.size() >= batchNum) {
						if(errMap.size()==0){
							int result = saveTo();
							if(result < 0){
								logger.error("任务ID【{}】 ->,保存到临时表失败", taskId);
							}
						}
					}
					return;
				}

				@Override
				public void finish() {	
					repeatMap.clear();
					bmsMaterialImportTaskCommon.setTaskProcess(taskId, 70);
					//保存数据到临时表
					if (errMap.size() == 0) {
						int result = saveTo();
						if (result < 0) {
							logger.error("任务ID【{}】 ->,保存到临时表失败", taskId);
						}
					}	
				}

				@Override
				public void error(Exception ex) {
					// TODO Auto-generated method stub
					
				}
			});
			
			//更新文件读取行数
			if(sheet.getRowCount()<=0){
				logger.info("未从excel读取到任何数据");
				bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.FAIL.getCode(),"未从excel读取到任何数据");
				return;
			}
			logger.info("excel读取完成，读取行数【"+sheet.getRowCount()+"】");
			BmsFileAsynTaskVo updateEntity2 = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 72,null, sheet.getRowCount(), null, null, null, null);
			bmsFileAsynTaskService.update(updateEntity2);
		
			long end = System.currentTimeMillis();
			logger.info("任务ID【{}】 -> excel读取完成，读取excel,耗时【{}】",taskId,end-start);
		}
		catch(Exception ex){
			logger.error("任务ID【{}】 -> excel解析异常{}",taskId,ex);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}/*finally {
			reader.close();
		}*/
		
		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errorMap.size()>0){
			logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
			try {
				exportResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		//数据库层面重复校验  false - 校验不通过 存在重复  原则上 同一运单号，同一耗材，只有一条
		if(!dbCheck()){
			try {
				exportResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errorMap.size()>0){
			logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
			try {
				exportResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 80);	
		logger.info("************ OK **********");
		
		//保存数据到正式表
		try{
			logger.info("任务ID【{}】 -> 保存数据到正式表",taskId);
			int k=bizOutstockPackmaterialService.saveDataFromTemp(taskId);
			if(k>0){
				logger.info("任务ID【{}】 -> 保存数据到正式表成功 耗时【{}】",taskId,System.currentTimeMillis()-start);
				bmsMaterialImportTaskCommon.setTaskProcess(taskId, 90);
				// 耗材打标
				Map<String,Object> condition = Maps.newHashMap();
				condition.put("batchNum", taskId);
				condition.put("taskId", taskId);
				logger.info("任务ID【{}】 -> 进行耗材打标操作",taskId);
				start = System.currentTimeMillis();
				bmsProductsMaterialService.markMaterial(condition);
				//bizOutstockPackmaterialTempService.deleteBybatchNum(taskId);
				BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 100,FileAsynTaskStatusEnum.SUCCESS.getCode(), null, JAppContext.currentTimestamp(), null, null, "导入成功");
				bmsFileAsynTaskService.update(updateEntity);
				logger.info("任务ID【{}】 -> 耗材打标成功,耗时【{}】",taskId,System.currentTimeMillis()-start);
			}else{
				logger.error("任务ID【{}】 -> 未从临时表中保存数据到业务表",taskId);
				bmsMaterialImportTaskCommon.setTaskStatus(taskId,99, FileAsynTaskStatusEnum.FAIL.getCode(),"未从临时表中保存数据到业务表，批次号【"+taskId+"】,任务编号【"+taskId+"】");
				bizOutstockPackmaterialTempService.deleteBybatchNum(taskId);
			}
//			errList.clear();
//			mapData.clear();
//			allList.clear();
			newList.clear();
		}catch(Exception e){
			logger.error("任务ID【{}】 -> 异步导入异常{}",taskId,e);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId,99, FileAsynTaskStatusEnum.EXCEPTION.getCode(),"从临时表中保存数据到业务表异常");
			bizOutstockPackmaterialTempService.deleteBybatchNum(taskId);
		}
		return;	
	}

	
	/**
	 * 初始化列头数据
	 */
	private void initColumnNames(){
		
		readColumnNames = new ArrayList<String>();
		materialCommonList = new ArrayList<String>();
		materialGroup = new ArrayList<Map<String,String>>();
		
		materialCommonList.add("出库日期");
		materialCommonList.add("仓库");
		materialCommonList.add("商家");
		materialCommonList.add("出库单号");
		materialCommonList.add("运单号");
		
		Map<String,String> groupList1 = Maps.newLinkedHashMap();
		groupList1.put("冰袋","code");
		groupList1.put("冰袋数量","num");
		materialGroup.add(groupList1);
		
		Map<String,String> groupList2 = Maps.newLinkedHashMap();
		groupList2.put("纸箱","code");
		groupList2.put("纸箱数量","num");
		materialGroup.add(groupList2);
		
		Map<String,String> groupList3 = Maps.newLinkedHashMap();
		groupList3.put("泡沫箱","code");
		groupList3.put("泡沫箱数量","num");
		materialGroup.add(groupList3);
		
		Map<String,String> groupList4 = Maps.newLinkedHashMap();
		groupList4.put("干冰","code");
		groupList4.put("干冰重量","weight");
		materialGroup.add(groupList4);
		
		Map<String,String> groupList5 = Maps.newLinkedHashMap();
		groupList5.put("快递袋","code");
		groupList5.put("快递袋数量","num");
		materialGroup.add(groupList5);
		
		Map<String,String> groupList6 = Maps.newLinkedHashMap();
		groupList6.put("面单","code");
		groupList6.put("面单数量","num");
		materialGroup.add(groupList6);

		Map<String,String> groupList7 = Maps.newLinkedHashMap();
		groupList7.put("标签纸","code");
		groupList7.put("标签纸数量","num");
		materialGroup.add(groupList7);

		Map<String,String> groupList8 = Maps.newLinkedHashMap();
		groupList8.put("防水袋","code");
		groupList8.put("防水袋数量","num");
		materialGroup.add(groupList8);

		Map<String,String> groupList9 = Maps.newLinkedHashMap();
		groupList9.put("缓冲材料","code");
		groupList9.put("缓冲材料数量","num");
		materialGroup.add(groupList9);

		Map<String,String> groupList10 = Maps.newLinkedHashMap();
		groupList10.put("胶带","code");
		groupList10.put("胶带数量","num");
		materialGroup.add(groupList10);
		
		Map<String,String> groupList11 = Maps.newLinkedHashMap();
		groupList11.put("问候卡","code");
		groupList11.put("问候卡数量","num");
		materialGroup.add(groupList11);
		
		Map<String,String> groupList12 = Maps.newLinkedHashMap();
		groupList12.put("好字贴","code");
		groupList12.put("好字贴数量","num");
		materialGroup.add(groupList12);
		
		Map<String,String> groupList13 = Maps.newLinkedHashMap();
		groupList13.put("其他","code");
		groupList13.put("其他数量","num");
		materialGroup.add(groupList13);
		
		Map<String,String> groupList14 = Maps.newLinkedHashMap();
		groupList14.put("塑封袋","code");
		groupList14.put("塑封袋数量","num");
		materialGroup.add(groupList14);
		
		Map<String,String> groupList15 = Maps.newLinkedHashMap();
		groupList15.put("保温袋","code");
		groupList15.put("保温袋数量","num");
		materialGroup.add(groupList15);
		
		Map<String,String> groupList16 = Maps.newLinkedHashMap();
		groupList16.put("保鲜袋","code");
		groupList16.put("保鲜袋数量","num");
		materialGroup.add(groupList16);
		
		for (String str : materialCommonList) {
			readColumnNames.add(str);
		}
		for (Map<String,String> map : materialGroup) {
			for (Map.Entry<String, String> entry : map.entrySet()) { 
				readColumnNames.add(entry.getKey());
			}
		}
		
	}
	

	/**
	 * 产生临时表数据
	 * @param contents 数据集
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private List<BizOutstockPackmaterialTempEntity> loadTemp(DataRow dr, String errorMsg) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BizOutstockPackmaterialTempEntity tempEntity = null;
		List<BizOutstockPackmaterialTempEntity> tempList = new ArrayList<BizOutstockPackmaterialTempEntity>();
		//本行是否拥有耗材
		boolean isHaveMaterial = false;
		tempEntity = new BizOutstockPackmaterialTempEntity();
		tempEntity.setRowExcelNo(dr.getRowNo());
		try {
			for (DataColumn dc : dr.getColumns()) {
				switch (dc.getTitleName()) {
				case "出库日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}else {
						errorMsg += "出库日期是必填项;";
					}
					break;
				case "仓库":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setWarehouseName(dc.getColValue());
						//如果没找到，报错
						if (wareHouseMap.containsKey(dc.getColValue())) {
							tempEntity.setWarehouseCode(wareHouseMap.get(dc.getColValue()).getWarehouseid());
						}else {
							errorMsg+="仓库不存在;";
						}
					}else {
						errorMsg+="仓库是必填项;";
					}
					break;
				case "商家":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setCustomerName(dc.getColValue());
						//如果没找到，报错
						if (customerMap.containsKey(dc.getColValue())) {
							tempEntity.setCustomerId(customerMap.get(dc.getColValue()).getCustomerid());
						}else {
							errorMsg+="商家不存在;";
						}
					}else {
						errorMsg+="商家是必填项;";
					}
					break;
				case "出库单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setOutstockNo(dc.getColValue());
					}else {
						errorMsg+="出库单号是必填项;";
					}
					break;
				case "运单号":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setWaybillNo(dc.getColValue());
					}else {
						errorMsg+="运单号是必填项;";
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			errorMsg+="第【"+ dr.getRowNo() +"】行格式不正确;";
		}

			
		//****************************************************************** 遍历耗材
		for (Map<String,String> map : materialGroup) {
			String codeName = "";
			String numName = "";
			String numType = "";
			for (Map.Entry<String, String> colgroup : map.entrySet()) {
				if("code".equals(colgroup.getValue())){
					codeName = colgroup.getKey();
				}
				else{
					numName = colgroup.getKey();
					numType = colgroup.getValue();
				}
			}

			BizOutstockPackmaterialTempEntity newTempEntity = new BizOutstockPackmaterialTempEntity();
			PropertyUtils.copyProperties(newTempEntity, tempEntity);
			BigDecimal num = null;
			String num0 = "";
			String materialName = "";
			for (DataColumn dc : dr.getColumns()) {
				try {
					if (dc.getTitleName().equals(codeName)) {
						materialName = dc.getTitleName();
						if (StringUtils.isNotBlank(dc.getColValue())) {
							newTempEntity.setConsumerMaterialCode(dc.getColValue().trim());
						}
						
					}else if (dc.getTitleName().equals(numName) && numType.equals("num")) {
						//数量
						num0 = dc.getColValue();
						if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
							break;
						}else if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
							errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
							break;
						}else if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isNotBlank(newTempEntity.getConsumerMaterialCode())) {
							errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
							break;
						}else {
							isHaveMaterial = true;
							//校验数量
							num0 = dc.getColValue();
							if(!StringUtil.isNumeric(dc.getColValue())){
								errorMsg += "【"+num0+"】不是数字;";
							}
							else{
								if(num0.contains("-") || "0".equals(num0)){
									errorMsg += "【"+dc.getTitleName()+"】必须>0";
								}
							}
							
							//校验耗材Code
							if(!materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
								errorMsg += "耗材【"+newTempEntity.getConsumerMaterialCode()+"】不存在;";
							}else if (!materialName.equals(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialType())) {
								errorMsg += materialName+"类型下无耗材【"+ newTempEntity.getConsumerMaterialCode() +"】;";
							}
							if(errorMsg.length()>0){
								break;
							}
							
							newTempEntity.setConsumerMaterialName(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialName());
							if(materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
								PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(newTempEntity.getConsumerMaterialCode());
								newTempEntity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
					  			+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
							}
							String key = newTempEntity.getWaybillNo() + newTempEntity.getConsumerMaterialCode();
							if(repeatMap.containsKey(key)){
								errorMsg += "数据重复--第【"+repeatMap.get(key)+"】行已存在运单【"+newTempEntity.getWaybillNo()+"】和耗材【"+newTempEntity.getConsumerMaterialCode()+"】的组合;";
							}
							else{
								repeatMap.put(key, dr.getRowNo());
							}
							
							if(errorMsg.length()>0){
								break;
							}else {
								num = new BigDecimal(num0);
								newTempEntity.setNum(num);
								newTempEntity.setAdjustNum(num);
							}
						}	
					}else if (dc.getTitleName().equals(numName) && numType.equals("weight")) {
						num0 = dc.getColValue();
						if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
							break;
						}else if (StringUtils.isNotBlank(dc.getColValue()) && StringUtils.isBlank(newTempEntity.getConsumerMaterialCode())) {
							errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
							break;
						}else if (StringUtils.isBlank(dc.getColValue()) && StringUtils.isNotBlank(newTempEntity.getConsumerMaterialCode())) {
							errorMsg += "【"+dc.getTitleName()+"】和【"+materialName+"】不能只存在一个有值;";
							break;
						}else {
							isHaveMaterial = true;
							//校验重量
							num0 = dc.getColValue();
							if(!StringUtil.isNumeric(dc.getColValue())){
								errorMsg += "【"+num0+"】不是数字;";
							}
							else{
								if(num0.contains("-") || "0".equals(num0)){
									errorMsg += "【"+dc.getTitleName()+"】必须>0";
								}
							}
							
							//校验耗材Code
							if(!materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
								errorMsg += "耗材【"+newTempEntity.getConsumerMaterialCode()+"】不存在;";
							}else if (!materialName.equals(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialType())) {
								errorMsg += materialName+"类型下无耗材【"+ newTempEntity.getConsumerMaterialCode() +"】;";
							}
							if(errorMsg.length()>0){
								break;
							}
							
							newTempEntity.setConsumerMaterialName(materialMap.get(newTempEntity.getConsumerMaterialCode()).getMaterialName());
							if(materialMap.containsKey(newTempEntity.getConsumerMaterialCode())){
								PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(newTempEntity.getConsumerMaterialCode());
								newTempEntity.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
					  			+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
							}
							String key = newTempEntity.getWaybillNo() + newTempEntity.getConsumerMaterialCode();
							if(repeatMap.containsKey(key)){
								errorMsg += "数据重复--第【"+repeatMap.get(key)+"】行已存在运单【"+newTempEntity.getWaybillNo()+"】和耗材【"+newTempEntity.getConsumerMaterialCode()+"】的组合;";
							}
							else{
								repeatMap.put(key, dr.getRowNo());
							}
							
							if(errorMsg.length()>0){
								break;
							}else {
								num = new BigDecimal(num0);
								newTempEntity.setWeight(num);
							}
						}		
					}					
				} catch (Exception e) {
					errorMsg+="列【"+ dc.getTitleName() + "】格式不正确;";
				}		
			}
			
			if (errorMsg.length() == 0 && StringUtils.isNotBlank(newTempEntity.getConsumerMaterialCode())) {
				newTempEntity.setRowExcelName(codeName);
				newTempEntity.setCreator(taskEntity.getCreator());
				newTempEntity.setDelFlag("0");
				newTempEntity.setExtattr5("origin");
				newTempEntity.setWriteTime(JAppContext.currentTimestamp());
				newTempEntity.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
				newTempEntity.setBatchNum(taskEntity.getTaskId());
				tempList.add(newTempEntity);
			}
			
		}
		
		
		int rowNo = dr.getRowNo();
		if(!StringUtil.isEmpty(errorMsg)){
			if(errMap.containsKey(rowNo)){
				errMap.put(rowNo, errMap.get(rowNo)+errorMsg);
			}
			else{
				errMap.put(rowNo, errorMsg);
			}
		}
		if(!isHaveMaterial){
			if(errMap.containsKey(rowNo)){
				errMap.put(rowNo, errMap.get(rowNo)+"本行未录入任何耗材");
			}
			else{
				errMap.put(rowNo, "本行未录入任何耗材");
			}
		}
				
		if (errMap.size() > 0) {
			throw new BizException(errMap.get(dr.getRowNo()));
		}
		return tempList;
	}
	
	/**
	 * 生成结果文件
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void exportResultFile(XlsxWorkBook reader) throws ParseException, IOException{
		bizOutstockPackmaterialTempService.deleteBybatchNum(taskEntity.getTaskId());
		if(!StringUtil.isEmpty(taskEntity.getResultFilePath())){
			logger.info("删除历史结果文件");
			boolean resultF = storageClient.deleteFile(taskEntity.getResultFilePath());
			if(resultF){
				logger.info("删除历史结果文件-成功");
			}
			else{
				logger.info("删除历史结果文件-失败");
			}
		}
		
		List<String> exportColumns = new ArrayList<String>();
		
		for (Map.Entry<Integer, String> map : originColumn.entrySet()) {
			exportColumns.add(map.getValue());
		}
		
		final POISXSSUtil poiUtil = new POISXSSUtil();
    	final SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
    	final List<Map<String, Object>> headDetailMapList = getBizHead(exportColumns); 
    	
    	//重新读取Excel，生成结果文件
    	logger.info("重新读取Excel--->生成结果文件");
    	try {
    		Sheet sheet = reader.getSheets().get(0);
			reader.readSheet(sheet.getSheetId(), new SheetReadCallBack() {

				@Override
				public void readTitle(List<String> columns) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void read(DataRow dr) {
					SimpleDateFormat format =  new SimpleDateFormat("yyyy/MM/dd");
			        Map<String, Object> dataItem = new HashMap<String, Object>();
					for (DataColumn dc : dr.getColumns()) {
						if ("出库日期".equals(dc.getTitleName())) {
							try {
								if (StringUtils.isBlank(dc.getColValue())) {
									continue;
								}
								dataItem.put(dc.getTitleName(), format.format(DateUtil.transStringToTimeStamp(dc.getColValue())));
							} catch (Exception e) {
								logger.error("日期格式异常！");
								errorMap.put(dr.getRowNo(), "日期格式异常！");
							}			
						}else {
							dataItem.put(dc.getTitleName(), dc.getColValue());
						}
					}
					if (errorMap.containsKey(dr.getRowNo())) {
		        		dataItem.put("备注", errorMap.get(dr.getRowNo()));
					}
		        	dataList.add(dataItem);
		        	
		        	//1000条写入一次
		        	if (dataList.size() >= 1) {
		        		try {
		        			poiUtil.exportExcel2FilePath(poiUtil, workbook, "耗材出库结果文件",roNo, headDetailMapList, dataList);
		        			roNo = roNo + dataList.size();
						} catch (IOException e) {
							logger.error("写入结果文件失败！", e);
						}
		        		dataList.clear();
					}
				}

				@Override
				public void finish() {
					try {
						poiUtil.exportExcel2FilePath(poiUtil, workbook, "耗材出库结果文件",roNo, headDetailMapList, dataList);
						roNo = 1;
					} catch (IOException e) {
						logger.error("写入结果文件失败！", e);
					}
	        		dataList.clear();
	        		errorMap.clear();
				}

				@Override
				public void error(Exception ex) {
					// TODO Auto-generated method stub
					
				}
				
			});
		} catch (Exception e) {
			logger.error("任务ID【{}】 -> 第二次excel解析异常{}",taskId, e);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 99, FileAsynTaskStatusEnum.EXCEPTION.getCode());
		}finally {
			reader.close();
		}
    	
    	logger.info("上传结果文件到fastDfs");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		workbook.dispose();
		byte[] b1 = os.toByteArray();
		StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
		String resultFullPath = resultStorePath.getFullPath();
	    logger.info("上传结果文件到FastDfs - 成功");
	    BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 99,FileAsynTaskStatusEnum.FAIL.getCode(), null, JAppContext.currentTimestamp(), taskEntity.getOriginFileName(), resultFullPath, REMARK);
		bmsFileAsynTaskService.update(updateEntity);
	}

	private List<Map<String, Object>> getBizHead(List<String> exportColumns){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = null;
		for (String colName : exportColumns) {
			itemMap = new HashMap<String, Object>();
			itemMap.put("title", colName);
			itemMap.put("columnWidth", 25);
			itemMap.put("dataKey", colName);
			headInfoList.add(itemMap);
		}
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "备注");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "备注");
		headInfoList.add(itemMap);
		return headInfoList;
	}
	
	/**
	 * 数据库重复性校验
	 * @return
	 */
	private boolean dbCheck(){
		
		
		List<BizOutstockPackmaterialTempEntity> list = bizOutstockPackmaterialTempService.queryContainsList(taskEntity.getTaskId());
			if(null == list || list.size() <= 0){
				return true;
			}
			Map<String,String> map=Maps.newLinkedHashMap();
			
			//一共几行,去重
			Set<Integer> rowNos = new TreeSet<>();
			for(BizOutstockPackmaterialTempEntity entity:list){
				rowNos.add(entity.getRowExcelNo());
			}
			
			//存在异常的dataRow
//			for (Integer rowNo : rowNos) {
//				errList.add(mapData.get(rowNo));
//			}
			
			//存在重复记录
			for(BizOutstockPackmaterialTempEntity entity:list){
				String row=String.valueOf(entity.getRowExcelNo());
				String mes="";
				if(map.containsKey(row)){
					mes=map.get(row);
					mes+=","+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
					map.put(row,mes);
				}else{
					mes="系统中已存在运单号【" + entity.getWaybillNo() + "】,"+entity.getRowExcelName()+"【"+entity.getConsumerMaterialCode()+"】";
					map.put(row,mes);
				}
			}
			Set<String> set=map.keySet();
			for(String key:set){
				Integer rowNum=Integer.valueOf(key);
				if(errorMap.containsKey(rowNum)){
					errorMap.put(rowNum, errorMap.get(rowNum)+","+map.get(key));
				}else{
					errorMap.put(rowNum, map.get(key));
				}
			}
			return false;
	}
	
	public boolean checkTitle(List<String> headColumn, String[] str) {
		if(headColumn == null){
			return false;
		}
		if(headColumn.size() < str.length){
			 return false;
		}
		for (String s : str) {
			if(!headColumn.contains(s)){
				return false;
			}
		} 
		return true;
	}
	
	private int saveTo(){
		int k = 0;
		logger.info("任务ID【{}】 -> 保存数据到临时表 转化成对象数【{}】",taskId,newList.size());
		try {
			//保存到临时表
			bizOutstockPackmaterialTempService.saveBatch(newList); 
		} catch (Exception e) {
			logger.info("任务ID【{}】 -> 写入临时表-失败:【{}】",taskId,e);
			k = -1;
		}

		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 75);
		newList.clear();
		return k;
    }
	
}
