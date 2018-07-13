/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.chargerule.receiverule;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.PubRuleCustomerReceivableEntity;
import com.jiuyescm.bms.chargerule.receiverule.service.IPubRuleCustomerReceivableService;
import com.jiuyescm.bms.rule.receiveRule.repository.IPubRuleCustomerReceivableRepository;

/**
 * 规则商家映射service实现
 * @author yangshuaishuai
 *
 */
@Service("pubRuleCustomerReceivableService")
public class PubRuleCustomerReceivableServiceImpl implements IPubRuleCustomerReceivableService {

	private static final Logger logger = Logger.getLogger(PubRuleCustomerReceivableServiceImpl.class.getName());
	
	@Autowired
    private IPubRuleCustomerReceivableRepository pubRuleCustomerReceivableRepository;

    @Override
    public PageInfo<PubRuleCustomerReceivableEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubRuleCustomerReceivableRepository.query(condition, pageNo, pageSize);
    }
    

	@Override
	public PubRuleCustomerReceivableEntity query(Map<String, Object> condition) {
		return pubRuleCustomerReceivableRepository.query(condition);
	}
	

    @Override
    public int save(PubRuleCustomerReceivableEntity entity) {
        return pubRuleCustomerReceivableRepository.save(entity);
    }

    @Override
    public int update(PubRuleCustomerReceivableEntity entity) {
        return pubRuleCustomerReceivableRepository.update(entity);
    }

}
