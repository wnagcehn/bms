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
import com.jiuyescm.bms.report.month.entity.ReportGoodsProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportGoodsProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportGoodsProfitRepository")
public class ReportGoodsProfitRepositoryImpl extends MyBatisDao<ReportGoodsProfitEntity> implements IReportGoodsProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportGoodsProfitRepositoryImpl.class.getName());

	public ReportGoodsProfitRepositoryImpl() {
		super();
	}

	@Override
	public List<ReportGoodsProfitEntity> queryAll(Map<String, Object> param) {
		List<ReportGoodsProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportGoodsProfitEntityMapper.queryAll", param);
		return list;
	}

	@Override
	public PageInfo<ReportGoodsProfitEntity> queryPage(
			Map<String, Object> param, int pageNo, int pageSize) {
		List<ReportGoodsProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportGoodsProfitEntityMapper.queryPage", param,new RowBounds(pageNo,pageSize));
		PageInfo<ReportGoodsProfitEntity> plist=new PageInfo<ReportGoodsProfitEntity>(list);
		return plist;
	}
	
}
