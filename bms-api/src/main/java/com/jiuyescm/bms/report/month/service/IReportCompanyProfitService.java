/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.month.entity.ReportCompanyProfitEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportCompanyProfitService {

    List<ReportCompanyProfitEntity> query(Map<String, Object> condition);

}
