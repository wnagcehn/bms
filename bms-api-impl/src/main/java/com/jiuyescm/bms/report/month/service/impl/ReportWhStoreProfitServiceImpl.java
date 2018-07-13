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
import com.jiuyescm.bms.report.month.entity.ReportWhStoreProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportWhStoreProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportWhStoreProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportWhStoreProfitService")
public class ReportWhStoreProfitServiceImpl implements IReportWhStoreProfitService {

	private static final Logger logger = Logger.getLogger(ReportWhStoreProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportWhStoreProfitRepository reportWhStoreProfitRepository;

	@Override
	public List<ReportWhStoreProfitEntity> queryAll(Map<String, Object> param) {
		return reportWhStoreProfitRepository.queryAll(param);
	}
	
}
