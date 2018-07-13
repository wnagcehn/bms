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
import com.jiuyescm.bms.report.month.entity.ReportOutcomesSubjectEntity;
import com.jiuyescm.bms.report.month.service.IReportOutcomesSubjectService;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("reportOutcomesSubjectController")
public class ReportOutcomesSubjectController {

	private static final Logger logger = Logger.getLogger(ReportOutcomesSubjectController.class.getName());

	@Resource
	private IReportOutcomesSubjectService reportOutcomesSubjectService;
	
	@DataProvider
	public List<ReportOutcomesSubjectEntity> queryAll(Map<String, Object> param){
		return reportOutcomesSubjectService.queryAll(param);
	}
}
