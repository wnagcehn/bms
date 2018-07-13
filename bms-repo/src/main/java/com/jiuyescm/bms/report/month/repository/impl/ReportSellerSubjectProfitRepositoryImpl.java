/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.report.month.entity.ReportSellerSubjectProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportSellerSubjectProfitRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportSellerSubjectProfitRepository")
public class ReportSellerSubjectProfitRepositoryImpl extends MyBatisDao<ReportSellerSubjectProfitEntity> implements IReportSellerSubjectProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportSellerSubjectProfitRepositoryImpl.class.getName());

	public ReportSellerSubjectProfitRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportSellerSubjectProfitEntity> queryAll(
			Map<String, Object> param) {
		List<ReportSellerSubjectProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportSellerSubjectProfitEntityMapper.queryAll", param);
		return list;
	}

	@Override
	public List<ReportSellerSubjectProfitEntity> queryAllCP(Map<String, Object> param) {
		List<ReportSellerSubjectProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportSellerSubjectProfitEntityMapper.queryAllCP", param);
		return list;
	}
	
}
