package com.jiuyescm.bms.quotation.out.dispatch.web;

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
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherService;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherTemplateService;
import com.jiuyescm.cfm.common.JAppContext;

@Controller("outDeliverOtherTemplateController")
public class OutDeliverOtherTemplateController {

	private static final Logger logger = Logger.getLogger(OutDeliverOtherTemplateController.class.getName());
	
	@Resource
	private IPriceOutDispatchOtherTemplateService priceOutDispatchTemplateService;
	
	@Resource 
	private SequenceService sequenceService;
	
	@Resource
	private IPriceOutDispatchOtherService priceOutOtherDispatchService;
	
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
	public void query(Page<PriceOutDispatchOtherTemplateEntity> page, Map<String, Object> param) {
		PageInfo<PriceOutDispatchOtherTemplateEntity> pageInfo = priceOutDispatchTemplateService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 新增
	 * @param entity
	 * @return
	 */
	@DataResolver
	public ResponseVo save(PriceOutDispatchOtherTemplateEntity entity){
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
				PriceOutDispatchOtherTemplateEntity queryEntity = priceOutDispatchTemplateService.query(condition);
				if (null != queryEntity) {
					return new ResponseVo(ResponseVo.FAIL, MessageConstant.QUOTE_STANDARD_EXIST_MSG);
				}
			}
			
			String templateNo =sequenceService.getBillNoOne(PriceOutDispatchOtherTemplateEntity.class.getName(), "OMBZ", "00000");
			entity.setTemplateCode(templateNo);
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceOutDispatchTemplateService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增配送其他报价模板,模板编码【"+templateNo+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_dispatch_other_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(templateNo);
				model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return new ResponseVo(ResponseVo.SUCCESS, MessageConstant.OPERATOR_SUCCESS_MSG);
		}
		catch(Exception ex){
			logger.error(ExceptionConstant.STORAGE_ADD_GENQUOTE_EX_MSG, ex);
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			ex.printStackTrace();
			return new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
		}
	}
	
	@DataResolver
	public void saveAll(Collection<PriceOutDispatchOtherTemplateEntity> datas) {
		if (datas == null) {
			return;
		}
		for (PriceOutDispatchOtherTemplateEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				priceOutDispatchTemplateService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新配送其他报价模板,模板编码【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_dispatch_other_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				priceOutDispatchTemplateService.delete(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除配送其他报价模板,模板编码【"+temp.getTemplateCode()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_dispatch_other_template");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getTemplateCode());
					model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else {
				// do nothing;
			}
		}
	}
	
	@DataResolver
	public String update(PriceOutDispatchOtherTemplateEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceOutDispatchTemplateService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新配送其他报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_dispatch_other_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			ex.printStackTrace();
			return "数据库操作失败";
		}
	}
	
	@DataResolver
	public ResponseVo delete(PriceOutDispatchOtherTemplateEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			
			Map<String,Object> condition=new HashMap<String, Object>();
			condition.put("templateId", entity.getId()+"");
			condition.put("lastModifier", JAppContext.currentUserName());
			condition.put("lastModifyTime", JAppContext.currentTimestamp());
			
			//逻辑删除报价
			priceOutOtherDispatchService.removeDispatchByMap(condition);

			//逻辑删除模板：
			int result=priceOutDispatchTemplateService.delete(entity);
			if(result<=0){
				return new ResponseVo(ResponseVo.FAIL, MessageConstant.DELETE_INFO_FAIL_MSG);
			}
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废配送其他报价模板,模板编码【"+entity.getTemplateCode()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_dispatch_other_template");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getTemplateCode());
				model.setUrlName(RecordLogUrlNameEnum.OUT_DELIVER_OTHER_PRICE.getCode());
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
			ex.printStackTrace();
			return new ResponseVo(ResponseVo.ERROR, MessageConstant.SYSTEM_ERROR_MSG);
		}
	}
	
	/**
	 * 批量作废模板下的报价
	 * @param entity
	 * @return
	 */
	@DataResolver
	public String cancelQuatation(PriceOutDispatchOtherTemplateEntity entity){
		try{	
			
			Map<String,Object> condition=new HashMap<String, Object>();
			condition.put("templateId", entity.getId()+"");
			condition.put("lastModifier", JAppContext.currentUserName());
			condition.put("lastModifyTime", JAppContext.currentTimestamp());
			
			//逻辑删除报价
			int num=priceOutOtherDispatchService.removeDispatchByMap(condition);
			if(num<=0){
				return "作废报价失败";
			}
			return "SUCCESS";
		}
		catch(Exception ex){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", ex.toString());
			
			ex.printStackTrace();
			return "作废报价失败";
		}
	}
}
