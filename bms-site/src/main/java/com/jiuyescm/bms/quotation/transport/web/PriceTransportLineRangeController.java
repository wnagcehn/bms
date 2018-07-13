/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.constants.TransportMessageConstant;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.enumtype.TransportProductTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineRangeService;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("transportLineRangeController")
public class PriceTransportLineRangeController {

	private static final Logger logger = Logger.getLogger(PriceTransportLineRangeController.class.getName());

	@Resource
	private IPriceTransportLineService priceTransportLineService;
	
	@Resource
	private IPriceTransportLineRangeService priceTransportLineRangeService;

	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceTransportLineRangeEntity> page, Map<String, Object> param) {
		if(param != null){
			param.put("delFlag", "0");
		}
		PageInfo<PriceTransportLineRangeEntity> pageInfo = priceTransportLineRangeService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceTransportLineRangeEntity entity) {
		if(Session.isMissing()){
			return MessageConstant.SESSION_INVALID_MSG;
		}else if(entity == null){
			return MessageConstant.PAGE_PARAM_ERROR_MSG;
		}
		if (StringUtils.isEmpty(entity.getLineId())) {
			return TransportMessageConstant.LINEID_NULL_MSG;
		}
		
		try {
			//校验该路线信息
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("id", entity.getLineId());
			List<PriceTransportLineEntity> lineList = priceTransportLineService.query(condition);
			if (null == lineList || lineList.size() <= 0) {
				return entity.getLineId() + TransportMessageConstant.LINEID_NOTFIND_MSG;
			}
			
			//根据产品类型不同校验运输路线非空信息
		/*	String errorInfo = checkTransportLineRangeSaveInfo(entity, lineList.get(0));
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}*/
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
			priceTransportLineRangeService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增运输线路报价,线路ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		} catch (Exception e) {
			logger.error(ExceptionConstant.LINERANGE_SAVE_EXCEPTION_MSG, e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			return ExceptionConstant.LINERANGE_SAVE_EXCEPTION_MSG;
		}
		return ConstantInterface.StatusResult.SUCC;
	}
	
	@DataResolver
	public String update(PriceTransportLineRangeEntity entity){
		if(Session.isMissing()){
			return MessageConstant.SESSION_INVALID_MSG;
		}else if(entity == null){
			return MessageConstant.PAGE_PARAM_ERROR_MSG;
		}
		if (StringUtils.isEmpty(entity.getLineId())) {
			return TransportMessageConstant.LINEID_NULL_MSG;
		}
		try{
			//校验该路线信息
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("id", entity.getLineId());
			List<PriceTransportLineEntity> lineList = priceTransportLineService.query(condition);
			if (null == lineList || lineList.size() <= 0) {
				return entity.getLineId() + TransportMessageConstant.LINEID_NOTFIND_MSG;
			}
			//根据产品类型不同校验运输路线非空信息
			String errorInfo = checkTransportLineRangeSaveInfo(entity, lineList.get(0));
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
			
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportLineRangeService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新运输线路报价,线路ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.LINERANGE_UPDATE_EXCEPTION_MSG, ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return ExceptionConstant.LINERANGE_UPDATE_EXCEPTION_MSG;
		}
		return ConstantInterface.StatusResult.SUCC;
	}

	@DataResolver
	public String delete(PriceTransportLineRangeEntity entity) {
		try{
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportLineRangeService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废运输线路报价,线路ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}
	
	
	/**
	 * 校验运输路线信息-新增
	 * @return
	 */
	private String checkTransportLineRangeSaveInfo(PriceTransportLineRangeEntity entity, 
			PriceTransportLineEntity lineEntity){
		Map<String,Object> myParamMap=new HashMap<String,Object>();
		myParamMap.put("code", lineEntity.getTransportTypeCode());
		List<SystemCodeEntity> sList=systemCodeService.queryCodeList(myParamMap);
		//父类类型
		String superTransportTypeCode="";
		//现属类型
		String transportTypeCode="";
		if(sList!=null && sList.size()>0){
			superTransportTypeCode=sList.get(0).getExtattr1();
			transportTypeCode=sList.get(0).getCode();
		}

		String errorInfo = "";
		if (isNullInfo(transportTypeCode)) {
			return TransportMessageConstant.LINE_TRANSPORTTYPE_NULL_MSG;
		}
		if (isNullDouble(entity.getUnitPrice())) {
			return TransportMessageConstant.UNITPRICE_NULL_GT0_MSG;
		}
		if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_TCZC.getCode().equals(transportTypeCode)) {
			//同城
			errorInfo = checkNullInfoTC(entity);
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
		}else if (TransportProductTypeEnum.CJLD.getCode().equals(superTransportTypeCode)) {
			//城际
			errorInfo = checkNullInfoCJ(entity);
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
		}else if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_DSZL.getCode().equals(transportTypeCode)) {
			//电商专列
			errorInfo = checkNullInfoDSZL(entity);
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
		}else if(TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_HXD.getCode().equals(transportTypeCode)) {
			//航鲜达
			errorInfo = checkNullInfoHXD(entity);
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
		}
		return null;
	}
	
	/**
	 * 非空校验-同城
	 */
	private String checkNullInfoTC(PriceTransportLineRangeEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//始发城市、始发区 、目的城市、目的区 、 车型
		if (isNullInfo(entity.getCarModelCode())){
			errorBuff.append(TransportMessageConstant.CARMODEL_NULL_MSG);
		}
		return errorBuff.toString();
	}
	
	/**
	 * 非空校验-城际
	 */
	private String checkNullInfoCJ(PriceTransportLineRangeEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//始发城市、  目的城市、是否泡货、重量/体积
		if (isNullDouble(entity.getWeightLowerLimit()) && 
				isNullDouble(entity.getWeightUpperLimit()) && 
				isNullDouble(entity.getVolumeLowerLimit()) && 
				isNullDouble(entity.getVolumeUpperLimit())){
			errorBuff.append(TransportMessageConstant.WEIGHT_VOLUME_GT0_MSG);
		}
		return errorBuff.toString();
	}
	
	/**
	 * 非空校验-电商专列
	 */
	private String checkNullInfoDSZL(PriceTransportLineRangeEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//始发城市、始发区 、目的城市、目的区 、 车型
		if (isNullInfo(entity.getCarModelCode())){
			errorBuff.append(TransportMessageConstant.CARMODEL_NULL_MSG);
		}
		return errorBuff.toString();
	}
	
	/**
	 * 非空校验-航鲜达
	 */
	private String checkNullInfoHXD(PriceTransportLineRangeEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		if(entity.getWeightLowerLimit()==null){
			errorBuff.append("重量下限不能为空!");
			return errorBuff.toString();
		}
		if(entity.getWeightUpperLimit()==null)
		{
			errorBuff.append("重量上限不能为空!");
			return errorBuff.toString();
		}
		//机场、  目的省份、目的城市、 重量
		if (isNullDouble(entity.getWeightUpperLimit())) 
		{
			errorBuff.append("重量上限不能为0!");
			return errorBuff.toString();
		}
		if (entity.getWeightUpperLimit()<entity.getWeightLowerLimit()) {
			errorBuff.append(TransportMessageConstant.WEIGHT_UPPER_MSG);
		}
		return errorBuff.toString();
	}
	
	/**
	 * 判断是否大于0
	 * @param d1
	 * @return
	 */
	private boolean isNullDouble(Double d1){
		if (null == d1 || 0.0 >= d1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为空
	 * @param info
	 * @return
	 */
	private boolean isNullInfo(String info){
		if (StringUtils.isEmpty(info)) {
			return true;
		}
		return false;
	}
	
}
