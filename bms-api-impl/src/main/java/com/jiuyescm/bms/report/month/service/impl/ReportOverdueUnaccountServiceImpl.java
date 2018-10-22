/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;
import com.jiuyescm.bms.report.month.repository.IReportOverdueUnaccountRepository;
import com.jiuyescm.bms.report.month.service.IReportCollectionRateService;
import com.jiuyescm.bms.report.month.service.IReportOverdueUnaccountService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportOverdueUnaccountService")
public class ReportOverdueUnaccountServiceImpl implements IReportOverdueUnaccountService {

	
	@Autowired
    private IReportOverdueUnaccountRepository reportOverdueUnaccountRepository;

	@Override
	public List<ReportOverdueUnaccountEntity> queryUnaccountCost(Map<String, Object> param) {
		return reportOverdueUnaccountRepository.queryUnaccountCost(param);
	}
	
	@Override
	public List<ReportOverdueUnaccountEntity> queryAccountCost(Map<String, Object> param) {
		return reportOverdueUnaccountRepository.queryAccountCost(param);
	}
	
	@Override
	public List<ReportOverdueUnaccountEntity> queryTotalAmount(Map<String, Object> param) {
		return reportOverdueUnaccountRepository.queryTotalAmount(param);
	}
	
}
