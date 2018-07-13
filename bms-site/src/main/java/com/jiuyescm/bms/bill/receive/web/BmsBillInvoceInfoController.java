/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInvoceInfoEntity;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInfoService;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInvoceInfoService;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.BillInvoceStatusEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.utils.ArithUtil;

/**
 * 账单开票
 * @author yangss
 *
 */
@Controller("bmsBillInvoceInfoController")
public class BmsBillInvoceInfoController {

	private static final Logger logger = Logger.getLogger(BmsBillInvoceInfoController.class.getName());

	@Resource
	private IBmsBillInvoceInfoService bmsBillInvoceInfoService;
	@Resource
	private IBmsBillInfoService bmsBillInfoService;
	@Autowired
	private ISystemCodeService systemCodeService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	@DataProvider
	public BmsBillInvoceInfoEntity findById(Long id) throws Exception {
		BmsBillInvoceInfoEntity entity = null;
		entity = bmsBillInvoceInfoService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsBillInvoceInfoEntity> page, Map<String, Object> param) {
		PageInfo<BmsBillInvoceInfoEntity> pageInfo = bmsBillInvoceInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 统计开票信息
	 * @param param
	 * @return
	 */
	@DataProvider
	public List<BmsBillInvoceInfoEntity> queryCountInvoceInfo(Map<String, Object> param){
		if(Session.isMissing()){
			return null;
		}
		if(param == null ||null == param.get("id") ||
				null == param.get("billNo") || StringUtils.isEmpty(param.get("billNo").toString())){
			return null;
		}
		List<BmsBillInvoceInfoEntity> list = null;
		try {
			BmsBillInfoEntity billEntity = bmsBillInfoService.findById(Long.valueOf(param.get("id").toString()));
			if (null == billEntity) {
				return null;
			}
			
			list = new ArrayList<BmsBillInvoceInfoEntity>();
			//param.put("status", BillInvoceStatusEnum.UNSETTLED.getCode());
			BmsBillInvoceInfoEntity entity = bmsBillInvoceInfoService.queryCountInvoceInfo(param);
			if (null != entity) {
				entity.setBillNo(billEntity.getBillNo());
				entity.setBillName(billEntity.getBillName());
				entity.setCustomerName(billEntity.getCustomerName());
				entity.setTotalAmount(billEntity.getTotalAmount());
				entity.setVersion(billEntity.getVersion());
				entity.setUnReceiveAmount(ArithUtil.sub(entity.getInvoceAmount(),entity.getReceiptAmount()));
				//未开票金额=账单实收金额-已开票金额
				if (null == entity.getInvoceAmount()) {
					entity.setUnInvoceAmount(entity.getTotalAmount());
				}else {
					entity.setUnInvoceAmount(ArithUtil.sub(entity.getTotalAmount(), entity.getInvoceAmount()));
				}
				/**
				 * 开票金额设定
				 * 未开票金额 >= 开票限额 = 开票限额
				 * 0 < 未开票金额 < 限额 = 未开票金额
				 */
				double invoceAmountLimit = queryInvoceAmountLimit();
				if(entity.getUnInvoceAmount() >= invoceAmountLimit){
					entity.setInvoceAmount(invoceAmountLimit);
				}else if (entity.getUnInvoceAmount() >= 0 && entity.getUnInvoceAmount() < invoceAmountLimit) {
					entity.setInvoceAmount(entity.getUnInvoceAmount());
				}
				entity.setIsNeedInvoce("UNNEED");
				list.add(entity);
			}else {
				//没开过票
				entity = new BmsBillInvoceInfoEntity();
				entity.setBillNo(billEntity.getBillNo());
				entity.setBillName(billEntity.getBillName());
				entity.setCustomerName(billEntity.getCustomerName());
				entity.setTotalAmount(billEntity.getTotalAmount());
				entity.setUnInvoceAmount(billEntity.getTotalAmount());
				entity.setVersion(billEntity.getVersion());
				entity.setUnReceiveAmount(0.0);
				entity.setReceiptAmount(0.0);
				/**
				 * 开票金额设定
				 * 未开票金额 >= 开票限额 = 开票限额
				 * 0 < 未开票金额 < 限额 = 未开票金额
				 */
				double invoceAmountLimit = queryInvoceAmountLimit();
				if(entity.getUnInvoceAmount() >= invoceAmountLimit){
					entity.setInvoceAmount(invoceAmountLimit);
				}else if (entity.getUnInvoceAmount() > 0 && entity.getUnInvoceAmount() < invoceAmountLimit) {
					entity.setInvoceAmount(entity.getUnInvoceAmount());
				}
				entity.setIsNeedInvoce("UNNEED");
				list.add(entity);
			}
		} catch (Exception e) {
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInvoceInfoController");
			bmsErrorLogInfoEntity.setMethodName("queryCountInvoceInfo");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		
		return list;
	}

	/**
	 * 开票
	 * @param entity
	 */
	@DataResolver
	public ResponseVo openInvoce(BmsBillInvoceInfoEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}
		if(null == entity){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo responseVo = null;
		try {
			responseVo = bmsBillInvoceInfoService.openInvoce(entity);
			if (null == responseVo) {
				responseVo = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
		} catch (Exception e) {
			responseVo = new ResponseVo(ResponseVo.FAIL, MessageConstant.SYSTEM_ERROR_MSG);
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInvoceInfoController");
			bmsErrorLogInfoEntity.setMethodName("openInvoce");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		
		return responseVo;
	}

	/**
	 * 删除发票
	 * @param entity
	 */
	@DataResolver
	public ResponseVo delInvoce(BmsBillInvoceInfoEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}
		if(null == entity){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo responseVo = null;
		try {
			responseVo = bmsBillInvoceInfoService.delInvoce(entity);
			if (null == responseVo) {
				responseVo = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
		} catch (Exception e) {
			responseVo = new ResponseVo(ResponseVo.FAIL, MessageConstant.SYSTEM_ERROR_MSG);
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInvoceInfoController");
			bmsErrorLogInfoEntity.setMethodName("delInvoce");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		
		return responseVo;
	}
	
	
	/**
	 * 统计收款信息
	 * @param param
	 * @return
	 */
	@DataProvider
	public List<BmsBillInvoceInfoEntity> queryCountReceiptInfo(Map<String, Object> param){
		if(Session.isMissing()){
			return null;
		}
		if(param == null || null == param.get("id") ||
				null == param.get("billNo") || StringUtils.isEmpty(param.get("billNo").toString())){
			return null;
		}
		List<BmsBillInvoceInfoEntity> list = null;
		try {
			param.put("receiptStatus", BillInvoceStatusEnum.UNSETTLED.getCode());
			BmsBillInvoceInfoEntity entity = bmsBillInvoceInfoService.queryCountReceiptInfo(param);
			if (null != entity) {
				list = new ArrayList<BmsBillInvoceInfoEntity>();
				list.add(entity);
			}
		} catch (Exception e) {
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInvoceInfoController");
			bmsErrorLogInfoEntity.setMethodName("queryCountReceiptInfo");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		
		return list;
	}
	
	/**
	 * 收款
	 * @param entity
	 */
	@DataResolver
	public ResponseVo receipt(BmsBillInvoceInfoEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}
		if(null == entity){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		ResponseVo responseVo = null;
		try {
			responseVo = bmsBillInvoceInfoService.receipt(entity);
			if (null == responseVo) {
				responseVo = new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}
		} catch (Exception e) {
			responseVo = new ResponseVo(ResponseVo.FAIL, MessageConstant.SYSTEM_ERROR_MSG);
			logger.error(MessageConstant.SYSTEM_ERROR_MSG, e);
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName("BmsBillInvoceInfoController");
			bmsErrorLogInfoEntity.setMethodName("receipt");
			bmsErrorLogInfoEntity.setErrorMsg(e.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
		}
		
		return responseVo;
	}
	
	@DataResolver
	public void save(BmsBillInvoceInfoEntity entity) {
		if (entity.getId() == null) {
			bmsBillInvoceInfoService.save(entity);
		} else {
			bmsBillInvoceInfoService.update(entity);
		}
	}

	/**
	 * 查询开票限额
	 * @return
	 */
	private double queryInvoceAmountLimit(){
		double limit = 0.0;
		List<SystemCodeEntity> invoceList = systemCodeService.findEnumList("INVOCE_AMOUNT");
		if (null != invoceList && invoceList.size() > 0) {
			SystemCodeEntity entity = invoceList.get(0);
			limit = Double.valueOf(entity.getCode());
		}
		return limit;
	}
	
}
