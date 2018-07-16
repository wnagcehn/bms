package com.jiuyescm.bms.base.reportCustomer.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.base.reportCustomer.repository.IReportWarehouseCustomerRepository;
import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("reportWarehouseCustomerRepository")
public class ReportWarehouseCustomerRepositoryImpl extends MyBatisDao<ReportWarehouseCustomerEntity> implements IReportWarehouseCustomerRepository{

	private static final Logger logger = Logger.getLogger(ReportWarehouseCustomerRepositoryImpl.class.getName());

	
	@Override
	public List<ReportWarehouseCustomerEntity> query(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.base.reportCustomer.mapper.ReportWarehouseCustomerMapper.query", map);
	}

	@Override
	public int save(ReportWarehouseCustomerEntity vo) {
		// TODO Auto-generated method stub
		try {
			return insert("com.jiuyescm.bms.base.reportCustomer.mapper.ReportWarehouseCustomerMapper.save", vo);
		} catch (Exception e) {
			logger.info(e);
		}
		return 0;
	}

	@Override
	public int update(ReportWarehouseCustomerEntity vo) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.base.reportCustomer.mapper.ReportWarehouseCustomerMapper.update", vo);
	}

	@Override
	public int updateList(List<ReportWarehouseCustomerEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.base.reportCustomer.mapper.ReportWarehouseCustomerMapper.update", list);
	}

	@Override
	public int saveList(List<ReportWarehouseCustomerEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.base.reportCustomer.mapper.ReportWarehouseCustomerMapper.save", list);
	}

	@Override
	public ReportWarehouseCustomerEntity queryOne(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (ReportWarehouseCustomerEntity) selectOne("com.jiuyescm.bms.base.reportCustomer.mapper.ReportWarehouseCustomerMapper.queryOne", map);
	}

}
