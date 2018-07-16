/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportCarrierProfitEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IReportCarrierProfitService {

	List<ReportCarrierProfitEntity> queryAll(Map<String, Object> param);

}