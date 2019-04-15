package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;


public interface IBillCheckLogService {
	/**
	 * 对账主表的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckLogVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	int addBillCheckLog(BillCheckLogVo logVo) throws Exception;
	
}