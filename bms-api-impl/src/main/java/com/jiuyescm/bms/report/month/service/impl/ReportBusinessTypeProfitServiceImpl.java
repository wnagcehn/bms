/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportBusinessTypeProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportBusinessTypeProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportBusinessTypeProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportBusinessTypeProfitService")
public class ReportBusinessTypeProfitServiceImpl implements IReportBusinessTypeProfitService {
	
	@Autowired
    private IReportBusinessTypeProfitRepository reportBusinessTypeProfitRepository;

	@Override
	public List<ReportBusinessTypeProfitEntity> queryAll(
			Map<String, Object> condition) {
		return reportBusinessTypeProfitRepository.queryAll(condition);
	}
	
}
