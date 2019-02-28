/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.chargerule.receiverule;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveRecordEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IReceiveRuleService;
import com.jiuyescm.bms.rule.receiveRule.repository.IBillRuleReceiveRecordRepository;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.common.JAppContext;

@Service("receiveRuleServiceImp")
public class ReceiveRuleServiceImp implements IReceiveRuleService{

	@Resource
	private IReceiveRuleRepository receiveRuleRepository;
	@Resource
	private IBillRuleReceiveRecordRepository billRuleReceiveRecordRepository;
	
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public void updateRule(BillRuleReceiveEntity aCondition) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("quotation", aCondition.getQuotationNo());
		BillRuleReceiveEntity billRuleReceiveEntity = receiveRuleRepository.queryRule(param);
		BillRuleReceiveRecordEntity recordEntity = new BillRuleReceiveRecordEntity();
		recordEntity.setQuotationNo(aCondition.getQuotationNo());
		recordEntity.setRule(billRuleReceiveEntity.getRule());
		recordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		recordEntity.setCreator(JAppContext.currentUserName());
		billRuleReceiveRecordRepository.save(recordEntity);
		receiveRuleRepository.updateRule(aCondition);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	@Override
	public void removeRule(String ruleId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", ruleId);
		BillRuleReceiveEntity billRuleReceiveEntity = receiveRuleRepository.queryRule(param);
		BillRuleReceiveRecordEntity recordEntity = new BillRuleReceiveRecordEntity();
		recordEntity.setQuotationNo(billRuleReceiveEntity.getQuotationNo());
		recordEntity.setRule(billRuleReceiveEntity.getRule());
		recordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
		recordEntity.setCreator(JAppContext.currentUserName());
		billRuleReceiveRecordRepository.save(recordEntity);
		receiveRuleRepository.removeRule(ruleId);
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