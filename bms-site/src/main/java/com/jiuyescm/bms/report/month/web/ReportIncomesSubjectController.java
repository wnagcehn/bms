/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.jiuyescm.bms.report.month.entity.ReportIncomesSubjectEntity;
import com.jiuyescm.bms.report.month.service.IReportIncomesSubjectService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportIncomesSubjectController")
public class ReportIncomesSubjectController {

	private static final Logger logger = Logger.getLogger(ReportIncomesSubjectController.class.getName());

	@Resource
	private IReportIncomesSubjectService reportIncomesSubjectService;
	
	@DataProvider
	public List<ReportIncomesSubjectEntity> queryAll(Map<String, Object> param){
		return reportIncomesSubjectService.queryAll(param);
	}

}
