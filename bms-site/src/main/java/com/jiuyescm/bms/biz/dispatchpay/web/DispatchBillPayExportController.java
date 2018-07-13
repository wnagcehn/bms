
package com.jiuyescm.bms.biz.dispatchpay.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillPayService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

@Controller("dispatchBillPayExportController")
public class DispatchBillPayExportController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(DispatchBillPayExportController.class.getName());
	
	@Resource
	private IBizDispatchBillPayService bizDispatchBillPayService;
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
		
		String deliverid = "";
		if (null != param.get("deliverid")) {
			deliverid = param.get("deliverid").toString();
		}
        try {
        	//校验该费用是否已生成Excel文件
        	if (StringUtils.isNotBlank(deliverid)) {
        		Map<String, Object> queryEntity = new HashMap<String, Object>();
            	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_PAY_DIS.getCode());
            	queryEntity.put("param2", deliverid);
            	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
            	if (StringUtils.isNotEmpty(existDel)) {
            		return existDel;
            	}
			}
        	
        	String path = getBizPayExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_PAY_DIS.getCode() + deliverid + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setParam2(deliverid);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("createTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_PAY_DIS.getDesc() + deliverid);
        	entity.setTaskType(FileTaskTypeEnum.BIZ_PAY_DIS.getCode());
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
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
    					bmsErrorLogInfoEntity.setClassName("DispatchBillPayExportController");
    					bmsErrorLogInfoEntity.setMethodName("asynExport");
    					bmsErrorLogInfoEntity.setErrorMsg(e.toString());
    					bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
    					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("DispatchBillPayExportController");
			bmsErrorLogInfoEntity.setMethodName("asynExport");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_PAY_DIS.getDesc() + deliverid + MessageConstant.EXPORT_TASK_BIZ_MSG;
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
		String path = getBizPayExportPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====应收商品按托库存导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收商品按托库存导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //干线运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收商品按托库存导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 商品按托库存
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		//物流商
		if(myparam.get("logistics") != null && StringUtils.equalsIgnoreCase(myparam.get("logistics").toString(), "ALL")){
			myparam.put("logistics", null);
		}
		
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BizDispatchBillPayEntity> pageInfo = 
					bizDispatchBillPayService.queryAll(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PAY_DIS.getDesc(), 
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
		itemMap.put("title", "仓库");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家订单号");
		itemMap.put("columnWidth", 50);
		itemMap.put("dataKey", "externalNo");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单数量");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "waybillNum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单生成时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        /*itemMap = new HashMap<String, Object>();
        itemMap.put("title", "转寄后运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "zexpressnum");
        headInfoList.add(itemMap);*/
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "大状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bigstatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "小状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "smallstatus");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "系统逻辑重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "systemWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalqty");
        headInfoList.add(itemMap);

        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "月结账号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "monthFeeCount");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "快递业务类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "expresstype");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveProvinceId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveCityId");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "收件人区县");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveDistrictId");
        headInfoList.add(itemMap);
		
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizDispatchBillPayEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> warehouseMap = getWarehouse();
	        for (BizDispatchBillPayEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("warehouseName", warehouseMap.get(entity.getWarehouseCode()));
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("outstockNo", entity.getOutstockNo());
	        	dataItem.put("externalNo", entity.getExternalNo());
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("waybillNum", entity.getWaybillNum());
	        	dataItem.put("createTime", entity.getCreateTime());
//	        	dataItem.put("zexpressnum", entity.getZexpressnum());
	        	dataItem.put("bigstatus", entity.getBigstatus());
	        	dataItem.put("smallstatus", entity.getSmallstatus());
	        	dataItem.put("systemWeight", entity.getSystemWeight());
	        	dataItem.put("totalWeight", entity.getTotalWeight());
	        	dataItem.put("totalqty", entity.getTotalqty());
	        	dataItem.put("productDetail", entity.getProductDetail());
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("monthFeeCount", entity.getMonthFeeCount());
	        	dataItem.put("expresstype", entity.getExpresstype());
	        	dataItem.put("receiveName", entity.getReceiveName());
	        	String provinceId="";
	    		String cityId="";
	    		String districtId="";
	    		//调整省市区不为空
	    		if(StringUtils.isNotBlank(entity.getAdjustProvinceId())||
	    				StringUtils.isNotBlank(entity.getAdjustCityId())||
	    				StringUtils.isNotBlank(entity.getAdjustDistrictId())){
	    			provinceId=entity.getAdjustProvinceId();
	    			cityId=entity.getAdjustCityId();
	    			districtId=entity.getAdjustDistrictId();
	    		}else{
	    			provinceId=entity.getReceiveProvinceId();
	    			cityId=entity.getReceiveCityId();
	    			districtId=entity.getReceiveDistrictId();
	    		}
	        	dataItem.put("receiveProvinceId", provinceId);
	        	dataItem.put("receiveCityId",cityId);
	        	dataItem.put("receiveDistrictId", districtId);
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
}