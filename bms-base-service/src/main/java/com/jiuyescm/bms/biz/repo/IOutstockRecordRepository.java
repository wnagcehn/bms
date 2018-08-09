package com.jiuyescm.bms.biz.repo;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;

public interface IOutstockRecordRepository {
	public int insert(BmsOutstockRecordEntity entity);
	
	public int insertList(List<BmsOutstockRecordEntity> list);
}
