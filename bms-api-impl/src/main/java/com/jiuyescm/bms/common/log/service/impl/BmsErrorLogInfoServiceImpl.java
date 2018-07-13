package com.jiuyescm.bms.common.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.repository.IBmsErrorLogInfoDao;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.cfm.common.JAppContext;

@Service("bmsErrorLogInfoServiceImpl")
public class BmsErrorLogInfoServiceImpl implements IBmsErrorLogInfoService{

	@Resource private IBmsErrorLogInfoDao bmsErrorLogInfoDao;
	
	@Override
	public int log(BmsErrorLogInfoEntity record) {
		// TODO Auto-generated method stub
		record.setCreateTime(JAppContext.currentTimestamp());
		return bmsErrorLogInfoDao.log(record);
	}

	@Override
	public int insertLog(String className, String methodName, String identify,
			String errorMsg) {
		// TODO Auto-generated method stub
		BmsErrorLogInfoEntity record=new BmsErrorLogInfoEntity(className,methodName,identify,errorMsg);
		record.setCreateTime(JAppContext.currentTimestamp());
		return bmsErrorLogInfoDao.log(record);
	}
	
	

}
