/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.abnormal.service.IFeesPayAbnormalService;
import com.jiuyescm.mdm.customer.api.IProjectService;
import com.jiuyescm.mdm.customer.vo.CusprojectRuleVo;

/**
 * 
 * @author zhangzw
 * 
 */
@Controller("feesAbnormalController")
public class FeesAbnormalController {

	private static final Logger logger = Logger.getLogger(FeesAbnormalController.class.getName());

	@Resource
	private IFeesAbnormalService feesAbnormalService;
	@Resource
	private IProjectService projectService;
	@Resource
	private IFeesPayAbnormalService feesPayAbnormalService;

	@DataProvider
	public FeesAbnormalEntity findById(Long id) throws Exception {
		FeesAbnormalEntity entity = null;
		entity = feesAbnormalService.findById(id);
		return entity;
	}

	/**
	 * 应收理赔分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<FeesAbnormalEntity> page, Map<String, Object> param) {
		if(null!=param && "ALL".equals(param.get("reasonId"))){
			param.put("reasonId", "");
		}
		if(null!=param){
			param.put("payment", "1"); 
			if(param.get("projectId")!=null&&StringUtils.isNotBlank(param.get("projectId").toString())){
				List<String> customerIds=projectService.queryAllCustomerIdByProjectId(param.get("projectId").toString());
				if(customerIds==null||customerIds.size()==0){
					page.setEntities(null);
					page.setEntityCount(0);
					return;
				}
				param.put("customerIdList", customerIds);
			}
		}	
		PageInfo<FeesAbnormalEntity> pageInfo = feesAbnormalService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 应付理赔分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryPay(Page<FeesPayAbnormalEntity> page, Map<String, Object> param) {
		if(null!=param && "ALL".equals(param.get("reasonId"))){
			param.put("reasonId", "");
		}
		if(null!=param){
			param.put("payment", "1"); 
			if(param.get("projectId")!=null&&StringUtils.isNotBlank(param.get("projectId").toString())){
				List<String> customerIds=projectService.queryAllCustomerIdByProjectId(param.get("projectId").toString());
				if(customerIds==null||customerIds.size()==0){
					page.setEntities(null);
					page.setEntityCount(0);
					return;
				}
				param.put("customerIdList", customerIds);
			}
		}	
		PageInfo<FeesPayAbnormalEntity> pageInfo = feesPayAbnormalService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitPayList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	private void InitPayList(List<FeesPayAbnormalEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesPayAbnormalEntity entity:list){
			if(entity.getCustomerId()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerId().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}
				}
			}
		}
	}
	
	private void InitList(List<FeesAbnormalEntity> list){
		List<CusprojectRuleVo> cusProjectRuleList=projectService.queryAllRule();
		for(FeesAbnormalEntity entity:list){
			if(entity.getCustomerId()!=null){
				for(CusprojectRuleVo vo:cusProjectRuleList){
					if(entity.getCustomerId().equals(vo.getCustomerid())){
						entity.setProjectId(vo.getProjectid());
						entity.setProjectName(vo.getProjectName());
						break;
					}
				}
			}
		}
	}
	@DataProvider
	public void queryReceive(Page<FeesAbnormalEntity> page, Map<String, Object> param) {
		if(param!=null){
			if("ALL".equals(param.get("reasonId"))){
				param.put("reasonId", "");
			}
			param.put("receive", "0"); 
		}		
		PageInfo<FeesAbnormalEntity> pageInfo = feesAbnormalService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			InitList(pageInfo.getList());
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(FeesAbnormalEntity entity) {
		if (entity.getId() == null) {
			feesAbnormalService.save(entity);
		} else {
			feesAbnormalService.update(entity);
		}
	}

	@DataResolver
	public void delete(FeesAbnormalEntity entity) {
		feesAbnormalService.delete(entity.getId());
	}
	
}
