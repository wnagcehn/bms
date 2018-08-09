package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsFeesStorageEntity;
import com.jiuyescm.bms.biz.repo.IFeesStorageRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

public class FeesStorageRepositoryImpl  extends MyBatisDao<BmsFeesStorageEntity> implements IFeesStorageRepository{

	@Override
	public int updateList(List<BmsFeesStorageEntity> list) {
		// TODO Auto-generated method stub
		return 0;
	}



}
