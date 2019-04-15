/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.entity.SnapshotBillReceiptEntity;
import com.jiuyescm.bms.general.service.ISnapshotBillReceiptService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Service("snapshotBillReceiptServiceImpl")
public class SnapshotBillReceiptServiceImpl extends MyBatisDao implements ISnapshotBillReceiptService {

	@Override
	public List<SnapshotBillReceiptEntity> query(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.general.mapper.SnapshotBillReceiptMapper.query", condition);
	}
	
	@Override
	public void InsertBatch(List<SnapshotBillReceiptEntity> entities) {
		this.insertBatch("com.jiuyescm.bms.general.mapper.SnapshotBillReceiptMapper.insert", entities);
	}
	
}