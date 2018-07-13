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
import com.jiuyescm.bms.report.month.entity.ReportProjectProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportProjectProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportProjectProfitRepository")
public class ReportProjectProfitRepositoryImpl extends MyBatisDao<ReportProjectProfitEntity> implements IReportProjectProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportProjectProfitRepositoryImpl.class.getName());

	public ReportProjectProfitRepositoryImpl() {
		super();
	}
	
	@Override
    public List<ReportProjectProfitEntity> query(Map<String, Object> condition) {
        List<ReportProjectProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportProjectProfitEntityMapper.query", condition);
        return list;
    }

}
