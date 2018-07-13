
package com.jiuyescm.bms.payable.dispatch.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;

public interface IBizDispatchBillPayService {
	
	/**
	 * 查询出所有的宅配运单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
    List<BizDispatchBillPayEntity> query(Map<String, Object> condition);
		
    /**
     * 单个更新业务数据
     * @param aCondition
     * @return
     */
    public int update(BizDispatchBillPayEntity entity);
    
    /**
     * 批量更新业务数据
     * @param aCondition
     * @return
     */
	public int updateBatch(List<BizDispatchBillPayEntity> list);
	
    
}