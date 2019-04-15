/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportRegionProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportRegionProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportRegionProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportRegionProfitService")
public class ReportRegionProfitServiceImpl implements IReportRegionProfitService {

	private static final Logger logger = Logger.getLogger(ReportRegionProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportRegionProfitRepository reportRegionProfitRepository;

	@Override
	public List<ReportRegionProfitEntity> query(Map<String, Object> param) {
		return reportRegionProfitRepository.query(param);
	}

}
