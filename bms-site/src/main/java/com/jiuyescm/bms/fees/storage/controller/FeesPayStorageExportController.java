package com.jiuyescm.bms.fees.storage.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.vo.ExportDataVoEntity;
import com.jiuyescm.bms.common.web.HttpCommanExport;
import com.jiuyescm.bms.fees.storage.entity.FeesPayStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesPayStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.exception.BizException;

/**
 * 成本费用导出异步下载
 * @author Wuliangfeng
 *
 */
@Controller("feesPayStorageExportController")
public class FeesPayStorageExportController {

	@Resource 
	private SequenceService sequenceService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IFeesPayStorageService feesPayStorageService;

	private static final Logger logger = Logger.getLogger(FeesPayStorageExportController.class.getName());	private String titleName="";
	@DataResolver
	public String exportData(Map<String,Object> param) throws Exception{
		if(param==null){
			return MessageConstant.STORAGE_FEES_INFO_ISNULL_MSG;
		}
		String json = JSONObject.toJSONString(param);
		Timestamp startTime=null;
		if(param.get("createTimeBegin")!=null&&!StringUtils.isBlank(param.get("createTimeBegin").toString())){
			startTime=DateUtil.formatTimestamp(param.get("createTimeBegin"));
		}
		Timestamp endTime=null;
		if(param.get("createTimeEnd")!=null&&!StringUtils.isBlank(param.get("createTimeEnd").toString())){
			endTime=DateUtil.formatTimestamp(param.get("createTimeEnd"));
		}
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
	
		//校验该费用是否已生成Excel文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("taskType", FileTaskTypeEnum.FEE_PAY_STORAGE.getCode());
		queryEntity.put("param1", json);
		List<FileExportTaskEntity> taskList = fileExportTaskService.query(queryEntity);
		if (null != taskList && taskList.size() > 0) {
			FileExportTaskEntity entity=taskList.get(0);
			entity.setDelFlag("1");
			int num=fileExportTaskService.update(entity);
			if(num<=0){
				return "删除费用失败";
			}
		}
		String path=getPath();
		String filePath=path+ FileConstant.SEPARATOR + 
				FileConstant.FEES_STORAGE_PREFIX+ getName() + FileConstant.SUFFIX_XLSX;
		FileExportTaskEntity entity = new FileExportTaskEntity();
		
		entity.setStartTime(startTime);
		entity.setEndTime(endTime);
		entity.setTaskName("耗材成本费用导出"+dateFormater.format(JAppContext.currentTimestamp()));
		entity.setTaskType(FileTaskTypeEnum.FEE_PAY_STORAGE.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(filePath);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setParam1(json);
		entity.setDelFlag("0");
		entity = fileExportTaskService.save(entity);
		//生成账单文件
		final Map<String, Object> condition = param;
		final String taskId = entity.getTaskId();
		final String file=filePath;
		new Thread(){
			public void run() {
				try {
					export(condition, taskId,file);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_REC_STORAGE_FEE_EXCEL_EX_MSG, e);
					//写入日志
					bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
				}
			};
		}.start();
		return MessageConstant.EXPORT_TASK_RECE_BILL_MSG;
	}
	private void export(Map<String, Object> param,String taskId,String file)throws Exception{
		updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		logger.info("====应收仓储费用导出：");
		
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	String filePath = file;
		
    	logger.info("====应收仓储费用导出：写入Excel begin.");
    	try{
    		/*
    		HttpCommanExport httpExport=new HttpCommanExport(filePath);
    		ExportDataVoEntity voEntity=new ExportDataVoEntity();
    		voEntity.setTitleName(titleName);
    		voEntity.setBaseType(getBsType());u*/
    		POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
    		
    		List<Map<String,Object>> headList=getHeadMap(getBsType());
    		boolean doLoop=true;
    		int pageSize=100;
    		int pageIndex=1;
    		int dispatchLineNo = 1;
    		while(doLoop){
    			PageInfo<FeesPayStorageEntity> pageInfo = feesPayStorageService.query(param, pageIndex, pageSize);
    			if (null != pageInfo && pageInfo.getList().size() > 0) {
    				if (pageInfo.getList().size() < pageSize) {
    					doLoop = false;
    				}else {
    					pageIndex += 1; 
    				}
    			}else {
    				doLoop = false;
    			}
    			List<Map<String,Object>> dataList=getItemMap(pageInfo.getList());
				poiUtil.exportExcel2FilePath(poiUtil, workbook, MessageConstant.BILL_STORAGE_DETAIL_MSG, 
						dispatchLineNo, headList, dataList);
				if (null != pageInfo && pageInfo.getList().size() > 0) {
					dispatchLineNo += pageInfo.getList().size();
				}
    		}
    		updateExportTask(taskId, null, 70);
    		poiUtil.write2FilePath(workbook, filePath);
    		updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    		//voEntity.setDataList(dataList);
    		//httpExport.exportFileAsync(voEntity);
    	}catch(Exception e){
    		throw e;
    	}
	}
	private List<Map<String,Object>> getItemMap(List<FeesPayStorageEntity> list){
		List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
		for(FeesPayStorageEntity entity:list){
			Map<String,Object> map=Maps.newHashMap();
			map.put("feesNo", entity.getFeesNo());
			map.put("customerId", entity.getCustomerId());
			map.put("customerName", entity.getCustomerName());
			map.put("warehouseCode", entity.getWarehouseCode());
			map.put("warehouseName", entity.getWarehouseName());
			map.put("orderNo", entity.getOrderNo());
			map.put("waybillNo", entity.getWaybillNo());
			map.put("productNo", entity.getProductNo());
			map.put("productName", entity.getProductName());
			map.put("externalProductNo", entity.getExternalProductNo());
			map.put("quantity", entity.getQuantity());
			map.put("unit", entity.getUnit());
			map.put("weight", entity.getWeight());
			map.put("calculateTime", entity.getCalculateTime());
			map.put("isCalculated", entity.getIsCalculated());
			map.put("cost", entity.getCost());
			map.put("derateAmount", entity.getDerateAmount());
			map.put("ruleNo", entity.getRuleNo());
			map.put("billNo", entity.getBillNo());
			map.put("status", entity.getStatus());
			map.put("operateTime", entity.getOperateTime());
			map.put("bizId", entity.getBizId());
			map.put("creator", entity.getCreator());
			map.put("createTime", entity.getCreateTime());
			dataList.add(map);
		}
		return dataList;
	}
	private List<Map<String,Object>> getHeadMap(BaseDataType bsType){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = null;
		for(DataProperty dataProp:bsType.dataProps){
			itemMap = new HashMap<String, Object>();	
			itemMap.put("title", dataProp.getPropertyName());
	        itemMap.put("columnWidth", 25);
	        itemMap.put("dataKey", dataProp.getPropertyId());
	        headInfoList.add(itemMap);
		}
		return headInfoList;
	}
	private BaseDataType getBsType(){
		BaseDataType bsType=new BaseDataType();
		bsType.dataProps.add(new DataProperty("feesNo","费用编号"));
		bsType.dataProps.add(new DataProperty("customerId","商家ID"));
		bsType.dataProps.add(new DataProperty("customerName","商家名称"));
		bsType.dataProps.add(new DataProperty("warehouseCode","仓库编码"));
		bsType.dataProps.add(new DataProperty("warehouseName","仓库名称"));
		bsType.dataProps.add(new DataProperty("orderNo","订单号"));
		bsType.dataProps.add(new DataProperty("waybillNo","运单号"));
		bsType.dataProps.add(new DataProperty("productNo","耗材条码"));
		bsType.dataProps.add(new DataProperty("productName","耗材名称"));
		bsType.dataProps.add(new DataProperty("externalProductNo","耗材编码"));
		bsType.dataProps.add(new DataProperty("quantity","数量"));
		bsType.dataProps.add(new DataProperty("unit","单位"));
		bsType.dataProps.add(new DataProperty("weight","重量"));
		bsType.dataProps.add(new DataProperty("calculateTime","计费时间"));
		bsType.dataProps.add(new DataProperty("isCalculated","计费状态"));
		bsType.dataProps.add(new DataProperty("cost","金额"));
		bsType.dataProps.add(new DataProperty("derateAmount","减免金额"));
		bsType.dataProps.add(new DataProperty("ruleNo","规则编号"));
		bsType.dataProps.add(new DataProperty("billNo","账单编号"));
		bsType.dataProps.add(new DataProperty("status","状态"));
		bsType.dataProps.add(new DataProperty("operateTime","操作时间"));
		bsType.dataProps.add(new DataProperty("bizId","业务数据ID"));
		bsType.dataProps.add(new DataProperty("creator","创建人"));
		bsType.dataProps.add(new DataProperty("createTime","创建时间"));
		return bsType;
	}
	private void updateExportTask(String taskId, String taskState, double process){
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		fileExportTaskService.update(entity);
	}
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PAY_BIZ");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PAY_BIZ");
		}
		return systemCodeEntity.getExtattr1();
	}
	/**
	 * 费用文件唯一值
	 */
	public String getName(){
		return sequenceService.getBillNoOne(FeesReceiveStorageExportController.class.getName(), "FY", "000000");
	}
}
