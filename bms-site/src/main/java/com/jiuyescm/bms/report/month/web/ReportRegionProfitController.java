/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.report.month.entity.ReportRegionProfitEntity;
import com.jiuyescm.bms.report.month.service.IReportRegionProfitService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportRegionProfitController")
public class ReportRegionProfitController {

	private static final Logger logger = Logger.getLogger(ReportRegionProfitController.class.getName());

	@Resource
	private IReportRegionProfitService reportRegionProfitService;

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public List<ReportRegionProfitEntity> query(Map<String, Object> param) {
		List<ReportRegionProfitEntity> list = reportRegionProfitService.query(param);
		boolean isCheck=true;
		if(param.containsKey("IsCheck")){
			isCheck=Boolean.valueOf(param.get("IsCheck").toString());
		}
		String feesType="";
		if(param.containsKey("feesType")){
			feesType=param.get("feesType").toString();
		}
		initList(list);
		List<ReportRegionProfitEntity> totalList=new ArrayList<ReportRegionProfitEntity>();

		List<List<ReportRegionProfitEntity>> listGroup=splitListByYear(list);//根据年份分组
		for(List<ReportRegionProfitEntity> group:listGroup){
			ReportRegionProfitEntity incomeEntity=null;
			ReportRegionProfitEntity costEntity=null;
			ReportRegionProfitEntity manageEntity=null;
			ReportRegionProfitEntity profitEntity=null;
			for(ReportRegionProfitEntity entity:group){
				switch(entity.getFeesType()){
				case 1:
					incomeEntity=entity;
					break;
				case 2:
					costEntity=entity;
					break; 
				case 3:
					manageEntity=entity;
					break;
					default:
						break;
				}
			}
			if(incomeEntity==null){
				incomeEntity=getDefaultEntity(group.get(0),1);
			}
			if(costEntity==null){
				costEntity=getDefaultEntity(group.get(0),2);
			}
			if(manageEntity==null){
				manageEntity=getDefaultEntity(group.get(0),3);
			}
			profitEntity=getprofitEntity(incomeEntity,costEntity,manageEntity,isCheck);
			
			if(StringUtils.isBlank(feesType)){
				totalList.add(incomeEntity);
				if(isCheck){
					totalList.add(manageEntity);
				}
				totalList.add(getRealCostEntity(costEntity,manageEntity));
				totalList.add(profitEntity);
			}else{
				switch(feesType){
				case "1":
					totalList.add(incomeEntity);
					break;
				case "2":
					totalList.add(getRealCostEntity(costEntity,manageEntity));
					break;
				case "3":
					if(isCheck){
						totalList.add(manageEntity);
					}
					break;
				case "4":
					totalList.add(profitEntity);
					break;
				}
			}
		}
		return totalList;
	}
	
	private void initList(List<ReportRegionProfitEntity> list){
		for(ReportRegionProfitEntity entity:list){
			if(entity.getAmount01()==null){
				entity.setAmount01(BigDecimal.ZERO);
			}
			if(entity.getAmount02()==null){
				entity.setAmount02(BigDecimal.ZERO);
			}
			if(entity.getAmount03()==null){
				entity.setAmount03(BigDecimal.ZERO);
			}
			if(entity.getAmount04()==null){
				entity.setAmount04(BigDecimal.ZERO);
			}
			if(entity.getAmount05()==null){
				entity.setAmount05(BigDecimal.ZERO);
			}
			if(entity.getAmount06()==null){
				entity.setAmount06(BigDecimal.ZERO);
			}
			if(entity.getAmount07()==null){
				entity.setAmount07(BigDecimal.ZERO);
			}
			if(entity.getAmount08()==null){
				entity.setAmount08(BigDecimal.ZERO);
			}
			if(entity.getAmount09()==null){
				entity.setAmount09(BigDecimal.ZERO);
			}
			if(entity.getAmount10()==null){
				entity.setAmount10(BigDecimal.ZERO);
			}
			if(entity.getAmount11()==null){
				entity.setAmount11(BigDecimal.ZERO);
			}
			if(entity.getAmount12()==null){
				entity.setAmount12(BigDecimal.ZERO);
			}
			if(entity.getAmountSum()==null){
				entity.setAmountSum(BigDecimal.ZERO);
			}
		}
	}
	
	/**
	 * 成本(不包含总部管理费)
	 * @param costEntity
	 * @param manageEntity
	 * @return
	 */
	private ReportRegionProfitEntity getRealCostEntity(
			ReportRegionProfitEntity costEntity,
			ReportRegionProfitEntity manageEntity) {
		ReportRegionProfitEntity realCostEntity=new ReportRegionProfitEntity();
		realCostEntity.setRegionName(costEntity.getRegionName());
		realCostEntity.setAmount01(costEntity.getAmount01());
		realCostEntity.setAmount02(costEntity.getAmount02());
		realCostEntity.setAmount03(costEntity.getAmount03());
		realCostEntity.setAmount04(costEntity.getAmount04());
		realCostEntity.setAmount05(costEntity.getAmount05());
		realCostEntity.setAmount06(costEntity.getAmount06());
		realCostEntity.setAmount07(costEntity.getAmount07());
		realCostEntity.setAmount08(costEntity.getAmount08());
		realCostEntity.setAmount09(costEntity.getAmount09());
		realCostEntity.setAmount10(costEntity.getAmount10());
		realCostEntity.setAmount11(costEntity.getAmount11());
		realCostEntity.setAmount12(costEntity.getAmount12());
		realCostEntity.setAmountSum(costEntity.getAmountSum());
		realCostEntity.setReportYear(costEntity.getReportYear());
		realCostEntity.setFeesType(costEntity.getFeesType());
		realCostEntity.setCreateTime(costEntity.getCreateTime());
		realCostEntity.setModifyTime(costEntity.getModifyTime());
		return realCostEntity;
	}

	/**
	 * 统计利润
	 * @param incomeEntity
	 * @param costEntity
	 * @param manageEntity
	 * @param isCheck
	 * @return
	 */
	private ReportRegionProfitEntity getprofitEntity(
			ReportRegionProfitEntity incomeEntity,
			ReportRegionProfitEntity costEntity,
			ReportRegionProfitEntity manageEntity, boolean isCheck) {
		ReportRegionProfitEntity entity=new ReportRegionProfitEntity();
		entity.setFeesType(4);
		entity.setReportYear(incomeEntity.getReportYear());
		if(isCheck){//成本包含总部管理费  利润=收入-成本
			entity.setAmount01(incomeEntity.getAmount01().subtract(costEntity.getAmount01()).subtract(manageEntity.getAmount01()));
			entity.setAmount02(incomeEntity.getAmount02().subtract(costEntity.getAmount02()).subtract(manageEntity.getAmount02()));
			entity.setAmount03(incomeEntity.getAmount03().subtract(costEntity.getAmount03()).subtract(manageEntity.getAmount03()));
			entity.setAmount04(incomeEntity.getAmount04().subtract(costEntity.getAmount04()).subtract(manageEntity.getAmount04()));
			entity.setAmount05(incomeEntity.getAmount05().subtract(costEntity.getAmount05()).subtract(manageEntity.getAmount05()));
			entity.setAmount06(incomeEntity.getAmount06().subtract(costEntity.getAmount06()).subtract(manageEntity.getAmount06()));
			entity.setAmount07(incomeEntity.getAmount07().subtract(costEntity.getAmount07()).subtract(manageEntity.getAmount07()));
			entity.setAmount08(incomeEntity.getAmount08().subtract(costEntity.getAmount08()).subtract(manageEntity.getAmount08()));
			entity.setAmount09(incomeEntity.getAmount09().subtract(costEntity.getAmount09()).subtract(manageEntity.getAmount09()));
			entity.setAmount10(incomeEntity.getAmount10().subtract(costEntity.getAmount10()).subtract(manageEntity.getAmount10()));
			entity.setAmount11(incomeEntity.getAmount11().subtract(costEntity.getAmount11()).subtract(manageEntity.getAmount11()));
			entity.setAmount12(incomeEntity.getAmount12().subtract(costEntity.getAmount12()).subtract(manageEntity.getAmount12()));
			entity.setAmountSum(incomeEntity.getAmountSum().subtract(costEntity.getAmountSum()).subtract(manageEntity.getAmountSum()));
		}else{//不包含总部管理费  利润=收入-成本
			entity.setAmount01(incomeEntity.getAmount01().subtract(costEntity.getAmount01()));
			entity.setAmount02(incomeEntity.getAmount02().subtract(costEntity.getAmount02()));
			entity.setAmount03(incomeEntity.getAmount03().subtract(costEntity.getAmount03()));
			entity.setAmount04(incomeEntity.getAmount04().subtract(costEntity.getAmount04()));
			entity.setAmount05(incomeEntity.getAmount05().subtract(costEntity.getAmount05()));
			entity.setAmount06(incomeEntity.getAmount06().subtract(costEntity.getAmount06()));
			entity.setAmount07(incomeEntity.getAmount07().subtract(costEntity.getAmount07()));
			entity.setAmount08(incomeEntity.getAmount08().subtract(costEntity.getAmount08()));
			entity.setAmount09(incomeEntity.getAmount09().subtract(costEntity.getAmount09()));
			entity.setAmount10(incomeEntity.getAmount10().subtract(costEntity.getAmount10()));
			entity.setAmount11(incomeEntity.getAmount11().subtract(costEntity.getAmount11()));
			entity.setAmount12(incomeEntity.getAmount12().subtract(costEntity.getAmount12()));
			entity.setAmountSum(incomeEntity.getAmountSum().subtract(costEntity.getAmountSum()));
		}
		entity.setRegionName(incomeEntity.getRegionName());
		return entity;
	}

	private ReportRegionProfitEntity getDefaultEntity(ReportRegionProfitEntity model, int feesType) {
		ReportRegionProfitEntity entity=new ReportRegionProfitEntity();
		entity.setReportYear(model.getReportYear());
		entity.setRegionName(model.getRegionName());
		entity.setFeesType(feesType);
		entity.setAmount01(BigDecimal.ZERO);
		entity.setAmount02(BigDecimal.ZERO);
		entity.setAmount03(BigDecimal.ZERO);
		entity.setAmount04(BigDecimal.ZERO);
		entity.setAmount05(BigDecimal.ZERO);
		entity.setAmount06(BigDecimal.ZERO);
		entity.setAmount07(BigDecimal.ZERO);
		entity.setAmount08(BigDecimal.ZERO);
		entity.setAmount09(BigDecimal.ZERO);
		entity.setAmount10(BigDecimal.ZERO);
		entity.setAmount11(BigDecimal.ZERO);
		entity.setAmount12(BigDecimal.ZERO);
		entity.setAmountSum(BigDecimal.ZERO);
		entity.setCreateTime(JAppContext.currentTimestamp());
		return entity;
	}

	private List<List<ReportRegionProfitEntity>> splitListByYear(
			List<ReportRegionProfitEntity> list) {
		List<List<ReportRegionProfitEntity>> groupList=new ArrayList<List<ReportRegionProfitEntity>>();
		Map<String,List<ReportRegionProfitEntity>> map=Maps.newLinkedHashMap();
		for(ReportRegionProfitEntity entity:list){
			if(map.containsKey(entity.getRegionName())){
				List<ReportRegionProfitEntity> mapList=map.get(entity.getRegionName());
				mapList.add(entity);
				map.put(entity.getRegionName(), mapList);
			}else{
				List<ReportRegionProfitEntity> mapList=new ArrayList<ReportRegionProfitEntity>();
				mapList.add(entity);
				map.put(entity.getRegionName(), mapList);
			}
		}
		for(String key:map.keySet()){
			groupList.add(map.get(key));
		}
		return groupList;
	}
	
}
