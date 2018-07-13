/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.report.month.entity.ReportIncomesSubjectEntity;
import com.jiuyescm.bms.report.month.repository.IReportIncomesSubjectRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportIncomesSubjectRepository")
public class ReportIncomesSubjectRepositoryImpl extends MyBatisDao<ReportIncomesSubjectEntity> implements IReportIncomesSubjectRepository {

	private static final Logger logger = Logger.getLogger(ReportIncomesSubjectRepositoryImpl.class.getName());

	public ReportIncomesSubjectRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportIncomesSubjectEntity> queryAll(Map<String, Object> param) {
		List<ReportIncomesSubjectEntity> list= selectList("com.jiuyescm.bms.report.month.ReportIncomesSubjectEntityMapper.queryAll", param);
		return list;
	}


}
