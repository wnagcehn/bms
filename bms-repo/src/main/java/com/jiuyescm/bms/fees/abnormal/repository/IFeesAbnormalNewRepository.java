package com.jiuyescm.bms.fees.abnormal.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;

public interface IFeesAbnormalNewRepository {
	public PageInfo<FeesAbnormalEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
}
