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
import com.jiuyescm.bms.report.month.entity.ReportBusinessTypeProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportBusinessTypeProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportBusinessTypeProfitRepository")
public class ReportBusinessTypeProfitRepositoryImpl extends MyBatisDao<ReportBusinessTypeProfitEntity> implements IReportBusinessTypeProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportBusinessTypeProfitRepositoryImpl.class.getName());

	public ReportBusinessTypeProfitRepositoryImpl() {
		super();
	}
	

	@Override
	public List<ReportBusinessTypeProfitEntity> queryAll(
			Map<String, Object> condition) {
		List<ReportBusinessTypeProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportBusinessTypeProfitEntityMapper.query", condition);
		return list;
	}
	
}
