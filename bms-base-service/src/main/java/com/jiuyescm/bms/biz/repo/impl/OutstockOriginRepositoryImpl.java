package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.entity.BmsOutstockOriginEntity;
import com.jiuyescm.bms.biz.repo.IOutstockOriginRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("outstockOriginRepositoryImpl")
public class OutstockOriginRepositoryImpl extends MyBatisDao<BmsOutstockOriginEntity> implements IOutstockOriginRepository{

	@Override
	public int updateList(List<BmsOutstockOriginEntity> list) {
		// TODO Auto-generated method stub
		return this.updateBatch("com.jiuyescm.bms.BmsOutstockOriginEntityMapper.update", list);
	}

}
