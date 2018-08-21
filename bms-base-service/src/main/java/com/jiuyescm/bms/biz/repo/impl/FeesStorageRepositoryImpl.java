package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.entity.BmsFeesStorageEntity;
import com.jiuyescm.bms.biz.repo.IFeesStorageRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesStorageRepositoryImpl")
public class FeesStorageRepositoryImpl  extends MyBatisDao<BmsFeesStorageEntity> implements IFeesStorageRepository{

	@Override
	public int updateList(List<BmsFeesStorageEntity> list) {
		// TODO Auto-generated method stub
		return this.updateBatch("com.jiuyescm.bms.BmsFeesStorageMapper.update", list);
	}
}
