package com.jiuyescm.bms.report.biz.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCustomerInOutEntity;

public interface IReportCustomerRepository {
	//查询新增商家 
	PageInfo<ReportCustomerInOutEntity> queryIn(Map<String, Object> condition, int pageNo, int pageSize);
	
	//查询新增商家 
	PageInfo<ReportCustomerInOutEntity> queryOut(Map<String, Object> condition, int pageNo, int pageSize);
	
}
