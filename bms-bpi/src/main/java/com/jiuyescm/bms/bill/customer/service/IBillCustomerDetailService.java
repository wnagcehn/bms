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

    /**
     * 对月份进行合并
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月17日 下午6:35:20
     *
     * @param condition
     * @return
     */
    List<BillCustomerDetailEntity> queryGroupByMonth(Map<String, Object> condition);

}
