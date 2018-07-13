package com.jiuyescm.bms.billcheck.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInvoiceVo;


public interface IBillCheckInvoiceService {
	/**
	 * 发票的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInvoiceVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 发票信息报表的查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInvoiceVo> queryReport(Map<String, Object> condition, int pageNo,
            int pageSize);

	BillCheckInvoiceVo queryInvoice(Map<String, Object> condition);
	
	List<BillCheckInvoiceVo> queryByParam(Map<String, Object> condition);
	
	int saveList(List<BillCheckInvoiceVo> list);

	List<BillCheckInvoiceVo> queryAllBillInvoice(List<Integer> checkIdList);

	void saveImportInovice(List<BillCheckInvoiceVo> list,
			List<BillCheckInfoVo> checkVoList) throws Exception;
	
	public int update(BillCheckInvoiceVo vo);
}