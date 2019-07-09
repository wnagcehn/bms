/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.report.month.entity.ReportCompanyProfitEntity;
import com.jiuyescm.bms.report.month.service.IReportCompanyProfitService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportCompanyProfitController")
public class ReportCompanyProfitController {

	private static final Logger logger = Logger.getLogger(ReportCompanyProfitController.class.getName());

	@Resource
	private IReportCompanyProfitService reportCompanyProfitService;
	@Autowired
	private ISystemCodeService systemCodeService;
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public List<ReportCompanyProfitEntity> query(Map<String, Object> param) {
		List<ReportCompanyProfitEntity> list = reportCompanyProfitService.query(param);
		boolean isCheck=true;
		if(param.containsKey("IsCheck")){
			isCheck=Boolean.valueOf(param.get("IsCheck").toString());
		}
		List<ReportCompanyProfitEntity> totalList=new ArrayList<ReportCompanyProfitEntity>();

		List<List<ReportCompanyProfitEntity>> listGroup=splitListByYear(list);//根据年份分组
		for(List<ReportCompanyProfitEntity> group:listGroup){
			ReportCompanyProfitEntity incomeEntity=null;
			ReportCompanyProfitEntity costEntity=null;
			ReportCompanyProfitEntity manageEntity=null;
			ReportCompanyProfitEntity profitEntity=null;
			for(ReportCompanyProfitEntity entity:group){
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
				incomeEntity=getDefaultEntity(group.get(0).getReportYear(),1);
			}
			if(costEntity==null){
				costEntity=getDefaultEntity(group.get(0).getReportYear(),2);
			}
			if(manageEntity==null){
				manageEntity=getDefaultEntity(group.get(0).getReportYear(),3);
			}
			profitEntity=getprofitEntity(incomeEntity,costEntity,manageEntity,isCheck);
			
			totalList.add(incomeEntity);
			if(isCheck){
				totalList.add(manageEntity);
			}
/*			totalList.add(getRealCostEntity(costEntity,manageEntity));
*/			totalList.add(costEntity);
			totalList.add(profitEntity);
		}
		return totalList;
	}
	//计算不包含总部管理费成本
	private ReportCompanyProfitEntity getRealCostEntity(ReportCompanyProfitEntity costEntity,ReportCompanyProfitEntity manageEntity){
		ReportCompanyProfitEntity realCostEntity=new ReportCompanyProfitEntity();
		realCostEntity.setAmount01(costEntity.getAmount01().subtract(manageEntity.getAmount01()));
		realCostEntity.setAmount02(costEntity.getAmount02().subtract(manageEntity.getAmount02()));
		realCostEntity.setAmount03(costEntity.getAmount03().subtract(manageEntity.getAmount03()));
		realCostEntity.setAmount04(costEntity.getAmount04().subtract(manageEntity.getAmount04()));
		realCostEntity.setAmount05(costEntity.getAmount05().subtract(manageEntity.getAmount05()));
		realCostEntity.setAmount06(costEntity.getAmount06().subtract(manageEntity.getAmount06()));
		realCostEntity.setAmount07(costEntity.getAmount07().subtract(manageEntity.getAmount07()));
		realCostEntity.setAmount08(costEntity.getAmount08().subtract(manageEntity.getAmount08()));
		realCostEntity.setAmount09(costEntity.getAmount09().subtract(manageEntity.getAmount09()));
		realCostEntity.setAmount10(costEntity.getAmount10().subtract(manageEntity.getAmount10()));
		realCostEntity.setAmount11(costEntity.getAmount11().subtract(manageEntity.getAmount11()));
		realCostEntity.setAmount12(costEntity.getAmount12().subtract(manageEntity.getAmount12()));
		realCostEntity.setAmountSum(costEntity.getAmountSum().subtract(manageEntity.getAmountSum()));
		realCostEntity.setReportYear(costEntity.getReportYear());
		realCostEntity.setFeesType(costEntity.getFeesType());
		realCostEntity.setCreateTime(costEntity.getCreateTime());
		realCostEntity.setModifyTime(costEntity.getModifyTime());
		return realCostEntity;
	}
	/*
	private ReportCompanyProfitEntity copyEntity(ReportCompanyProfitEntity entity){
		ReportCompanyProfitEntity model=new ReportCompanyProfitEntity();
		model.setAmount01(entity.getAmount01());
		model.setAmount02(entity.getAmount02());
		model.setAmount03(entity.getAmount03());
		model.setAmount04(entity.getAmount04());
		model.setAmount05(entity.getAmount05());
		model.setAmount06(entity.getAmount06());
		model.setAmount07(entity.getAmount07());
		model.setAmount08(entity.getAmount08());
		model.setAmount09(entity.getAmount09());
		model.setAmount10(entity.getAmount10());
		model.setAmount11(entity.getAmount11());
		model.setAmount12(entity.getAmount12());
		model.setAmountSum(entity.getAmountSum());
		model.setReportYear(entity.getReportYear());
		model.setFeesType(entity.getFeesType());
		model.setCreateTime(entity.getCreateTime());
		model.setModifyTime(entity.getModifyTime());
		return model;
	}*/
	private ReportCompanyProfitEntity getprofitEntity(
			ReportCompanyProfitEntity incomeEntity,
			ReportCompanyProfitEntity costEntity,
			ReportCompanyProfitEntity manageEntity, boolean isCheck) {
		ReportCompanyProfitEntity entity=new ReportCompanyProfitEntity();
		entity.setFeesType(4);//利润
		entity.setReportYear(incomeEntity.getReportYear());
		if(isCheck){//成本包含总部管理费  利润=收入-成本   成本包含总部管理费  利润=收入-成本 -总部管理费
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
		}else{//成本不包含总部管理费  利润=收入-（成本-总部管理费） 成本不包含总部管理费  利润=收入-成本
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
		return entity;
	}

	private ReportCompanyProfitEntity getDefaultEntity(String year,int feesType) {
		ReportCompanyProfitEntity entity=new ReportCompanyProfitEntity();
		entity.setReportYear(year);
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

	//根据年份分组  list
	private List<List<ReportCompanyProfitEntity>> splitListByYear(
			List<ReportCompanyProfitEntity> list) {
		List<List<ReportCompanyProfitEntity>> groupList=new ArrayList<List<ReportCompanyProfitEntity>>();
		Map<String,List<ReportCompanyProfitEntity>> map=Maps.newLinkedHashMap();
		for(ReportCompanyProfitEntity entity:list){
			if(map.containsKey(entity.getReportYear())){
				List<ReportCompanyProfitEntity> yearList=map.get(entity.getReportYear());
				yearList.add(entity);
				map.put(entity.getReportYear(), yearList);
			}else{
				List<ReportCompanyProfitEntity> yearList=new ArrayList<ReportCompanyProfitEntity>();
				yearList.add(entity);
				map.put(entity.getReportYear(), yearList);
			}
		}
		for(String year:map.keySet()){
			groupList.add(map.get(year));
		}
		return groupList;
	}

	@DataProvider
	public Map<String,String> getYear(){
		SystemCodeEntity sysStart=systemCodeService.getSystemCode("REPORT_YEAR", "REPORT_YEAR_START");
		int startYear=Calendar.getInstance().get(Calendar.YEAR)-4;//默认取4年前为起始时间
		if(sysStart!=null){
			if(!StringUtils.isBlank(sysStart.getExtattr1())){
				try{
					startYear=Integer.valueOf(sysStart.getExtattr1());
				}catch(Exception e){
				    logger.error("异常:", e);
				}
			}
		}
		SystemCodeEntity sysEnd=systemCodeService.getSystemCode("REPORT_YEAR", "REPORT_YEAR_END");
		int endYear=Calendar.getInstance().get(Calendar.YEAR)+4;//默认取4年后为结束时间
		if(sysEnd!=null){
			if(!StringUtils.isBlank(sysEnd.getExtattr1())){
				try{
					endYear=Integer.valueOf(sysEnd.getExtattr1());
				}catch(Exception e){
				    logger.error("异常:", e);
				}
			}
		}
		Map<String,String> map=Maps.newLinkedHashMap();
		for(int i=startYear;i<endYear+1;i++){
			map.put(String.valueOf(i),String.valueOf(i));
		}
		return map;
	}
	@DataProvider
	public Map<String,String> getFeesType(){
		List<SystemCodeEntity> list=systemCodeService.findEnumList("REPORT_FEESTYPE");
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put(" ", "全部");
		for(SystemCodeEntity codeEntity:list){
			map.put(codeEntity.getCode(), codeEntity.getCodeName());
		}
		return map;
	}
}
