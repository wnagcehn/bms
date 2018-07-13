/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.system.controller;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;
import com.jiuyescm.bms.quotation.system.service.IBmsJiuyeQuotationSystemService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bmsJiuyeQuotationSystemController")
public class BmsJiuyeQuotationSystemController {

	private static final Logger logger = Logger.getLogger(BmsJiuyeQuotationSystemController.class.getName());

	@Resource
	private IBmsJiuyeQuotationSystemService bmsJiuyeQuotationSystemService;

	@DataProvider
	public BmsJiuyeQuotationSystemEntity findById(Long id) throws Exception {
		BmsJiuyeQuotationSystemEntity entity = null;
		entity = bmsJiuyeQuotationSystemService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsJiuyeQuotationSystemEntity> page, Map<String, Object> param) {
		PageInfo<BmsJiuyeQuotationSystemEntity> pageInfo = bmsJiuyeQuotationSystemService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BmsJiuyeQuotationSystemEntity entity) {
		Timestamp operatorTime = JAppContext.currentTimestamp();
		String operatorName=JAppContext.currentUserName();
		if (entity.getId() == null) {
			entity.setCreateTime(operatorTime);
			entity.setCreator(operatorName);
			entity.setLastModifier(operatorName);
			entity.setLastModifyTime(operatorTime);
			bmsJiuyeQuotationSystemService.save(entity);
		} else {
			entity.setLastModifier(operatorName);
			entity.setLastModifyTime(operatorTime);
			bmsJiuyeQuotationSystemService.update(entity);
		}
	}

	@DataResolver
	public void delete(BmsJiuyeQuotationSystemEntity entity) {
		bmsJiuyeQuotationSystemService.delete(entity.getId());
	}
	
}
