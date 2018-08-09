package com.jiuyescm.bms.biz.repo;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsOutstockRecordEntity;

public interface IOutstockRecordRepository {
	public int updateList(List<BmsOutstockRecordEntity> list);
}
