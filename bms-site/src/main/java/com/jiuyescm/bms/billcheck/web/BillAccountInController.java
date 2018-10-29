/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.web;

import java.util.Map;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("billAccountInController")
public class BillAccountInController {

	private static final Logger logger = Logger.getLogger(BillAccountInController.class.getName());

	@Resource
	private IBmsBillAccountInService billAccountInService;

	@DataProvider
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
		if (entity.getId() == null) {
			entity.setCreateTime(JAppContext.currentTimestamp());
			entity.setCreator(JAppContext.currentUserName());
			entity.setDelFlag("0");
			billAccountInService.save(entity);
		} else {
			billAccountInService.update(entity);
		}
	}

	@DataResolver
	public void delete(BillAccountInEntity entity) {
		billAccountInService.delete(entity.getId());
	}
	
}
