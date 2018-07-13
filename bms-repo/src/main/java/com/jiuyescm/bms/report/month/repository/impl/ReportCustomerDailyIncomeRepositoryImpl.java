package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.month.entity.ReportCustomerDailyIncomeEntity;
import com.jiuyescm.bms.report.month.repository.IReportCustomerDailyIncomeRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("reportCustomerDailyIncomeRepository")
public class ReportCustomerDailyIncomeRepositoryImpl extends MyBatisDao<ReportCustomerDailyIncomeEntity> implements IReportCustomerDailyIncomeRepository{

	@Override
	public PageInfo<ReportCustomerDailyIncomeEntity> queryGroup(
			Map<String,Object> queryCondition, int pageNo,
			int pageSize) {
		List<ReportCustomerDailyIncomeEntity> list=this.selectList("com.jiuyescm.bms.report.month.ReportCustomerDailyIncomeMapper.queryGroup", queryCondition,new RowBounds(pageNo, pageSize));
		return new PageInfo<ReportCustomerDailyIncomeEntity>(list);
	}

	@Override
	public List<ReportCustomerDailyIncomeEntity> queryDetail(
			Map<String, Object> parameter) {
		
		return this.selectList("com.jiuyescm.bms.report.month.ReportCustomerDailyIncomeMapper.queryDetail", parameter);
	}

	@Override
	public PageInfo<ReportCustomerDailyIncomeEntity> queryDetailList(Map<String, Object> parameter, 
			int pageNo, int pageSize) {
		List<ReportCustomerDailyIncomeEntity> list=this.selectList("com.jiuyescm.bms.report.month.ReportCustomerDailyIncomeMapper.queryDetailList", parameter,new RowBounds(pageNo, pageSize));
		return new PageInfo<ReportCustomerDailyIncomeEntity>(list);
	}

}
