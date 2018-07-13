package com.jiuyescm.bms.fees.bill.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchCompareEntity;
import com.jiuyescm.bms.fees.bill.dispatch.entity.BillDispatchDistinctEntity;

public interface IBillDispatchDistinctRepository {
	
	int insertBatchExistUpdate(List<BillDispatchDistinctEntity> list);
	
	int update(BillDispatchDistinctEntity entity);
	
	/**
	 * 查询所有的差异账单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillDispatchCompareEntity> queryVo(Map<String, Object> condition, int pageNo,
            int pageSize);

	/**
	 * 批量修改对账差异表
	 * @param aCodition
	 * @return
	 */
	public int updateList(List<BillDispatchDistinctEntity> aCodition);
	
	List<String> queryByWayBillNoList(List<String> list);
	
	/**
	 * 查询所有账单编号
	 * @return
	 */
	List<String> queryBillNoList();
	
	List<BillDispatchDistinctEntity> queryListByBillNo(String billNo);
}
