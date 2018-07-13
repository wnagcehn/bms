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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockPackmaterialTempService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bizOutstockPackmaterialTempController")
public class BizOutstockPackmaterialTempController {

	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialTempController.class.getName());

	@Resource
	private IBizOutstockPackmaterialTempService bizOutstockPackmaterialTempService;

	@DataProvider
	public BizOutstockPackmaterialTempEntity findById(Long id) throws Exception {
		BizOutstockPackmaterialTempEntity entity = null;
		entity = bizOutstockPackmaterialTempService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BizOutstockPackmaterialTempEntity> page, Map<String, Object> param) {
		PageInfo<BizOutstockPackmaterialTempEntity> pageInfo = bizOutstockPackmaterialTempService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	
}
