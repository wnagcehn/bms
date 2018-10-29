package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckInfoEntity;

/**
 * 
 * @author liuzhicheng
 * 
 */
public interface IBmsAccountOutService {

	PageInfo<BillCheckInfoEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize);
	
	/**
	 * 查询账单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */

	
}
