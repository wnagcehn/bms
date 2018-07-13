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
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;
import com.jiuyescm.bms.biz.storage.service.IBizBaseFeeService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizBaseFeeController")
public class BizBaseFeeController {

	private static final Logger logger = Logger.getLogger(BizBaseFeeController.class.getName());

	@Resource
	private IBizBaseFeeService bizBaseFeeService;

	@DataProvider
	public BizBaseFeeEntity findById(Long id) throws Exception {
		BizBaseFeeEntity entity = null;
		entity = bizBaseFeeService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizBaseFeeEntity> page, Map<String, Object> param) {
		PageInfo<BizBaseFeeEntity> pageInfo = bizBaseFeeService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BizBaseFeeEntity entity) {
		if (entity.getId() == null) {
			bizBaseFeeService.save(entity);
		} else {
			bizBaseFeeService.update(entity);
		}
	}

	@DataResolver
	public void delete(BizBaseFeeEntity entity) {
		bizBaseFeeService.delete(entity.getId());
	}
	
}
