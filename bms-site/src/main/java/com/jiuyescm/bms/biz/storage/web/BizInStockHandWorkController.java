package com.jiuyescm.bms.biz.storage.web;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.base.system.BaseController;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizInStockHandWorkService;
import com.jiuyescm.bms.biz.storage.vo.BizInstockHandworkVo;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

/**
 * 入库单
 * @author yangss
 */
@Controller("bizInStockHandWorkController")
public class BizInStockHandWorkController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(BizInStockHandWorkController.class.getName());
	
	@Autowired private IBizInStockHandWorkService service;
	@Resource  private IFeesReceiveStorageService feesReceiveStorageService;
	@Resource  private IPriceContractService contractService;
	@Resource private SequenceService sequenceService;
	@Resource private IFileExportTaskService fileExportTaskService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	/**
	 * 入库单主表分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryMaster(Page<BizInstockHandworkVo> page, Map<String, Object> param){
		if ("ALL".equals(param.get("isCalculated"))) {
			param.put("isCalculated", null);
		}
		PageInfo<BizInstockHandworkVo> pageInfo = service.queryMaster(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int)pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public @ResponseBody Object updateMaster(BizInstockHandworkVo entity){
		ReturnData result = new ReturnData();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		String operatorName=JAppContext.currentUserName();
		if (null == entity) {
			result.setCode("fail");
			result.setData("请选择要调整的记录");
			return result;
		}
		//是否有费用编号
		if (StringUtils.isNotBlank(entity.getFeesNo())) {
			//判断是否生成费用，判断费用的状态是否为未过账
			Map<String, Object> condition = new HashMap<String,Object>();
			condition.put("feesNo", entity.getFeesNo());
			PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageService.query(condition, 0, Integer.MAX_VALUE);
			if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
				FeesReceiveStorageEntity feesReceiveStorageEntity = pageInfo.getList().get(0);
				//获取此时的费用状态
				String status = feesReceiveStorageEntity.getStatus();
				if(status.equals("1")){
					result.setCode("fail");
					result.setData("该费用已过账，无法调整数量");
					return result;
				}
			}
		}
		
		//更新
		BizInstockHandworkVo updateEntity = new BizInstockHandworkVo();
		updateEntity.setId(entity.getId());
		updateEntity.setAdjustWeight(entity.getAdjustWeight());
		updateEntity.setIsCalculated("0");//如果没有过账的话,就允许调整数量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
		updateEntity.setLastModifier(operatorName);
		updateEntity.setLastModifyTime(operatorTime);
		int updateNum = service.updateMaster(updateEntity);
		if(updateNum > 0){
			result.setCode("SUCCESS");
		}else{
			result.setCode("fail");
			result.setData("更新失败");
		}
		return result;
	}
	
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		String customerId = param.get("customerId").toString();
		String customerName = param.get("customerName").toString();
        try {
        	//校验该费用是否已生成Excel文件
        	Map<String, Object> queryEntity = new HashMap<String, Object>();
        	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_INSTOCK_HANDWORK.getCode());
        	queryEntity.put("customerid", customerId);
        	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
        	if (StringUtils.isNotEmpty(existDel)) {
        		return existDel;
        	}
        	
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_INSTOCK_HANDWORK.getCode() + customerId + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setCustomerid(customerId);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_INSTOCK_HANDWORK.getDesc() + "【"+customerName+"】");
        	entity.setTaskType(FileTaskTypeEnum.BIZ_INSTOCK_HANDWORK.getCode());
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
    					bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
    					bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
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
			bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
			bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setIdentify("启动线程失败");
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_INSTOCK_HANDWORK.getDesc() +"【"+customerName+"】" + MessageConstant.EXPORT_TASK_BIZ_MSG;
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
		
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====入库单导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //入库单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====入库单导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	}
	
	/**
	 * 入库单
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param myparam
	 * @throws Exception
	 */
	private void handBiz(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, Map<String, Object> myparam)throws Exception{
		if ("ALL".equals(myparam.get("isCalculated"))) {
			myparam.put("isCalculated", null);
		}
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BizInstockHandworkVo> pageInfo = 
					service.queryMaster(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_INSTOCK_HANDWORK.getDesc(), 
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
		itemMap.put("title", "入库单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "instockNo");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "仓库名称");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "warehouseName");
		headInfoList.add(itemMap);
		
		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "商家");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "customerName");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "externalNum");
        headInfoList.add(itemMap);
        
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "入库类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "instockType");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建者");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "creator");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
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
		
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizInstockHandworkVo> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        for (BizInstockHandworkVo entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("instockNo", entity.getInstockNo());
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("externalNum", entity.getExternalNum());
	        	dataItem.put("instockType", entity.getInstockType());
	        	dataItem.put("num", entity.getWeight());
	        	dataItem.put("adjustNum", entity.getAdjustWeight());
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataItem.put("creator", entity.getCreator());
	        	dataItem.put("remark", entity.getRemark());
	        	dataItem.put("feesNo", entity.getFeesNo());
	        	dataItem.put("isCalculated", calculateMap.get(entity.getIsCalculated()));
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = service.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(service.reCalculate(param) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
}
