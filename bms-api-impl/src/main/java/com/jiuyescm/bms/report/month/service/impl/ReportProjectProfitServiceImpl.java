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
import com.jiuyescm.bms.report.month.entity.ReportProjectProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportProjectProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportProjectProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportProjectProfitService")
public class ReportProjectProfitServiceImpl implements IReportProjectProfitService {

	private static final Logger logger = Logger.getLogger(ReportProjectProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportProjectProfitRepository reportProjectProfitRepository;

    @Override
    public List<ReportProjectProfitEntity> query(Map<String, Object> condition) {
        return reportProjectProfitRepository.query(condition);
    }
	
}
