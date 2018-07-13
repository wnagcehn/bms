/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.chargerule.receiverule.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.PubRuleCustomerReceivableEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPubRuleCustomerReceivableService {

    PageInfo<PubRuleCustomerReceivableEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
    
    PubRuleCustomerReceivableEntity query(Map<String, Object> condition);

    int save(PubRuleCustomerReceivableEntity entity);

    int update(PubRuleCustomerReceivableEntity entity);

}
