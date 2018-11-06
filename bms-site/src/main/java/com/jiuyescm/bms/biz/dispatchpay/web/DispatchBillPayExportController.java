
package com.jiuyescm.bms.biz.dispatchpay.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataResolver;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillPayService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("dispatchBillPayExportController")
public class DispatchBillPayExportController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(DispatchBillPayExportController.class.getName());
	
	@Resource
	private IBizDispatchBillPayService bizDispatchBillPayService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Autowired
	private IWarehouseService warehouseService;
	@Autowired 
	private ICustomerService customerService;
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Resource
	private IBizOutstockPackmaterialService bizOutstockPackmaterialServiceImpl;
	
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
        itemMap.put("title", "泡重");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "originThrowWeight");
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
        itemMap.put("title", "物流产品类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "servicename");
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
	        	dataItem.put("originThrowWeight", entity.getOriginThrowWeight());
	        	dataItem.put("totalqty", entity.getTotalqty());
	        	dataItem.put("productDetail", entity.getProductDetail());
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("monthFeeCount", entity.getMonthFeeCount());
	        	dataItem.put("servicename", entity.getServicename());
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
	
	private Map<String, String> getwarehouse() {
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	private Map<String, String> getCustomer() {
		List<CustomerVo> CustomerVos = customerService.queryAll();
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (CustomerVo customerVo : CustomerVos) {
			map.put(customerVo.getCustomerid(), customerVo.getCustomername());
		}
		return map;
	}
	
	/**
	 * 原始数据导出
	 */
	@DataResolver
	public String originAsynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		Map<String, String> warehouseMap = getWarehouse();
		Map<String, String> customerMap = getCustomer();
		
		String customerId = "";
		if (null != param.get("customerId")) {
			customerId = param.get("customerId").toString();
		}
		String warehouseCode = "";
		if (null != param.get("warehouseCode")) {
			warehouseCode = param.get("warehouseCode").toString();
		}
        try {
        	//校验该费用是否已生成Excel文件
        	if (StringUtils.isNotBlank(customerId) && StringUtils.isBlank(warehouseCode)) {
        		Map<String, Object> queryEntity = new HashMap<String, Object>();
            	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_PACK_OUTSTOCK.getCode());
            	queryEntity.put("taskName", "原始耗材出库明细"+customerMap.get(customerId));
            	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
            	if (StringUtils.isNotEmpty(existDel)) {
            		return existDel;
            	}
			}
        	
        	if (StringUtils.isNotBlank(warehouseCode) && StringUtils.isBlank(customerId)) {
        		Map<String, Object> queryEntity = new HashMap<String, Object>();
            	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_PACK_OUTSTOCK.getCode());
            	queryEntity.put("taskName", "原始耗材出库明细"+warehouseMap.get(warehouseCode));
            	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
            	if (StringUtils.isNotEmpty(existDel)) {
            		return existDel;
            	}
			}
        	
        	String path = getPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_PACK_OUTSTOCK.getCode() + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	if (!"".equals(customerId) && "".equals(warehouseCode)) {
        		entity.setTaskName(FileTaskTypeEnum.BIZ_PAY_DIS_ORIGIN_DATA.getDesc() + customerMap.get(customerId));
			}else if (!"".equals(warehouseCode) && "".equals(customerId)) {
				entity.setTaskName(FileTaskTypeEnum.BIZ_PAY_DIS_ORIGIN_DATA.getDesc() + warehouseMap.get(warehouseCode));
			}else {
				entity.setTaskName(FileTaskTypeEnum.BIZ_PAY_DIS_ORIGIN_DATA.getDesc());
			}
        	entity.setTaskType(FileTaskTypeEnum.BIZ_PACK_OUTSTOCK.getCode());
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
    					originExport(condition, taskId,filePath);
    				} catch (Exception e) {
    					fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
    					logger.error(ExceptionConstant.ASYN_REC_DISPATCH_FEE_EXCEL_EX_MSG, e);
    					
    					//写入日志
    					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
    					bmsErrorLogInfoEntity.setClassName("DispatchBillPayExportController");
    					bmsErrorLogInfoEntity.setMethodName("originAsynExport");
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
			bmsErrorLogInfoEntity.setMethodName("originAsynExport");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_PAY_DIS_ORIGIN_DATA.getDesc() + MessageConstant.EXPORT_TASK_BIZ_MSG;
	}
	
	/**
	 * 异步导出
	 * @param param
	 * @param taskId
	 * @param file
	 * @throws Exception
	 */
	private void originExport(Map<String, Object> param,String taskId,String filePath)throws Exception{
		fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
		String path = getPath();
		long beginTime = System.currentTimeMillis();
		
		logger.info("====原始耗材导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
		// 如果文件存在直接删除，重新生成
		String fileName = "原始耗材出库明细" + FileConstant.SUFFIX_XLSX;
		String filepath = path + FileConstant.SEPARATOR + fileName;
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		
    	logger.info("====原始耗材导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //运单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handOriginMaterial(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====原始耗材导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 原始耗材
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handOriginMaterial(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		
		List<Map<String, Object>> dataPackMaterialList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> headPackMaterialMapList = null;
		//List<FeesReceiveMaterial> dataList = new ArrayList<FeesReceiveMaterial>();
		
		List<PubMaterialInfoVo> materialInfoList = getAllMaterial();
		List<BizOutstockPackmaterialEntity> ListHead = bizOutstockPackmaterialServiceImpl
				.queryOriginMaterialFromBizData(myparam);
		List<String> materialCodeList = getMaterialCodeList(ListHead);
		headPackMaterialMapList = getHeadPackMaterialMap(materialCodeList, materialInfoList);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				
		int lineNo = 1;
//		int pageNo = 1;
//		boolean doLoop = true;
//		while (doLoop) {
//			PageInfo<FeesReceiveMaterial> packMaterialList = bizOutstockPackmaterialServiceImpl
//					.queryMaterialFromBizData(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
//			if (packMaterialList.getList().size() < FileConstant.EXPORTPAGESIZE) {
//				doLoop = false;
//			} else {
//				pageNo += 1;
//			}
//			dataList.addAll(packMaterialList.getList());
//		}
		
		for (BizOutstockPackmaterialEntity materialEntity : ListHead) {
			boolean flag = false;
			Map<String, Object> matchMap = null;
			for (Map<String, Object> map : dataPackMaterialList) {
				if (map.get("waybillNo").equals(
						materialEntity.getWaybillNo())) {
					flag = true;
					matchMap = map;
					break;
				}
			}
			if (flag) {
				// 检查耗材类型
				String marterialType = getMaterialType(materialInfoList,
						materialEntity.getConsumerMaterialCode());
				if (matchMap.containsKey(marterialType + "_name")) {
					matchMap.put(
							marterialType + "_name",
							matchMap.get(marterialType + "_name") + ","
									+ materialEntity.getConsumerMaterialCode() == null ? ""
									: materialEntity.getConsumerMaterialCode());
					if (materialEntity.getConsumerMaterialCode().contains("GB")) {
						matchMap.put(
								marterialType + "_count",
								matchMap.get(marterialType + "_count")
										+ "," + materialEntity.getWeight() == null ? ""
										: Double.valueOf(materialEntity.getWeight()));
					} else {
						matchMap.put(
								marterialType + "_count",
								matchMap.get(marterialType + "_count")
										+ ","
										+ materialEntity.getNum() == null ? ""
										: Double.valueOf(materialEntity.getNum()));
					}
				} else {
					matchMap.put(marterialType + "_name",
							materialEntity.getConsumerMaterialCode() == null ? ""
									: materialEntity.getConsumerMaterialCode());
					if (materialEntity.getConsumerMaterialCode().contains("GB")) {
						matchMap.put(marterialType + "_count",
								materialEntity.getWeight() == null ? ""
										: Double.valueOf(materialEntity.getWeight()));
					} else {
						matchMap.put(marterialType + "_count",
								materialEntity.getNum() == null ? ""
										: Double.valueOf(materialEntity.getNum()));
					}
				}
			} else {
				Map<String, Object> dataItem = new HashMap<String, Object>();
				dataItem.put("createTime",sdf.format(materialEntity.getCreateTime()));
				dataItem.put("warehouseName",materialEntity.getWarehouseName());
				dataItem.put("customerName",materialEntity.getCustomerName());
				dataItem.put("outstockNo", materialEntity.getOutstockNo());
				dataItem.put("waybillNo", materialEntity.getWaybillNo());
				String marterialType = getMaterialType(materialInfoList,
						materialEntity.getConsumerMaterialCode());
				dataItem.put(marterialType + "_name",
						materialEntity.getConsumerMaterialCode());
				dataItem.put(marterialType + "_code",
						materialEntity.getConsumerMaterialCode());
				dataItem.put(marterialType + "_type",
						materialEntity.getSpecDesc());
				if (materialEntity.getConsumerMaterialCode().contains("GB")) {
					dataItem.put(marterialType + "_count", materialEntity
							.getWeight() == null ? "" : Double.valueOf(materialEntity
							.getWeight()));
				} else {
					dataItem.put(marterialType + "_count", materialEntity
							.getNum() == null ? "" : Double.valueOf(materialEntity
							.getNum()));
				}
				dataPackMaterialList.add(dataItem);
			}		
		}
		
		if (headPackMaterialMapList != null && dataPackMaterialList != null) {
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PAY_DIS_ORIGIN_DATA.getDesc(), 
					lineNo, headPackMaterialMapList, dataPackMaterialList);
		}	
		
	}
	
	
	private List<PubMaterialInfoVo> getAllMaterial() {
		Map<String, Object> conditionMap = Maps.newHashMap();
		conditionMap.put("delFlag", 0);
		return pubMaterialInfoService.queryList(conditionMap);
	}
	
	private List<String> getMaterialCodeList(List<BizOutstockPackmaterialEntity> ListHead) {
		List<String> materialCodeList = new ArrayList<String>();
		for (BizOutstockPackmaterialEntity entity : ListHead) {
			materialCodeList.add(entity.getConsumerMaterialCode());
		}
		return materialCodeList;
	}
	
	private List<Map<String, Object>> getHeadPackMaterialMap(List<String> materialCodeList,
			List<PubMaterialInfoVo> materialInfoList) {
		Map<String, String> headMap = getHeadMap(materialCodeList, materialInfoList);
		List<Map<String, Object>> headMapList = new ArrayList<Map<String, Object>>();
		Set<String> set = headMap.keySet();
		for (String key : set) {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			itemMap = new HashMap<String, Object>();
			itemMap.put("title", headMap.get(key));
			itemMap.put("columnWidth", 25);
			itemMap.put("dataKey", key);
			headMapList.add(itemMap);
		}
		return headMapList;
	}
	
	private Map<String, String> getHeadMap(List<String> materialCodeList, List<PubMaterialInfoVo> materialInfoList) {
		Map<String, String> map = Maps.newLinkedHashMap();
		map.put("createTime", "出库日期");
		map.put("warehouseName", "仓库");
		map.put("customerName", "商家");
		map.put("outstockNo", "出库单号");
		map.put("waybillNo", "运单号");
		
		// 按序号获取systemCode表中的类别并排序
		Map<String, Object> param = new HashMap<>();
		param.put("typeCode", "PACKMAGERIAL_SORT");
		List<SystemCodeEntity> entityList = systemCodeService.queryBySortNo(param);
		final List<String> orderList = new ArrayList<>();
		for (SystemCodeEntity entity : entityList) {
			orderList.add(entity.getCodeName());
		}

		List<String> materialTypeList = new ArrayList<String>();
		HashMap<String, Object> codeMap = new HashMap<>();
		for (String code : materialCodeList) {
			String marterialType = getMaterialType(materialInfoList, code);

			if (!materialTypeList.contains(marterialType)) {
				materialTypeList.add(marterialType);
				codeMap.put(marterialType, code);
			}
		}

		// 按上述list进行排序
		Collections.sort(materialTypeList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
		    	if (orderList.indexOf(o1) >= 0 && orderList.indexOf(o2) >= 0) {
					return orderList.indexOf(o1)-orderList.indexOf(o2);
				}else if (orderList.indexOf(o1) >= 0 && orderList.indexOf(o2) <= 0) {
					return -1;
				}else if (orderList.indexOf(o1) <= 0 && orderList.indexOf(o2) >= 0) {
					return 1;
				}else {
					return 0;
				}
			}
		});

		// 遍历输出
		for (String marterialTypeDetail : materialTypeList) {
			map.put(marterialTypeDetail + "_name", marterialTypeDetail);
			if (((String) codeMap.get(marterialTypeDetail)).contains("GB")) {
				map.put(marterialTypeDetail + "_count", marterialTypeDetail+"重量");
			} else {
				map.put(marterialTypeDetail + "_count", marterialTypeDetail+"数量");
			}
		}
		return map;
	}
	
	private String getMaterialType(List<PubMaterialInfoVo> materialInfoList, String materialCode) {
		for (PubMaterialInfoVo infoVo : materialInfoList) {
			if (StringUtils.isNotBlank(infoVo.getBarcode()) && infoVo.getBarcode().equals(materialCode)) {
				return infoVo.getMaterialType();
			}
		}
		return "其他";
	}
	
	/**
	 * 获取原始耗材出库明细导出的文件路径
	 * @return
	 */
	public String getPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_ORIGIN_PAY_BIZ");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_ORIGIN_PAY_BIZ");
		}
		return systemCodeEntity.getExtattr1();
	}
	
}