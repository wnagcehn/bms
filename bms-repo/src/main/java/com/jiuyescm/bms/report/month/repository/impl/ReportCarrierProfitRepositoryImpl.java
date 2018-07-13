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
import com.jiuyescm.bms.report.month.entity.ReportCarrierProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportCarrierProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportCarrierProfitRepository")
public class ReportCarrierProfitRepositoryImpl extends MyBatisDao<ReportCarrierProfitEntity> implements IReportCarrierProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportCarrierProfitRepositoryImpl.class.getName());

	public ReportCarrierProfitRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportCarrierProfitEntity> queryAll(Map<String, Object> param) {
		List<ReportCarrierProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportCarrierProfitEntityMapper.queryAll",param);
		return list;
	}

}
