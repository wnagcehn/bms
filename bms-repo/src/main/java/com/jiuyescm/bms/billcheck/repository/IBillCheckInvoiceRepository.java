package com.jiuyescm.bms.billcheck.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckInvoiceEntity;
import com.jiuyescm.bms.billcheck.entity.BillInvoiceEntity;

public interface IBillCheckInvoiceRepository {
	PageInfo<BillCheckInvoiceEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 发票信息报表的查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillInvoiceEntity> queryReport(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	BillCheckInvoiceEntity queryInvoice(Map<String, Object> condition);
	
	int saveList(List<BillCheckInvoiceEntity> list);
	
	int save(BillCheckInvoiceEntity entity);
	
	List<BillCheckInvoiceEntity> queryByParam(Map<String, Object> condition);

	List<BillCheckInvoiceEntity> queryAllBillInvoice(List<Integer> checkIdList);
	
	public int update(BillCheckInvoiceEntity vo);
}
