package com.jiuyescm.bms.common.message.repository.impl;

import org.springframework.stereotype.Repository;

import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.common.message.entity.OmsMessageEntity;
import com.jiuyescm.bms.common.message.repository.OmsMessageEntityDao;

/**
 * 报文表操作Dao
 * @author chenfei
 *
 */
@SuppressWarnings("rawtypes")
@Repository("omsMessageEntityDao")
public class OmsMessageEntityDaoImpl extends MyBatisDao implements OmsMessageEntityDao {

	@SuppressWarnings("unchecked")
	@Override
	public int saveOmsMessage(OmsMessageEntity omsMessage) {
		return this.insert("com.jiuyescm.bms.common.message.entity.OmsMessageEntityMapper.insert", omsMessage);
	}

}
