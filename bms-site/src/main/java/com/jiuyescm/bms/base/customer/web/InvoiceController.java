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
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.contract.custom.api.IContractCustomBaseService;
import com.jiuyescm.contract.custom.vo.ContractCustomBaseQueryVo;
import com.jiuyescm.contract.custom.vo.ContractCustomBaseVo;


@Controller("invoiceController")
public class InvoiceController {

	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	@Resource
	private IContractCustomBaseService contractCustomBaseService;
	
	@Expose
	public BillCheckInfoVo query(Map<String, Object> param) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("invoiceName", param.get("invoiceName").toString());
		BillCheckInfoVo billCheckInfoVo = billCheckInfoService.getLatestBill(map);
		return billCheckInfoVo;
	}
	
	@DataProvider
	public void query(Page<ContractCustomBaseVo> page, Map<String, Object> param) {
		ContractCustomBaseQueryVo vo = new ContractCustomBaseQueryVo();
		if (param != null && param.size() > 0) {
			vo.setCustomName(param.get("customName").toString());
		}
		vo.setPageNo(page.getPageNo());
		vo.setPageSize(page.getPageSize());
		PageInfo<ContractCustomBaseVo> pageInfo = contractCustomBaseService.queryAll(vo);
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
}