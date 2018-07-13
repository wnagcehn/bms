/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportProjectProfitEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportProjectProfitRepository {

	public List<ReportProjectProfitEntity> query(Map<String, Object> condition);

}
