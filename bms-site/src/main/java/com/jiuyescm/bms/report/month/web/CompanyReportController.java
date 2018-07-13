package com.jiuyescm.bms.report.month.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.service.IBmsMonthReportService;
import com.jiuyescm.bms.report.month.vo.BmsMonthReportVo;


@Controller("companyReportController")
public class CompanyReportController {

	@Resource private IBmsMonthReportService bmsReportCompanyProfitService;

	@DataProvider  
	public void queryAll(Page<BmsMonthReportVo> page,Map<String,Object> parameter){
		PageInfo<BmsMonthReportVo> tmpPageInfo = bmsReportCompanyProfitService.query(parameter, page.getPageNo(),page.getPageSize());
		if (tmpPageInfo != null) {
			page.setEntities(tmpPageInfo.getList());
			page.setEntityCount((int) tmpPageInfo.getTotal());
		}
	}
	
}
