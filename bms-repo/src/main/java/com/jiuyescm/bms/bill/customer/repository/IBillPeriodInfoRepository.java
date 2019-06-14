package com.jiuyescm.bms.bill.customer.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillPeriodInfoEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillPeriodInfoRepository {
	
	PageInfo<BillPeriodInfoEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillPeriodInfoEntity> query(Map<String, Object> condition);

    BillPeriodInfoEntity save(BillPeriodInfoEntity entity);

    BillPeriodInfoEntity update(BillPeriodInfoEntity entity);

    List<BillPeriodInfoEntity> queryByCustomer(Map<String, Object> condition);

}
