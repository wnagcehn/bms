package com.jiuyescm.bms.biz.repo;

import java.util.List;

import com.jiuyescm.bms.biz.entity.BmsFeesStorageEntity;

public interface IFeesStorageRepository {
	public int updateList(List<BmsFeesStorageEntity> list);
}
