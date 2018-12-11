/**
 * 
 */
package com.jiuyescm.bms.report.biz.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.vo.ReportCustomerInOutVo;

/**
 * 商家维度报表服务
 * @author yangss
 */
public interface IReportCustomerService {
	
	/**
	 * 新增商家分页查询
	 * @param condition 条件
	 * @param pageNo 
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportCustomerInOutVo> queryIn(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 退仓商家分页查询
	 * @param condition 条件
	 * @param pageNo 
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportCustomerInOutVo> queryOut(Map<String, Object> condition, int pageNo, int pageSize);
	
}
