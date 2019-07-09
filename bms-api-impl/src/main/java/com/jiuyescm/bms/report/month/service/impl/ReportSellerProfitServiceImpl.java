/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportSellerProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportSellerProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportSellerProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportSellerProfitService")
public class ReportSellerProfitServiceImpl implements IReportSellerProfitService {
	
	@Autowired
    private IReportSellerProfitRepository reportSellerProfitRepository;

    @Override
    public List<ReportSellerProfitEntity> query(Map<String, Object> condition) {
        return reportSellerProfitRepository.query(condition);
    }
}
