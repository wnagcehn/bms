/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportDeliverProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportDeliverProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportDeliverProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportDeliverProfitService")
public class ReportDeliverProfitServiceImpl implements IReportDeliverProfitService {
	
	@Autowired
    private IReportDeliverProfitRepository reportDeliverProfitRepository;

	@Override
	public List<ReportDeliverProfitEntity> queryAll(Map<String, Object> param) {
		return reportDeliverProfitRepository.queryAll(param);
	}
	
}
