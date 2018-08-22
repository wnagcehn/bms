/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.rule.receiveRule.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;

public interface IReceiveRuleRepository{
	/**
	 * 查询所有的应收计费规则
	 * @param parameter
	 * @param aPageNo
	 * @param aPageSize
	 * @return
	 */
	public PageInfo<BillRuleReceiveEntity> queryAll(Map<String,Object> parameter,int aPageNo,int aPageSize);
	
	List<BillRuleReceiveEntity> queryAll(Map<String,Object> parameter);
	
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
	 * 根据商家查询规则
	 * @param parameter
	 * @return
	 */
	public BillRuleReceiveEntity queryByCustomer(Map<String,Object> parameter);
	
	/**
	 * 根据商家费用科目查询规则
	 */
	public BillRuleReceiveEntity  queryByCustomerId(Map<String, Object> parameter);
	
	/**
	 * 根据报价形式查询规则
	 */
	public BillRuleReceiveEntity queryRuleByPriceType(Map<String, Object> parameter);
	
	/**
	 * 根据条件查询规则
	 */
	public BillRuleReceiveEntity queryRule(Map<String, Object> parameter);
}