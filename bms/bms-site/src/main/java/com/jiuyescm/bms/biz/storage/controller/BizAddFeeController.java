/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.controller;

import java.util.Map;
import org.apache.log4j.Logger;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.service.IBizAddFeeService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizAddFeeController")
public class BizAddFeeController {

	private static final Logger logger = Logger.getLogger(BizAddFeeController.class.getName());

	@Resource
	private IBizAddFeeService bizAddFeeService;

	@DataProvider
	public BizAddFeeEntity findById(Long id) throws Exception {
		BizAddFeeEntity entity = null;
		entity = bizAddFeeService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizAddFeeEntity> page, Map<String, Object> param) {
		PageInfo<BizAddFeeEntity> pageInfo = bizAddFeeService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizAddFeeEntity entity) {
		if (entity.getId() == null) {
			bizAddFeeService.save(entity);
		} else {
			bizAddFeeService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizAddFeeEntity entity) {
		bizAddFeeService.delete(entity.getId());
	}
	
}
