package com.jiuyescm.bms.biz.repo;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsOutstockInfoEntity;

public interface IOutstockInfoRepository {
	public int updateList(List<BmsOutstockInfoEntity> list);
	
	public int update(BmsOutstockInfoEntity entity);
}
