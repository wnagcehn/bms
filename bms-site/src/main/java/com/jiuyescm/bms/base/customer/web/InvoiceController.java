package com.jiuyescm.bms.base.customer.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.dict.api.ICustomerDictService;
import com.jiuyescm.bms.base.dict.vo.PubCustomerBaseVo;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.contract.custom.api.IContractCustomBaseService;


@Controller("invoiceController")
public class InvoiceController {

	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	@Resource
	private IContractCustomBaseService contractCustomBaseService;
    @Resource
    private ICustomerDictService customerDictService;
	
	@Expose
	public BillCheckInfoVo query(Map<String, Object> param) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("invoiceName", param.get("invoiceName").toString());
		BillCheckInfoVo billCheckInfoVo = billCheckInfoService.getLatestBill(map);
		return billCheckInfoVo;
	}
	
	@DataProvider
	public void query(Page<PubCustomerBaseVo> page, Map<String, Object> param) {
		PageInfo<PubCustomerBaseVo> pageInfo = customerDictService.queryPubCustomerBase(param,page.getPageNo(),page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
}