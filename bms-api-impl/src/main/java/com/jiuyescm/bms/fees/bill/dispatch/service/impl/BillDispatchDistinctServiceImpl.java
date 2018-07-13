package com.jiuyescm.bms.fees.bill.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchCompareEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchDistinctEntity;
import com.jiuyescm.bms.fees.bill.dispatch.repository.IBillDispatchDistinctRepository;
import com.jiuyescm.bms.fees.bill.dispatch.service.IBillDispatchDistinctService;
/**
 * 应收账单-宅配对账差异service实现类
 * 
 * @author yangshuaishuai
 *
 */
@Service("billDispatchDistinctService")
public class BillDispatchDistinctServiceImpl implements IBillDispatchDistinctService{

	@Resource
	private IBillDispatchDistinctRepository repository;
	
	@Override
	public int insertBatchExistUpdate(List<BillDispatchDistinctEntity> list) {
		return repository.insertBatchExistUpdate(list);
	}

	@Override
	public int update(BillDispatchDistinctEntity entity) {
		return repository.update(entity);
	}

	@Override
	public PageInfo<BillDispatchCompareEntity> queryVo(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.queryVo(condition, pageNo, pageSize);
	}

	@Override
	public int updateList(List<BillDispatchDistinctEntity> aCodition) {
		return repository.updateList(aCodition);
	}

	@Override
	public List<String> queryByWayBillNoList(List<String> list) {
		return repository.queryByWayBillNoList(list);
	}

	@Override
	public List<String> queryBillNoList() {
		return repository.queryBillNoList();
	}

	@Override
	public List<BillDispatchDistinctEntity> queryListByBillNo(String billNo) {
		return repository.queryListByBillNo(billNo);
	}

}
