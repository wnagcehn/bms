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

import com.jiuyescm.bms.report.month.entity.ReportIncomesSubjectEntity;
import com.jiuyescm.bms.report.month.repository.IReportIncomesSubjectRepository;
import com.jiuyescm.bms.report.month.service.IReportIncomesSubjectService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportIncomesSubjectService")
public class ReportIncomesSubjectServiceImpl implements IReportIncomesSubjectService {

	private static final Logger logger = Logger.getLogger(ReportIncomesSubjectServiceImpl.class.getName());
	
	@Autowired
    private IReportIncomesSubjectRepository reportIncomesSubjectRepository;

	@Override
	public List<ReportIncomesSubjectEntity> queryAll(Map<String, Object> param) {

		return reportIncomesSubjectRepository.queryAll(param);
	}

	
}
