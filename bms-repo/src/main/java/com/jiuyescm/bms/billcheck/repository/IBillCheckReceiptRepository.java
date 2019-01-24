package com.jiuyescm.bms.billcheck.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckReceiptEntity;
import com.jiuyescm.bms.billcheck.entity.BillReceiptEntity;

public interface IBillCheckReceiptRepository {
	PageInfo<BillCheckReceiptEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 收款信息报表的查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillReceiptEntity> queryReport(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	BillCheckReceiptEntity queyReceipt(Map<String, Object> condition);
	
	List<BillCheckReceiptEntity> queryByParam(Map<String, Object> condition);
	
	int saveList(List<BillCheckReceiptEntity> list);
	
	int save(BillCheckReceiptEntity entity);

	public int update(BillCheckReceiptEntity entity);
}
