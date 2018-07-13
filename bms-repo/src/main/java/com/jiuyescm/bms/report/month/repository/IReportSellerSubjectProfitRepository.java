/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;
import com.jiuyescm.bms.report.month.entity.ReportSellerSubjectProfitEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportSellerSubjectProfitRepository {

	public List<ReportSellerSubjectProfitEntity> queryAllCP(
			Map<String, Object> param);
	
	public List<ReportSellerSubjectProfitEntity> queryAll(
			Map<String, Object> param);

}
