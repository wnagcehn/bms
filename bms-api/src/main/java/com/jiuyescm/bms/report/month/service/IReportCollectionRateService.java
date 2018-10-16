/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportCollectionRateEntity;
import com.jiuyescm.bms.report.month.entity.ReportIncomesSubjectEntity;
import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportCollectionRateService {
	
	/**
	 * 收款达成率金额查询
	 * @param param
	 * @return
	 */
	List<ReportCollectionRateEntity> queryAmount(Map<String, Object> param);

}
