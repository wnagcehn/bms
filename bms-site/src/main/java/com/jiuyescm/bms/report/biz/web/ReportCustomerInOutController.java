package com.jiuyescm.bms.report.biz.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.service.IReportCustomerService;
import com.jiuyescm.bms.report.vo.ReportCustomerInOutVo;

/**
 * 商家报表
 * @author yangss
 */
@Controller("reportCustomerInOutController")
public class ReportCustomerInOutController {

	private static final Logger logger = Logger.getLogger(ReportCustomerInOutController.class.getName());

	@Autowired 
	private IReportCustomerService reportCustomerService;
	

	@DataProvider
	public void queryIn(Page<ReportCustomerInOutVo> page, Map<String, Object> parameter) {
		if (null == parameter) {
			parameter = new HashMap<String, Object>();
		}
		
		PageInfo<ReportCustomerInOutVo> pageInfo = reportCustomerService.queryIn(parameter, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataProvider
	public void queryOut(Page<ReportCustomerInOutVo> page, Map<String, Object> parameter) {
		if (null == parameter) {
			parameter = new HashMap<String, Object>();
		}
		
		PageInfo<ReportCustomerInOutVo> pageInfo = reportCustomerService.queryOut(parameter, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
}
