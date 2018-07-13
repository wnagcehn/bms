/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.subjectGroup.web;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;
import com.jiuyescm.bms.common.tool.Session;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bmsSubjectInfoController")
public class BmsSubjectInfoController {

	private static final Logger logger = Logger.getLogger(BmsSubjectInfoController.class.getName());

	@Resource
	private IBmsSubjectInfoService bmsSubjectInfoService;


	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsSubjectInfoEntity> page, Map<String, Object> param) {
		
		if(param==null){
			param=new HashMap<String, Object>();
		}	
		
		if(param.containsKey("delFlag")){
			boolean isCheck=Boolean.valueOf(param.get("delFlag").toString());
			if(true==isCheck){
				param.put("delFlag", "1");
			}else{
				param.put("delFlag", "0");
			}
		}
		
		PageInfo<BmsSubjectInfoEntity> pageInfo = bmsSubjectInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public Map<String,String> getInOutTypeCode(){
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put("INPUT", "收入");
		map.put("OUTPUT", "支出");
		return map;
	}
	
	@DataProvider
	public Map<String,String> getBizTypeCode(){
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put("STORAGE", "仓储");
		map.put("DISPATCH", "配送");
		map.put("TRANSPORT", "运输");
		return map;
	}
	
	@DataProvider
	public Map<String,String> getDelFlag(){
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put("0", "否");
		map.put("1", "是");
		return map;
	}
	
	/**
	 * 保存数据
	 * @param datas
	 */
	@DataResolver
	public String save(Collection<BmsSubjectInfoEntity> datas) {
		if(Session.isMissing()){
			return "长时间未操作，用户已失效，请重新登录再试！";
		}
		try {		
			Timestamp nowdate = JAppContext.currentTimestamp();
			String userid=JAppContext.currentUserName();
			for(BmsSubjectInfoEntity temp:datas){
				if(EntityState.NEW.equals(EntityUtils.getState(temp))){
					
					//先判断当前科目编码是否已存在					
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("inOutTypecode", temp.getInOutTypecode());
					map.put("bizTypecode", temp.getBizTypecode());
					map.put("subjectCode", temp.getSubjectCode());
					map.put("delFlag", "0");
					
					BmsSubjectInfoEntity sub1=bmsSubjectInfoService.querySubject(map);
					if(sub1!=null){
						return "费用科目编码【"+temp.getSubjectCode()+"】已存在";
					}
					
					//先判断当前科目名称是否已存在
					map.clear();
					map.put("inOutTypecode", temp.getInOutTypecode());
					map.put("bizTypecode", temp.getBizTypecode());
					map.put("subjectName", temp.getSubjectName());
					map.put("delFlag", "0");
					
					BmsSubjectInfoEntity sub2=bmsSubjectInfoService.querySubject(map);
					if(sub2!=null){
						return "费用科目名称【"+temp.getSubjectName()+"】已存在";
					}
					
					temp.setDelFlag("0");
					temp.setCreator(userid);
					temp.setCreateTime(nowdate);
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					BmsSubjectInfoEntity entity=bmsSubjectInfoService.save(temp);
					if(entity==null){
						return "新增失败";
					}
					return "SUCESS";
				}else if(EntityState.MODIFIED.equals(EntityUtils.getState(temp))){
					
					//先判断当前科目编码是否已存在					
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("inOutTypecode", temp.getInOutTypecode());
					map.put("bizTypecode", temp.getBizTypecode());
					map.put("subjectCode", temp.getSubjectCode());
					map.put("delFlag", "0");
					
					BmsSubjectInfoEntity sub1=bmsSubjectInfoService.querySubject(map);
					if(sub1!=null && !sub1.getId().equals(temp.getId())){
						return "费用科目编码【"+temp.getSubjectCode()+"】已存在";
					}
					
					//先判断当前科目名称是否已存在
					map.clear();
					map.put("inOutTypecode", temp.getInOutTypecode());
					map.put("bizTypecode", temp.getBizTypecode());
					map.put("subjectName", temp.getSubjectName());
					map.put("delFlag", "0");
					
					BmsSubjectInfoEntity sub2=bmsSubjectInfoService.querySubject(map);
					if(sub2!=null && !sub2.getId().equals(temp.getId())){
						return "费用科目名称【"+temp.getSubjectName()+"】已存在";
					}
					
					
					temp.setLastModifier(userid);
					temp.setLastModifyTime(nowdate);
					BmsSubjectInfoEntity entity=bmsSubjectInfoService.update(temp);
					if(entity==null){
						return "保存失败";
					}
					return "SUCESS";
				}
				
			}
			
			return "SUCESS";
			
		} catch (Exception e) {
			//写入日志
			e.printStackTrace();
			return "数据库操作失败";
		}

	}

	@DataResolver
	public String delete(BmsSubjectInfoEntity entity) {
		entity.setDelFlag("1");
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		BmsSubjectInfoEntity re=bmsSubjectInfoService.update(entity);
		if(re==null){
			return "删除失败";
		}
		return "删除成功";
	}
	
}
