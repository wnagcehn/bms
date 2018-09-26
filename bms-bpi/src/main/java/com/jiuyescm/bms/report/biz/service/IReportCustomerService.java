/**
 * 
 */
package com.jiuyescm.bms.report.biz.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.vo.ReportCustomerInOutVo;

/**
 * @author yangss
 */
public interface IReportCustomerService {
	
	//查询新增商家 
	PageInfo<ReportCustomerInOutVo> queryIn(Map<String, Object> condition, int pageNo, int pageSize);
	
	//查询新增商家 
	PageInfo<ReportCustomerInOutVo> queryOut(Map<String, Object> condition, int pageNo, int pageSize);
	
}
