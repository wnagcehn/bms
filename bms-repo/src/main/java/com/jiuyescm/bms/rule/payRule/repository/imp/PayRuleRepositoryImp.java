/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.rule.payRule.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.BillRulePayEntity;
import com.jiuyescm.bms.rule.payRule.repository.IPayRuleRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("payRuleRepositoryImp")
@SuppressWarnings("rawtypes")
public class PayRuleRepositoryImp extends MyBatisDao implements IPayRuleRepository{

	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BillRulePayEntity> queryAll(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<BillRulePayEntity> p=selectList("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.queryAll", parameter, new RowBounds(aPageNo,aPageSize));
		PageInfo<BillRulePayEntity> list=new PageInfo<>(p);
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int createRule(BillRulePayEntity aCondition) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.saveRule", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateRule(BillRulePayEntity aCondition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.updateRule", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int removeRule(String ruleId) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.removeRule", ruleId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BillRulePayEntity queryOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		try{
			BillRulePayEntity entity = (BillRulePayEntity) selectOne("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.queryAll", parameter);
			return entity;
		}
		catch(Exception ex){
			return null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public BillRulePayEntity queryByCustomer(Map<String, Object> parameter) {
		BillRulePayEntity entity =(BillRulePayEntity)selectOne("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.queryByCustomer", parameter);
		return entity;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BillRulePayEntity queryByDeliverId(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		BillRulePayEntity entity=(BillRulePayEntity) selectOne("com.jiuyescm.bms.rule.payRule.mapper.PayRuleMapper.queryByDeliverId", parameter);
		return entity;
	}
	
}