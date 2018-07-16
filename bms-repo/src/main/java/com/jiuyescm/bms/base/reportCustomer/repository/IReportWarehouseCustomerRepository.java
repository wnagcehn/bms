package com.jiuyescm.bms.base.reportCustomer.repository;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;

public interface IReportWarehouseCustomerRepository {
	List<ReportWarehouseCustomerEntity> query(Map<String,Object> map);
	
	ReportWarehouseCustomerEntity queryOne(Map<String,Object> map);
	
	int save(ReportWarehouseCustomerEntity vo);
	
	int update(ReportWarehouseCustomerEntity vo);
	
	int updateList(List<ReportWarehouseCustomerEntity> list);
	
	int saveList(List<ReportWarehouseCustomerEntity> list);
}
