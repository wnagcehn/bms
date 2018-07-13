package com.jiuyescm.bms.fees.bill.dispatch.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchCompareEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchDistinctEntity;
import com.jiuyescm.bms.fees.bill.dispatch.repository.IBillDispatchDistinctRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("billDispatchDistinctRepository")
@SuppressWarnings("rawtypes")
public class BillDispatchDistinctRepositoryImpl extends MyBatisDao implements
		IBillDispatchDistinctRepository {

	@Override
	@SuppressWarnings("unchecked")
	public int insertBatchExistUpdate(List<BillDispatchDistinctEntity> list) {
		return insertBatch("com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.saveExistUpdate", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int update(BillDispatchDistinctEntity entity) {
		return update("com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.update", entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BillDispatchCompareEntity> queryVo(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<BillDispatchCompareEntity> list = selectList(
				"com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.query",
				condition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BillDispatchCompareEntity>(list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateList(List<BillDispatchDistinctEntity> aCodition) {
		return updateBatch("com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.update", aCodition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> queryByWayBillNoList(List<String> list) {
		return selectList("com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.queryByWayBillNoList", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> queryBillNoList() {
		return selectList("com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.queryBillNoList", null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BillDispatchDistinctEntity> queryListByBillNo(String billNo) {
		return selectList("com.jiuyescm.bms.bill.dispatch.mapper.BillDispatchDistinctMapper.queryListByBillNo", billNo);
	}

}
