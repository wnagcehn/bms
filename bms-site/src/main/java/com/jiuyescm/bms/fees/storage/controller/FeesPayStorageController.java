/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.controller;

import java.util.Map;
import org.apache.log4j.Logger;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.FeesPayStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesPayStorageService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("feesPayStorageController")
public class FeesPayStorageController {

	private static final Logger logger = Logger.getLogger(FeesPayStorageController.class.getName());

	@Resource
	private IFeesPayStorageService feesPayStorageService;

	@DataProvider
	public FeesPayStorageEntity findById(Long id) throws Exception {
		FeesPayStorageEntity entity = null;
		entity = feesPayStorageService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FeesPayStorageEntity> page, Map<String, Object> param) {
		PageInfo<FeesPayStorageEntity> pageInfo = feesPayStorageService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}


	
}
