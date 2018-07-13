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
import com.jiuyescm.bms.report.month.entity.ReportWarehouseProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportWarehouseProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportWarehouseProfitRepository")
public class ReportWarehouseProfitRepositoryImpl extends MyBatisDao<ReportWarehouseProfitEntity> implements IReportWarehouseProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportWarehouseProfitRepositoryImpl.class.getName());

	public ReportWarehouseProfitRepositoryImpl() {
		super();
	}
	
	@Override
    public List<ReportWarehouseProfitEntity> query(Map<String, Object> condition) {
        List<ReportWarehouseProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportWarehouseProfitEntityMapper.query", condition);
        return list;
    }	
}
