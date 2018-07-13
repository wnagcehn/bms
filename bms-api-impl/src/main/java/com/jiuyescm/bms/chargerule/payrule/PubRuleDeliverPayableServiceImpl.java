/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.chargerule.payrule;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.PubRuleDeliverPayableEntity;
import com.jiuyescm.bms.chargerule.payrule.service.IPubRuleDeliverPayableService;
import com.jiuyescm.bms.rule.payRule.repository.IPubRuleDeliverPayableRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Service("pubRuleDeliverPayableService")
public class PubRuleDeliverPayableServiceImpl implements IPubRuleDeliverPayableService {

	private static final Logger logger = Logger.getLogger(PubRuleDeliverPayableServiceImpl.class.getName());
	
	@Autowired
    private IPubRuleDeliverPayableRepository pubRuleDeliverPayableRepository;

	@Override
	public PageInfo<PubRuleDeliverPayableEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return pubRuleDeliverPayableRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public PubRuleDeliverPayableEntity query(Map<String, Object> condition) {
		return pubRuleDeliverPayableRepository.query(condition);
	}

	@Override
	public int save(PubRuleDeliverPayableEntity entity) {
		return pubRuleDeliverPayableRepository.save(entity);
	}

	@Override
	public int update(PubRuleDeliverPayableEntity entity) {
		return pubRuleDeliverPayableRepository.update(entity);
	}

	
}
