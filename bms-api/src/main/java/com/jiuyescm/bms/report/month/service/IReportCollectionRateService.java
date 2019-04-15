/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.month.entity.ReportCollectionRateEntity;

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
