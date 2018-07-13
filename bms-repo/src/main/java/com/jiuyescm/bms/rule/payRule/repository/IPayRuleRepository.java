/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.rule.payRule.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;

public interface IPayRuleRepository{
	/**
	 * 查询所有的应收计费规则
	 * @param parameter
	 * @param aPageNo
	 * @param aPageSize
	 * @return
	 */
	public PageInfo<BillRulePayEntity> queryAll(Map<String,Object> parameter,int aPageNo,int aPageSize);

	
	/**
	 * 保存计费规则
	 * @param aCondition
	 * @return
	 */
	public int createRule(BillRulePayEntity aCondition);
	
	/**
	 * 更新消费规则
	 * @param aCondition
	 * @return
	 */
	public int updateRule(BillRulePayEntity aCondition);
	
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
	public BillRulePayEntity queryOne(Map<String,Object> parameter);
	
	/**
	 * 根据商家查询规则
	 * @param parameter
	 * @return
	 */
	public BillRulePayEntity queryByCustomer(Map<String,Object> parameter);
	
	/**
	 * 根据宅配商费用科目查询规则
	 */
	public BillRulePayEntity  queryByDeliverId(Map<String, Object> parameter);

}