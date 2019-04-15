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

import com.jiuyescm.bms.report.month.entity.ReportCompanyProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportCompanyProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportCompanyProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportCompanyProfitService")
public class ReportCompanyProfitServiceImpl implements IReportCompanyProfitService {

	private static final Logger logger = Logger.getLogger(ReportCompanyProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportCompanyProfitRepository reportCompanyProfitRepository;

    @Override
    public List<ReportCompanyProfitEntity> query(Map<String, Object> condition) {
        return reportCompanyProfitRepository.query(condition);
    }
	
}
