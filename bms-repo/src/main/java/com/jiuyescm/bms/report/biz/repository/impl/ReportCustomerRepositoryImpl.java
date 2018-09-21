package com.jiuyescm.bms.report.biz.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.ReportCustomerInOutEntity;
import com.jiuyescm.bms.report.biz.repository.IReportCustomerRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("ReportCustomerRepository")
public class ReportCustomerRepositoryImpl extends MyBatisDao<ReportCustomerInOutEntity> implements IReportCustomerRepository{

	@Override
	public PageInfo<ReportCustomerInOutEntity> queryIn(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<ReportCustomerInOutEntity> list = selectList("com.jiuyescm.bms.report.biz.mapper.ReportCustomerMapper.queryIn", condition, new RowBounds(pageNo, pageSize));
		PageInfo<ReportCustomerInOutEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

	@Override
	public PageInfo<ReportCustomerInOutEntity> queryOut(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<ReportCustomerInOutEntity> list = selectList("com.jiuyescm.bms.report.biz.mapper.ReportCustomerMapper.queryOut", condition, new RowBounds(pageNo, pageSize));
		PageInfo<ReportCustomerInOutEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

}
