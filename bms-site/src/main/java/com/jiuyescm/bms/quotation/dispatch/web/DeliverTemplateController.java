package com.jiuyescm.bms.quotation.dispatch.web;

import java.util.ArrayList;
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
import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.common.constants.DispatchMessageConstant;
import com.jiuyescm.bms.common.constants.ExceptionConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;
import com.jiuyescm.bms.quotation.dispatch.service.IBmsQuoteDispatchDetailService;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("deliverTemplateController")
public class DeliverTemplateController {
	
	private static final Logger logger = Logger.getLogger(DeliverTemplateController.class.getName());
	
	@Resource
	private IPriceDispatchTemplateService priceDispatchTemplateService;
	
	@Resource 
	private SequenceService sequenceService;
	
//	@Resource
//	private IPriceDispatchService priceDispatchService;
	@Resource
	private IBmsQuoteDispatchDetailService bmsQuoteDispatchDetailService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;
	@Resource
	private ISystemCodeService systemCodeService;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PriceDispatchTemplateEntity> page, Map<String, Object> param) {
		PageInfo<PriceDispatchTemplateEntity> pageInfo = priceDispatchTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public ResponseVo save(PriceDispatchTemplateEntity entity){
		try{
			if(Session.isMissing()){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
			}else if(entity == null){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
			}
			//校验是否为标准报价
			if (StringUtils.isEmpty(entity.getDeliver())) {
				return new ResponseVo(ResponseVo.FAIL, DispatchMessageConstant.DELIVER_NULL_MSG);
			}
			if (StringUtils.isEmpty(entity.getTemplateType())) {
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_TYPE_NULL_MSG);
			}else if (StringUtils.isNotEmpty(entity.getTemplateType()) && 
					TemplateTypeEnum.STANDARD.getCode().equals(entity.getTemplateType())) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
				condition.put("deliver", entity.getDeliver());
				PriceDispatchTemplateEntity queryEntity = priceDispatchTemplateService.query(condition);
				if (null != queryEntity) {
					return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_STANDARD_EXIST_MSG);
				}
			}
			List<SystemCodeEntity> codeList = systemCodeService.findEnumList("DISPATCH_COMPANY");
			for (SystemCodeEntity scEntity : codeList) {
				if (scEntity.getCode().equals(entity.getDeliver())) {
					entity.setCarrierid(scEntity.getExtattr1());
					break;
				}
			}
			String templateNo =sequenceService.getBillNoOne(PriceDispatchTemplateEntity.class.getName(), "IMB", "00000");
			entity.setTemplateCode(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceDispatchTemplateService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增宅配报价模板,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_dispatch_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.STORAGE_ADD_GENQUOTE_EX_MSG, ex);
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity();
			bmsErrorLogInfoEntity.setClassName(this.getClass().getSimpleName());
			bmsErrorLogInfoEntity.setMethodName(Thread.currentThread().getStackTrace()[1].getMethodName());
			bmsErrorLogInfoEntity.setErrorMsg(ex.toString());
			bmsErrorLogInfoEntity.setCreateTime(JAppContext.currentTimestamp());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);	
			return new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
		}
	}
	
	@DataResolver
	public ResponseVo update(PriceDispatchTemplateEntity temp) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(temp == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		//校验是否为标准报价
		if (StringUtils.isEmpty(temp.getDeliver())) {
			return new ResponseVo(ResponseVo.FAIL, DispatchMessageConstant.DELIVER_NULL_MSG);
		}
		if (StringUtils.isEmpty(temp.getTemplateType())) {
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_TYPE_NULL_MSG);
		}else if (StringUtils.isNotEmpty(temp.getTemplateType()) && 
				TemplateTypeEnum.STANDARD.getCode().equals(temp.getTemplateType())) {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
			condition.put("deliver", temp.getDeliver());
			PriceDispatchTemplateEntity queryEntity = priceDispatchTemplateService.query(condition);
			if (null != queryEntity) {
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_STANDARD_EXIST_MSG);
			}
		}
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("DISPATCH_COMPANY");
		for (SystemCodeEntity scEntity : codeList) {
			if (scEntity.getCode().equals(temp.getDeliver())) {
				temp.setCarrierid(scEntity.getExtattr1());
				break;
			}
		}
		temp.setLastModifier(JAppContext.currentUserName());
		temp.setLastModifyTime(JAppContext.currentTimestamp());
		priceDispatchTemplateService.update(temp);
		try{
			PubRecordLogEntity model=new PubRecordLogEntity();
			model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
			model.setNewData(JSON.toJSONString(temp));
			model.setOldData("");
			model.setOperateDesc("更新宅配报价模板,模板编码【"+temp.getTemplateCode()+"】");
			model.setOperatePerson(JAppContext.currentUserName());
			model.setOperateTable("price_dispatch_template");
			model.setOperateTime(JAppContext.currentTimestamp());
			model.setOperateType(RecordLogOperateType.UPDATE.getCode());
			model.setRemark("");
			model.setOperateTableKey(temp.getTemplateCode());
			model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
			pubRecordLogService.AddRecordLog(model);
		}catch(Exception e){
			logger.error("记录日志失败,失败原因:"+e.getMessage());
		}
		
		return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
	}
	
	/*@DataResolver
	public String update(PriceDispatchTemplateEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}			
			priceDispatchTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新宅配报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_dispatch_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
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
	*/
	/**
	 * 删除模板功能
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo delete(PriceDispatchTemplateEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			
			Map<String,Object> condition=new HashMap<String, Object>();
			condition.put("templateCode", entity.getTemplateCode());
			condition.put("lastModifier", JAppContext.currentUserName());
			condition.put("lastModifyTime", JAppContext.currentTimestamp());
			//逻辑删除报价
//			priceDispatchService.removeDispatchByMap(condition);
			bmsQuoteDispatchDetailService.removeDispatchByMap(condition);
			
			//逻辑删除模板：
			int result=priceDispatchTemplateService.delete(entity);
			if(result<=0){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.DELETE_INFO_FAIL_MSG);
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData("");
				model.setOldData(JSON.toJSONString(entity));
				model.setOperateDesc("删除宅配报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_dispatch_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.DELETE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.IN_DELIVER_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.DELETE_INFO_SUCCESS_MSG);
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.DISPATCH_DEL_QUOTE_EX_MSG, ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
		}
	}
	
	/**
	 * 批量作废模板下的报价
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String cancelQuatation(PriceDispatchTemplateEntity entity){
		try{	
			
			Map<String,Object> condition=new HashMap<String, Object>();
			condition.put("templateCode", entity.getTemplateCode());
			condition.put("lastModifier", JAppContext.currentUserName());
			condition.put("lastModifyTime", JAppContext.currentTimestamp());
			
//			//逻辑删除报价
//			priceDispatchService.removeDispatchByMap(condition);
			bmsQuoteDispatchDetailService.removeDispatchByMap(condition);
					
			return "SUCCESS";
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "作废报价失败";
		}
	}
	
	/**
	 *  判断费用科目
	 */
	@DataProvider
	public List<SystemCodeEntity> getAllDeliver(){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		
		//List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_COMPANY");

		Map<String,String> map=bmsGroupSubjectService.getSubject("receive_ds_contract_subject");
		for(String key:map.keySet()){  		
			SystemCodeEntity code=new SystemCodeEntity();
			code.setCode(key);
			code.setCodeName(map.get(key));
			list.add(code);			
		}  
		return list;
	}
	
}
