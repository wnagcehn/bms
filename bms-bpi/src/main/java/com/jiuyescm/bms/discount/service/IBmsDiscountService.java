package com.jiuyescm.bms.discount.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.discount.vo.BmsDiscountAccountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveDispatchDiscountVo;
import com.jiuyescm.bms.discount.vo.FeesReceiveStorageDiscountVo;

public interface IBmsDiscountService {
	
	/**
	 * 汇总统计配送
	 * @param condition
	 * @return
	 */
	public BmsDiscountAccountVo queryAccount(Map<String,Object> condition);
	
	/**
	 * 汇总统计仓储
	 * @param condition
	 * @return
	 */
	public BmsDiscountAccountVo queryStorageAccount(Map<String,Object> condition);
	
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
    
	/**
	 * 分页查询折扣费用表
	 */
	PageInfo<FeesReceiveStorageDiscountVo> queryStorageAll(Map<String, Object> condition, int pageNo,
            int pageSize);
    
	
	/**
	 * 批量折扣费用
	 * @param list
	 * @return
	 */
	int updateList(List<FeesReceiveDispatchDiscountVo> list);
	
	/**
	 * 批量折扣费用
	 * @param list
	 * @return
	 */
	int updateStorageList(List<FeesReceiveStorageDiscountVo> list);
	
	/**
	 * 根据条件删除原折扣费用记录
	 */
	public int deleteFeeStorageDiscount(Map<String,Object> condition);
	
	/**
	 * 批量插入到折扣费用记录表中
	 * @param condition
	 * @return
	 */
	public int insertFeeStorageDiscount(Map<String,Object> condition);
}
