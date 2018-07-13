package com.jiuyescm.bms.billcheck.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckReceiptVo;


public interface IBillCheckReceiptService {
	/**
	 * 对账主表回款的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckReceiptVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 收款信息报表的查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckReceiptVo> queryReport(Map<String, Object> condition, int pageNo,
            int pageSize);

	BillCheckReceiptVo queyReceipt(Map<String, Object> condition);
	
	int saveList(List<BillCheckReceiptVo> list);

	void saveImportList(List<BillCheckReceiptVo> list,
			List<BillCheckInfoVo> billCheckVoList) throws Exception;
	
	public int update(BillCheckReceiptVo vo);
}