package com.jiuyescm.bms.billcheck.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerInfoService;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;

@Controller("billAccountOutController")
public class BillAccountOutController {
	
	@Autowired 
	private IBmsAccountOutService bmsAccountOutService;
	
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	/**
	 * 分页查询账单
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BillCheckInfoVo> page, Map<String, Object> param) {		

		if (param == null){
			param = new HashMap<String, Object>();
		}		

		PageInfo<BillCheckInfoVo> pageInfo = billCheckInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}	
	}

}
