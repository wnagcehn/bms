/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.trunkwaybill.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.CargoIsLightEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.enumtype.TransportWayBillTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

/**
 * 干线运单业务数据导出类
 * @author yangss
 */
@Controller("trunkWaybillExportController")
public class BizGanxianWayBillExportController extends BaseController{

	private static final Logger logger = Logger.getLogger(BizGanxianWayBillExportController.class.getName());

	@Resource
	private IBizGanxianWayBillService bizGanxianWayBillService;
	@Autowired
	private IFeesReceiverDeliverService deliverService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		String customerId = param.get("customerId").toString();
        try {
        	//校验该费用是否已生成Excel文件
        	Map<String, Object> queryEntity = new HashMap<String, Object>();
        	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_WAYBILL.getCode());
        	queryEntity.put("customerid", customerId);
        	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
        	if (StringUtils.isNotEmpty(existDel)) {
        		return existDel;
        	}
        	
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_WAYBILL.getCode() + customerId + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setCustomerid(customerId);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_WAYBILL.getDesc() + customerId);
        	entity.setTaskType(FileTaskTypeEnum.BIZ_WAYBILL.getCode());
        	entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
        	entity.setProgress(0d);
        	entity.setFilePath(filepath);
        	entity.setCreator(JAppContext.currentUserName());
        	entity.setCreateTime(JAppContext.currentTimestamp());
        	entity.setDelFlag(ConstantInterface.DelFlag.NO);
        	entity = fileExportTaskService.save(entity);
        	
        	//生成账单文件
    		final Map<String, Object> condition = param;
    		final String taskId = entity.getTaskId();
    		final String filePath=filepath;
    		new Thread(){
    			public void run() {
    				try {
    					export(condition, taskId,filePath);
    				} catch (Exception e) {
    					fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
    					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
    					//写入日志
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
    					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "启动线程失败", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_WAYBILL.getDesc() + customerId + MessageConstant.EXPORT_TASK_BIZ_MSG;
	}
	
	/**
	 * 异步导出
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void export(Map<String, Object> param,String taskId,String filePath)throws Exception{
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getBizReceiveExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收干线运单导出：");
    	File storeFolder=new File(path);
		//如果存放上传文件的目录不存在就新建
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收干线运单导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //干线运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handGanxianWayBill(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收干线运单导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 干线运单
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handGanxianWayBill(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		if(null == myparam.get("isCalculated") ||
				StringUtils.equalsIgnoreCase(String.valueOf(myparam.get("isCalculated")), "ALL")){
			myparam.put("isCalculated", null);
		}else{
			myparam.put("isCalculated", String.valueOf(myparam.get("isCalculated")));
		}
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BizGanxianWayBillEntity> pageInfo = 
					bizGanxianWayBillService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				if (pageInfo.getList().size() < FileConstant.EXPORTPAGESIZE) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
			}else {
				doLoop = false;
			}
			
			//头、内容信息
			List<Map<String, Object>> headDetailMapList = getBizHead(); 
			List<Map<String, Object>> dataDetailList = getBizHeadItem(pageInfo.getList());
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_WAYBILL.getDesc(), 
					lineNo, headDetailMapList, dataDetailList);
			if (null != pageInfo && pageInfo.getList().size() > 0) {
				lineNo += pageInfo.getList().size();
			}
		}
	}
	
	/**
	 * 干线运单
	 */
	public List<Map<String, Object>> getBizHead(){
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "业务类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bizTypeCode");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "订单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "orderNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "路单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "ganxianNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发站点");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "startStation");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的站点");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "endStation");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "车型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carModel");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发省份");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendProvinceId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发城市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendCityId");
        headInfoList.add(itemMap);
             
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "始发区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendDistrictId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的省份");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiverProvinceId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的城市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiverCityId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiverDistrictId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总箱数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalBox");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总件数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalPackage");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总品种数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalVarieties");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "体积");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalVolume");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否泡货");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isLight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "发货时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "sendTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
		
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizGanxianWayBillEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        Map<String, String> isLightMap = CargoIsLightEnum.getMap();
	        Map<String, String> bizTypeMap = TransportWayBillTypeEnum.getMap();
	        for (BizGanxianWayBillEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("bizTypeCode", bizTypeMap.get(entity.getBizTypeCode()));
	        	dataItem.put("orderNo", entity.getOrderNo());
	        	dataItem.put("ganxianNo", entity.getGanxianNo());
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("feesNo", entity.getFeesNo());
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("startStation", entity.getStartStation());
	        	dataItem.put("endStation", entity.getEndStation());
	        	dataItem.put("carModel", entity.getCarModel());
	        	dataItem.put("sendProvinceId", entity.getSendProvinceId());
	        	dataItem.put("sendCityId", entity.getSendCityId());
	        	dataItem.put("sendDistrictId", entity.getSendDistrictId());
	        	dataItem.put("receiverProvinceId", entity.getReceiverProvinceId());
	        	dataItem.put("receiverCityId", entity.getReceiverCityId());
	        	dataItem.put("receiverDistrictId", entity.getReceiverDistrictId());
	        	dataItem.put("totalBox", entity.getTotalBox());
	        	dataItem.put("totalPackage", entity.getTotalPackage());
	        	dataItem.put("totalVarieties", entity.getTotalVarieties());
	        	dataItem.put("totalVolume", entity.getTotalVolume());
	        	dataItem.put("totalWeight", entity.getTotalWeight());
	        	dataItem.put("adjustWeight", entity.getAdjustWeight());
	        	dataItem.put("isLight", isLightMap.get(entity.getIsLight()));
	        	dataItem.put("feesNo", entity.getFeesNo());
	        	dataItem.put("isCalculated", calculateMap.get(entity.getIsCalculated()));
	        	dataItem.put("remark", entity.getRemark());
	        	dataItem.put("sendTime", entity.getSendTime());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
}