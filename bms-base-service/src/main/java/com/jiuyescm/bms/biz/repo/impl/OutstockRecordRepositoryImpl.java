package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;
import com.jiuyescm.bms.biz.repo.IOutstockRecordRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("outstockRecordRepositoryImpl")
public class OutstockRecordRepositoryImpl extends MyBatisDao<BmsOutstockRecordEntity> implements IOutstockRecordRepository{

	@Override
	public int insert(BmsOutstockRecordEntity entity) {
		// TODO Auto-generated method stub
		return this.insert("com.jiuyescm.bms.BmsOutstockRecordEntityMapper.save", entity);
	}

	@Override
	public int insertList(List<BmsOutstockRecordEntity> list) {
		// TODO Auto-generated method stub
		return this.insertBatch("com.jiuyescm.bms.BmsOutstockRecordEntityMapper.save", list);
	}
}
