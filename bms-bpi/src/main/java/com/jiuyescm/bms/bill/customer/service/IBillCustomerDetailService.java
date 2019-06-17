package com.jiuyescm.bms.bill.customer.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillCustomerDetailService {

    BillCustomerDetailEntity findById(Long id);
	
    PageInfo<BillCustomerDetailEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillCustomerDetailEntity> query(Map<String, Object> condition);

    BillCustomerDetailEntity save(BillCustomerDetailEntity entity);

    BillCustomerDetailEntity update(BillCustomerDetailEntity entity);

    void delete(Long id);

}
