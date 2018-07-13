package com.jiuyescm.bms.common.message.repository;

import com.jiuyescm.bms.common.message.entity.OmsMessageEntity;

public interface OmsMessageEntityDao {
	/**
	 * 保存报文
	 * @param omsMessage
	 * @return
	 */
	public int saveOmsMessage(OmsMessageEntity omsMessage);
}
