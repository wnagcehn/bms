package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsFeesStorageDiscountEntity;
import com.jiuyescm.bms.biz.repo.IFeesStorageDiscountRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

public class FeesStorageDiscountRepositoryImpl  extends MyBatisDao<BmsFeesStorageDiscountEntity> implements IFeesStorageDiscountRepository{

	@Override
	public int updateList(List<BmsFeesStorageDiscountEntity> list) {
		// TODO Auto-generated method stub
		return 0;
	}



}
