/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.general.entity.SnapshotBillReceiptEntity;

public interface ISnapshotBillReceiptService {

	public List<SnapshotBillReceiptEntity> query(Map<String, Object> condition);

	void InsertBatch(List<SnapshotBillReceiptEntity> entities);

}