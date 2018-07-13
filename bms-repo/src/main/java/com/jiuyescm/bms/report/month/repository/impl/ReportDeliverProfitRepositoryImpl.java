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
import com.jiuyescm.bms.report.month.entity.ReportDeliverProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportDeliverProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportDeliverProfitRepository")
public class ReportDeliverProfitRepositoryImpl extends MyBatisDao<ReportDeliverProfitEntity> implements IReportDeliverProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportDeliverProfitRepositoryImpl.class.getName());

	public ReportDeliverProfitRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportDeliverProfitEntity> queryAll(Map<String, Object> param) {
		
		List<ReportDeliverProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportDeliverProfitEntityMapper.queryAll", param);
		return list;
	}

	
}
