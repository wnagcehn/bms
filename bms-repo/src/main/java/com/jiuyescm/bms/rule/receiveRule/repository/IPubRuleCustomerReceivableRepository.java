/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.rule.receiveRule.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.PubRuleCustomerReceivableEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPubRuleCustomerReceivableRepository {

	public PageInfo<PubRuleCustomerReceivableEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	PubRuleCustomerReceivableEntity query(Map<String, Object> condition);

    public int save(PubRuleCustomerReceivableEntity entity);

    public int update(PubRuleCustomerReceivableEntity entity);

}
