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
import com.jiuyescm.bms.report.month.entity.ReportSellerProfitEntity;
import com.jiuyescm.bms.report.month.repository.IReportSellerProfitRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("reportSellerProfitRepository")
public class ReportSellerProfitRepositoryImpl extends MyBatisDao<ReportSellerProfitEntity> implements IReportSellerProfitRepository {

	private static final Logger logger = Logger.getLogger(ReportSellerProfitRepositoryImpl.class.getName());

	public ReportSellerProfitRepositoryImpl() {
		super();
	}
	
	@Override
    public List<ReportSellerProfitEntity> query(Map<String, Object> condition) {
        List<ReportSellerProfitEntity> list = selectList("com.jiuyescm.bms.report.month.ReportSellerProfitEntityMapper.query", condition);
        return list;
    }
}
