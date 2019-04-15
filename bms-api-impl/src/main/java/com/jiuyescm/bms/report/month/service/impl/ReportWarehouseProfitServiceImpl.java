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

import com.jiuyescm.bms.report.month.entity.ReportWarehouseProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportWarehouseProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportWarehouseProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportWarehouseProfitService")
public class ReportWarehouseProfitServiceImpl implements IReportWarehouseProfitService {

	private static final Logger logger = Logger.getLogger(ReportWarehouseProfitServiceImpl.class.getName());
	
	@Autowired
    private IReportWarehouseProfitRepository reportWarehouseProfitRepository;

	@Override
	public List<ReportWarehouseProfitEntity> query(Map<String, Object> condition) {
		return reportWarehouseProfitRepository.query(condition);
	}

	/*
    @Override
    public PageInfo<ReportWarehouseProfitEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return reportWarehouseProfitRepository.query(condition, pageNo, pageSize);
    }*/
}
