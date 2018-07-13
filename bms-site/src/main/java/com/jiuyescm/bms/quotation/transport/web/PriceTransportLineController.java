/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.web;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.service.IPubAirportService;
import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.constants.TransportMessageConstant;
import com.jiuyescm.bms.common.entity.ErrorMessageVo;
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
import com.jiuyescm.cfm.common.sequence.SequenceGenerator;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.BeanToMapUtil;
import com.jiuyescm.common.utils.FileOperationUtil;
import com.jiuyescm.common.utils.excel.FileReaderFactory;
import com.jiuyescm.common.utils.excel.IFileReader;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DataProperty;
import com.jiuyescm.common.utils.upload.TransportLineRangeImportTemplateDataType;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("transportLineController")
public class PriceTransportLineController {

	private static final Logger logger = Logger.getLogger(PriceTransportLineController.class.getName());

	@Resource
	private IPriceTransportLineService priceTransportLineService;
	
	@Resource
	private IPriceTransportLineRangeService priceTransportLineRangeService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Resource
	private IPubAirportService airportService;
	
	@Resource 
	private IEwareHouseService elecWareHouseService;
	
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
	public void query(Page<PriceTransportLineEntity> page, Map<String, Object> param) {
		if(param != null){
			param.put("delFlag", "0");
		}
		PageInfo<PriceTransportLineEntity> pageInfo = priceTransportLineService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceTransportLineEntity> page, PriceTransportLineEntity lineEntity) {
		if(lineEntity != null){
			lineEntity.setDelFlag("0");
		}
		Map<String, Object> lineParam = this.convertObjToMap(lineEntity);
		PageInfo<PriceTransportLineEntity> pageInfo = priceTransportLineService.query(lineParam, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
		
	}
	
	/** 
     * 实体类对象转换成Map 
     * @param obj 
     * @return 
     */  
    public static Map<String, Object> convertObjToMap(Object obj) {  
        Map<String, Object> resultMap = new HashMap<String, Object>();  
        if (obj == null){  
            return null;
        }
        try{
        	 BeanInfo info = Introspector.getBeanInfo(obj.getClass());  
             for (PropertyDescriptor pd : info.getPropertyDescriptors()) {  
                 Method reader = pd.getReadMethod();  
                 //内容为null的过滤掉  
                 if (reader == null || reader.invoke(obj) == null) {  
                     continue;  
                 }  
                //默认继承Object类的属性，过滤掉  
                 if (pd.getName().equalsIgnoreCase("class")) {  
                     continue;  
                 }  
                 resultMap.put(pd.getName(), reader.invoke(obj));  
             }  
	        printOut(resultMap);
        }catch(Exception e){
        	logger.error(obj.getClass(), e);
        	return null;
        }
        return resultMap;  
    } 
    /** 
     * 打印结果 
     * */  
    private static void printOut(Map<String, Object> resMap) {  
        for (String key : resMap.keySet()) {  
            logger.info("Key:" + key + ";Value:" + resMap.get(key));  
        }  
    }  

	@DataResolver
	public String save(PriceTransportLineEntity entity) {
		if(Session.isMissing()){
			return MessageConstant.SESSION_INVALID_MSG;
		}else if(entity == null){
			return MessageConstant.PAGE_PARAM_ERROR_MSG;
		}
		//根据产品类型不同校验运输路线非空信息
		/*String errorInfo = checkTransportLineSaveInfo(entity);
		if (!isNullInfo(errorInfo)) {
			return errorInfo;
		}*/
		try {
			String templateNo =sequenceService.getBillNoOne(PriceTransportLineEntity.class.getName(), "TL", "00000");
			entity.setTransportLineNo(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
			priceTransportLineService.save(entity);
			
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增运输线路,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		} catch (Exception e) {
			logger.error(ExceptionConstant.TRANSLINE_SAVE_EXCEPTION_MSG, e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		
		return ConstantInterface.StatusResult.SUCC;
	}
	
	/**
	 * 校验运输路线信息-新增
	 * @return
	 */
	private String checkTransportLineSaveInfo(PriceTransportLineEntity entity){
		//获取产品类型			
		Map<String,Object> myParamMap=new HashMap<String,Object>();
		myParamMap.put("code", entity.getTransportTypeCode());
		List<SystemCodeEntity> sList=systemCodeService.queryCodeList(myParamMap);
		//父类类型
		String superTransportTypeCode="";
		//现属类型
		String transportTypeCode="";
		
		if(sList!=null && sList.size()>0){
			superTransportTypeCode=sList.get(0).getExtattr1();
			transportTypeCode=entity.getTransportTypeCode();
		}

		String errorInfo = "";
		if (isNullInfo(transportTypeCode)) {
			return TransportMessageConstant.TRANSPORTTYPE_NULL_MSG;
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
		}else if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_HXD.getCode().equals(transportTypeCode)) {
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
	private String checkNullInfoTC(PriceTransportLineEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//始发省份、始发城市、目的省份 、目的城市、 车型
		if (isNullInfo(entity.getFromProvinceId())){
			errorBuff.append(TransportMessageConstant.FROMPROVINCE_NULL_MSG);
		}
		if (isNullInfo(entity.getFromCityId())){
			errorBuff.append(TransportMessageConstant.FROMCITY_NULL_MSG);
		}
		if (isNullInfo(entity.getToProvinceId())) {
			errorBuff.append(TransportMessageConstant.TOPROVINCE_NULL_MSG);
		}
		if (isNullInfo(entity.getToCityId())) {
			errorBuff.append(TransportMessageConstant.TOCITY_NULL_MSG);
		}
		return errorBuff.toString();
	}
	
	/**
	 * 非空校验-城际
	 */
	private String checkNullInfoCJ(PriceTransportLineEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//始发省份、始发城市、 目的省份、 目的城市、是否泡货、重量/体积
		if (isNullInfo(entity.getFromProvinceId())){
			errorBuff.append(TransportMessageConstant.FROMPROVINCE_NULL_MSG);
		}
		if (isNullInfo(entity.getFromCityId())){
			errorBuff.append(TransportMessageConstant.FROMCITY_NULL_MSG);
		}
		if (isNullInfo(entity.getToProvinceId())) {
			errorBuff.append(TransportMessageConstant.TOPROVINCE_NULL_MSG);
		}
		if (isNullInfo(entity.getToCityId())) {
			errorBuff.append(TransportMessageConstant.TOCITY_NULL_MSG);
		}
		return errorBuff.toString();
	}
	
	/**
	 * 非空校验-电商专列
	 */
	private String checkNullInfoDSZL(PriceTransportLineEntity entity){
		Map<String ,String> eWareHouseMap=getEwareHouse();
		StringBuffer errorBuff = new StringBuffer();
		//始发仓 、 电商仓(目的站点)、 车型
		if(isNullInfo(entity.getFromWarehouseId())){
			errorBuff.append(TransportMessageConstant.FROMWAREHOUSE_NULL_MSG);
		}
		//电商仓
		if(isNullInfo(entity.getEndStation())){
			errorBuff.append(TransportMessageConstant.ENDSTATION_NULL_MSG);
		}else {
			//电商仓库设置到结束站点
			if(!eWareHouseMap.containsKey(entity.getEndStation()) || isNullInfo(eWareHouseMap.get(entity.getEndStation()))){
				errorBuff.append("目的站点["+entity.getEndStation()+"]没有在电商仓库表中维护!");
			}
		}
		return errorBuff.toString();
	}
	
	/**
	 * 非空校验-航鲜达
	 */
	private String checkNullInfoHXD(PriceTransportLineEntity entity){
		Map<String,PubAirportEntity>  airportMap=getAirMap();	
		StringBuffer errorBuff = new StringBuffer();
		//机场、  目的省份、目的城市、 重量
		if (isNullInfo(entity.getStartStation())) {
			errorBuff.append(TransportMessageConstant.STARTSTATION_NULL_MSG);
		}else {
			//机场设置到开始站点
			if(!airportMap.containsKey(entity.getStartStation()) || null == airportMap.get(entity.getStartStation())){
				errorBuff.append("始发站点["+entity.getStartStation()+"]没有在机场表中维护!");
			}else {
				PubAirportEntity airportEntity = airportMap.get(entity.getStartStation());
				entity.setToProvinceId(airportEntity.getProvince());
				entity.setToCityId(airportEntity.getCity());
			}
		}
	
		return errorBuff.toString();
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
	
	@DataResolver
	public String saveAll(Collection<PriceTransportLineEntity> datas) {
		if (datas == null) {
			return MessageConstant.PAGE_PARAM_ERROR_MSG;
		}
		for (PriceTransportLineEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				return this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				
				//根据产品类型不同校验运输路线非空信息
				String errorInfo = checkTransportLineSaveInfo(temp);
				if (!isNullInfo(errorInfo)) {
					return errorInfo;
				}
				
				priceTransportLineService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新运输线路,模板编码【"+temp.getTransportLineNo()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_line");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTransportLineNo());
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				temp.setDelFlag("1");
				priceTransportLineService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("作废运输线路,模板编码【"+temp.getTransportLineNo()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_line");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.CANCEL.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTransportLineNo());
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else {
				// do nothing;
			}
		}
		return null;
	}
	
	@DataResolver
	public String update(PriceTransportLineEntity entity){
		if(Session.isMissing()){
			return MessageConstant.SESSION_INVALID_MSG;
		}else if(entity == null){
			return MessageConstant.PAGE_PARAM_ERROR_MSG;
		}
		//根据产品类型不同校验运输路线非空信息
		String errorInfo = checkTransportLineUpdateInfo(entity);
		if (!isNullInfo(errorInfo)) {
			return errorInfo;
		}
		
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportLineService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废运输线路,模板编码【"+entity.getTransportLineNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTransportLineNo());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.TRANSLINE_UPDATE_EXCEPTION_MSG, ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return ExceptionConstant.TRANSLINE_UPDATE_EXCEPTION_MSG;
		}
		return ConstantInterface.StatusResult.SUCC;
	}
	
	/**
	 * 校验运输路线信息
	 * @return
	 */
	private String checkTransportLineUpdateInfo(PriceTransportLineEntity entity){
		//获取产品类型			
		Map<String,Object> myParamMap=new HashMap<String,Object>();
		myParamMap.put("code", entity.getTransportTypeCode());
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
			return TransportMessageConstant.TRANSPORTTYPE_NULL_MSG;
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
			errorInfo = checkNullUpdateInfoDSZL(entity);
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
		}else if (TransportProductTypeEnum.TCZC.getCode().equals(superTransportTypeCode) && TransportProductTypeEnum.TCZC_HXD.getCode().equals(transportTypeCode)) {
			//航鲜达
			errorInfo = checkNullUpdateInfoHXD(entity);
			if (!isNullInfo(errorInfo)) {
				return errorInfo;
			}
		}
		return null;
	}
	
	/**
	 * 修改
	 * 非空校验-电商专列
	 */
	private String checkNullUpdateInfoDSZL(PriceTransportLineEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//始发仓 、 电商仓、 车型
		if(isNullInfo(entity.getFromWarehouseId())){
			errorBuff.append(TransportMessageConstant.FROMWAREHOUSE_NULL_MSG);
		}
		//电商仓
		if(isNullInfo(entity.getEndStation()) && isNullInfo(entity.getElecWarehouseCode())){
			errorBuff.append(TransportMessageConstant.ENDSTATION_NULL_MSG);
		}else if (!isNullInfo(entity.getElecWarehouseCode())) {
			//电商仓库设置到结束站点
			entity.setEndStation(entity.getElecWarehouseCode());
		}
		return errorBuff.toString();
	}
	
	/**
	 * 修改
	 * 非空校验-航鲜达
	 */
	private String checkNullUpdateInfoHXD(PriceTransportLineEntity entity){
		StringBuffer errorBuff = new StringBuffer();
		//机场、  重量
		if (isNullInfo(entity.getStartStation()) && isNullInfo(entity.getAirPort())) {
			errorBuff.append(TransportMessageConstant.STARTSTATION_NULL_MSG);
		}else if (isNullInfo(entity.getStartStation()) && !isNullInfo(entity.getAirPort())){
			//机场设置到开始站点
			entity.setStartStation(entity.getAirPort());
		}
		return errorBuff.toString();
	}

	@DataResolver
	public String delete(PriceTransportLineEntity entity) {
		try{
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportLineService.update(entity);
			//找到运输路线对应的梯度报价
			Map<String, Object> rangeParam = new HashMap<String, Object>();
			rangeParam.put("lineId", entity.getId());
			rangeParam.put("delFlag", "0");
			List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeService.query(rangeParam);
			if(rangeList != null && rangeList.size()>0){
				for(PriceTransportLineRangeEntity range: rangeList){
					range.setDelFlag("1");
					range.setLastModifier(JAppContext.currentUserName());
					range.setLastModifyTime(JAppContext.currentTimestamp());
					//删除梯度报价
					priceTransportLineRangeService.update(range);
				}
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废运输线路,模板编码【"+entity.getTransportLineNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_transport_line");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTransportLineNo());
				model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return ConstantInterface.StatusResult.SUCC;
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}
	
	@FileProvider
	public DownloadFile accquireTransportLineRangeTemplate(Map<String, String> parameter) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/transport/transport_line_range_template.xlsx");
		return new DownloadFile("运输路线梯度报价导入模板.xlsx", is);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 导入运输梯度模板
	 * @param file
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileResolver
	public Map<String, Object> importTransportLineRange(UploadFile file, Map<String, Object> parameter) throws Exception {
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 10);
		long starTime=System.currentTimeMillis();	//开始时间
		
		//获取当前的id
		String id=(String)parameter.get("id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 校验信息（报错提示）
		List<ErrorMessageVo> infoList = new ArrayList<ErrorMessageVo>();
		ErrorMessageVo errorVo = null;
		
		// 导入成功返回模板信息
		List<PriceTransportLineRangeEntity> lineRangeList = new ArrayList<PriceTransportLineRangeEntity>();
		// 当期时间
		Timestamp currentTime = JAppContext.currentTimestamp();
		String userName = JAppContext.currentUserName();
		try {
			BaseDataType bs = new TransportLineRangeImportTemplateDataType();
			// 检查导入模板是否正确
			boolean isTemplate = FileOperationUtil.checkExcelTitle(file, bs);
			if (!isTemplate) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("Excel导入格式错误请参考标准模板检查!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 100);
			// 解析Excel
			lineRangeList = readExcelProduct(file,bs);
			if (null == lineRangeList || lineRangeList.size() <= 0) {
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 999);
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("导入的Excel数据为空或者数据格式不对!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				return map;
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 300);
			
			String currentNo = SequenceGenerator.uuidOf36String("t");// 当前操作ID
			/*condition.put("tempid", currentNo);*/
			
			// 模板信息必填项校验
			map = impExcelCheckInfoProduct(infoList, lineRangeList, map, currentNo);
			if (map.get(ConstantInterface.ImportExcelStatus.IMP_ERROR) != null) { // 有基本的错误信息(必填，数据类型不正确)
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 999);
				return map;
			}
			
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 700);
			// 获得初步校验后和转换后的数据
			lineRangeList = (List<PriceTransportLineRangeEntity>) map.get(ConstantInterface.ImportExcelStatus.IMP_SUCC);
			
			
			//设置属性
			for(int i=0;i<lineRangeList.size();i++){
				PriceTransportLineRangeEntity lineRange = lineRangeList.get(i);
				String billNo = id;
				lineRange.setLineId(billNo);
				lineRange.setDelFlag("0");// 设置为未作废
				lineRange.setCreator(userName);
				lineRange.setCreateTime(currentTime);
				lineRange.setLastModifier(userName);
				lineRange.setLastModifyTime(currentTime);
			}
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 800);
			//插入正式表
			int insertNum = priceTransportLineRangeService.saveList(lineRangeList);
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 950);
			
			if (insertNum <= 0) {
				errorVo = new ErrorMessageVo();
				errorVo.setMsg("存档运输路线梯度报价失败!");
				infoList.add(errorVo);
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 999);
				return map;
			}else{
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, "0");
				DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 1000);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData("");
					model.setOldData("");
					model.setOperateDesc("导入运输线路,共计【"+insertNum+"】条");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_transport_line");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.IMPORT.getCode());
					model.setRemark("");
					model.setOperateTableKey("");
					model.setUrlName(RecordLogUrlNameEnum.IN_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return map;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 999);
		}

		return map;
	}
	
	
	/**
	 * 读取Excel中的数据（不包含列名）
	 * 
	 * @param file
	 * @return
	 */
	private List<PriceTransportLineRangeEntity> readExcelProduct(UploadFile file,BaseDataType bs) {
		String fileSuffix = StringUtils.substringAfterLast(file.getFileName(), ".");
		IFileReader reader = FileReaderFactory.getFileReader(fileSuffix);
		try {
			List<Map<String, String>> list = reader.getFileContent(file.getInputStream());
			List<Map<String, String>> datas = Lists.newArrayList();
			//BaseDataType ddt = new DispatchTemplateDataType();
			List<DataProperty> props = bs.getDataProps();
			for (Map<String, String> map : list) {
				Map<String, String> data = Maps.newHashMap();
				for (DataProperty prop : props) {
					
					data.put(prop.getPropertyId(), map.get(prop.getPropertyName().toLowerCase()));
				}
				datas.add(data);
			}
			List<PriceTransportLineRangeEntity> lineList = Lists.newArrayList();
			for (Map<String, String> data : datas) {
				PriceTransportLineRangeEntity p = (PriceTransportLineRangeEntity) BeanToMapUtil.convertMapNull(PriceTransportLineRangeEntity.class, data);
				lineList.add(p);
			}
			return lineList;
		} catch (Exception e) {
			e.printStackTrace();
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}
		return null;
	}
	
	
	/**
	 * 校验Excel里面的内容合法性
	 * 
	 * @param infoList
	 * @param list
	 * @param map
	 * @param objList
	 * @param currentNo
	 * @return
	 */
	private Map<String, Object> impExcelCheckInfoProduct(List<ErrorMessageVo> infoList, List<PriceTransportLineRangeEntity> rangeList, Map<String, Object> map, String currentNo) {
		int lineNo = 0;
		Map<String, String> carModelMap = new HashMap<String, String>();
		Map<String, String> productTypeMap = new HashMap<String, String>();
		Map<String, String> temperatureMap = new HashMap<String, String>();
		//当导入数据量达到多少时,先一次加载再判断;否则数据量小的时候每次检索校验.
		if(rangeList != null && rangeList.size()>0){
			//以下是基础数据字典
			//(1)车型
			Map<String, Object> carModelParam = new HashMap<String, Object>();
			carModelParam.put("typeCode", "MOTORCYCLE_TYPE");
			List<SystemCodeEntity> carModelList = systemCodeService.queryCodeList(carModelParam);
			if(carModelList != null && carModelList.size()>0){
				for(SystemCodeEntity scEntity : carModelList){
					carModelMap.put(scEntity.getCodeName(), scEntity.getCode());
				}
			}
			//(2)品类
			Map<String, Object> productTypeParam=new HashMap<String, Object>();
			productTypeParam.put("typeCode", "PRODUCT_CATEGORY_TYPE");
			List<SystemCodeEntity> productTypeList = systemCodeService.queryCodeList(productTypeParam);
			if(productTypeList != null && productTypeList.size()>0){
				for(SystemCodeEntity scEntity : carModelList){
					productTypeMap.put(scEntity.getCodeName(), scEntity.getCode());
				}
			}
			
			//(3)温度类型
			//温度类型
			Map<String, Object> temperatureParam = new HashMap<String, Object>();
			temperatureParam.put("typeCode", "TEMPERATURE_TYPE");
			List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(temperatureParam);
			if(temperatureList != null && temperatureList.size()>0){
			    for(SystemCodeEntity scEntity : temperatureList){
			    	temperatureMap.put(scEntity.getCodeName(), scEntity.getCode());
			    }
			}
		}
		for (int i = 0; i < rangeList.size(); i++) {
			PriceTransportLineRangeEntity range = rangeList.get(i);
			lineNo = lineNo+1;
			
			//判断温度类型是否在时效表中维护
			String temperatureTypeName= range.getTemperatureTypeName();
			if(StringUtils.isNotBlank(temperatureTypeName)){
				if(temperatureMap.containsKey(temperatureTypeName) && StringUtils.isNotBlank(temperatureMap.get(temperatureTypeName))){
					logger.info(String.format("从温度类型缓存Map中取数据[%s]", temperatureTypeName));
					range.setTemperatureTypeCode(temperatureMap.get(temperatureTypeName));
				}else{
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("typeCode", "TEMPERATURE_TYPE");
					param.put("codeName", temperatureTypeName);
					List<SystemCodeEntity> temperatureList = systemCodeService.queryCodeList(param);
					if(temperatureList==null || temperatureList.size()==0){
						setMessage(infoList, lineNo,"该温度类型没有在表中维护!");
					}else{
						range.setTemperatureTypeCode(temperatureList.get(0).getCode());
						//放入缓存temperatureMap
						temperatureMap.put(temperatureList.get(0).getCodeName(), temperatureList.get(0).getCode());
					}
				}
			}
			
			//判断品类是否在字典表中维护
			String productTypeName=range.getProductTypeName();
			if(StringUtils.isNotBlank(productTypeName)){
				if(productTypeMap.containsKey(productTypeName) && StringUtils.isNotBlank(productTypeMap.get(productTypeName))){
					logger.info(String.format("从品类缓存Map中取数据[%s]", productTypeName));
					range.setProductTypeCode(productTypeMap.get(productTypeName));
				}else{
					Map<String, Object> param=new HashMap<String, Object>();
					param.put("typeCode", "PRODUCT_CATEGORY_TYPE");
					param.put("codeName", productTypeName);
					List<SystemCodeEntity> productTypeList = systemCodeService.queryCodeList(param);
					if(productTypeList==null || productTypeList.size()==0){
						setMessage(infoList, lineNo,"该品类没有在表中维护!");
					}else{
						range.setProductTypeCode(productTypeList.get(0).getCode());
						//放入缓存productTypeMap
						productTypeMap.put(productTypeName, productTypeList.get(0).getCode());
					}
				}
			}
			
			//判断车型是否在字典表中维护
			String carModelName=range.getCarModelName();
			if(StringUtils.isNotBlank(carModelName)){
				//从缓存carModelMap中直接取
				if(carModelMap.containsKey(carModelName) && StringUtils.isNotBlank(carModelMap.get(carModelName))){
					logger.info(String.format("从车型缓存Map中取数据[%s]", carModelName));
					range.setCarModelCode(carModelMap.get(carModelName));
				}else{
					Map<String, Object> param=new HashMap<String, Object>();
					param.put("typeCode", "MOTORCYCLE_TYPE");
					param.put("codeName", carModelName);
					List<SystemCodeEntity> carModelList = systemCodeService.queryCodeList(param);
					if(carModelList==null || carModelList.size()==0){
						setMessage(infoList, lineNo,"该车型没有在表中维护!");
					}else{
						range.setCarModelCode(carModelList.get(0).getCode());
						//放入缓存carModelMap
						carModelMap.put(carModelName, carModelList.get(0).getCode());
					}
				}
			}
			
			//判断【最低起运】不能为非数字
			if(range.getMinWeightShipment() !=null && !isNumber(range.getMinWeightShipment())){
				setMessage(infoList, lineNo,"【最低起运】不能为非数字!");
			}
			//判断【距离下限】不能为非数字
			if(range.getMinDistance() !=null && !isNumber(range.getMinDistance())){
				setMessage(infoList, lineNo,"【距离下限】不能为非数字!");
			}
			//判断【距离上限】不能为非数字
			if(range.getMaxDistance() !=null && !isNumber(range.getMaxDistance())){
				setMessage(infoList, lineNo,"【距离上限】不能为非数字!");
			}
			
			//判断【时间下限】不能为非数字
			if(range.getMinTime() !=null && !isNumber(range.getMinTime())){
				setMessage(infoList, lineNo,"【时间下限】不能为非数字!");
			}
			//判断【时间上限】不能为非数字
			if(range.getMaxTime() !=null && !isNumber(range.getMaxTime())){
				setMessage(infoList, lineNo,"【时间上限】不能为非数字!");
			}
			
			//判断【重量下限】不能为非数字
			if(range.getWeightLowerLimit() !=null && !isNumber(range.getWeightLowerLimit())){
				setMessage(infoList, lineNo,"【重量下限】不能为非数字!");
			}
			//判断【重量上限】不能为非数字
			if(range.getWeightUpperLimit() !=null && !isNumber(range.getWeightUpperLimit())){
				setMessage(infoList, lineNo,"【重量上限】不能为非数字!");
			}
			
			//判断【数量下限】不能为非数字
			if(range.getNumLowerLimit()!=null && !isNumber(range.getNumLowerLimit())){
				setMessage(infoList, lineNo,"【数量下限】不能为非数字!");
			}
			
			//判断【数量上限】不能为非数字
			if(range.getNumUpperLimit()!=null && !isNumber(range.getNumUpperLimit())){
				setMessage(infoList, lineNo,"【数量上限】不能为非数字!");
			}
			//判断【SKU下限】不能为非数字
			if(range.getSkuLowerLimit()!=null && !isNumber(range.getSkuLowerLimit())){
				setMessage(infoList, lineNo,"【SKU下限】不能为非数字!");
			}
			//判断【SKU上限】不能为非数字
			if(range.getSkuUpperLimit()!=null && !isNumber(range.getSkuUpperLimit())){
				setMessage(infoList, lineNo,"【SKU上限】不能为非数字!");
			}
			
			//判断【体积下限】不能为非数字
			if(range.getVolumeLowerLimit()!=null && !isNumber(range.getVolumeLowerLimit())){
				setMessage(infoList, lineNo,"【体积下限】不能为非数字!");
			}
			//判断【体积上限】不能为非数字
			if(range.getVolumeUpperLimit()!=null && !isNumber(range.getVolumeUpperLimit())){
				setMessage(infoList, lineNo,"【体积上限】不能为非数字!");
			}
			//判断【单价】不能为非数字
			if(range.getUnitPrice()!=null && !isNumber(range.getUnitPrice())){
				setMessage(infoList, lineNo,"【单价不能】为非数字!");
			}
			
			
			if (infoList != null && infoList.size() > 0) { // 有错误信息
				map.put(ConstantInterface.ImportExcelStatus.IMP_ERROR, infoList);
			} else {
				map.put(ConstantInterface.ImportExcelStatus.IMP_SUCC, rangeList); // 无基本错误信息
			}
			
			DecimalFormat decimalFormat=new DecimalFormat("0");
			double addNum = ((double)(i+1)/rangeList.size())*400;
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 300+Integer.valueOf(decimalFormat.format(addNum)));
		}
		return map;
	}
	
	public static boolean isNumber(String str){  
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    } 
	
	public static boolean isNumber(Double data){  
		String str = String.valueOf(data);
        String reg = "^[0-9]+(.[0-9]+)?$";  
        return str.matches(reg);  
    }
	
	private void setMessage(List<ErrorMessageVo> infoList, int lineNo,String msg) {
		ErrorMessageVo errorVo;
		errorVo =new ErrorMessageVo();
		errorVo.setLineNo(lineNo);
		errorVo.setMsg(msg);
		infoList.add(errorVo);
	}
	

	@Expose
	public int getRangeProgress() {
		Object progressRangeFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressRangeFlag");
		if (progressRangeFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 0);
			return 1;
		}
		return (int)(DoradoContext.getAttachedRequest().getSession().getAttribute("progressRangeFlag")); 
	}
    
	
	@Expose
	public void setRangeProgress() {
		Object progressRangeFlag = DoradoContext.getAttachedRequest().getSession().getAttribute("progressRangeFlag");
		if (progressRangeFlag == null){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 0);
		 
		} else {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressRangeFlag", 0);
		} 
	}
	
	/**
	 * 获取所有的电商仓信息
	 * @return
	 */
	private Map<String,String> getEwareHouse(){
		Map<String, String> elecWareHouseMap =new HashMap<>();
		//电商仓库
		PageInfo<PubElecWarehouseEntity> ewareHousePageInfo = elecWareHouseService.queryAll(null, 0, Integer.MAX_VALUE);
		if (null != ewareHousePageInfo && ewareHousePageInfo.getList().size() > 0) {
			for (PubElecWarehouseEntity elecWareHouse : ewareHousePageInfo.getList()) {
				if (null != elecWareHouse) {
					elecWareHouseMap.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseCode());
				}
			}
		}
		return elecWareHouseMap;
	}
	
	private Map<String,PubAirportEntity> getAirMap(){
		//获取所有的机场信息
		Map<String, PubAirportEntity> airportMap = new HashMap<String, PubAirportEntity>();
		//机场
		List<PubAirportEntity> airportList = airportService.query(null);
		if (null != airportList && airportList.size() > 0) {
			for (PubAirportEntity aiportEntity : airportList) {
				airportMap.put(aiportEntity.getAirportName(), aiportEntity);
			}
		}
		return airportMap;
	}
	
	public int deleteBatch(Map<String,String> param){
		return priceTransportLineService.deleteBatch(Long.getLong(param.get("id")));
	}
	
	@DataResolver
	public int deleteBatchList(List<PriceTransportLineEntity> rangeList){
		return priceTransportLineService.deleteBatchList(rangeList);
		
	}
	
	@DataResolver
	public Object calculate(BizGanxianWayBillEntity data){
		ReturnData returnResult = new ReturnData();
		
		if("HXD".equals(data.getBizTypeCode())){
			Map<String,PubAirportEntity>  airportMap=getAirMap();
			PubAirportEntity airportEntity = airportMap.get(data.getStartStation());
			data.setReceiverProvinceId(airportEntity.getProvince());
			data.setReceiverCityId(airportEntity.getCity());
		}
		
//		//通过该商家id和费用科目查询计费规则
//		Map<String, Object> ruleParam = new HashMap<String, Object>();
//		ruleParam.put("customerid", data.getCustomerId());
//		ruleParam.put("subjectId",data.getBizTypeCode());
//		BillRuleReceiveEntity ruleEntity=receiveRuleService.queryByCustomerId(ruleParam);
//		if(ruleEntity==null){
//			returnResult.setCode("SUCCESS");
//			returnResult.setData("未查询到该商家的规则");
//			return returnResult;
//		}
//		CalcuReqVo reqVo= new CalcuReqVo();
//		List<PriceTransportLineEntity> list=new ArrayList<PriceTransportLineEntity>(data.getPriceList());
//		PriceTransportLineEntity passLine=list.get(0);
//		
//		Map<String, Object> rangeParam = new HashMap<String,Object>();
//		rangeParam.put("lineId", passLine.getId());
//		rangeParam.put("delFlag", "0");
//		List<PriceTransportLineRangeEntity> rangeList = priceTransportLineRangeService.query(rangeParam);
//		passLine.setChild(rangeList);
//		
//		reqVo.setQuoEntites(list);
//		reqVo.setBizData(data);
//		reqVo.setRuleNo(ruleEntity.getQuotationNo());
//		reqVo.setRuleStr(ruleEntity.getRule());
		
		try {
			CalcuResultVo vo=priceTransportLineService.trial(data);				
			if("succ".equals(vo.getSuccess())){
				data.setCalResult(vo.getPrice());			
				returnResult.setData(vo.getPrice()==null?"无匹配价格":vo.getPrice());
			}else {
				returnResult.setData(vo.getMsg());
			}
		} catch (Exception e) {
			returnResult.setData(MessageConstant.SYSTEM_ERROR_MSG);
			logger.error("运输试算异常", e);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

		}

		returnResult.setCode("SUCCESS");
		return returnResult;
	}
	
	
}
