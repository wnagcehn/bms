/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEncapEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEntity;

public interface IBillPayDispatchDistinctService {
	/**
	 * 查询所有的差异账单
	 */
	PageInfo<BillPayDispatchDistinctEncapEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillPayDispatchDistinctEntity update(BillPayDispatchDistinctEntity entity);
    
    int insertBatchExistUpdate(List<BillPayDispatchDistinctEntity> list);
    
	/**
	 * 查询所有账单编号
	 */
	List<String> queryBillNoList();

	List<BillPayDispatchDistinctEntity> queryListByBillNo(String billNo);
	
	/**
	 * 批量修改对账差异表
	 */
	public int updateList(List<BillPayDispatchDistinctEntity> list);
}
