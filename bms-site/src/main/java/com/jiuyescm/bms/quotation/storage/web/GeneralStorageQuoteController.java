package com.jiuyescm.bms.quotation.storage.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
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
import com.jiuyescm.bms.quotation.contract.entity.ContractDetailEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.service.IPriceGeneralQuotationService;
import com.jiuyescm.bms.quotation.storage.service.IPriceStepQuotationService;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("generalStorageQuoteController")
public class GeneralStorageQuoteController {

	private static final Logger logger = LoggerFactory.getLogger(GeneralStorageQuoteController.class.getName());
			
	@Resource 
	private IPriceGeneralQuotationService priceGeneralQuotationService;
	@Resource 
	private SequenceService sequenceService;
	@Resource
	private IPriceStepQuotationService  service;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	@Resource
	private IPubRecordLogService pubRecordLogService;
	@Resource
	private IReceiveRuleService receiveRuleService;
		
	/**
	 * 分页查询
	 * @param page

	 */
	@DataProvider
	public void query(Page<PriceGeneralQuotationEntity> page, Map<String, Object> param) throws Exception 
	{
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		if("ALL".equals(param.get("subjectId"))){
			param.put("subjectId", null);
		}
		
		PageInfo<PriceGeneralQuotationEntity> pageInfo = priceGeneralQuotationService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null)
		{
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	
	@DataProvider
	public void queryMaterial(Page<PriceGeneralQuotationEntity> page, Map<String, Object> param) throws Exception 
	{
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		if("ALL".equals(param.get("subjectId"))){
			param.put("subjectId", null);
		}
		
		//param.put("subjectId", );
		
		PageInfo<PriceGeneralQuotationEntity> pageInfo = priceGeneralQuotationService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null)
		{
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataResolver
	public ResponseVo save(PriceGeneralQuotationEntity entity) {
		if(Session.isMissing()){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.SESSION_INVALID_MSG);
		}else if(entity == null){
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		try{
			//校验是否为标准报价
			if (StringUtils.isEmpty(entity.getPriceType())) {
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTETYPE_NULL_MSG);
			}
			if (StringUtils.isEmpty(entity.getSubjectId())) {
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.SUBJECTID_NULL_MSG);
			}
			if (StringUtils.isEmpty(entity.getTemplateType())) {
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_TYPE_NULL_MSG);
			}else if (StringUtils.isNotEmpty(entity.getTemplateType()) && 
					TemplateTypeEnum.STANDARD.getCode().equals(entity.getTemplateType())) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
				condition.put("priceType", entity.getPriceType());
				condition.put("subjectId", entity.getSubjectId());
				PriceGeneralQuotationEntity queryEntity = priceGeneralQuotationService.query(condition);
				if (null != queryEntity) {
					return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_STANDARD_EXIST_MSG);
				}
			}
			
			String quotationNo =sequenceService.getBillNoOne(PriceGeneralQuotationEntity.class.getName(), "RC", "00000");
			entity.setQuotationNo(quotationNo);
			entity.setBizTypeCode("STORAGE");
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
			Integer i = 0;
			i = priceGeneralQuotationService.insert(entity);
			if(i>0){
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(entity));
					model.setOldData("");
					model.setOperateDesc("新增报价,报价编号:【"+quotationNo+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_general_quotation");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.INSERT.getCode());
					model.setRemark("");
					model.setOperateTableKey(quotationNo);
					model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
				return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
			}else{
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.OPERATOR_FAIL_MSG);
			}
			
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.STORAGE_ADD_GENERAL_EX_MSG, ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
		}
	}
	
	@DataResolver
	public String update(PriceGeneralQuotationEntity entity)
	{
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try{
			
			//验证报价
			if (StringUtils.isEmpty(entity.getPriceType())) {
				return MessageConstant.QUOTETYPE_NULL_MSG;
			}
			if (StringUtils.isEmpty(entity.getSubjectId())) {
				return MessageConstant.SUBJECTID_NULL_MSG;
			}
			if (StringUtils.isEmpty(entity.getTemplateType())) {
				return MessageConstant.QUOTE_TYPE_NULL_MSG;
			}else if (StringUtils.isNotEmpty(entity.getTemplateType()) && 
					TemplateTypeEnum.STANDARD.getCode().equals(entity.getTemplateType())) {
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("templateType", TemplateTypeEnum.STANDARD.getCode());
				condition.put("priceType", entity.getPriceType());
				condition.put("subjectId", entity.getSubjectId());
				PriceGeneralQuotationEntity queryEntity = priceGeneralQuotationService.query(condition);
				if (null != queryEntity) {
					return MessageConstant.QUOTE_STANDARD_EXIST_MSG;
				}
			}
			
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setBizTypeCode("STORAGE");
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				//PriceGeneralQuotationEntity oldTemp=priceGeneralQuotationService.queryByQuotationNo(entity.getQuotationNo());
				model.setOldData("");
				model.setOperateDesc("更新报价,报价编号:【"+entity.getQuotationNo()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_general_quotation");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getQuotationNo());
				model.setUrlName(RecordLogUrlNameEnum.IN_STORAGE_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			priceGeneralQuotationService.update(entity);
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
	}
	
	@FileProvider
	public DownloadFile getDispatchTemplate(Map<String, String> parameter) throws IOException {
		String subjectId=parameter.get("subjectId");
		if("JIUYE_DISPATCH".equals(subjectId)){
			InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/storage/generalStorage_template.xlsx");
			return new DownloadFile("九曳仓储基础报价模板.xlsx", is);
		}
		
		
		return null;
	}
	
	@DataResolver
	public String copyTemplate(PriceGeneralQuotationEntity entity){
		try{
			if(Session.isMissing()){
				return "长时间未操作，用户已失效，请重新登录再试！";
			}else if(entity == null){
				return "页面传递参数有误！";
			}
			
			PriceGeneralQuotationEntity newEntity = new PriceGeneralQuotationEntity();
			//先复制主模板
			String quotationNo =sequenceService.getBillNoOne(PriceGeneralQuotationEntity.class.getName(), "RC", "00000");
			newEntity.setQuotationNo(quotationNo);
			newEntity.setBizTypeCode("STORAGE");
			newEntity.setCustomerId(entity.getCustomerId());
			newEntity.setCustomerName(entity.getCustomerName());
			newEntity.setSubjectId(entity.getSubjectId());
			newEntity.setFeeUnitCode(entity.getFeeUnitCode());
			newEntity.setUnitPrice(entity.getUnitPrice());
			newEntity.setRemark(entity.getRemark());
			newEntity.setPriceType(entity.getPriceType());
			newEntity.setTemplateType(entity.getTemplateType());
			
			newEntity.setCreateTime(JAppContext.currentTimestamp());
			newEntity.setCreator(JAppContext.currentUserName());
			newEntity.setLastModifier(JAppContext.currentUserName());
			newEntity.setLastModifyTime(JAppContext.currentTimestamp());
			newEntity.setDelFlag("0");
			
			Integer i = 0;
			i = priceGeneralQuotationService.insert(newEntity);
			
			if(i>0){
				PriceGeneralQuotationEntity  newEn = priceGeneralQuotationService.findByNo(quotationNo);
				
				Map<String, Object> params = new HashMap<String,Object>();
				params.put("quotationId",entity.getId());
				PageInfo<PriceStepQuotationEntity> page = service.query(params, 0, Integer.MAX_VALUE);
				
				if(null!=page){
					List<PriceStepQuotationEntity> list = page.getList();
					
					for(PriceStepQuotationEntity vo:list){
						vo.setId(null);
						vo.setQuotationId(newEn.getId().toString());
						
					}
					
					service.insertBatchTmp(list);
				}
				
				
				
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());

			return "数据库操作失败";
		}
		
	}
	
	
	//查询所有的服务
	@Expose
	public String queryRule(Map<String,Object> parameter){
		String result="";
		
		BillRuleReceiveEntity rule=receiveRuleService.queryOne(parameter);
		if(rule!=null){
			result=rule.getQuotationNo()+"&"+rule.getQuotationName();
		}
		return result;
	}
	
}
