/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.web;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;




import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.Expose;
/**
 * 
 * @author stevenl
 * 
 */
@Component
@Controller("billAccountInController")
public class BillAccountInController {

	private static final Logger logger = Logger.getLogger(BillAccountInController.class.getName());

	@Resource
	private IBmsBillAccountInService billAccountInService;

	@Resource 
	private IBmsAccountInfoService billAccountInfoService;
	
	@Expose
	public BillAccountInEntity findById(Long id) throws Exception {
		BillAccountInEntity entity = null;
		entity = billAccountInService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BillAccountInEntity> page, Map<String, Object> param) {
		PageInfo<BillAccountInEntity> pageInfo = billAccountInService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BillAccountInEntity entity) {
		BillAccountInfoEntity accountEntity = billAccountInfoService.findByCustomerId(entity.getCustomerId());
		if(null == accountEntity){
			BillAccountInfoEntity accountVo = new BillAccountInfoEntity();
			accountVo.setCustomerId(entity.getCustomerId());
			accountVo.setCustomerName(entity.getCustomerName());
			accountVo.setAccountNo("1111111111");
			accountVo.setCreatorId(JAppContext.currentUserID());
			accountVo.setCreateTime(JAppContext.currentTimestamp());
			accountVo.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
//			accountEntity.setAmount(00000.000000);
			billAccountInfoService.save(accountVo);
		}
		if (entity.getId() == null) {
			entity.setCreatorId(JAppContext.currentUserID());
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
			billAccountInService.save(entity);
		} else {
			entity.setLastModifierId(JAppContext.currentUserID());
			entity.setLastModifyTime(JAppContext.currentTimestamp());
			entity.setLastModifier(JAppContext.currentUserName());
			billAccountInService.update(entity);
		}
	}

	@DataResolver
	public void delete(BillAccountInEntity entity) {
		billAccountInService.delete(entity.getId());
	}
	
	@DataProvider
	public Map<String, String> getConfirmStatus() {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("0", "未确认");
		mapValue.put("1", "已确认");
		return mapValue;
	}
	
}
