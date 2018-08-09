package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.entity.BmsFeesDispatchEntity;
import com.jiuyescm.bms.biz.repo.IFeesDispatchRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesDispatchRepositoryImpl")
public class FeesDispatchRepositoryImpl extends MyBatisDao<BmsFeesDispatchEntity> implements IFeesDispatchRepository{

	private static final Logger logger = LoggerFactory.getLogger(FeesDispatchRepositoryImpl.class.getName());

	
	@Override
	public int updateList(List<BmsFeesDispatchEntity> list) {
		// TODO Auto-generated method stub
		return this.updateBatch("com.jiuyescm.bms.BmsFeesDispatchEntityMapper.update", list);
	}
}
