package com.jiuyescm.bms.biz.dispatch.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.api.IDispatchService;
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillEntity;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;

@Controller("outStockController")
public class OutStockController {
	
	private static final Logger logger = Logger.getLogger(OutStockController.class.getName());
	
	@Resource
	private IDispatchService dispatchService;
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryAll(Page<BmsDispatchVo> page, Map<String, Object> param) {
		if (param == null){
			param = new HashMap<String, Object>();
		}
		
		PageInfo<BmsDispatchVo> pageInfo = dispatchService.queryAll(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
}
