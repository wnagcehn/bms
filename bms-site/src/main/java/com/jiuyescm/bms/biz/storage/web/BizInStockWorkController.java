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
import com.jiuyescm.bms.biz.storage.entity.BizInStockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.BizInStockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizInStockWorkService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

/**
 * 入库单
 * @author yangss
 */
@Controller("bizInStockWorKController")
public class BizInStockWorkController extends BaseController{
	
	private static final Logger logger = Logger.getLogger(BizInStockWorkController.class.getName());
	
	@Autowired private IBizInStockWorkService service;
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
	public void queryMaster(Page<BizInStockMasterEntity> page, Map<String, Object> param){
		if ("ALL".equals(param.get("isCalculated"))) {
			param.put("isCalculated", null);
		}
		PageInfo<BizInStockMasterEntity> pageInfo = service.queryMaster(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int)pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public @ResponseBody Object updateMaster(BizInStockMasterEntity entity){
		ReturnData result = new ReturnData();
		Timestamp operatorTime = JAppContext.currentTimestamp();
		String operatorName=JAppContext.currentUserName();
		if (null == entity) {
			result.setCode("fail");
			result.setData("请选择要调整的记录");
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
		BizInStockMasterEntity updateEntity = new BizInStockMasterEntity();
		updateEntity.setId(entity.getId());
		updateEntity.setAdjustNum(entity.getAdjustNum());
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
	 * 入库单明细表分页查询
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryDetail(Page<BizInStockDetailEntity> page, Map<String, Object> param){
		if ("999".equals(param.get("temperatureCode"))) {
			param.put("temperatureCode", null);
		}
		PageInfo<BizInStockDetailEntity> pageInfo = service.queryDetail(param, page.getPageNo(), page.getPageSize());
		if (null != pageInfo) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public @ResponseBody Object reCount(List<BizInStockMasterEntity> list){
		
		ReturnData result = new ReturnData();
		List<String>  arr = new  ArrayList<String>();
		Map<String,Object> aCondition=new HashMap<>();
		CalculateVo calcuVo = null;
		for(BizInStockMasterEntity instock :list){
            
			Double  tempDouble = instock.getNum();//初始化数量值,也就是原来的值，不能因为计算而改变
			if(instock.getNum()==null){
				instock.setNum(0.0);
			}
			calcuVo = new CalculateVo();
			//查询合同
			aCondition.put("customerid", instock.getCustomerid());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
			List<PriceContractInfoEntity> contractEntity = contractService.queryContract(aCondition);
			if(contractEntity == null ){
				continue;
			}
			calcuVo.setContractCode(contractEntity.get(0).getContractCode());//计算对象设置合同编号
			try{
				if(instock.getAdjustNum() == null){
					instock.setAdjustNum(0.0);
				}				
				instock.setNum(instock.getNum()+instock.getAdjustNum());//zhangzw 算钱的时候加起来
			}
			catch(Exception e){
//				logger.error("入库单计算商品数量异常");
				instock.setIsCalculated("2");//0-未计算  1-已计算  2-计算异常  
				//写入日志
				BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
				bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
				bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
				bmsErrorLogInfoEntity.setErrorMsg(e.toString());
				bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
				bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				continue;
			}
			if(StringUtils.isEmpty(instock.getInstockType())){
//				logger.error("未指定入库类型");
				instock.setIsCalculated("4");
				continue;
			}
			String subjectID = null;
			switch (instock.getInstockType()) {
			case "采购入库单":
				subjectID = "wh_instock_service";
				break;
			case "退货入库单":
				subjectID = "wh_return_storage";
				break;
			default:
				continue;
			}
			
//**********************************计算完毕，写入仓储费用表**********************************	
			calcuVo.setBizTypeCode("STORAGE");
			calcuVo.setSubjectId(subjectID);
			calcuVo.setObj(instock);
			//calcuVo = calculateService.calculate(calcuVo);
			if(calcuVo.getSuccess() && calcuVo.getPrice()!=null){
				//仓储费用表
				try {
					FeesReceiveStorageEntity storageFeeEntity = new FeesReceiveStorageEntity();
					storageFeeEntity.setQuantity(new Double(instock.getNum()).intValue());//商品数量
					storageFeeEntity.setCreator(JAppContext.currentUserName());
					//费用表的创建时间应为业务表的创建时间
					storageFeeEntity.setCreateTime(instock.getCreateTime());
					
					storageFeeEntity.setCustomerId(instock.getCustomerid());		//商家ID
					storageFeeEntity.setCustomerName(instock.getCustomerName());	//商家名称
					storageFeeEntity.setWarehouseCode(instock.getWarehouseCode());	//仓库ID
					storageFeeEntity.setWarehouseName(instock.getWarehouseName());	//仓库名称
					storageFeeEntity.setOrderType(instock.getInstockType());		//订单类型
					storageFeeEntity.setOrderNo(instock.getInstockNo());			//oms订单号
					storageFeeEntity.setProductType("");							//商品类型
					storageFeeEntity.setStatus("0");								//状态
					storageFeeEntity.setOperateTime(instock.getCreateTime());		//操作时间
					storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
					storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());		//费用科目
					storageFeeEntity.setRuleNo(calcuVo.getRuleno());
					storageFeeEntity.setCost(calcuVo.getPrice());	//入仓金额
					storageFeeEntity.setUnitPrice(calcuVo.getUnitPrice());//生成单价
					storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());
					//storageFeeEntity.setLastModifier(JAppContext.currentUserName());
					//storageFeeEntity.setLastModifyTime(JAppContext.currentTimestamp());
					storageFeeEntity.setBizId(String.valueOf(instock.getId()));//业务数据主键
					
					boolean isInsert = StringUtils.isEmpty(instock.getFeesNo())?true:false; //true-新增  false-更新
					String feesNo =StringUtils.isEmpty(instock.getFeesNo())?
							sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000")
							:instock.getFeesNo();
					storageFeeEntity.setFeesNo(feesNo);//费用编号
					instock.setFeesNo(feesNo);
					
					if(isInsert){
						feesReceiveStorageService.save(storageFeeEntity);
					}else{
						feesReceiveStorageService.update(storageFeeEntity);
					}
					instock.setIsCalculated("1");
					instock.setCalculateTime(JAppContext.currentTimestamp());		
					instock.setNum(tempDouble);//还原以前的数据 要不会累加
					service.updateMaster(instock);
				} catch (Exception e) {
					 arr.add(instock.getInstockNo());
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
					bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
					bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
					bmsErrorLogInfoEntity.setErrorMsg(e.toString());
					bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
				}
			}else{
				 arr.add(instock.getInstockNo());
			}
		}
		
		if(arr.size()>0){
			result.setCode("fail");
			StringBuffer buf = new StringBuffer();
			for(int i=0;i<arr.size();i++)
			{
				buf.append(arr.get(i));
				if(i!=arr.size()-1){
					buf.append(",");
				}
			}	
			if(arr.size()==list.size()){
				result.setData("全部未更新成功！");
			}else{
				result.setData("未更新成功的出库单号是："+buf.toString());
			}
					
		}else{
			result.setCode("SUCCESS");
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
        try {
        	//校验该费用是否已生成Excel文件
        	Map<String, Object> queryEntity = new HashMap<String, Object>();
        	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_INSTOCK.getCode());
        	queryEntity.put("customerid", customerId);
        	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
        	if (StringUtils.isNotEmpty(existDel)) {
        		return existDel;
        	}
        	
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_INSTOCK.getCode() + customerId + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setCustomerid(customerId);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_INSTOCK.getDesc() + customerId);
        	entity.setTaskType(FileTaskTypeEnum.BIZ_INSTOCK.getCode());
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
		return FileTaskTypeEnum.BIZ_INSTOCK.getDesc() + customerId + MessageConstant.EXPORT_TASK_BIZ_MSG;
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
			PageInfo<BizInStockMasterEntity> pageInfo = 
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
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_INSTOCK.getDesc(), 
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
        itemMap.put("title", "数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "num");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "adjustNum");
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
	
	private List<Map<String, Object>> getBizHeadItem(List<BizInStockMasterEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        for (BizInStockMasterEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("instockNo", entity.getInstockNo());
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("externalNum", entity.getExternalNum());
	        	dataItem.put("instockType", entity.getInstockType());
	        	dataItem.put("num", entity.getNum());
	        	dataItem.put("adjustNum", entity.getAdjustNum());
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
