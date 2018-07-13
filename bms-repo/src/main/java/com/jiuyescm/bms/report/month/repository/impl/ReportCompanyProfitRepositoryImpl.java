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
import com.jiuyescm.bms.report.month.entity.ReportCompanyProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportCompanyProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportCompanyProfitRepository")
public class ReportCompanyProfitRepositoryImpl extends MyBatisDao<ReportCompanyProfitEntity> implements IReportCompanyProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportCompanyProfitRepositoryImpl.class.getName());

	public ReportCompanyProfitRepositoryImpl() {
		super();
	}
	
	@Override
    public List<ReportCompanyProfitEntity> query(Map<String, Object> condition) {
        List<ReportCompanyProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportCompanyProfitEntityMapper.query", condition);
       return list;
    }
}
