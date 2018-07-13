/**
 * 
 */
/**应收计费规则service
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.chargerule.receiverule.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;


public interface IReceiveRuleService{
	
	/**
	 * 查询所有的应收计费规则
	 * @param parameter
	 * @param aPageNo
	 * @param aPageSize
	 * @return
	 */
	public PageInfo<BillRuleReceiveEntity> queryAll(Map<String,Object> parameter,int aPageNo,int aPageSize);
	
	public PageInfo<BillRuleReceiveEntity> queryAllRule(Map<String,Object> parameter,int aPageNo,int aPageSize);

	/**
	 * 保存计费规则
	 * @param aCondition
	 * @return
	 */
	public int createRule(BillRuleReceiveEntity aCondition);
	
	/**
	 * 更新消费规则
	 * @param aCondition
	 * @return
	 */
	public int updateRule(BillRuleReceiveEntity aCondition);
	
	/**
	 * 删除计费规则
	 * @param ruleId
	 * @return
	 */
	public int removeRule(String ruleId);
	
	/**
	 * 查询计费规则
	 * @param parameter
	 * @return
	 */
	public BillRuleReceiveEntity queryOne(Map<String,Object> parameter);
	/**
	 * 根据商家费用科目查询规则
	 */
	public BillRuleReceiveEntity  queryByCustomer(Map<String, Object> parameter);
	
	/**
	 * 根据商家费用科目查询规则
	 */
	public BillRuleReceiveEntity  queryByCustomerId(Map<String, Object> parameter);

}