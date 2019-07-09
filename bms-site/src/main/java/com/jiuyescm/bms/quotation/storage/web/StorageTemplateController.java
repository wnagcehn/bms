package com.jiuyescm.bms.quotation.storage.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.transport.entity.GenericTemplateEntity;
import com.jiuyescm.bms.quotation.transport.service.IGenericTemplateService;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("storageTemplateController")
public class StorageTemplateController {

	private static final Logger logger = Logger.getLogger(StorageTemplateController.class.getName());
	
	@Resource
	private IGenericTemplateService genericTemplateService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubRecordLogService pubRecordLogService;
	/*
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<GenericTemplateEntity> page, Map<String, Object> param) {
		if(null==param){
			param = new HashMap<String,Object>();
		}
		param.put("storageTemplateType", "STORAGE_EXTRA_PRICE_TEMPLATE");
	
		PageInfo<GenericTemplateEntity> pageInfo = genericTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public void queryMaterial(Page<GenericTemplateEntity> page, Map<String, Object> param) {
		if(null==param){
			param = new HashMap<String,Object>();
		}
		param.put("bizTypeCode", "STORAGE");
		param.put("storageTemplateType", "STORAGE_MATERIAL_PRICE_TEMPLATE");
		PageInfo<GenericTemplateEntity> pageInfo = genericTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
		
	@DataResolver
	public ResponseVo save(GenericTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(entity == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			
			//校验是否为标准报价
			if (StringUtils.isEmpty(entity.getTemplateType())) {
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_TYPE_NULL_MSG);
			}else if (StringUtils.isNotEmpty(entity.getTemplateType()) && 
					TemplateTypeEnum.STANDARD.getCode().equals(entity.getTemplateType())) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
				condition.put("storageTemplateType", entity.getStorageTemplateType());
				GenericTemplateEntity queryEntity = genericTemplateService.query(condition);
				if (null != queryEntity) {
					return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_STANDARD_EXIST_MSG);
				}
			}
			String templateNo =sequenceService.getBillNoOne(GenericTemplateEntity.class.getName(), "MB", "00000");
			
			entity.setTemplateCode(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			genericTemplateService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增报价,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_general_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.STORAGE_ADD_GENQUOTE_EX_MSG, ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
		}
	}
	
	@DataResolver
	public void saveAll(Collection<GenericTemplateEntity> datas) {
		if (datas == null) {
			return;
		}
		for (GenericTemplateEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				genericTemplateService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新报价,报价编号:【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_general_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
				
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				genericTemplateService.delete(temp.getId());
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除报价,报价编号:【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_general_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:", e);
				}
			} else {
				// do nothing;
			}
		}
	}
	
	@DataResolver
	public String update(GenericTemplateEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			
			if (StringUtils.isEmpty(entity.getTemplateType())) {
				return  MessageConstant.QUOTE_TYPE_NULL_MSG;
			}else if (StringUtils.isNotEmpty(entity.getTemplateType()) && 
					TemplateTypeEnum.STANDARD.getCode().equals(entity.getTemplateType())) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
				condition.put("storageTemplateType", entity.getStorageTemplateType());
				GenericTemplateEntity queryEntity = genericTemplateService.query(condition);
				if (null != queryEntity) {
					return MessageConstant.QUOTE_STANDARD_EXIST_MSG;
				}
			}
			
			genericTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));	
				model.setOldData("");
				model.setOperateDesc("更新报价,报价编号:【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_general_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public String delete(GenericTemplateEntity entity){
		try{
			
			genericTemplateService.delete(entity.getId());
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("删除报价,报价编号:【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_general_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:", e);
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			logger.error("异常:", ex);
			return "数据库操作失败";
		}
	}
}
