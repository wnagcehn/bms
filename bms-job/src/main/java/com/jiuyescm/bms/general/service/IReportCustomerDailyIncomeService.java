package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.general.entity.ReportCustomerDailyIncomeEntity;

public interface IReportCustomerDailyIncomeService {
	
	int saveList(List<ReportCustomerDailyIncomeEntity> list);
	
	int update(Map<String, Object> condition);
	
}
