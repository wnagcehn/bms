/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.entity.CalculateVo;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.fees.calculate.service.IFeesCalculateService;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizOutstockMasterController")
public class BizOutstockMasterController extends BaseController{

	private static final Logger logger = Logger.getLogger(BizOutstockMasterController.class.getName());

	@Resource
	private IBizOutstockMasterService bizOutstockMasterService;
	
	@Resource
	private IPriceContractService contractService;
	
	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;
	
	@Resource
	private IFeesCalculateService calculateService;
	
	@Resource
	private IFeesReceiveStorageService feesReceiveService;
	
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	

	@DataProvider
	public BizOutstockMasterEntity findById(Long id) throws Exception {
		BizOutstockMasterEntity entity = null;
		entity = bizOutstockMasterService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizOutstockMasterEntity> page, Map<String, Object> param) {
		if("ALL".equals(param.get("isCalculated"))){
			param.put("isCalculated", "");
		}
		if(null!=param.get("billTypeName")){
			String buf = (String)param.get("billTypeName");
			String str[] = buf.split(",");
			
			if(str.length>1){
				param.put("billTypeNameOther", "dd");
				param.put("str0", str[0]);
				param.put("str1", str[1]);
			}else if("ALL".equals(buf)){
				param.put("billTypeName", "");
			}
		}
		PageInfo<BizOutstockMasterEntity> pageInfo = bizOutstockMasterService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizOutstockMasterEntity entity) {
		if (entity.getId() == null) {
			bizOutstockMasterService.save(entity);
		} else {
			bizOutstockMasterService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizOutstockMasterEntity entity) {
		bizOutstockMasterService.delete(entity.getId());
	}
	
	@DataResolver
	public @ResponseBody Object update(BizOutstockMasterEntity entity){

		ReturnData result = new ReturnData();
		
		double  w = entity.getResizeWeight()==null?0: entity.getResizeWeight().doubleValue()+(entity.getTotalWeight()==null?0:entity.getTotalWeight().doubleValue());
		
		double  n = entity.getResizeNum()==null?0:entity.getResizeNum() + (entity.getTotalQuantity()==null?0:entity.getTotalQuantity());
		
		if(w<=0||n<=0){
			result.setCode("fail");
			result.setData("调整数量或重量不能小于0");
			
			return result;
		}
		
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid=JAppContext.currentUserName();
		
		//判断是否生成费用，判断费用的状态是否为未过账
		Map<String, Object> condition = new HashMap<String,Object>();
		condition.put("outstockNo", entity.getOutstockNo());
		String feesNo = entity.getFeesNo();
		PageInfo<FeesReceiveStorageEntity> pageInfo = null;
		FeesReceiveStorageEntity feesReceiveStorageEntity = null;
		if(StringUtils.isNotBlank(feesNo)){
			Map<String,Object> param = new  HashMap<String,Object>();
			param.put("feesNo", feesNo);
		    pageInfo = feesReceiveStorageService.query(param, 0, Integer.MAX_VALUE);
			if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
				feesReceiveStorageEntity=pageInfo.getList().get(0);
				//获取此时的费用状态
				String status = String.valueOf(feesReceiveStorageEntity.getStatus());
				if(status.equals("1")){
					result.setCode("fail");
					result.setData("该费用已过账，无法调整托数");
					return result;
				}
			}
		}
			
		//此为修改业务数据
		//根据名字查出对应的id
		entity.setLastModifier(userid);
		entity.setLastModifyTime(nowdate);
		
		
		int i = 0;
		entity.setIsCalculated("0");//设置为未计算
		i = bizOutstockMasterService.update(entity);
		if(i>0){
			result.setCode("SUCCESS");
		}else{
			result.setCode("fail");
			result.setData("更新失败");
		}
		
		return result;
	}
	
	@DataResolver
	public @ResponseBody Object reCount(List<BizOutstockMasterEntity> list){
		
		ReturnData result = new ReturnData();
		
		List<String>  arr = new  ArrayList<String>();
		
		Map<String,Object> aCondition=new HashMap<>();
		
		for(BizOutstockMasterEntity entity:list){
			
			Double initWeight = entity.getTotalWeight();
			Double initQuantity = entity.getTotalQuantity();
			if(entity.getTotalWeight()==null){
				entity.setTotalWeight(0.0);
			}
			if(entity.getTotalQuantity()==null)
			{
				entity.setTotalQuantity(0.0);
			}
			
			aCondition.put("customerid", entity.getCustomerid());
			aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
            
			List<PriceContractInfoEntity> contractEntity = contractService.queryContract(aCondition);
			FeesReceiveStorageEntity storageFeeEntity = null;
			
			if(entity.getResizeNum()==null){
				entity.setResizeNum(0.0);
			}
			if(entity.getResizeWeight()==null){
				entity.setResizeWeight(0.0);
			}
			entity.setTotalQuantity(entity.getTotalQuantity()+entity.getResizeNum());
			entity.setTotalWeight(entity.getTotalWeight()+entity.getResizeWeight());
			
			String subjectID = null;
			switch (entity.getBillTypeName()) {
			case "产地直配发货":
				subjectID = "wh_b2c_work";
				break;
			case "销售出库单":
				subjectID = "wh_b2c_work";
				break;
			case "B2B其他":
				subjectID = "wh_b2b_work";
				break;
			case "调拨出库单":
				subjectID = "wh_product_out";
				break;
			default:
				continue;
			}
			
			 CalculateVo calcuVo = new CalculateVo();
		     calcuVo.setBizTypeCode("STORAGE");//业务类型
		     calcuVo.setObj(entity);//业务数据
		     calcuVo.setContractCode(contractEntity.get(0).getContractCode());//合同编号
		     calcuVo.setSubjectId(subjectID);//费用科目
		     calcuVo = calculateService.calculate(calcuVo);
		     
		     if(calcuVo.getSuccess() && calcuVo.getPrice()!=null)
		     {
		    	    try {
						storageFeeEntity = new FeesReceiveStorageEntity();
						storageFeeEntity.setCreator(JAppContext.currentUserName());
						storageFeeEntity.setCreateTime(entity.getCreateTime());
						storageFeeEntity.setCustomerId(entity.getCustomerid());		//商家ID
						storageFeeEntity.setCustomerName(entity.getCustomerName());	//商家名称
						storageFeeEntity.setWarehouseCode(entity.getWarehouseCode());	//仓库ID
						storageFeeEntity.setWarehouseName(entity.getWarehouseName());	//仓库名称
						storageFeeEntity.setOrderType(entity.getBillTypeName());		//订单类型
						if(null!=entity.getTotalVarieties()){
							storageFeeEntity.setVarieties(entity.getTotalVarieties().intValue());
						}
						boolean isInsert = StringUtils.isEmpty(entity.getFeesNo())?true:false; //true-新增  false-更新
						String feesNo =StringUtils.isEmpty(entity.getFeesNo())?
								sequenceService.getBillNoOne(FeesReceiveStorageEntity.class.getName(), "STO", "0000000000")
								:entity.getFeesNo();
						storageFeeEntity.setFeesNo(feesNo);
						storageFeeEntity.setOrderNo(entity.getOutstockNo());			//oms订单号
						storageFeeEntity.setProductType("");							//商品类型
						storageFeeEntity.setQuantity((new Double(entity.getTotalQuantity())).intValue());//商品数量
						storageFeeEntity.setStatus("0");								//状态
						storageFeeEntity.setWeight(entity.getTotalWeight());
						storageFeeEntity.setOperateTime(entity.getCreateTime());
						storageFeeEntity.setCostType("FEE_TYPE_GENEARL");
						storageFeeEntity.setCost(calcuVo.getPrice());	//入仓金额
						storageFeeEntity.setRuleNo(calcuVo.getRuleno());
						storageFeeEntity.setUnitPrice(calcuVo.getUnitPrice());//生成单价
						storageFeeEntity.setSubjectCode(calcuVo.getSubjectId());
						storageFeeEntity.setBizId(String.valueOf(entity.getId()));//业务数据主键
						
						if(isInsert){
							feesReceiveStorageService.save(storageFeeEntity);
						}else{
							feesReceiveStorageService.update(storageFeeEntity);
						}
						entity.setFeesNo(feesNo);
						entity.setTotalWeight(initWeight);//恢复到以前
						entity.setTotalQuantity(initQuantity);
						entity.setIsCalculated("1");
						bizOutstockMasterService.update(entity);
					} catch (Exception e) {
						 arr.add(entity.getExternalNo());
						logger.error(e.getMessage());
					}
		     }else{
		    	 arr.add(entity.getExternalNo());
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
	 * 分组统计查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryGroup(Page<BizOutstockMasterEntity> page, Map<String, Object> param) {
		if("ALL".equals(param.get("isCalculated"))){
			param.put("isCalculated", "");
		}
		if(null!=param.get("billTypeName")){
			String buf = (String)param.get("billTypeName");
			String str[] = buf.split(",");
			
			if(str.length>1){
				param.put("billTypeNameOther", "dd");
				param.put("str0", str[0]);
				param.put("str1", str[1]);
			}else if("ALL".equals(buf)){
				param.put("billTypeName", "");
			}
		}
		PageInfo<BizOutstockMasterEntity> pageInfo = bizOutstockMasterService.queryGroup(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	
	@Expose
	public Properties validRetry(Map<String, Object> param) {
		Properties ret = bizOutstockMasterService.validRetry(param);
		return ret;
	}
	
	@Expose
	public String reCalculate(Map<String, Object> param){
		if(bizOutstockMasterService.reCalculate(param) == 0){
			return "重算异常";
		}
		return "操作成功! 正在重算...";
	}
		
	/**
	 * 导出
	 */
	@DataResolver
	public String asynExport(Map<String, Object> param) {
		if (null == param) {
			return MessageConstant.QUERY_PARAM_NULL_MSG;
		}
		
		String customerId = param.get("customerid").toString();
        try {
        	//校验该费用是否已生成Excel文件
        	Map<String, Object> queryEntity = new HashMap<String, Object>();
        	queryEntity.put("taskType", FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getCode());
        	queryEntity.put("customerid", customerId);
        	String existDel = fileExportTaskService.isExistDeleteTask(queryEntity);
        	if (StringUtils.isNotEmpty(existDel)) {
        		return existDel;
        	}
        	
        	String path = getBizReceiveExportPath();
        	String filepath=path+ FileConstant.SEPARATOR + 
        			FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getCode() + customerId + FileConstant.SUFFIX_XLSX;
        	
        	FileExportTaskEntity entity = new FileExportTaskEntity();
        	entity.setCustomerid(customerId);
        	entity.setStartTime(DateUtil.formatTimestamp(param.get("startTime")));
        	entity.setEndTime(DateUtil.formatTimestamp(param.get("endTime")));
        	entity.setTaskName(FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getDesc() + customerId);
        	entity.setTaskType(FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getCode());
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
    				}
    			};
    		}.start();
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG, e);
			return ExceptionConstant.ASYN_BIZ_EXCEL_EX_MSG;
		}
		return FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getDesc() + customerId + MessageConstant.EXPORT_TASK_BIZ_MSG;
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
		
		logger.info("====应收商品出库单导出：");
		//如果存放上传文件的目录不存在就新建
    	File storeFolder=new File(path);
		if(!storeFolder.isDirectory()){
			storeFolder.mkdirs();
		}
		
    	logger.info("====应收商品出库单导出：写入Excel begin.");
    	fileExportTaskService.updateExportTask(taskId, null, 30);
    	POISXSSUtil poiUtil = new POISXSSUtil();
    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
        //商品出库单
    	fileExportTaskService.updateExportTask(taskId, null, 70);
    	handBiz(poiUtil, workbook, filePath, param);
    	//最后写到文件
    	fileExportTaskService.updateExportTask(taskId, null, 90);
    	poiUtil.write2FilePath(workbook, filePath);
    	
    	fileExportTaskService.updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
    	logger.info("====应收商品出库单导出：写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
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
		if("ALL".equals(myparam.get("isCalculated"))){
			myparam.put("isCalculated", "");
		}
		if(null!=myparam.get("billTypeName")){
			String buf = (String)myparam.get("billTypeName");
			String str[] = buf.split(",");
			
			if(str.length>1){
				myparam.put("billTypeNameOther", "dd");
				myparam.put("str0", str[0]);
				myparam.put("str1", str[1]);
			}else if("ALL".equals(buf)){
				myparam.put("billTypeName", "");
			}
		}
		
		int pageNo = 1;
		int lineNo = 1;
		boolean doLoop = true;
		while (doLoop) {			
			PageInfo<BizOutstockMasterEntity> pageInfo = 
					bizOutstockMasterService.query(myparam, pageNo, FileConstant.EXPORTPAGESIZE);
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
			
			poiUtil.exportExcel2FilePath(poiUtil, workbook, FileTaskTypeEnum.BIZ_PRO_OUTSTOCK.getDesc(), 
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
		itemMap.put("title", "订单号");
		itemMap.put("columnWidth", 25);
		itemMap.put("dataKey", "outstockNo");
		headInfoList.add(itemMap);
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用编号");
        itemMap.put("columnWidth", 50);
        itemMap.put("dataKey", "feesNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "deliverName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流商名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "是否拆箱");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unpacking");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureTypeName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单据类型名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billTypeName");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "B2B标识");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "b2bFlag");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalQuantity");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总品种数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalVarieties");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "resizeNum");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "调整重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "resizeWeight");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "isCalculated");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "费用计算时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "calculateTime");
        headInfoList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "备注");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "remark");
        headInfoList.add(itemMap);
		
        return headInfoList;
	}
	
	private List<Map<String, Object>> getBizHeadItem(List<BizOutstockMasterEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	        Map<String, Object> dataItem = null;
	        Map<String, String> calculateMap = CalculateState.getMap();
	        for (BizOutstockMasterEntity entity : list) {
	        	dataItem = new HashMap<String, Object>();
	        	dataItem.put("outstockNo", entity.getOutstockNo());
	        	dataItem.put("feesNo", entity.getFeesNo());
	        	dataItem.put("warehouseName", entity.getWarehouseName());
	        	dataItem.put("externalNo", entity.getExternalNo());
	        	dataItem.put("customerName", entity.getCustomerName());
	        	dataItem.put("deliverName", entity.getDeliverName());
	        	dataItem.put("carrierName", entity.getCarrierName());
	        	dataItem.put("waybillNo", entity.getWaybillNo());
	        	dataItem.put("unpacking", entity.getUnpacking());
	        	dataItem.put("temperatureTypeName", entity.getTemperatureTypeName());
	        	dataItem.put("billTypeName", entity.getBillTypeName());
	        	dataItem.put("b2bFlag", entity.getB2bFlag());
	        	dataItem.put("totalWeight", entity.getTotalWeight());
	        	dataItem.put("totalQuantity", entity.getTotalQuantity());
	        	dataItem.put("totalVarieties", entity.getTotalVarieties());
	        	dataItem.put("resizeNum", entity.getResizeNum());
	        	dataItem.put("resizeWeight", entity.getResizeWeight());
	        	dataItem.put("isCalculated", calculateMap.get(entity.getIsCalculated()));
	        	dataItem.put("createTime", entity.getCreateTime());
	        	dataItem.put("calculateTime", entity.getCalculateTime());
	        	dataItem.put("remark", entity.getRemark());
	        	dataList.add(dataItem);
			}
	        
		return dataList;
	}
	
}
