package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsFeesDispatchEntity;
import com.jiuyescm.bms.biz.repo.IFeesDispatchRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

public class FeesDispatchRepositoryImpl extends MyBatisDao<BmsFeesDispatchEntity> implements IFeesDispatchRepository{

	@Override
	public int updateList(List<BmsFeesDispatchEntity> list) {
		// TODO Auto-generated method stub
		return 0;
	}


}
