package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.entity.BmsOutstockInfoEntity;
import com.jiuyescm.bms.biz.repo.IOutstockInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("outstockInfoRepositoryImpl")
public class OutstockInfoRepositoryImpl  extends MyBatisDao<BmsOutstockInfoEntity> implements IOutstockInfoRepository{

	@Override
	public int updateList(List<BmsOutstockInfoEntity> list) {
		// TODO Auto-generated method stub
		return this.updateBatch("com.jiuyescm.bms.biz.BmsOutstockInfoEntityMapper.update", list);
	}

	@Override
	public int update(BmsOutstockInfoEntity entity) {
		// TODO Auto-generated method stub
		return this.update("com.jiuyescm.bms.biz.BmsOutstockInfoEntityMapper.update", entity);
	}


}
