package com.jiuyescm.bms.billcheck.web;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;



@Controller("billDetailController")
public class BillAccountDetailController {
	@Resource private IBmsBillAccountInService billAccountInService;
	@Resource private IBmsAccountOutService billAccountOutService;
	
	@DataProvider  
	public void queryIn(Page<BillAccountInEntity> page,Map<String,Object> parameter){
		
		PageInfo<BillAccountInEntity> tmpPageInfo = billAccountInService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
	@DataProvider
	public Map<String, String> getOutType() {
		Map<String, String> mapValues = new LinkedHashMap<String, String>();
		mapValues.put("1", "账单扣款");
		mapValues.put("2", "其他扣款");
		return mapValues;
	}
	
	@DataProvider  
	public void queryOut(Page<BillAccountOutEntity> page,Map<String,Object> parameter){
		
		PageInfo<BillAccountOutEntity> tmpPageInfo = billAccountOutService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
}
