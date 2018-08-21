/**
 * 
 */
/**
 * @author zhaofeng
 *
 */
package com.jiuyescm.bms.rule.receiveRule.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveEntity;
import com.jiuyescm.bms.rule.receiveRule.repository.IReceiveRuleRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("receiveRuleRepositoryImp")
@SuppressWarnings("rawtypes")
public class ReceiveRuleRepositoryImp extends MyBatisDao implements IReceiveRuleRepository{

	private static final Logger logger = Logger.getLogger(ReceiveRuleRepositoryImp.class.getName());
	
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<BillRuleReceiveEntity> queryAll(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		List<BillRuleReceiveEntity> p=selectList("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryAll", parameter, new RowBounds(aPageNo,aPageSize));
		PageInfo<BillRuleReceiveEntity> list=new PageInfo<>(p);
		return list;
	}
	
	@Override
	public PageInfo<BillRuleReceiveEntity> queryAllRule(
			Map<String, Object> parameter, int aPageNo, int aPageSize) {
		List<BillRuleReceiveEntity> p=selectList("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryAllRule", parameter, new RowBounds(aPageNo,aPageSize));
		PageInfo<BillRuleReceiveEntity> list=new PageInfo<>(p);
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int createRule(BillRuleReceiveEntity aCondition) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.saveRule", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int updateRule(BillRuleReceiveEntity aCondition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.updateRule", aCondition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int removeRule(String ruleId) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.removeRule", ruleId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BillRuleReceiveEntity queryOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		try{
			BillRuleReceiveEntity entity = (BillRuleReceiveEntity) selectOne("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryOne", parameter);
			return entity;
		}
		catch(Exception ex){
			logger.info("查询规则异常--"+ex.getMessage());
			return null;
		}
	}

	@Override
	public BillRuleReceiveEntity queryByCustomer(Map<String, Object> parameter) {
		return (BillRuleReceiveEntity)super.selectOne("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryByCustomer", parameter);
	}
	
	@Override
	public BillRuleReceiveEntity queryByCustomerId(Map<String, Object> parameter) {
		return (BillRuleReceiveEntity)super.selectOne("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryByCustomerId", parameter);
	}

	@Override
	public BillRuleReceiveEntity queryRuleByPriceType(
			Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return (BillRuleReceiveEntity) super.selectOne("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryRuleByPriceType", parameter);
	}

	@Override
	public List<BillRuleReceiveEntity> queryAll(Map<String, Object> parameter) {
		List<BillRuleReceiveEntity> list=selectList("com.jiuyescm.bms.rule.receiveRule.mapper.ReceiveRuleMapper.queryAll", parameter);
		return list;
	}
}