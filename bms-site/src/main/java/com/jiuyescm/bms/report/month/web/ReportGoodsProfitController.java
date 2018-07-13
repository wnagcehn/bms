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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.report.month.entity.ReportGoodsProfitEntity;
import com.jiuyescm.bms.report.month.service.IReportGoodsProfitService;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * wuliangfeng
 * 
 */
@Controller("reportGoodsProfitController")
public class ReportGoodsProfitController {

	private static final Logger logger = Logger.getLogger(ReportGoodsProfitController.class.getName());

	@Resource
	private IReportGoodsProfitService reportGoodsProfitService;
	@Autowired
	private ISystemCodeService systemCodeService;

	@DataProvider
	public void queryAll(Page<ReportGoodsProfitEntity> page,Map<String, Object> param){
		if(param==null){
			param=Maps.newHashMap();
		}
		//return getTotalList(new ArrayList<ReportGoodsProfitEntity>(),param);
		
		PageInfo<ReportGoodsProfitEntity> tmpPageInfo =reportGoodsProfitService.queryPage(param,page.getPageNo(),page.getPageSize());
		if(tmpPageInfo!=null){
			int pageNo=page.getPageNo();
			int pageSize=page.getPageSize();
			//page=new Page<ReportGoodsProfitEntity>(pageSize,pageNo);
			List<ReportGoodsProfitEntity> totalList=getTotalList(tmpPageInfo.getList(),param);
			page.setEntities(totalList);
			page.setEntityCount((int) tmpPageInfo.getTotal());
			
		}
	}
	private List<ReportGoodsProfitEntity> getTotalList(List<ReportGoodsProfitEntity> queryList,Map<String, Object> param){
		List<String> goodSkus=new ArrayList<String>();
		for(ReportGoodsProfitEntity entity:queryList){
			goodSkus.add(entity.getGoodsCode());
		}
		param.put("goodSkus", goodSkus);
		List<ReportGoodsProfitEntity> list=reportGoodsProfitService.queryAll(param);
		
		boolean isCheck=true;
		if(param.containsKey("IsCheck")){
			isCheck=Boolean.valueOf(param.get("IsCheck").toString());
		}
		String feesType="";
		if(param.containsKey("feesType")){
			feesType=param.get("feesType").toString();
		}
		initList(list);
		List<ReportGoodsProfitEntity> totalList=new ArrayList<ReportGoodsProfitEntity>();

		List<List<ReportGoodsProfitEntity>> listGroup=splitListByYear(list);//根据年份分组
		for(List<ReportGoodsProfitEntity> group:listGroup){
			ReportGoodsProfitEntity incomeEntity=null;
			ReportGoodsProfitEntity singleIncomeEntity=null;
			ReportGoodsProfitEntity costEntity=null;
			ReportGoodsProfitEntity realCostEntity=null;
			ReportGoodsProfitEntity singleCostEntity=null;
			ReportGoodsProfitEntity manageEntity=null;
			ReportGoodsProfitEntity singleManageEntity=null;
			ReportGoodsProfitEntity profitEntity=null;
			ReportGoodsProfitEntity singleProfitEntity=null;
			ReportGoodsProfitEntity countEntity=null;
			for(ReportGoodsProfitEntity entity:group){
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
			realCostEntity=getRealCostEntity(costEntity,manageEntity);
			countEntity=getCountEntity(group.get(0));
			singleIncomeEntity=getSingleEntity(incomeEntity,countEntity,5);
			singleCostEntity=getSingleEntity(realCostEntity,countEntity,6);
			singleManageEntity=getSingleEntity(manageEntity,countEntity,8);
			singleProfitEntity=getSingleEntity(profitEntity,countEntity,7);
			if(StringUtils.isBlank(feesType)){
				totalList.add(countEntity);
				totalList.add(incomeEntity);
				if(isCheck){
					totalList.add(manageEntity);
				}
				totalList.add(realCostEntity);
				totalList.add(profitEntity);
				totalList.add(singleIncomeEntity);
				if(isCheck){
					totalList.add(singleManageEntity);
				}
				totalList.add(singleCostEntity);
				totalList.add(singleProfitEntity);
			}else{
				switch(feesType){
				case "0":
					totalList.add(countEntity);
					break;
				case "1":
					totalList.add(incomeEntity);
					break;
				case "2":
					totalList.add(realCostEntity);
					break;
				case "3":
					if(isCheck){
						totalList.add(manageEntity);
					}
					break;
				case "4":
					totalList.add(profitEntity);
					break;
				case "5":
					totalList.add(singleIncomeEntity);
					break;
				case "6":
					totalList.add(singleCostEntity);
					break;
				case "7":
					totalList.add(singleProfitEntity);
					break;
				case "8":
					if(isCheck){
						totalList.add(singleManageEntity);
					}
					break;
				
				}
			}
			
		}
		return totalList;
	}
	
	private ReportGoodsProfitEntity getSingleEntity(
			ReportGoodsProfitEntity incomeEntity,
			ReportGoodsProfitEntity countEntity,int feesType) {
		ReportGoodsProfitEntity model=new ReportGoodsProfitEntity();
		model.setFeesType(feesType);
		model.setGoodsCode(incomeEntity.getGoodsCode());
		model.setGoodsName(incomeEntity.getGoodsName());
		if(incomeEntity.getAmount01()==BigDecimal.ZERO||countEntity.getAmount01()==BigDecimal.ZERO){
			model.setAmount01(BigDecimal.ZERO);
		}else{
			model.setAmount01(incomeEntity.getAmount01().divide(countEntity.getAmount01()));
		}
		if(incomeEntity.getAmount02()==BigDecimal.ZERO||countEntity.getAmount02()==BigDecimal.ZERO){
			model.setAmount02(BigDecimal.ZERO);
		}else{
			model.setAmount02(incomeEntity.getAmount02().divide(countEntity.getAmount02()));
		}
		if(incomeEntity.getAmount03()==BigDecimal.ZERO||countEntity.getAmount03()==BigDecimal.ZERO){
			model.setAmount03(BigDecimal.ZERO);
		}else{
			model.setAmount03(incomeEntity.getAmount03().divide(countEntity.getAmount03()));
		}
		if(incomeEntity.getAmount04()==BigDecimal.ZERO||countEntity.getAmount04()==BigDecimal.ZERO){
			model.setAmount04(BigDecimal.ZERO);
		}else{
			model.setAmount04(incomeEntity.getAmount04().divide(countEntity.getAmount04()));
		}
		if(incomeEntity.getAmount05()==BigDecimal.ZERO||countEntity.getAmount05()==BigDecimal.ZERO){
			model.setAmount05(BigDecimal.ZERO);
		}else{
			model.setAmount05(incomeEntity.getAmount05().divide(countEntity.getAmount05()));
		}
		if(incomeEntity.getAmount06()==BigDecimal.ZERO||countEntity.getAmount06()==BigDecimal.ZERO){
			model.setAmount06(BigDecimal.ZERO);
		}else{
			model.setAmount06(incomeEntity.getAmount06().divide(countEntity.getAmount06()));
		}
		if(incomeEntity.getAmount07()==BigDecimal.ZERO||countEntity.getAmount07()==BigDecimal.ZERO){
			model.setAmount07(BigDecimal.ZERO);
		}else{
			model.setAmount07(incomeEntity.getAmount07().divide(countEntity.getAmount07()));
		}
		if(incomeEntity.getAmount08()==BigDecimal.ZERO||countEntity.getAmount08()==BigDecimal.ZERO){
			model.setAmount08(BigDecimal.ZERO);
		}else{
			model.setAmount08(incomeEntity.getAmount08().divide(countEntity.getAmount08()));
		}
		if(incomeEntity.getAmount09()==BigDecimal.ZERO||countEntity.getAmount09()==BigDecimal.ZERO){
			model.setAmount09(BigDecimal.ZERO);
		}else{
			model.setAmount09(incomeEntity.getAmount09().divide(countEntity.getAmount09()));
		}
		if(incomeEntity.getAmount10()==BigDecimal.ZERO||countEntity.getAmount10()==BigDecimal.ZERO){
			model.setAmount10(BigDecimal.ZERO);
		}else{
			model.setAmount10(incomeEntity.getAmount10().divide(countEntity.getAmount10()));
		}
		if(incomeEntity.getAmount11()==BigDecimal.ZERO||countEntity.getAmount11()==BigDecimal.ZERO){
			model.setAmount11(BigDecimal.ZERO);
		}else{
			model.setAmount11(incomeEntity.getAmount11().divide(countEntity.getAmount11()));
		}
		if(incomeEntity.getAmount12()==BigDecimal.ZERO||countEntity.getAmount12()==BigDecimal.ZERO){
			model.setAmount12(BigDecimal.ZERO);
		}else{
			model.setAmount12(incomeEntity.getAmount12().divide(countEntity.getAmount12()));
		}
		if(incomeEntity.getAmountSum()==BigDecimal.ZERO||countEntity.getAmountSum()==BigDecimal.ZERO){
			model.setAmountSum(BigDecimal.ZERO);
		}else{
			model.setAmountSum(incomeEntity.getAmountSum().divide(countEntity.getAmountSum()));
		}
		return model;
	}
	private ReportGoodsProfitEntity getCountEntity(
			ReportGoodsProfitEntity entity) {
		ReportGoodsProfitEntity model=new ReportGoodsProfitEntity();
		model.setFeesType(0);
		model.setAmount01(BigDecimal.valueOf(entity.getGoodsQty01()));
		model.setAmount02(BigDecimal.valueOf(entity.getGoodsQty02()));
		model.setAmount03(BigDecimal.valueOf(entity.getGoodsQty03()));
		model.setAmount04(BigDecimal.valueOf(entity.getGoodsQty04()));
		model.setAmount05(BigDecimal.valueOf(entity.getGoodsQty05()));
		model.setAmount06(BigDecimal.valueOf(entity.getGoodsQty06()));
		model.setAmount07(BigDecimal.valueOf(entity.getGoodsQty07()));
		model.setAmount08(BigDecimal.valueOf(entity.getGoodsQty08()));
		model.setAmount09(BigDecimal.valueOf(entity.getGoodsQty09()));
		model.setAmount10(BigDecimal.valueOf(entity.getGoodsQty10()));
		model.setAmount11(BigDecimal.valueOf(entity.getGoodsQty11()));
		model.setAmount12(BigDecimal.valueOf(entity.getGoodsQty12()));
		model.setAmountSum(BigDecimal.valueOf(entity.getGoodsQtySum()));
		model.setGoodsCode(entity.getGoodsCode());
		model.setGoodsName(entity.getGoodsName());
		return model;
	}
	private void initList(List<ReportGoodsProfitEntity> list){
		for(ReportGoodsProfitEntity entity:list){
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
	private ReportGoodsProfitEntity getRealCostEntity(
			ReportGoodsProfitEntity costEntity,
			ReportGoodsProfitEntity manageEntity) {
		ReportGoodsProfitEntity realCostEntity=new ReportGoodsProfitEntity();
		realCostEntity.setGoodsCode(costEntity.getGoodsCode());
		realCostEntity.setGoodsName(costEntity.getGoodsName());
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

	private ReportGoodsProfitEntity getprofitEntity(
			ReportGoodsProfitEntity incomeEntity,
			ReportGoodsProfitEntity costEntity,
			ReportGoodsProfitEntity manageEntity, boolean isCheck) {
		ReportGoodsProfitEntity entity=new ReportGoodsProfitEntity();
		entity.setFeesType(4);//利润
		entity.setReportYear(incomeEntity.getReportYear());
		if(isCheck){//成本包含总部管理费  利润=收入-成本
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
		}else{//成本不包含总部管理费  利润=收入-（成本-总部管理费）
			entity.setAmount01(incomeEntity.getAmount01().subtract(costEntity.getAmount01().subtract(manageEntity.getAmount01())));
			entity.setAmount02(incomeEntity.getAmount02().subtract(costEntity.getAmount02().subtract(manageEntity.getAmount02())));
			entity.setAmount03(incomeEntity.getAmount03().subtract(costEntity.getAmount03().subtract(manageEntity.getAmount03())));
			entity.setAmount04(incomeEntity.getAmount04().subtract(costEntity.getAmount04().subtract(manageEntity.getAmount04())));
			entity.setAmount05(incomeEntity.getAmount05().subtract(costEntity.getAmount05().subtract(manageEntity.getAmount05())));
			entity.setAmount06(incomeEntity.getAmount06().subtract(costEntity.getAmount06().subtract(manageEntity.getAmount06())));
			entity.setAmount07(incomeEntity.getAmount07().subtract(costEntity.getAmount07().subtract(manageEntity.getAmount07())));
			entity.setAmount08(incomeEntity.getAmount08().subtract(costEntity.getAmount08().subtract(manageEntity.getAmount08())));
			entity.setAmount09(incomeEntity.getAmount09().subtract(costEntity.getAmount09().subtract(manageEntity.getAmount09())));
			entity.setAmount10(incomeEntity.getAmount10().subtract(costEntity.getAmount10().subtract(manageEntity.getAmount10())));
			entity.setAmount11(incomeEntity.getAmount11().subtract(costEntity.getAmount11().subtract(manageEntity.getAmount11())));
			entity.setAmount12(incomeEntity.getAmount12().subtract(costEntity.getAmount12().subtract(manageEntity.getAmount12())));
			entity.setAmountSum(incomeEntity.getAmountSum().subtract(costEntity.getAmountSum().subtract(manageEntity.getAmountSum())));
		}
		entity.setGoodsCode(incomeEntity.getGoodsCode());
		entity.setGoodsName(incomeEntity.getGoodsName());
		return entity;
	}

	private ReportGoodsProfitEntity getDefaultEntity(ReportGoodsProfitEntity model, int feesType) {
		ReportGoodsProfitEntity entity=new ReportGoodsProfitEntity();
		entity.setReportYear(model.getReportYear());
		entity.setGoodsCode(model.getGoodsCode());
		entity.setGoodsName(model.getGoodsName());
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

	private List<List<ReportGoodsProfitEntity>> splitListByYear(
			List<ReportGoodsProfitEntity> list) {
		List<List<ReportGoodsProfitEntity>> groupList=new ArrayList<List<ReportGoodsProfitEntity>>();
		Map<String,List<ReportGoodsProfitEntity>> map=Maps.newLinkedHashMap();
		for(ReportGoodsProfitEntity entity:list){
			if(map.containsKey(entity.getGoodsCode())){
				List<ReportGoodsProfitEntity> mapList=map.get(entity.getGoodsCode());
				mapList.add(entity);
				map.put(entity.getGoodsCode(), mapList);
			}else{
				List<ReportGoodsProfitEntity> mapList=new ArrayList<ReportGoodsProfitEntity>();
				mapList.add(entity);
				map.put(entity.getGoodsCode(), mapList);
			}
		}
		for(String key:map.keySet()){
			groupList.add(map.get(key));
		}
		return groupList;
	}
	
	@DataProvider
	public Map<String,String> getFeesType(){
		List<SystemCodeEntity> list=systemCodeService.findEnumList("REPORT_FEESTYPE");
		Map<String,String> map=Maps.newLinkedHashMap();
		map.put("0", "数量");
		for(SystemCodeEntity codeEntity:list){
			map.put(codeEntity.getCode(), codeEntity.getCodeName());
		}
		map.put("5", "单品收入");
		map.put("6", "单品成本");
		map.put("7", "单品利润");
		map.put("8", "单品总部管理费");
		return map;
	}
}
