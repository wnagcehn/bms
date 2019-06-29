package com.jiuyescm.bms.discount.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.discount.BmsDiscountAccountEntity;
import com.jiuyescm.bms.discount.FeesReceiveDispatchDiscountEntity;
import com.jiuyescm.bms.discount.FeesReceiveStorageDiscountEntity;


public interface IBmsDiscountRepository {
	public BmsDiscountAccountEntity queryAccount(Map<String,Object> condition);
	
	/**
	 * 通过物流产品类型汇总统计配送
	 * @param condition
	 * @return
	 */
	public List<BmsDiscountAccountEntity> queryAccountServiceList(Map<String,Object> condition);
	
	/**
	 * 汇总统计仓储
	 * @param condition
	 * @return
	 */
	public BmsDiscountAccountEntity queryStorageAccount(Map<String,Object> condition);
	
	/**
	 * 物流产品统计
	 * <功能描述>
	 * 
	 * @author zhaofeng
	 * @date 2019年6月14日 下午4:01:28
	 *
	 * @param condition
	 * @return
	 */
	public List<BmsDiscountAccountEntity> queryServiceAccount(Map<String,Object> condition);
	
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
	
	/**
	 * 分页查询折扣费用表
	 */
	PageInfo<FeesReceiveStorageDiscountEntity> queryStorageAll(Map<String, Object> condition, int pageNo,
            int pageSize);
	/**
	 * 批量折扣费用
	 * @param list
	 * @return
	 */
	int updateList(List<FeesReceiveDispatchDiscountEntity> list);
	
	/**
	 * 批量折扣费用
	 * @param list
	 * @return
	 */
	int updateStorageList(List<FeesReceiveStorageDiscountEntity> list);
	
	/**
	 * 批量插入到折扣费用记录表中
	 * @param condition
	 * @return
	 */
	public int insertFeeStorageDiscount(Map<String,Object> condition);
	
	/**
	 * 根据条件删除原折扣费用记录
	 */
	public int deleteFeeStorageDiscount(Map<String,Object> condition);
}
