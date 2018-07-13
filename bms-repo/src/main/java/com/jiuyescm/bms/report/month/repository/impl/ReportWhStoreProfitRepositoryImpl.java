/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.report.month.entity.ReportWhStoreProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportWhStoreProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportWhStoreProfitRepository")
public class ReportWhStoreProfitRepositoryImpl extends MyBatisDao<ReportWhStoreProfitEntity> implements IReportWhStoreProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportWhStoreProfitRepositoryImpl.class.getName());

	public ReportWhStoreProfitRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportWhStoreProfitEntity> queryAll(Map<String, Object> param) {
		List<ReportWhStoreProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportWhStoreProfitEntityMapper.queryAll", param);
		return list;
	}
	
}
