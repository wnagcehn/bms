package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportCustomerDailyIncomeEntity;

public interface IReportCustomerDailyIncomeRepository {
	
	PageInfo<ReportCustomerDailyIncomeEntity> queryGroup(Map<String,Object> queryCondition,int pageNo,int pageSize);

	List<ReportCustomerDailyIncomeEntity> queryDetail(Map<String, Object> parameter);
	
	PageInfo<ReportCustomerDailyIncomeEntity> queryDetailList(Map<String, Object> parameter, int pageNo, int pageSize);
}
