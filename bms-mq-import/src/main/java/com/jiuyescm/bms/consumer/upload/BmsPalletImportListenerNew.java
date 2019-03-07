package com.jiuyescm.bms.consumer.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoTempEntity;
import com.jiuyescm.bms.biz.pallet.service.IBizPalletInfoTempService;
import com.jiuyescm.bms.common.enumtype.status.FileAsynTaskStatusEnum;
import com.jiuyescm.bms.consumer.common.BmsMaterialImportTask;
import com.jiuyescm.bms.excel.callback.SheetReadCallBack;
import com.jiuyescm.bms.excel.data.DataColumn;
import com.jiuyescm.bms.excel.data.DataRow;
import com.jiuyescm.bms.excel.data.Sheet;
import com.jiuyescm.bms.excel.data.XlsxWorkBook;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.bs.util.StringUtil;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.fastdfs.client.StorageClient;
import com.jiuyescm.framework.fastdfs.model.StorePath;
import com.jiuyescm.framework.fastdfs.protocol.storage.callback.DownloadByteArray;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Service("bmsPalletImportListenerNew")
public class BmsPalletImportListenerNew implements MessageListener{

	private static final Logger logger = LoggerFactory.getLogger(BmsPalletImportListenerNew.class);
	
	@Autowired private BmsMaterialImportTask bmsMaterialImportTaskCommon;
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private StorageClient storageClient;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private ISystemCodeService systemCodeService; //业务类型
	@Autowired private ICustomerService customerService;
	@Autowired private IBizPalletInfoTempService bizPalletInfoTempService;
	
	private static final String REMARK = "导入数据不规范,请下载查看最后一列说明";
	BmsFileAsynTaskVo taskEntity = new BmsFileAsynTaskVo();
	
	public Map<Integer, String> errMap = null;
	private Map<Integer, String> errorMap = null;
	public List<String> readColumnNames = null;
	public String[] neededColumnNames = null;
	
	private Map<String,String> wareHouseMap = null;
	private Map<String,String> customerMap = null;
	private Map<String,String> temperatureMap = null;
	
	private String taskId;
	private int batchNum = 1000;

	TreeMap<Integer,String> originColumn = new TreeMap<Integer,String>(); //源生表头信息
	List<BizPalletInfoTempEntity> newList = new ArrayList<BizPalletInfoTempEntity>();
	List<BizPalletInfoTempEntity> repeatList = new ArrayList<BizPalletInfoTempEntity>();
	
	List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	private int roNo = 1;
	
	//----------初始化基础数据--------
	public void initKeyValue(){
		//----------初始化基础数据--------
		wareHouseMap = new HashMap<String, String>();
		customerMap = new HashMap<String, String>();
		temperatureMap = new HashMap<String, String>();

		//仓库
		List<WarehouseVo> wareHouseList = warehouseService.queryAllWarehouse();
		if(wareHouseList != null && wareHouseList.size()>0){
			for(WarehouseVo wareHouse : wareHouseList){
				wareHouseMap.put(wareHouse.getWarehousename().trim(),wareHouse.getWarehouseid());
			}
		}
		//商家
		PageInfo<CustomerVo> tmpPageInfo = customerService.query(null, 0, Integer.MAX_VALUE);
		if (tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0) {
			for(CustomerVo customer : tmpPageInfo.getList()){
				if(customer != null){
					customerMap.put(customer.getCustomername().trim(), customer.getCustomerid());
				}
			}
		}	
		//温度类型
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "TEMPERATURE_TYPE");
		List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCodeName().trim(), scEntity.getCode());
		    }
		}
		logger.info("任务ID【{}】 -> 成功获取所有仓库,商家,耗材信息 ",taskId);
	}
	
	@Override
	public void onMessage(Message message) {
		try {
			initKeyValue();
		} catch (Exception e) {
			logger.error("任务ID【{}】 -> 仓库,商家,耗材信息异常{}",taskId,e);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 10, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}	
		
		logger.info("--------------------MQ处理操作日志开始---------------------------");
		long start = System.currentTimeMillis();
		try {
			taskId = ((TextMessage)message).getText();
		} catch (JMSException e1) {
			logger.error("读取消息->更新任务表失败", e1);
			return;
		}
		try {
			handImportFile();		// 处理导入文件
		} catch (Exception e1) {
			logger.error("异步文件处理异常", e1);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 0, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}
		
		long end = System.currentTimeMillis();
		try {
			message.acknowledge();
		} catch (JMSException e) {
			logger.info("消息应答失败");
		}
		errMap = null;
		errorMap=null;
		logger.info("--------------------MQ处理操作日志结束,耗时:"+(end-start)+"ms---------------");
	}
	
	protected void handImportFile() throws Exception{
		
		errMap = new HashMap<Integer, String>();
		errorMap = new HashMap<Integer, String>();
		
		bmsMaterialImportTaskCommon.setTaskStatus(taskId, 0, FileAsynTaskStatusEnum.PROCESS.getCode());
		
		//----------查询任务
		taskEntity = bmsFileAsynTaskService.findByTaskId(taskId);
		if (null == taskEntity) {
			logger.error("任务不存在");
			return;
		}
		logger.info("已领取任务 任务ID【"+taskId+"】");
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 5);
		
		//初始化需要读取的列
		readColumnNames = initColumnNames();
		neededColumnNames = initColumnsNamesForNeed();
		
		long start = System.currentTimeMillis();
		XlsxWorkBook reader = null;
		byte[] bytes = storageClient.downloadFile(taskEntity.getOriginFilePath(), new DownloadByteArray());
		logger.info("任务ID【{}】 -> byte长度【{}】",taskId,bytes.length);
		InputStream inputStream = new ByteArrayInputStream(bytes);

		try{	
			reader = new XlsxWorkBook(inputStream);	
			Sheet sheet = reader.getSheets().get(0);
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
					
					logger.info("任务ID【{}】 -> 校验表头...",taskId); 
					//表头校验
					if(!checkTitle(columns,neededColumnNames)){
						String msg = "模板列格式错误,必须包含:";
						for (String str : neededColumnNames) {
							msg+=str;
						}
						logger.info(msg);
						bmsMaterialImportTaskCommon.setTaskStatus(taskId, 35, FileAsynTaskStatusEnum.FAIL.getCode(), msg);
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
					List<BizPalletInfoTempEntity> tempList = null;
					try {
						tempList = loadTemp(dr, errorMsg);
					} catch (Exception e) {
						errorMap.put(dr.getRowNo(), e.getMessage());
						errMap.clear();
					}

					//组装好的数据存入全局List中
					if (tempList == null) {
						tempList = new ArrayList<BizPalletInfoTempEntity>();
					}
					for( int i = 0 ; i < tempList.size() ; i++){
						newList.add(tempList.get(i));
						repeatList.add(tempList.get(i));
					}
					
					//1000条数据	
					if (newList.size() >= batchNum) {
						if(errorMap.size()==0){
							int result = saveTo();
							if(result<=0){
								logger.error("任务ID【{}】 ->,保存到临时表失败", taskId);
							}
						}
					}
							
					return;
					
				}

				@Override
				public void finish() {	
					bmsMaterialImportTaskCommon.setTaskProcess(taskId, 70);
					//保存数据到临时表
					if(errorMap.size()==0){
						int result = saveTo();
						if (result <= 0) {
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
			BmsFileAsynTaskVo updateEntity2 = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 80,null, sheet.getRowCount(), null, null, null, null);
			bmsFileAsynTaskService.update(updateEntity2);
			
		
			
			long end = System.currentTimeMillis();
			logger.info("*****读取excel,耗时:" + (end-start)/1000 + "秒*****");
		}
		catch(Exception ex){
			logger.error("excel解析异常--",ex);
			bmsMaterialImportTaskCommon.setTaskStatus(taskId, 20, FileAsynTaskStatusEnum.EXCEPTION.getCode());
			return;
		}/*finally {
			reader.close();
		}*/
		
 		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errorMap.size()>0){
			logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
			try {
				createResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		//重复性校验
        if(repeatList!=null&&repeatList.size()>0){
       	  checkPallet(repeatList);		
        }
		
 		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errorMap.size()>0){
			logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
			try {
				createResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		//数据库层面重复校验  false - 校验不通过 存在重复  原则上（时间+仓库+商家+温度+类型）只有一条  
		if(!dbCheck()){
			try {
				createResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		//如果excel数据本身存在问题，直接生产结果文件返回给用户
		if(errorMap.size()>0){
			logger.info("任务ID【{}】 -> 数据不合法,产生结果文件",taskId);
			try {
				createResultFile(reader);
			} catch (Exception e) {
				logger.error("文件创建失败！", e);
			}
			return;
		}
		
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 80);	
		logger.info("************ OK **********");
		//往正式表存数据
		try{
			int k=saveDataFromTemp(taskEntity.getTaskId());
			if(k>0){
				logger.error("托数从临时表写入业务表成功");
				bmsMaterialImportTaskCommon.setTaskProcess(taskEntity.getTaskId(), 90);
				BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskEntity.getTaskId(), 100,FileAsynTaskStatusEnum.SUCCESS.getCode(), null, JAppContext.currentTimestamp(), null, null, "导入成功");
				bmsFileAsynTaskService.update(updateEntity);
			}else{
				logger.error("未从临时表中保存数据到业务表，批次号【"+taskEntity.getTaskId()+"】,任务编号【"+taskEntity.getTaskId()+"】");
				bmsMaterialImportTaskCommon.setTaskStatus(taskEntity.getTaskId(),99, FileAsynTaskStatusEnum.FAIL.getCode(),"未从临时表中保存数据到业务表，批次号【"+taskEntity.getTaskId()+"】,任务编号【"+taskEntity.getTaskId()+"】");
				bizPalletInfoTempService.deleteBybatchNum(taskEntity.getTaskId());
			}
			newList.clear();
		}catch(Exception e){
			logger.error("异步导入异常", e);
			bmsMaterialImportTaskCommon.setTaskStatus(taskEntity.getTaskId(),99, FileAsynTaskStatusEnum.EXCEPTION.getCode(),"从临时表中保存数据到业务表异常");
			bizPalletInfoTempService.deleteBybatchNum(taskEntity.getTaskId());
		}
		return;
	}
	
	
	/**
	 * 初始化列
	 * @return
	 */
	protected List<String> initColumnNames() {
		String[] str = {"库存日期","仓库","商家全称","商品冷冻","商品冷藏","商品常温","商品恒温","耗材冷冻","耗材冷藏","耗材常温","耗材恒温","入库托数","出库托数"};
		return Arrays.asList(str); 
	}

	protected String[] initColumnsNamesForNeed() {
		String[] str = {"库存日期","仓库","商家全称"};
		return str;
	}
	
	/**
	 * 表头校验
	 * @param headColumn
	 * @param str
	 * @return
	 */
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
	
	/**
	 * 生成结果文件
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void createResultFile(XlsxWorkBook reader) throws IOException, ParseException{
		
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
			        Map<String, Object> dataItem = new HashMap<String, Object>();
					for (DataColumn dc : dr.getColumns()) {
						if ("库存日期".equals(dc.getTitleName())) {
							try {
								if (StringUtils.isBlank(dc.getColValue())) {
									continue;
								}
								dataItem.put(dc.getTitleName(), DateUtil.transStringToTimeStamp(dc.getColValue()).toString());
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
		        	if (dataList.size() >= 1000) {
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
		
    	errorMap.clear();
		String resultFullPath="";	
		logger.info("上传结果文件到fastDfs");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		workbook.write(os);
		workbook.dispose();
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
	
	private boolean dbCheck(){
		Map<String, String> tranTemperature = new HashMap<String, String>();
		tranTemperature.put("LD", "冷冻");
		tranTemperature.put("LC", "冷藏");
		tranTemperature.put("CW", "常温");
		tranTemperature.put("HW", "恒温");
		List<BizPalletInfoTempEntity> palletlist = bizPalletInfoTempService.queryInBiz(taskEntity.getTaskId());
		if(null == palletlist || palletlist.size() <= 0){
			return true;
		}
		
		Map<String,String> map=Maps.newLinkedHashMap();
			
		//一共几行,去重
		Set<Integer> rowNos = new TreeSet<>();
		for(BizPalletInfoTempEntity entity:palletlist){
			rowNos.add(entity.getRowExcelNo());
		}
		
		//存在重复记录
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for(BizPalletInfoTempEntity entity:palletlist){
			String row=String.valueOf(entity.getRowExcelNo());
			String mes="";
			if(map.containsKey(row)){
				mes=map.get(row);
				mes+=",【"+formatter.format(entity.getCurTime())+"】【"+entity.getWarehouseName()+"】【"+entity.getCustomerName()+"】【"+tranTemperature.get(entity.getTemperatureTypeCode())+"】【"+entity.getBizType()+"】";
				map.put(row,mes);
			}else{
				mes="系统中已存在,【"+formatter.format(entity.getCurTime())+"】【"+entity.getWarehouseName()+"】【"+entity.getCustomerName()+"】【"+entity.getTemperatureTypeCode()+"】【"+entity.getBizType()+"】";
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
	
	private void checkPallet(List<BizPalletInfoTempEntity> list){
		//验证导入数据有重复
		List<String> keyList=new ArrayList<String>();
		for(BizPalletInfoTempEntity temp:list){
			String key=getPalletKey(temp);
			if(!keyList.contains(key)){//excel数据无重复 验证与数据库对比
				keyList.add(key);
			}else{			
				errorMap.put(temp.getRowExcelNo(), "Excel中数据重复");
			}
		}
		repeatList.clear();
	}
	
	private String getPalletKey(BizPalletInfoTempEntity dataEntity){
		String key=dataEntity.getCurTime()+dataEntity.getWarehouseCode()+dataEntity.getCustomerId()+dataEntity.getTemperatureTypeCode()+dataEntity.getBizType();
		return key;
	}
	
	private int saveTo(){
		logger.info("任务ID【{}】 -> 保存数据到临时表 转化成对象数【{}】",taskId,newList.size());
		try {
			bizPalletInfoTempService.saveBatch(newList); //保存到临时表
		} catch (Exception e) {
			logger.error("任务ID【{}】 -> 所有数据写入临时表-失败",taskId);
			return 0;
		}
		logger.info("任务ID【{}】 -> 所有数据写入临时表-成功",taskId);
		bmsMaterialImportTaskCommon.setTaskProcess(taskId, 75);
		return 1;
    }
	
	private int saveDataFromTemp(String taskId){
		int result=bizPalletInfoTempService.saveTempData(taskId);		
		return result;
	}
	
	/**
	 * 产生临时表数据
	 * @param dr
	 * @param errorMsg
	 * @return
	 * @throws ParseException 
	 */
	private List<BizPalletInfoTempEntity> loadTemp(DataRow dr, String errorMsg) throws ParseException{
		logger.info("数据校验并初始化临时表数据");
		Date date = null;
		BizPalletInfoTempEntity tempEntity = null;
		BizPalletInfoTempEntity tempEntity1 = null;
		BizPalletInfoTempEntity tempEntity2 = null;
		BizPalletInfoTempEntity tempEntity3 = null;
		BizPalletInfoTempEntity tempEntity4 = null;
		BizPalletInfoTempEntity tempEntity5 = null;
		BizPalletInfoTempEntity tempEntity6 = null;
		BizPalletInfoTempEntity tempEntity7 = null;
		BizPalletInfoTempEntity tempEntity8 = null;
		BizPalletInfoTempEntity tempEntity9 = null;
		BizPalletInfoTempEntity tempEntity10 = null;
		List<BizPalletInfoTempEntity> tempList = new ArrayList<BizPalletInfoTempEntity>();
		
		boolean isCustomerNull = false;
		boolean isDateNull = false;
		boolean isAllEmpty=false;
		tempEntity = new BizPalletInfoTempEntity();
		tempEntity.setRowExcelNo(dr.getRowNo());
		tempEntity.setWriteTime(JAppContext.currentTimestamp());
		tempEntity.setCreator(taskEntity.getCreator());
		tempEntity.setCreatorId(taskEntity.getCreatorId());
		tempEntity.setTaskId(taskEntity.getTaskId());
		
		try {
			for (DataColumn dc : dr.getColumns()) {			
				switch (dc.getTitleName()) {
				case "库存日期":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						date = new Date(DateUtil.transStringToTimeStamp(dc.getColValue()).getTime());	
						tempEntity.setCurTime(date);
						tempEntity.setCreateTime(DateUtil.transStringToTimeStamp(dc.getColValue()));
					}else {
						errorMsg += "库存日期必填;";
						isDateNull = true;
					}
					break;
				case "仓库":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setWarehouseName(dc.getColValue());
						//如果没找到，报错
						if (wareHouseMap.containsKey(dc.getColValue())) {
							tempEntity.setWarehouseCode(wareHouseMap.get(dc.getColValue()));
						}else {
							errorMsg+="仓库不存在;";
						}
					}else {
						errorMsg+="仓库必填;";
					}
					break;
				case "商家全称":
					if (StringUtils.isNotBlank(dc.getColValue())) {
						tempEntity.setCustomerName(dc.getColValue());
						//如果没找到，报错
						if (customerMap.containsKey(dc.getColValue())) {
							tempEntity.setCustomerId(customerMap.get(dc.getColValue()));
						}else {
							errorMsg+="商家不存在;";
						}
					}else {
						errorMsg+="商家必填;";
						isCustomerNull = true;
					}
					break;
				case "商品冷冻":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity1 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity1, tempEntity);
							tempEntity1.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity1.setTemperatureTypeCode(temperatureMap.get("冷冻"));
							tempEntity1.setBizType("product");
							tempList.add(tempEntity1);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "商品冷藏":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity2 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity2, tempEntity);
							tempEntity2.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity2.setTemperatureTypeCode(temperatureMap.get("冷藏"));
							tempEntity2.setBizType("product");
							tempList.add(tempEntity2);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "商品常温":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity3 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity3, tempEntity);
							tempEntity3.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity3.setTemperatureTypeCode(temperatureMap.get("常温"));
							tempEntity3.setBizType("product");
							tempList.add(tempEntity3);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "商品恒温":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity4 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity4, tempEntity);
							tempEntity4.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity4.setTemperatureTypeCode(temperatureMap.get("恒温"));
							tempEntity4.setBizType("product");
							tempList.add(tempEntity4);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "耗材冷冻":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity5 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity5, tempEntity);
							tempEntity5.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity5.setTemperatureTypeCode(temperatureMap.get("冷冻"));
							tempEntity5.setBizType("material");
							tempList.add(tempEntity5);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "耗材冷藏":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity6 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity6, tempEntity);
							tempEntity6.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity6.setTemperatureTypeCode(temperatureMap.get("冷藏"));
							tempEntity6.setBizType("material");
							tempList.add(tempEntity6);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "耗材常温":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity7 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity7, tempEntity);
							tempEntity7.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity7.setTemperatureTypeCode(temperatureMap.get("常温"));
							tempEntity7.setBizType("material");
							tempList.add(tempEntity7);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "耗材恒温":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity8 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity8, tempEntity);
							tempEntity8.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity8.setTemperatureTypeCode(temperatureMap.get("恒温"));
							tempEntity8.setBizType("material");
							tempList.add(tempEntity8);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "入库托数":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity9 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity9, tempEntity);
							tempEntity9.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity9.setBizType("instock");
							tempList.add(tempEntity9);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				case "出库托数":
					if (StringUtils.isNotBlank(dc.getColValue()) && !"0".equals(dc.getColValue())) {
						isAllEmpty = true;
						if (ExportUtil.isNumber(dc.getColValue())) {
							tempEntity10 = new BizPalletInfoTempEntity();
							PropertyUtils.copyProperties(tempEntity10, tempEntity);
							tempEntity10.setPalletNum(Double.valueOf(dc.getColValue()));
							tempEntity10.setBizType("outstock");
							tempList.add(tempEntity10);
						}else {
							errorMsg+="列【"+dc.getColNo()+"】为非数字;";
						}
					}
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			errorMsg+="第【"+ dr.getRowNo() +"】行格式不正确;";
		}
		
		if (isDateNull && isCustomerNull) {
			return tempList;
		}
		
		if(!isAllEmpty){
			errorMsg+="数值列不能都为空;";
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
		
		if (errMap.size() > 0) {
			throw new BizException(errMap.get(dr.getRowNo()));
		}
		return tempList;
	}
	
}


