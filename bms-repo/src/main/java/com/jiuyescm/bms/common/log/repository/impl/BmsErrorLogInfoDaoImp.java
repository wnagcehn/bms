package com.jiuyescm.bms.common.log.repository.impl;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.common.log.entity.BmsErrorLogInfoEntity;
import com.jiuyescm.bms.common.log.repository.IBmsErrorLogInfoDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsErrorLogInfoDaoImp")
public class BmsErrorLogInfoDaoImp extends MyBatisDao<BmsErrorLogInfoEntity> implements IBmsErrorLogInfoDao{

	@Override
	public int log(BmsErrorLogInfoEntity record) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.common.log.BmsErrorLogInfoMapper.save", record);
	}

}
