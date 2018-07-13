/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.chargerule.payrule.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.payrule.entity.PubRuleDeliverPayableEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPubRuleDeliverPayableService {

    PageInfo<PubRuleDeliverPayableEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    PubRuleDeliverPayableEntity query(Map<String, Object> condition);

    int save(PubRuleDeliverPayableEntity entity);

    int update(PubRuleDeliverPayableEntity entity);
}
