/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.chargerule.payrule;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.chargerule.payrule.service.IPayRuleService;
import com.jiuyescm.bms.rule.payRule.repository.IPayRuleRepository;

@Service("payRuleServiceImp")
public class PayRuleServiceImp implements IPayRuleService{

	@Resource
	private IPayRuleRepository payRuleRepository;
	
	//查询所有的应收计费规则
	@Override
	public PageInfo<BillRulePayEntity> queryAll(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return payRuleRepository.queryAll(parameter, aPageNo, aPageSize);
	}

	@Override
	public int createRule(BillRulePayEntity aCondition) {
		// TODO Auto-generated method stub
		return payRuleRepository.createRule(aCondition);
	}

	@Override
	public int updateRule(BillRulePayEntity aCondition) {
		// TODO Auto-generated method stub
		return payRuleRepository.updateRule(aCondition);
	}

	@Override
	public int removeRule(String ruleId) {
		// TODO Auto-generated method stub
		return payRuleRepository.removeRule(ruleId);
	}

	@Override
	public BillRulePayEntity queryOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return payRuleRepository.queryOne(parameter);
	}
	
	@Override
	public BillRulePayEntity queryByCustomer(Map<String, Object> parameter) {
		return payRuleRepository.queryByCustomer(parameter);
	}

	@Override
	public BillRulePayEntity queryByDeliverId(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return payRuleRepository.queryByDeliverId(parameter);
	}
}