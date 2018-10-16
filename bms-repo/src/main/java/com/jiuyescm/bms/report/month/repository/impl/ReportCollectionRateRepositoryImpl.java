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
import com.jiuyescm.bms.report.month.entity.ReportCollectionRateEntity;
import com.jiuyescm.bms.report.month.entity.ReportIncomesSubjectEntity;
import com.jiuyescm.bms.report.month.entity.ReportOverdueUnaccountEntity;
import com.jiuyescm.bms.report.month.repository.IReportCollectionRateRepository;
import com.jiuyescm.bms.report.month.repository.IReportIncomesSubjectRepository;
import com.jiuyescm.bms.report.month.repository.IReportOverdueUnaccountRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportCollectionRateRepository")
public class ReportCollectionRateRepositoryImpl extends MyBatisDao<ReportCollectionRateEntity> implements IReportCollectionRateRepository {

	//private static final Logger logger = Logger.getLogger(ReportCollectionRateRepositoryImpl.class.getName());

	public ReportCollectionRateRepositoryImpl() {
		super();
	}

	
	@Override
	public List<ReportCollectionRateEntity> queryAmount(Map<String, Object> param) {
		return this.selectList("com.jiuyescm.bms.report.month.ReportCollectionRateMapper.queryAmount", param);
	}

}
