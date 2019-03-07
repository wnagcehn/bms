package com.jiuyescm.bms.base.customer.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerLookupEntity;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerVo;
import com.jiuyescm.bms.quotation.system.service.IBmsJiuyeQuotationSystemService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;

@Controller("customerController")
public class CustomerController {

	@Autowired private ICustomerService customerService;
	
	@Autowired
	private IBmsJiuyeQuotationSystemService bmsCustomerService;
	@Autowired
	private ICustomerDictService customerDictService;
	
	@DataProvider
	public void query(Page<PubCustomerVo> page,Map<String,Object> parameter) {
		if(null==parameter)parameter=new HashMap<String,Object>();
		PageInfo<PubCustomerVo> pageInfo = customerDictService.queryPubCustomer(parameter, page.getPageNo(), page.getPageSize());
		if(null!=page){
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int)pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public void queryLookup(Page<PubCustomerLookupEntity> page,Map<String,Object> parameter) {
		if(null==parameter)parameter=new HashMap<String,Object>();
		PageInfo<PubCustomerLookupEntity> pageInfo = customerDictService.queryPubCustomerLookup(parameter, page.getPageNo(), page.getPageSize());
		if(null!=page){
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int)pageInfo.getTotal());
		}
	}
}
