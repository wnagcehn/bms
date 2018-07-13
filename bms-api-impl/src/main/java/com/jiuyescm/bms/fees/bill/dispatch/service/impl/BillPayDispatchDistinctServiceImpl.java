/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEncapEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEntity;
import com.jiuyescm.bms.fees.bill.dispatch.repository.IBillPayDispatchDistinctRepository;
import com.jiuyescm.bms.fees.bill.dispatch.service.IBillPayDispatchDistinctService;

@Service("billPayDispatchDistinctService")
public class BillPayDispatchDistinctServiceImpl implements IBillPayDispatchDistinctService {

	@Autowired
    private IBillPayDispatchDistinctRepository billPayDispatchDistinctRepository;

    @Override
	public PageInfo<BillPayDispatchDistinctEncapEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return billPayDispatchDistinctRepository.query(condition, pageNo, pageSize);
	}
	
    @Override
    public BillPayDispatchDistinctEntity update(BillPayDispatchDistinctEntity entity) {
        return billPayDispatchDistinctRepository.update(entity);
    }

	@Override
	public int insertBatchExistUpdate(List<BillPayDispatchDistinctEntity> list) {
		return billPayDispatchDistinctRepository.insertBatchExistUpdate(list);
	}

	@Override
	public List<String> queryBillNoList() {
		return billPayDispatchDistinctRepository.queryBillNoList();
	}

	@Override
	public List<BillPayDispatchDistinctEntity> queryListByBillNo(String billNo) {
		return billPayDispatchDistinctRepository.queryListByBillNo(billNo);
	}

	@Override
	public int updateList(List<BillPayDispatchDistinctEntity> list) {
		return billPayDispatchDistinctRepository.updateList(list);
	}
	
}
