package com.jiuyescm.bms.biz.repo.impl;

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
}
