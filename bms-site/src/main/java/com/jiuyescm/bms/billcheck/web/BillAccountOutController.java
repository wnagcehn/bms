package com.jiuyescm.bms.billcheck.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.service.IBillCheckInfoService;
import com.jiuyescm.bms.billcheck.service.IBmsAccountInfoService;
import com.jiuyescm.bms.billcheck.service.IBmsAccountOutService;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;

@Controller("billAccountOutController")
public class BillAccountOutController {
	
	private static final Logger logger = Logger.getLogger(BillAccountOutController.class.getName());
	
	@Autowired 
	private IBmsAccountOutService bmsAccountOutService;
	
	@Autowired
	private IBillCheckInfoService billCheckInfoService;
	
	@Autowired 
	private IBmsAccountInfoService bmsAccountInfoService;
	
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
	
	/**
	 * 冲抵
	 * 
	 * @param param
	 */
	@DataResolver
	public String save(Map<String, Object> param) {

		return 	bmsAccountOutService.save(param);
	}
}
