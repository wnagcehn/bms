/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportOverdueUnaccountService {
	
	/**
	 * 超期未收款 = 1.未收款金额+2.收款金额
	 * 1.未收款金额
	 * @param param
	 * @return
	 */
	List<ReportOverdueUnaccountEntity> queryUnaccountCost(Map<String, Object> param);

	/**
	 * 超期未收款 = 1.未收款金额+2.收款金额
	 * 2.收款金额
	 * @param param
	 * @return
	 */
	List<ReportOverdueUnaccountEntity> queryAccountCost(Map<String, Object> param);
	
	/**
	 * 超期未收款总额/应收款金额总额
	 * @param param
	 * @return
	 */
	List<ReportOverdueUnaccountEntity> queryTotalAmount(Map<String, Object> param);

}
