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

import com.jiuyescm.bms.report.month.entity.ReportSellerSubjectProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportSellerSubjectProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportSellerSubjectProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportSellerSubjectProfitService")
public class ReportSellerSubjectProfitServiceImpl implements IReportSellerSubjectProfitService {

	private static final Logger logger = Logger.getLogger(ReportSellerSubjectProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportSellerSubjectProfitRepository reportSellerSubjectProfitRepository;

	@Override
	public List<ReportSellerSubjectProfitEntity> queryAll(
			Map<String, Object> param) {
		return reportSellerSubjectProfitRepository.queryAll(param);
	}

	@Override
	public List<ReportSellerSubjectProfitEntity> queryAllCP(Map<String, Object> param) {
		return reportSellerSubjectProfitRepository.queryAllCP(param);
	}
	
}
