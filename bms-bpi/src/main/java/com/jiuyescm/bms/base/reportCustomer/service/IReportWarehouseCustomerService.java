package com.jiuyescm.bms.base.reportCustomer.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.base.reportCustomer.vo.ReportWarehouseCustomerVo;

public interface IReportWarehouseCustomerService {
	List<ReportWarehouseCustomerVo> query(Map<String,Object> map);
	
	int save(ReportWarehouseCustomerVo vo);
	
	int update(ReportWarehouseCustomerVo vo);
	
	int updateList(List<ReportWarehouseCustomerVo> list);
}
