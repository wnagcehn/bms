/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.controller;

import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.fees.storage.entity.PackCostReportEntity;
import com.jiuyescm.bms.fees.storage.service.IPackCostReportService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("packCostReportController")
public class PackCostReportController {

	private static final Logger logger = Logger.getLogger(PackCostReportController.class.getName());

	@Resource
	private IPackCostReportService packCostReportService;

	@DataProvider
	public PackCostReportEntity findById(Long id) throws Exception {
		PackCostReportEntity entity = null;
		entity = packCostReportService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<PackCostReportEntity> page, Map<String, Object> param) {
		PageInfo<PackCostReportEntity> pageInfo = packCostReportService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(PackCostReportEntity entity) {
		/*
		if (entity.getId() == null) {
			packCostReportService.save(entity);
		} else {
			packCostReportService.update(entity);
		}*/
	}

	@DataResolver
	public void delete(PackCostReportEntity entity) {
		//packCostReportService.delete(entity.getId());
	}
	

}
