package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsOutstockInfoEntity;
import com.jiuyescm.bms.biz.repo.IOutstockInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

public class OutstockInfoRepositoryImpl  extends MyBatisDao<BmsOutstockInfoEntity> implements IOutstockInfoRepository{

	@Override
	public int updateList(List<BmsOutstockInfoEntity> list) {
		// TODO Auto-generated method stub
		return 0;
	}


}
