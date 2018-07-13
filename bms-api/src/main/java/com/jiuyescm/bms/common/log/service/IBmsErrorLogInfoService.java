package com.jiuyescm.bms.common.log.service;

import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;

public interface IBmsErrorLogInfoService {
	int log(BmsErrorLogInfoEntity record);
	
	int insertLog(String className, String methodName,
			String identify, String errorMsg);
}
