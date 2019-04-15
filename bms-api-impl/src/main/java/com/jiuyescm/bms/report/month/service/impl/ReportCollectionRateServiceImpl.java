/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportCollectionRateEntity;
import com.jiuyescm.bms.report.month.repository.IReportCollectionRateRepository;
import com.jiuyescm.bms.report.month.service.IReportCollectionRateService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportCollectionRateService")
public class ReportCollectionRateServiceImpl implements IReportCollectionRateService {

	
	@Autowired
    private IReportCollectionRateRepository reportCollectionRateRepository;

	@Override
	public List<ReportCollectionRateEntity> queryAmount(Map<String, Object> param) {
		return reportCollectionRateRepository.queryAmount(param);
	}
	
}
