package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;
import com.jiuyescm.bms.biz.repo.IOutstockRecordRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("outstockRecordRepositoryImpl")
public class OutstockRecordRepositoryImpl extends MyBatisDao<BmsOutstockRecordEntity> implements IOutstockRecordRepository{

	@Override
	public int insert(BmsOutstockRecordEntity entity) {
		// TODO Auto-generated method stub
		return this.insert("com.jiuyescm.bms.biz.BmsOutstockRecordMapper.save", entity);
	}

	@Override
	public int insertList(List<BmsOutstockRecordEntity> list) {
		// TODO Auto-generated method stub
		return this.insertBatch("com.jiuyescm.bms.biz.BmsOutstockRecordMapper.save", list);
	}

	@Override
	public PageInfo<BmsOutstockRecordEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		 List<BmsOutstockRecordEntity> list = selectList("com.jiuyescm.bms.biz.BmsOutstockRecordMapper.query", condition, new RowBounds(
	                pageNo, pageSize));
	     PageInfo<BmsOutstockRecordEntity> pageInfo = new PageInfo<BmsOutstockRecordEntity>(list);
	     return pageInfo;
	}
}
