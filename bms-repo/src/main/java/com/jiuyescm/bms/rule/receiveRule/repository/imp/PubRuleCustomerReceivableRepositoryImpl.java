/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.rule.receiveRule.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.PubRuleCustomerReceivableEntity;
import com.jiuyescm.bms.rule.receiveRule.repository.IPubRuleCustomerReceivableRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("pubRuleCustomerReceivableRepository")
public class PubRuleCustomerReceivableRepositoryImpl extends MyBatisDao<PubRuleCustomerReceivableEntity> implements IPubRuleCustomerReceivableRepository {

	private static final Logger logger = Logger.getLogger(PubRuleCustomerReceivableRepositoryImpl.class.getName());
	
	@Override
    public PageInfo<PubRuleCustomerReceivableEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubRuleCustomerReceivableEntity> list = selectList("com.jiuyescm.bms.rule.receiveRule.mapper.PubRuleCustomerReceivableMapper.queryAll", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubRuleCustomerReceivableEntity> pageInfo = new PageInfo<PubRuleCustomerReceivableEntity>(list);
        return pageInfo;
    }

	@Override
	public PubRuleCustomerReceivableEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.rule.receiveRule.mapper.PubRuleCustomerReceivableMapper.query", condition);
	}
	
    @Override
    public int save(PubRuleCustomerReceivableEntity entity) {
    	return insert("com.jiuyescm.bms.rule.receiveRule.mapper.PubRuleCustomerReceivableMapper.save", entity);
    }

    @Override
    public int update(PubRuleCustomerReceivableEntity entity) {
    	return update("com.jiuyescm.bms.rule.receiveRule.mapper.PubRuleCustomerReceivableMapper.update", entity);
    }
	
}
