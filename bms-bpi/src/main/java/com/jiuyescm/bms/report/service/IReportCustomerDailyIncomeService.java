package com.jiuyescm.bms.report.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.vo.ReportCustomerDailyIncomeVo;

public interface IReportCustomerDailyIncomeService {
	
	PageInfo<ReportCustomerDailyIncomeVo> queryGroup(Map<String,Object> queryCondition,int pageNo,int pageSize) throws Exception;

	List<ReportCustomerDailyIncomeVo> queryDetail(Map<String, Object> parameter) throws Exception;
	
	PageInfo<ReportCustomerDailyIncomeVo> queryDetailList(Map<String, Object> parameter, int pageNo, int pageSize) throws Exception;
}
