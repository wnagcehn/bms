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
import com.jiuyescm.bms.report.month.entity.ReportGoodsProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportGoodsProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportGoodsProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportGoodsProfitService")
public class ReportGoodsProfitServiceImpl implements IReportGoodsProfitService {

	private static final Logger logger = Logger.getLogger(ReportGoodsProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportGoodsProfitRepository reportGoodsProfitRepository;

	@Override
	public List<ReportGoodsProfitEntity> queryAll(Map<String, Object> param) {
		return reportGoodsProfitRepository.queryAll(param);
	}

	@Override
	public PageInfo<ReportGoodsProfitEntity> queryPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		return reportGoodsProfitRepository.queryPage(param,pageNo,pageSize);
	}
	
}
