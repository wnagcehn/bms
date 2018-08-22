/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.chargerule.receiverule;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;

@Service("receiveRuleServiceImp")
public class ReceiveRuleServiceImp implements IReceiveRuleService{

	@Resource
	private IReceiveRuleRepository receiveRuleRepository;
	
	//查询所有的应收计费规则
	@Override
	public PageInfo<BillRuleReceiveEntity> queryAll(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		return receiveRuleRepository.queryAll(parameter, aPageNo, aPageSize);
	}
	
	@Override
	public PageInfo<BillRuleReceiveEntity> queryAllRule(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		return receiveRuleRepository.queryAllRule(parameter, aPageNo, aPageSize);
	}

	@Override
	public int createRule(BillRuleReceiveEntity aCondition) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.createRule(aCondition);
	}

	@Override
	public int updateRule(BillRuleReceiveEntity aCondition) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.updateRule(aCondition);
	}

	@Override
	public int removeRule(String ruleId) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.removeRule(ruleId);
	}

	@Override
	public BillRuleReceiveEntity queryOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.queryOne(parameter);
	}

	@Override
	public BillRuleReceiveEntity queryByCustomer(Map<String, Object> parameter) {
		return receiveRuleRepository.queryByCustomer(parameter);
	}

	@Override
	public BillRuleReceiveEntity queryByCustomerId(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.queryByCustomerId(parameter);
	}

	@Override
	public BillRuleReceiveEntity queryRuleByPriceType(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.queryRuleByPriceType(parameter);
	}

	@Override
	public BillRuleReceiveEntity queryRule(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return receiveRuleRepository.queryRule(parameter);
	}

}