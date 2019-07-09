/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.report.month.entity.ReportCarrierProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportCarrierProfitRepository;
import com.jiuyescm.bms.report.month.service.IReportCarrierProfitService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportCarrierProfitService")
public class ReportCarrierProfitServiceImpl implements IReportCarrierProfitService {
	
	@Autowired
    private IReportCarrierProfitRepository reportCarrierProfitRepository;

	@Override
	public List<ReportCarrierProfitEntity> queryAll(Map<String, Object> param) {
		
		return reportCarrierProfitRepository.queryAll(param);
	}

}
