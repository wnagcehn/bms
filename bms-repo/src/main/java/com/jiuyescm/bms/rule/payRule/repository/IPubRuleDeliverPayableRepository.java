/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.rule.payRule.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.PubRuleDeliverPayableEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPubRuleDeliverPayableRepository {

	public PageInfo<PubRuleDeliverPayableEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	PubRuleDeliverPayableEntity query(Map<String, Object> condition);
	
    public int save(PubRuleDeliverPayableEntity entity);

    public int update(PubRuleDeliverPayableEntity entity);



}
