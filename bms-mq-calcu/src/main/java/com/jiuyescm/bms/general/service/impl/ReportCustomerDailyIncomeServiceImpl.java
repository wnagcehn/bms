package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.entity.ReportCustomerDailyIncomeEntity;
import com.jiuyescm.bms.general.service.IReportCustomerDailyIncomeService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Service("customerDailyIncomeService")
public class ReportCustomerDailyIncomeServiceImpl extends MyBatisDao<ReportCustomerDailyIncomeEntity> implements IReportCustomerDailyIncomeService{

	private static final Logger logger = Logger.getLogger(ReportCustomerDailyIncomeServiceImpl.class.getName());
	
	@Override
	public int saveList(List<ReportCustomerDailyIncomeEntity> list) {
		return insertBatch("com.jiuyescm.bms.general.mapper.ReportCustomerDailyIncomeMapper.save", list);
	}

	@Override
	public int update(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.update("com.jiuyescm.bms.general.mapper.ReportCustomerDailyIncomeMapper.updateByFeesDate", condition);
	}
	

}
