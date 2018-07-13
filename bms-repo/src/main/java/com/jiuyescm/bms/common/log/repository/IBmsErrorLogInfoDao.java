package com.jiuyescm.bms.common.log.repository;

import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;

public interface IBmsErrorLogInfoDao {
	int log(BmsErrorLogInfoEntity record);
}
