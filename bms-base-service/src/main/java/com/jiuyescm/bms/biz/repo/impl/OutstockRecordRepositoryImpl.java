package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;
import com.jiuyescm.bms.biz.repo.IOutstockRecordRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

public class OutstockRecordRepositoryImpl extends MyBatisDao<BmsOutstockRecordEntity> implements IOutstockRecordRepository{

	@Override
	public int updateList(List<BmsOutstockRecordEntity> list) {
		// TODO Auto-generated method stub
		return 0;
	}



}
