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
import com.jiuyescm.bms.report.month.entity.ReportRegionProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportRegionProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportRegionProfitRepository")
public class ReportRegionProfitRepositoryImpl extends MyBatisDao<ReportRegionProfitEntity> implements IReportRegionProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportRegionProfitRepositoryImpl.class.getName());

	public ReportRegionProfitRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportRegionProfitEntity> query(Map<String, Object> param) {
		 List<ReportRegionProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportRegionProfitEntityMapper.query", param);
		 return list;
		 
	}
}
