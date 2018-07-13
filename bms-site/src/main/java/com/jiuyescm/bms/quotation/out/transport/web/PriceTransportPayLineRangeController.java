/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.web;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.enumtype.RecordLogBizTypeEnum;
import com.jiuyescm.bms.common.enumtype.RecordLogOperateType;
import com.jiuyescm.bms.common.enumtype.RecordLogUrlNameEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.sequence.service.SequenceService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.pub.IPubRecordLogService;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineRangeService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("transportPayLineRangeController")
public class PriceTransportPayLineRangeController {

	private static final Logger logger = Logger.getLogger(PriceTransportPayLineRangeController.class.getName());

	@Resource
	private IPriceTransportPayLineRangeService priceTransportPayLineRangeService;

	@Resource 
	private SequenceService sequenceService;
	
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
	public void query(Page<PriceTransportPayLineRangeEntity> page, Map<String, Object> param) {
		if(param != null){
			param.put("delFlag", "0");
		}
		PageInfo<PriceTransportPayLineRangeEntity> pageInfo = priceTransportPayLineRangeService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public String save(PriceTransportPayLineRangeEntity entity) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}else if(entity == null){
			return "页面传递参数有误！";
		}
		if (entity.getId() == null) {
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setDelFlag("0");
			priceTransportPayLineRangeService.save(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("新增应付运输阶梯报价,路线ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.INSERT.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		} else {
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportPayLineRangeService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新应付运输阶梯报价,路线ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
				pubRecordLogService.AddRecordLog(model);
			}catch(Exception e){
				logger.error("记录日志失败,失败原因:"+e.getMessage());
			}
		}
		return null;
	}
	
	@DataResolver
	public void saveAll(Collection<PriceTransportPayLineRangeEntity> datas) {
		if (datas == null) {
			return;
		}
		for (PriceTransportPayLineRangeEntity temp : datas) {
			temp.setLastModifier(JAppContext.currentUserName());
			temp.setLastModifyTime(JAppContext.currentTimestamp());
			if (EntityState.NEW.equals(EntityUtils.getState(temp))) {
				this.save(temp);
			} else if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				priceTransportPayLineRangeService.update(temp);
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("更新应付运输阶梯报价,路线ID【"+temp.getLineId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_line_range");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.UPDATE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getLineId());
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
					pubRecordLogService.AddRecordLog(model);
				}catch(Exception e){
					logger.error("记录日志失败,失败原因:"+e.getMessage());
				}
			} else if (EntityState.DELETED.equals(EntityUtils.getState(temp))) {
				priceTransportPayLineRangeService.delete(temp.getId());
				try{
					PubRecordLogEntity model=new PubRecordLogEntity();
					model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
					model.setNewData(JSON.toJSONString(temp));
					model.setOldData("");
					model.setOperateDesc("删除应付运输阶梯报价,路线ID【"+temp.getLineId()+"】");
					model.setOperatePerson(JAppContext.currentUserName());
					model.setOperateTable("price_out_transport_line_range");
					model.setOperateTime(JAppContext.currentTimestamp());
					model.setOperateType(RecordLogOperateType.DELETE.getCode());
					model.setRemark("");
					model.setOperateTableKey(temp.getLineId());
					model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
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
	public String update(PriceTransportPayLineRangeEntity entity){
		try{
			if(entity != null){
				entity.setLastModifier(JAppContext.currentUserName());
				entity.setLastModifyTime(JAppContext.currentTimestamp());
			}
			priceTransportPayLineRangeService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("更新应付运输阶梯报价,路线ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.UPDATE.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
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

	@DataResolver
	public String delete(PriceTransportPayLineRangeEntity entity) {
		try{
			entity.setDelFlag("1");
			entity.setLastModifier(JAppContext.currentUserName());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			priceTransportPayLineRangeService.update(entity);
			try{
				PubRecordLogEntity model=new PubRecordLogEntity();
				model.setBizType(RecordLogBizTypeEnum.PRICE.getCode());
				model.setNewData(JSON.toJSONString(entity));
				model.setOldData("");
				model.setOperateDesc("作废应付运输阶梯报价,路线ID【"+entity.getLineId()+"】");
				model.setOperatePerson(JAppContext.currentUserName());
				model.setOperateTable("price_out_transport_line_range");
				model.setOperateTime(JAppContext.currentTimestamp());
				model.setOperateType(RecordLogOperateType.CANCEL.getCode());
				model.setRemark("");
				model.setOperateTableKey(entity.getLineId());
				model.setUrlName(RecordLogUrlNameEnum.OUT_TRANSPORT_BASE_PRICE.getCode());
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
	
}
