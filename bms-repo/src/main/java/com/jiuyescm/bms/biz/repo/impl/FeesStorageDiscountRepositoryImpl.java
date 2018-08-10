package com.jiuyescm.bms.biz.repo.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.entity.BmsFeesStorageDiscountEntity;
import com.jiuyescm.bms.biz.repo.IFeesStorageDiscountRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("feesStorageDiscountRepositoryImpl")
public class FeesStorageDiscountRepositoryImpl  extends MyBatisDao<BmsFeesStorageDiscountEntity> implements IFeesStorageDiscountRepository{

	@Override
	public int updateList(List<BmsFeesStorageDiscountEntity> list) {
		// TODO Auto-generated method stub
		
		return this.updateBatch("com.jiuyescm.bms.biz.BmsFeesStorageDiscountEntityMapper.update", list);
	}



}
