/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.web;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.service.IFileExportTaskService;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInfoService;
import com.jiuyescm.bms.bill.receive.service.IBmsBillSubjectInfoService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.dispatch.service.IBizDispatchBillService;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialService;
import com.jiuyescm.bms.biz.storage.service.IBizPackStorageService;
import com.jiuyescm.bms.biz.storage.service.IBizProductStorageService;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianWayBillService;
import com.jiuyescm.bms.common.constants.BillConstant;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.enumtype.FileTaskTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.fees.IFeesBillService;
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.DateUtil;
import com.jiuyescm.common.utils.MD5Util;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.framework.lock.Lock;
import com.jiuyescm.framework.lock.LockCallback;
import com.jiuyescm.framework.lock.LockCantObtainException;
import com.jiuyescm.framework.lock.LockInsideExecutedException;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;

/**
 * 应收账单主表
 * @author yangss
 *
 */
@Controller("bmsBillInfoController")
public class BmsBillInfoController {

	private static final Logger logger = Logger.getLogger(BmsBillInfoController.class.getName());

	@Resource
	private IBmsBillInfoService bmsBillInfoService;
	@Resource
	private IPriceContractService priceContractService;
	@Resource
	private IBizOutstockMasterService bizOutstockMasterService;
	@Resource
	private IBizOutstockPackmaterialService bizOutstockPackmaterialService;
	@Resource 
	private IBizProductStorageService bizProductStorageService;
	@Resource
	private IBizPackStorageService bizPackStorageService;
	@Resource
	private IBizDispatchBillService bizDispatchBillService;
	@Resource
	private IBizGanxianWayBillService bizGanxianWayBillService;
	@Resource
	private IFileExportTaskService fileExportTaskService;
	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired 
	private IWarehouseService warehouseService;
	@Autowired 
	private IBmsBillSubjectInfoService  bmsBillSubjectInfoService;
	@Resource
	private IProjectService projectService;
	
	@Resource
	private IFeesBillService feesBillService;
	
	@Resource
	private IPriceContractService contractService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private Lock lock;
	
	final int pageSize = 20000;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsBillInfoEntity> page, Map<String, Object> param) {
		PageInfo<BmsBillInfoEntity> pageInfo = null;
		try {
			pageInfo = bmsBillInfoService.query(param, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		} catch (Exception e) {
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInfoController");
			bmsErrorLogInfoEntity.setMethodName("query");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
	}
	
	/**
	 * 默认生成账单时判断是否有生成计算异常的数据
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo haveCalculException(BmsBillInfoEntity entity){
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}
		if(null == entity){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}

		if (null != entity.getStartTime()) {
			entity.setStartTime(DateUtil.formatYYYYMMDD2TimestampBegin(entity.getStartTime()));
		}
		if(null != entity.getEndTime()){
			entity.setEndTime(DateUtil.formatYYYYMMDD2TimestampEnd(entity.getEndTime()));
		}
		
		//查找该宅配商的合同，判断是否签约提货费
		Map<String,Object> aCondition=new HashMap<>();
		aCondition.put("customerId", entity.getCustomerId());
		aCondition.put("contractTypeCode", "CUSTOMER_CONTRACT");
		List<PriceContractInfoEntity> contractList=priceContractService.queryContract(aCondition);	
		if(contractList.size()<=0){
			return new ResponseVo(ResponseVo.FAIL, "该商家未签合同，无法生成账单");
		}
		//合同
		PriceContractInfoEntity contract=contractList.get(0);
		Map<String,Object> condition=new HashMap<String,Object>();
		condition.put("customerid", contract.getCustomerId());
		condition.put("createTime", entity.getStartTime());
		condition.put("endTime", entity.getEndTime());
		
		StringBuffer strBuff = new StringBuffer();
		//根据业务类型判断费用科目
		Map<String,Object> contractCondition=new HashMap<String,Object>();
		contractCondition.put("contractCode", contract.getContractCode());
		List<ContractDetailEntity> detailList=priceContractService.findAllContractItem(contractCondition);
		for(int i=0;i<detailList.size();i++){
			ContractDetailEntity en=detailList.get(i);
			String subjectCode=en.getSubjectId();
			//订单操作费
			if("wh_b2c_work".equals(subjectCode)){
				//查询出库明细判断是否有计算异常的订单操作费
				BizOutstockMasterEntity bizOutstockMasterEntity=bizOutstockMasterService.queryExceptionOne(condition);
				if(null != bizOutstockMasterEntity){
					strBuff.append("订单操作费中存在异常费用,");
				}
			}else if("wh_material_use".equals(subjectCode)){
				//耗材使用费
				BizOutstockPackmaterialEntity bizOutstockPackmaterialEntity=bizOutstockPackmaterialService.queryExceptionOne(condition);
				if(null != bizOutstockPackmaterialEntity){
					strBuff.append("耗材使用费中存在异常费用,");
				}
			}else if("wh_product_storage".equals(subjectCode)){
				//商品存储费
				BizProductStorageEntity bizProductStorageEntity=bizProductStorageService.queryExceptionOne(condition);
				if(null != bizProductStorageEntity){
					strBuff.append("商品存储费中存在异常费用,");
				}
			}else if("wh_material_storage".equals(subjectCode)){
				//耗材存储费
				BizPackStorageEntity bizPackStorageEntity=bizPackStorageService.queryExceptionOne(condition);
				if(null != bizPackStorageEntity){
					strBuff.append("耗材存储费中存在异常费用,");
				}
			}else if("JIUYE_DISPATCH".equals(subjectCode)){
				condition.put("carrierId", "1500000016");
				//九曳配送费
				BizDispatchBillEntity bizDispatchBillEntity=bizDispatchBillService.queryExceptionOne(condition);
				if(null != bizDispatchBillEntity){
					strBuff.append("九曳配送费中存在异常费用,");
				}
			}else if("SHUNFENG_DISPATCH".equals(subjectCode)){
				condition.put("carrierId", "1500000015");
				//顺丰配送费
				BizDispatchBillEntity bizDispatchBillEntity=bizDispatchBillService.queryExceptionOne(condition);
				if(null != bizDispatchBillEntity){
					strBuff.append("顺丰配送费中存在异常费用,");
				}
			}
		}
		
		ResponseVo result = new ResponseVo();
		if (strBuff.length() > 0) {
			result = new ResponseVo(ResponseVo.FAIL, strBuff.toString());
		}else {
			result = new ResponseVo(ResponseVo.SUCCESS, "");
		}
		return result;
	}
	
	
	/**
	 * 生成账单
	 * @param entity
	 */
	@DataResolver
	public ResponseVo generBill(BmsBillInfoEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}
		long beginTime = System.currentTimeMillis();
		if(null == entity){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo responseVo = null;
		
		// projectId设置。1、当月已生成账单，从已生成的账单中取projectId; 2、当月未生成的，从MDM查询
		String customerId = entity.getCustomerId();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("endTime", entity.getEndTime());
		condition.put("customerId", customerId);
		List<BmsBillInfoEntity> bmsBillInfoList = bmsBillInfoService.queryBmsBill(condition);
		if (bmsBillInfoList.isEmpty()) {
			// 当月未生成过账单，直接从mdm中查询
			CusprojectRuleVo voEntity=projectService.queryCusProjectByCustomerId(customerId);
			if(voEntity!=null){
				entity.setProjectId(voEntity.getProjectid());
				entity.setProjectName(voEntity.getProjectName());
			}
		}else {
			BmsBillInfoEntity bmsBillInfo = bmsBillInfoList.get(0);
			entity.setProjectId(bmsBillInfo.getProjectId());
			entity.setProjectName(bmsBillInfo.getProjectName());
		}
		
		String md5String = MD5Util.getMd5("GENERBILL" + customerId);
		final BmsBillInfoEntity finalEntity = entity;
		responseVo = lock.lock(md5String, 200, new LockCallback<ResponseVo>() {
			@Override
			public ResponseVo handleObtainLock() {
				return handGenerBill(finalEntity);
			}
			
			@Override
			public ResponseVo handleNotObtainLock() throws LockCantObtainException {
				return new ResponseVo(ResponseVo.FAIL, BillConstant.GENER_BILL_LOCK_MSG);
			}
			
			@Override
			public ResponseVo handleException(LockInsideExecutedException e)
					throws LockInsideExecutedException {
				logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
				return new ResponseVo(ResponseVo.FAIL, e.getMessage());
			}
		});
			
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
		logger.info("======一共用时：" + (System.currentTimeMillis() - beginTime));
		return responseVo;
	}
	
	/**
	 * 生成账单操作
	 * @param entity
	 * @return
	 */
	private ResponseVo handGenerBill(BmsBillInfoEntity entity){
		ResponseVo responseVo = null;
		try {
			if (null != entity.getStartTime()) {
				entity.setStartTime(DateUtil.formatYYYYMMDD2TimestampBegin(entity.getStartTime()));
			}
			
			if(null != entity.getEndTime()){
				entity.setEndTime(DateUtil.formatYYYYMMDD2TimestampEnd(entity.getEndTime()));
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 100);
			
			//生成账单操作
			responseVo = bmsBillInfoService.generReceiveBill(entity);
			if (null == responseVo) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 1000);
				responseVo = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
		} catch (BizException e) {
			logger.error(ExceptionConstant.ASYN_REC_BILL_EXCEL_EX_MSG, e);
			responseVo = new ResponseVo(ResponseVo.FAIL, e.getMessage());
		} catch (Exception e) {
			logger.error(ExceptionConstant.ASYN_REC_BILL_EXCEL_EX_MSG, e);
			responseVo = new ResponseVo(ResponseVo.FAIL, MessageConstant.SYSTEM_ERROR_MSG);
		}
		return responseVo;
	}
	
	/**
	 * 获取商家 最后一次生成账单 时间
	 */
	@Expose
	public BmsBillInfoEntity queryLastBillTime(String parameter) throws Exception{
		Map<String, Object> param=new HashMap<String,Object>();
		param.put("customerId", parameter);
		BmsBillInfoEntity entity = bmsBillInfoService.queryLastBillTime(param);
		if (null != entity) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String dateString=formatter.format(entity.getEndTime());
			entity.setEndTime(Timestamp.valueOf(dateString));
		}
		return entity;
	}
	
	/**
	 * 确认账单
	 */
	@DataResolver
	public ResponseVo confirmFeesBill(BmsBillInfoEntity entity) throws Exception{
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(entity == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo result = null;
		try {
			result = bmsBillInfoService.confirmFeesBill(entity);
			if (null == result) {
				result = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
			//更新账单费用科目
			Map<String, Object> condition=new HashMap<String, Object>();
			condition.put("status", BillFeesSubjectStatusEnum.CONFIRM);
			condition.put("lastModifier", JAppContext.currentUserName());
			condition.put("lastModifyTime", JAppContext.currentTimestamp());
			condition.put("billNo", entity.getBillNo());
			bmsBillSubjectInfoService.updateBillSubject(condition);
		} catch (Exception e) {
			result = new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInfoController");
			bmsErrorLogInfoEntity.setMethodName("confirmFeesBill");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		return result;
	}
	
	/**
	 * 删除账单
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo deleteBill(BmsBillInfoEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(entity == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo result = null;
		try {
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setStatus(ConstantInterface.FeeStatus.STATUS_0);
			entity.setDelFlag(ConstantInterface.DelFlag.YES);
			result = bmsBillInfoService.deleteReceiveBill(entity);
			if (null == result) {
				result = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
		} catch (Exception e) {
			result = new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInfoController");
			bmsErrorLogInfoEntity.setMethodName("deleteBill");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		return result;
	}
	
	/**
	 * 结账
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo sellteBill(BmsBillInfoEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(entity == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo result = null;
		try {
			result = bmsBillInfoService.sellteBill(entity);
			if (null == result) {
				result = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
		} catch (Exception e) {
			result = new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInfoController");
			bmsErrorLogInfoEntity.setMethodName("sellteBill");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		return result;
	}
	
	/**
	 * 获取进度数值
	 */
	@Expose
	public int getProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag")); 
	}
    
	
	@Expose
	public void setProgress() {
		Object progressFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressFlag");
		if (progressFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
		} 
	}
	
	/**
	 * 异步生成账单文件
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@DataResolver
	public String asynExport(BmsBillInfoEntity paramer)throws Exception{
		if (null == paramer) {
			return MessageConstant.BILL_INFO_ISNULL_MSG;
		}
		String billno=paramer.getBillNo();
		if (StringUtils.isBlank(billno)) {
			return MessageConstant.BILL_NO_ISNULL_MSG;
		}
		
		//校验该账单是否已生成账单文件
		Map<String, Object> queryEntity = new HashMap<String, Object>();
		queryEntity.put("billNo", billno);

		PageInfo<FileExportTaskEntity> pageInfo = fileExportTaskService.query(queryEntity, 1, Integer.MAX_VALUE);
		if (null != pageInfo && pageInfo.getList().size() > 0) {
			return MessageConstant.BILL_FILE_ISEXIST_MSG;
		}
		
		String path = getPath();
		FileExportTaskEntity entity = new FileExportTaskEntity();
		entity.setTaskName(paramer.getBillName());
		entity.setBillNo(billno);
		entity.setStartTime(paramer.getStartTime());
		entity.setEndTime(paramer.getEndTime());
		entity.setTaskType(FileTaskTypeEnum.REC_BILL.getCode());
		entity.setTaskState(FileTaskStateEnum.BEGIN.getCode());
		entity.setProgress(0d);
		entity.setFilePath(path+ FileConstant.SEPARATOR + 
				FileConstant.REC_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX);
		entity.setCreator(JAppContext.currentUserName());
		entity.setCreateTime(JAppContext.currentTimestamp());
		entity.setDelFlag("0");
		entity = fileExportTaskService.save(entity);
		
		//生成账单文件
		final BmsBillInfoEntity condition = paramer;
		final String taskId = entity.getTaskId();
		new Thread(){
			public void run() {
				try {
					bmsBillSubjectInfoService.export(condition, taskId);
				} catch (Exception e) {
					updateExportTask(taskId, FileTaskStateEnum.FAIL.getCode(), 0);
					logger.error(ExceptionConstant.ASYN_REC_BILL_EXCEL_EX_MSG, e);
					//写入日志
					BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
					bmsErrorLogInfoEntity.setClassName("BmsBillInfoController");
					bmsErrorLogInfoEntity.setMethodName("asynExport");
					bmsErrorLogInfoEntity.setErrorMsg(e.toString());
					bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
					bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
				}
			};
		}.start();
		return MessageConstant.FILE_EXPORT_TASK_RECE_BILL_MSG;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_BILL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIVE_BILL");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 修改导出任务列表
	 * @param taskId
	 * @param process
	 * @param taskState
	 */
	private void updateExportTask(String taskId, String taskState, double process){
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		fileExportTaskService.update(entity);
	}
	
}
