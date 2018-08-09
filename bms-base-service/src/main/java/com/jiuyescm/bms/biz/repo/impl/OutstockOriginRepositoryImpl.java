package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsOutstockOriginEntity;
import com.jiuyescm.bms.biz.repo.IOutstockOriginRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

public class OutstockOriginRepositoryImpl extends MyBatisDao<BmsOutstockOriginEntity> implements IOutstockOriginRepository{

	@Override
	public int updateList(List<BmsOutstockOriginEntity> list) {
		// TODO Auto-generated method stub
		return 0;
	}

}
