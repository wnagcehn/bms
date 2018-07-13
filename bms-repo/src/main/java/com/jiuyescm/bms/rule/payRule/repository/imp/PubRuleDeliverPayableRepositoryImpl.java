/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.rule.payRule.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.PubRuleDeliverPayableEntity;
import com.jiuyescm.bms.rule.payRule.repository.IPubRuleDeliverPayableRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("pubRuleDeliverPayableRepository")
public class PubRuleDeliverPayableRepositoryImpl extends MyBatisDao<PubRuleDeliverPayableEntity> implements IPubRuleDeliverPayableRepository {

	private static final Logger logger = Logger.getLogger(PubRuleDeliverPayableRepositoryImpl.class.getName());

	@Override
	public PageInfo<PubRuleDeliverPayableEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubRuleDeliverPayableEntity> list = selectList("com.jiuyescm.bms.rule.payRule.mapper.PubRuleDeliverPayableMapper.queryAll", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubRuleDeliverPayableEntity> pageInfo = new PageInfo<PubRuleDeliverPayableEntity>(list);
        return pageInfo;
    }

	@Override
	public PubRuleDeliverPayableEntity query(Map<String, Object> condition) {
		SqlSession session = getSqlSessionTemplate();
		return session.selectOne("com.jiuyescm.bms.rule.payRule.mapper.PubRuleDeliverPayableMapper.query", condition);
	}

	@Override
	public int save(PubRuleDeliverPayableEntity entity) {
		return insert("com.jiuyescm.bms.rule.payRule.mapper.PubRuleDeliverPayableMapper.save", entity);
    }

	@Override
	public int update(PubRuleDeliverPayableEntity entity) {
    	return update("com.jiuyescm.bms.rule.payRule.mapper.PubRuleDeliverPayableMapper.update", entity);
    }

}
