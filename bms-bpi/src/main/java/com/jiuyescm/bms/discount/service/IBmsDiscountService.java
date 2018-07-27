package com.jiuyescm.bms.discount.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.discount.vo.BmsDiscountAccountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveDispatchDiscountVo;

public interface IBmsDiscountService {
	
	/**
	 * 汇总统计
	 * @param condition
	 * @return
	 */
	public BmsDiscountAccountVo queryAccount(Map<String,Object> condition);
	
	/**
	 * 更新taskId到折扣费用表
	 * @param condition
	 * @return
	 */
	public int updateFeeDiscountTask(Map<String,Object> condition);
	
	
	/**
	 * 分页查询折扣费用表
	 */
	PageInfo<FeesReceiveDispatchDiscountVo> queryAll(Map<String, Object> condition, int pageNo,
            int pageSize);
    
	
}
