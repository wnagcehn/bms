/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;
import com.jiuyescm.bms.biz.storage.entity.ReturnData;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockDetailService;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverQueryEntity;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizOutstockDetailController")
public class BizOutstockDetailController {

	private static final Logger logger = Logger.getLogger(BizOutstockDetailController.class.getName());

	@Resource
	private IBizOutstockDetailService bizOutstockDetailService;
	
	@Resource
	private IBizOutstockMasterService bizOutstockMasterService;
	
	@Autowired
	private IFeesReceiverDeliverService deliverService;
	
	@DataProvider
	public BizOutstockDetailEntity findById(Long id) throws Exception {
		BizOutstockDetailEntity entity = null;
		entity = bizOutstockDetailService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizOutstockDetailEntity> page, Map<String, Object> param) {
		PageInfo<BizOutstockDetailEntity> pageInfo = bizOutstockDetailService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizOutstockDetailEntity entity) {
		if (entity.getId() == null) {
			bizOutstockDetailService.save(entity);
		} else {
			bizOutstockDetailService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizOutstockDetailEntity entity) {
		bizOutstockDetailService.delete(entity.getId());
	}
	
	@DataResolver
	public @ResponseBody Object update(BizOutstockDetailEntity entity){

		ReturnData result = new ReturnData();
		
		Timestamp nowdate = JAppContext.currentTimestamp();
		String userid=JAppContext.currentUserName();
		
		//判断是否生成费用，判断费用的状态是否为未过账
		Map<String, Object> condition = new HashMap<String,Object>();
		condition.put("outstockNo", entity.getOutstockNo());
		String feeId = bizOutstockMasterService.query(condition, 1, 20).getList().get(0).getFeesNo();
		if(StringUtils.isNotBlank(feeId)){
			FeesReceiveDeliverQueryEntity paramEntity = new FeesReceiveDeliverQueryEntity();
			paramEntity.setFeesNo(feeId);
			PageInfo<FeesReceiveDeliverEntity> pageInfo = deliverService.query(paramEntity, 0, Integer.MAX_VALUE);
			if (null != pageInfo && null!=pageInfo.getList() && pageInfo.getList().size()>0) {
				FeesReceiveDeliverEntity feesReceiveDeliverEntity=pageInfo.getList().get(0);
				//获取此时的费用状态
				String status = String.valueOf(feesReceiveDeliverEntity.getState());
				if("1".equals(status)){
					result.setCode("fail");
					result.setData("该费用已过账，无法调整托数");
					return result;
				}
			}
		}
			
		//此为修改业务数据
		//根据名字查出对应的id
		entity.setLastModifier(userid);
		entity.setLastModifyTime(nowdate);
		
		
		int i = 0;
		i = bizOutstockDetailService.update(entity);
		if(i>0){
			result.setCode("SUCCESS");
		}else{
			result.setCode("fail");
			result.setData("更新失败");
		}
		
		return result;
	}
	
}
