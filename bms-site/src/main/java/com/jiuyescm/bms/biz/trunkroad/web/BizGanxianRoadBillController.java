/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.trunkroad.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianRoadBillService;
import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author wubangjun
 * 
 */
@Controller("trunkRoadBillController")
public class BizGanxianRoadBillController {

	private static final Logger logger = Logger.getLogger(BizGanxianRoadBillController.class.getName());

	@Resource
	private IBizGanxianRoadBillService bizGanxianRoadBillService;
	
	@Autowired
	private IFeesReceiverDeliverService deliverService;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;

	@DataProvider
	public BizGanxianRoadBillEntity findById(Long id) throws Exception {
		BizGanxianRoadBillEntity entity = null;
		entity = bizGanxianRoadBillService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizGanxianRoadBillEntity> page, Map<String, Object> param) {
		if(param!=null && param.containsKey("isCalculated")){
			if(StringUtils.equalsIgnoreCase(String.valueOf(param.get("isCalculated")), "ALL")){
				param.put("isCalculated", "");
			}else{
				param.put("isCalculated", String.valueOf(param.get("isCalculated")));
			}
		}
		PageInfo<BizGanxianRoadBillEntity> pageInfo = bizGanxianRoadBillService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizGanxianRoadBillEntity entity) {
		if (entity.getId() == null) {
			bizGanxianRoadBillService.save(entity);
		} else {
			bizGanxianRoadBillService.update(entity);
		}
	}

	@DataResolver
	public void saveAll(Collection<BizGanxianRoadBillEntity> datas) {
		if (datas == null) {
			return;
		}
		List<BizGanxianRoadBillEntity> modifyList = new ArrayList<BizGanxianRoadBillEntity>(datas.size());
		for (BizGanxianRoadBillEntity temp : datas) {
			if (EntityState.MODIFIED.equals(EntityUtils.getState(temp))) {
				temp.setLastModifier(JAppContext.currentUserName());
				temp.setLastModifyTime(JAppContext.currentTimestamp());
				modifyList.add(temp);
			}
		}
		if(modifyList != null && modifyList.size()>0){
			bizGanxianRoadBillService.updateList(modifyList);
		}
	}
	
	@DataResolver
	public String adjustWeight(Collection<BizGanxianRoadBillEntity> datas) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BizGanxianRoadBillEntity bizData:datas){
				//对操作类型进行判断
				//此为新增业务数据
				if(EntityState.NEW.equals(EntityUtils.getState(bizData))){
					//判断该计费规则是否已存在
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(bizData))){
					
					//判断是否生成费用，判断费用的状态是否为未过账
					String feeId = bizData.getFeesNo();
					//如果没有过账的话,就允许调整重量,且调整完后,状态重置为未计算,定时任务重新扫描到并重新生成应收费用.
					bizData.setIsCalculated("0");
						
					//此为修改业务数据
					//根据名字查出对应的id
					bizData.setLastModifier(userid);
					bizData.setLastModifyTime(nowdate);
					bizGanxianRoadBillService.update(bizData);
				}
			}
			return "调整重量成功";
		} catch (Exception e) {
			//写入日志
			BmsErrorLogInfoEntity bmsErrorLogInfoEntity=new BmsErrorLogInfoEntity(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			bmsErrorLogInfoService.log(bmsErrorLogInfoEntity);
			return "调整重量失败";
		}
	}
	
	@DataResolver
	public void delete(BizGanxianRoadBillEntity entity) {
		bizGanxianRoadBillService.delete(entity.getId());
	}
	
}
