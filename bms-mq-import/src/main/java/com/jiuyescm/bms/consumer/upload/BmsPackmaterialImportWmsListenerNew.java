package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

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
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.excel.util.Excel07Reader;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("bmsPackmaterialImportWmsListenerNew")
public class BmsPackmaterialImportWmsListenerNew implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsPackmaterialImportWmsListenerNew.class);
	
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private StorageClient storageClient;
	@Autowired private BmsMaterialImportTask bmsMaterialImportTaskCommon;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private ICustomerService customerService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
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
	private Map<String,Integer> repeatMap = null;
	Excel07Reader reader = new Excel07Reader();
	
	BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
	
	
	@Override
	public void onMessage(Message message) {
		
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		String taskId = null;
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("任务ID【{}】 -> 读取消息异常",taskId, e1);
			return;
		}
		
		try {
			handImportFile(taskId);		// 处理导入文件
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
		reader = null;
		errMap = null;
		logger.info("任务ID【{}】 -> MQ处理操作日志结束,耗时【{}】",taskId,end-start);
	}
	
	/**
	 * 异步处理Excel文件
	 * @param taskId 任务编号
	 */
	private void handImportFile(String taskId) throws Exception {
		
		errMap = new HashMap<Integer, String>();
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
		
		try{
			long start = System.currentTimeMillis();
			byte[] bytes = storageClient.downloadFile(taskEntity.getOriginFilePath(), new DownloadByteArray());
			logger.info("任务ID【{}】 -> byte长度【{}】",taskId,bytes.length);
			InputStream inputStream = new ByteArrayInputStream(bytes);
			reader = new Excel07Reader(inputStream, 1, readColumnNames);
			logger.info("任务ID【{}】 -> excel读取完成，读取行数【{}】",taskId,reader.getRowCount());
			logger.info("表头信息"+reader.getHeadColumn());
			BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 30,null, reader.getRowCount(), null, null, null, null);
			bmsFileAsynTaskService.update(updateEntity);
			long end = System.currentTimeMillis();
			logger.info("任务ID【{}】 -> excel读取完成，读取excel,耗时【{}】",taskId,end-start);
		}
		catch(Exception ex){
			logger.error("任务ID【{}】 -> excel解析异常{}",taskId,ex);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
		if(reader.getRowCount()<=0){
			logger.info("任务ID【{}】 -> 未从excel读取到任何数据",taskId);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.FAIL.getCode(),"未从excel读取到任何数据");
			return;
		}
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 35);
		
		
		//----------校验表头
		logger.info("任务ID【{}】 -> 校验表头...",taskId);
		String[] str = {"出库日期", "仓库", "商家", "出库单号", "运单号"}; //必填列
		if(!bmsMaterialImportTaskCommon.checkTitle(reader.getHeadColumn(),str)){
			logger.info("任务ID【{}】 -> 模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号",taskId);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 32, FileAsynTaskStatusEnum.FAIL.getCode(), "模板列格式错误,必须包含 出库日期,仓库,商家,出库单号,运单号");
			return;
		}
		
		//----------初始化基础数据
		try{
			wareHouseMap = bmsMaterialImportTaskCommon.queryAllWarehouse();
			customerMap  = bmsMaterialImportTaskCommon.queryAllCustomer();
			materialMap  = bmsMaterialImportTaskCommon.queryAllMaterial();
		}
		catch(Exception ex){
			logger.error("任务ID【{}】 -> 仓库,商家,耗材信息异常{}",taskId,ex);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 35, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
		logger.info("任务ID【{}】 -> 成功获取所有仓库,商家,耗材信息 ",taskId);
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 40);
		
		logger.info("任务ID【{}】 -> 分批遍历所有行",taskId);
		Map<Integer, Map<String,String>> pageContents = new HashMap<Integer, Map<String,String>>();
		int i = 1;
		for (Entry<Integer, Map<String, String>> row : reader.getContents().entrySet()) { 
			if(i == 1000){
				pageContents.put(row.getKey(), row.getValue());
				List<BizOutstockPackmaterialTempEntity> tempList = loadTemp(pageContents);
				long start = System.currentTimeMillis();
				if(errMap.size()==0){
					//如果excel数据本身存在问题，就没有将数据写入临时表的必要
					logger.info("任务ID【{}】 -> 保存数据到临时表 行数【{}】",taskId,pageContents.size());
					bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
				}
				logger.info("任务ID【{}】 -> 保存至临时表成功 耗时【{}】",taskId,System.currentTimeMillis()-start);
				pageContents.clear();
				i = 1;
			}
			else{
				pageContents.put(row.getKey(), row.getValue());
				i++;
			}
		}
		
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 70);
		
		List<BizOutstockPackmaterialTempEntity> tempList = loadTemp(pageContents);
		if(errMap.size()==0){
			//如果excel数据本身存在问题，就没有将数据写入临时表的必要
			long start = System.currentTimeMillis();
			logger.info("任务ID【{}】 -> 保存数据到临时表 行数【{}】",taskId,tempList.size());
			bizOutstockPackmaterialTempService.saveBatch(tempList);//保存到临时表
			logger.info("任务ID【{}】 -> 保存至临时表成功 耗时【{}】",taskId,System.currentTimeMillis()-start);
		}
		logger.info("任务ID【{}】 -> 所有数据写入临时表-成功",taskId);
		pageContents.clear();
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 75);
		
		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errMap.size()>0){
			createResultFile();
			return;
		}
		
		//数据库层面重复校验  false - 校验不通过 存在重复  原则上 同一运单号，同一耗材，只有一条
		if(!dbCheck()){
			createResultFile();
			return;
		}
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 80);
		
		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errMap.size()>0){
			logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
			createResultFile();
			return;
		}		
		logger.info("************ OK **********");
		try{
			logger.info("任务ID【{}】 -> 保存数据到正式表",taskId);
			long start = System.currentTimeMillis();
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
	 */
	private List<BizOutstockPackmaterialTempEntity> loadTemp(Map<Integer, Map<String,String>> contents){
		logger.info("数据校验并初始化临时表数据");
		List<BizOutstockPackmaterialTempEntity> tempList = new ArrayList<BizOutstockPackmaterialTempEntity>();
		for (Entry<Integer, Map<String, String>> row : contents.entrySet()) { 
			
			int rowNo = row.getKey(); 	//行号
			Map<String, String> cells = row.getValue();
			String errorMsg="";			//行错误信息
			Timestamp createTime = null; 	//出库时间
			String warehouseName = "";		//仓库
			String warehouseCode = "";
			String customerName  = "";		//商家
			String customerId  = "";
			String outstockNo = "";			//出库单号
			String waybillNo = "";			//运单号
			String creator = taskEntity.getCreator();	//创建人
			
			//****************************************************************** 非空校验
			for (String s : materialCommonList) {
				if(StringUtil.isEmpty(cells.get(s))){
					errorMsg += s+"为空;";
				}
			}
			
			//****************************************************************** 日期校验
			String outTimeExcel=cells.get("出库日期");//出库日期
			try {
				createTime = reader.changeValueToTimestamp(outTimeExcel);
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String tsStr = sdf.format(createTime);
				reader.getContents().get(rowNo).put("出库日期", tsStr);
			} catch (Exception e) {
				errorMsg += "出库日期【"+outTimeExcel+"】格式不正确;";
			}
			
			//****************************************************************** 仓库校验
			warehouseName = cells.get("仓库");//仓库名称
			if(!StringUtil.isEmpty(warehouseName)){
				if(wareHouseMap.containsKey(warehouseName)){
					warehouseCode = wareHouseMap.get(warehouseName).getWarehouseid();
				}
				else{
					errorMsg += "仓库【"+warehouseName+"】不存在;";
				}
			}
			
			//****************************************************************** 商家校验
			customerName = cells.get("商家");//商家名称
			if(!StringUtil.isEmpty(warehouseName)){
				if(customerMap.containsKey(customerName)){
					customerId = customerMap.get(customerName).getCustomerid();
				}
				else{
					errorMsg += "商家【"+customerName+"】不存在;";
				}
			}
			
			
			outstockNo = cells.get("出库单号");
			waybillNo = cells.get("运单号");
			
			boolean isHaveMaterial = false; //本行是否拥有耗材
			
			
			//****************************************************************** 遍历耗材
			for (Map<String,String> map : materialGroup) {
				String codeName = "";
				String numName = "";
				String numType = "";
				for (Map.Entry<String, String> colgroup : map.entrySet()) {
					if(colgroup.getValue().equals("code")){
						codeName = colgroup.getKey();
					}
					else{
						numName = colgroup.getKey();
						numType = colgroup.getValue();
					}
				}
				
				if(cells.containsKey(codeName) && cells.containsKey(numName)){
					//耗材与数量同时存在
					isHaveMaterial = true;
					if(!materialMap.containsKey(cells.get(codeName))){
						errorMsg += "耗材【"+codeName+"】不存在;";
					}
					String num0 = cells.get(numName);
					if(!StringUtil.isNumeric(cells.get(numName))){
						errorMsg += numName+"【"+num0+"】不是数字;";
					}
					else{
						if(num0.contains("-")){
							errorMsg += "【"+num0+"】必须>0";
						}
					}
					if(errorMsg.length()>0){
						continue;
					}
					else{
						
						BizOutstockPackmaterialTempEntity tempChild = new BizOutstockPackmaterialTempEntity();
						tempChild.setConsumerMaterialName(materialMap.get(cells.get(codeName)).getMaterialName());
						tempChild.setConsumerMaterialCode(cells.get(codeName));
						
						if(materialMap.containsKey(cells.get(codeName))){
							PubMaterialInfoVo pubMaterialInfoVo=materialMap.get(cells.get(codeName));
							tempChild.setSpecDesc("外径规格【"+pubMaterialInfoVo.getOutLength().doubleValue()+"*"+pubMaterialInfoVo.getOutWidth().doubleValue()+"*"+pubMaterialInfoVo.getOutHeight().doubleValue()+"】,"
				  			+"内径规格【"+pubMaterialInfoVo.getInLength().doubleValue()+"*"+pubMaterialInfoVo.getInWidth().doubleValue()+"*"+pubMaterialInfoVo.getInHeight().doubleValue()+"】");
						}
						
						String key = waybillNo + tempChild.getConsumerMaterialCode();
						if(repeatMap.containsKey(key)){
							errorMsg += "数据重复--第【"+repeatMap.get(key)+"】行已存在运单【"+waybillNo+"】和耗材【"+tempChild.getConsumerMaterialCode()+"】的组合;";
						}
						else{
							repeatMap.put(key, rowNo);
						}
						
						BigDecimal num = new BigDecimal(num0);
						if(numType.equals("num")){
							tempChild.setNum(num);
						}
						else{
							tempChild.setWeight(num);
						}
						if(errorMsg.length() == 0){
							tempChild.setAdjustNum(num);
							tempChild.setRowExcelNo(rowNo);
							tempChild.setRowExcelName(codeName);
							tempChild.setCreateTime(createTime);
							tempChild.setWarehouseName(warehouseName);
							tempChild.setWarehouseCode(warehouseCode);
							tempChild.setCustomerName(customerName);
							tempChild.setCustomerId(customerId);
							tempChild.setOutstockNo(outstockNo);
							tempChild.setWaybillNo(waybillNo);
							tempChild.setCreator(creator);
							tempChild.setBatchNum(taskEntity.getTaskId());
							tempChild.setDelFlag("0");// 设置为未作废
							tempChild.setExtattr5("origin");
							tempChild.setWriteTime(JAppContext.currentTimestamp());
							tempChild.setIsCalculated(String.valueOf(ConstantInterface.Calculate.CALCULATE_NO));
							tempList.add(tempChild);
						}
					}
				}
				//耗材与数量均为空,合理数据,但不处理
				else if(!cells.containsKey(codeName) && !cells.containsKey(numName)){
					continue;
				}
				//耗材与数量有一个为空
				else{
					errorMsg += "【"+numName+"】和【"+codeName+"】不能同时为空;";
				}
				
			}
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
		}
		return tempList;
	}
	
	/**
	 * 生成结果文件
	 * @throws IOException 
	 */
	private void createResultFile() throws IOException{
		
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
		for (String colName : readColumnNames) {
			if(reader.getHeadColumn().containsKey(colName)){
				exportColumns.add(colName);
			}
		}
		
		
		POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
    	List<Map<String, Object>> headDetailMapList = getBizHead(exportColumns); 
		List<Map<String, Object>> dataDetailList = getBizHeadItem();
		poiUtil.exportExcel2FilePath(poiUtil, workbook, "Sheet1",1, headDetailMapList, dataDetailList);
		
		String resultFullPath="";	
		logger.info("上传结果文件到fastDfs");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		byte[] b1 = os.toByteArray();
		StorePath resultStorePath = storageClient.uploadFile(new ByteArrayInputStream(b1), b1.length, "xlsx");
	    resultFullPath = resultStorePath.getFullPath();
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
	
	private List<Map<String, Object>> getBizHeadItem(){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();	 
        Map<String, Object> dataItem = null;
        for (Entry<Integer, Map<String, String>> row : reader.getContents().entrySet()) {
        	dataItem = new HashMap<String, Object>();
        	for (Map.Entry<String, String> map : row.getValue().entrySet()) {
				dataItem.put(map.getKey(), map.getValue());
			}
        	if(errMap.containsKey(row.getKey())){
        		dataItem.put("备注", errMap.get(row.getKey()));
        	}
        	dataList.add(dataItem);
        }
        return dataList;
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
				if(errMap.containsKey(rowNum)){
					errMap.put(rowNum, errMap.get(rowNum)+","+map.get(key));
				}else{
					errMap.put(rowNum, map.get(key));
				}
			}
			return false;
	}
	
}
