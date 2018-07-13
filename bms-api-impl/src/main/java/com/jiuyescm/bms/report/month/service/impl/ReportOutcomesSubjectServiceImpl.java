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

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportOutcomesSubjectEntity;
import com.jiuyescm.bms.report.month.repository.IReportOutcomesSubjectRepository;
import com.jiuyescm.bms.report.month.service.IReportOutcomesSubjectService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportOutcomesSubjectService")
public class ReportOutcomesSubjectServiceImpl implements IReportOutcomesSubjectService {

	private static final Logger logger = Logger.getLogger(ReportOutcomesSubjectServiceImpl.class.getName());
	
	@Autowired
    private IReportOutcomesSubjectRepository reportOutcomesSubjectRepository;

	@Override
	public List<ReportOutcomesSubjectEntity> queryAll(Map<String, Object> param) {

		return reportOutcomesSubjectRepository.queryAll(param);
	}

}
