package com.jiuyescm.bms.bill.customer.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillCustomerDetailRepository {

    BillCustomerDetailEntity findById(Long id);
	
	PageInfo<BillCustomerDetailEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillCustomerDetailEntity> query(Map<String, Object> condition);

    BillCustomerDetailEntity save(BillCustomerDetailEntity entity);

    BillCustomerDetailEntity update(BillCustomerDetailEntity entity);

    void delete(Long id);

}
