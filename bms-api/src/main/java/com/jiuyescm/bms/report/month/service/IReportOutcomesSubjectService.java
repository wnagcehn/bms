/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.month.entity.ReportOutcomesSubjectEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportOutcomesSubjectService {

	List<ReportOutcomesSubjectEntity> queryAll(Map<String, Object> param);

}
