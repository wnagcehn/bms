/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportGoodsProfitEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportGoodsProfitRepository {

	public List<ReportGoodsProfitEntity> queryAll(Map<String, Object> param);

	public PageInfo<ReportGoodsProfitEntity> queryPage(
			Map<String, Object> param, int pageNo, int pageSize);

}
