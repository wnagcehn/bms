package com.jiuyescm.bms.discount.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.discount.BmsDiscountAccountEntity;
import com.jiuyescm.bms.discount.FeesReceiveDispatchDiscountEntity;


public interface IBmsDiscountRepository {
	public BmsDiscountAccountEntity queryAccount(Map<String,Object> condition);
	
	/**
	 * 更新taskId到折扣费用表
	 * @param condition
	 * @return
	 */
	public int updateFeeDiscountTask(Map<String,Object> condition);
	
	/**
	 * 分页查询折扣费用表
	 */
	PageInfo<FeesReceiveDispatchDiscountEntity> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);
}
