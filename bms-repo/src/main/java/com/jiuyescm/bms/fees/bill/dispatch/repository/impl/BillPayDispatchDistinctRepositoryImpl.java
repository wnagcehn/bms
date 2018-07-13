/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.bill.dispatch.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEncapEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillPayDispatchDistinctEntity;
import com.jiuyescm.bms.fees.bill.dispatch.repository.IBillPayDispatchDistinctRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("billPayDispatchDistinctRepository")
public class BillPayDispatchDistinctRepositoryImpl extends MyBatisDao<BillPayDispatchDistinctEntity> implements IBillPayDispatchDistinctRepository {

	@Override
    public PageInfo<BillPayDispatchDistinctEncapEntity> query(
    		Map<String, Object> condition, int pageNo, int pageSize) {
		SqlSession session = getSqlSessionTemplate();
		List<BillPayDispatchDistinctEncapEntity> list = session.selectList(
				"com.jiuyescm.bms.fees.bill.dispatch.mapper.BillPayDispatchDistinctMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BillPayDispatchDistinctEncapEntity>(list);
	}

    @Override
    public BillPayDispatchDistinctEntity update(BillPayDispatchDistinctEntity entity) {
        update("com.jiuyescm.bms.fees.bill.dispatch.mapper.BillPayDispatchDistinctMapper.update", entity);
        return entity;
    }

	@Override
	public int insertBatchExistUpdate(List<BillPayDispatchDistinctEntity> list) {
		return insertBatch("com.jiuyescm.bms.fees.bill.dispatch.mapper.BillPayDispatchDistinctMapper.saveExistUpdate", list);
	}

	@Override
	public List<String> queryBillNoList() {
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.fees.bill.dispatch.mapper.BillPayDispatchDistinctMapper.queryBillNoList", null);
	}

	@Override
	public List<BillPayDispatchDistinctEntity> queryListByBillNo(String billNo) {
		return selectList("com.jiuyescm.bms.fees.bill.dispatch.mapper.BillPayDispatchDistinctMapper.queryListByBillNo", billNo);
	}

	@Override
	public int updateList(List<BillPayDispatchDistinctEntity> list) {
		return updateBatch("com.jiuyescm.bms.fees.bill.dispatch.mapper.BillPayDispatchDistinctMapper.update", list);
	}
	
}
