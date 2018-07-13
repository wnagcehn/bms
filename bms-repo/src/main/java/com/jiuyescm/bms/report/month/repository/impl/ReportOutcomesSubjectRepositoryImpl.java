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
import com.jiuyescm.bms.report.month.entity.ReportOutcomesSubjectEntity;
import com.jiuyescm.bms.report.month.repository.IReportOutcomesSubjectRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportOutcomesSubjectRepository")
public class ReportOutcomesSubjectRepositoryImpl extends MyBatisDao<ReportOutcomesSubjectEntity> implements IReportOutcomesSubjectRepository {

	private static final Logger logger = Logger.getLogger(ReportOutcomesSubjectRepositoryImpl.class.getName());

	public ReportOutcomesSubjectRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportOutcomesSubjectEntity> queryAll(Map<String, Object> param) {
		List<ReportOutcomesSubjectEntity> list=selectList("com.jiuyescm.bms.report.month.ReportOutcomesSubjectEntityMapper.queryAll", param);
		return list;
	}

	
}
