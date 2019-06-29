package com.jiuyescm.bms.bill.customer.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillPeriodInfoEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillPeriodInfoService {
	
    PageInfo<BillPeriodInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillPeriodInfoEntity> query(Map<String, Object> condition);

    BillPeriodInfoEntity save(BillPeriodInfoEntity entity);

    BillPeriodInfoEntity update(BillPeriodInfoEntity entity);

    void delete(BillPeriodInfoEntity entity);

    /**
     * 根据主商家查询
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月13日 下午3:58:15
     *
     * @param condition
     * @return
     */
    List<BillPeriodInfoEntity> queryByCustomer(Map<String, Object> condition);

}
