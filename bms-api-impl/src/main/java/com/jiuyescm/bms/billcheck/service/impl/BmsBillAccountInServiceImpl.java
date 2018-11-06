/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service.impl;

import java.util.Map;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;
import com.jiuyescm.bms.billcheck.repository.IBillAccountInRepository;
import com.jiuyescm.bms.billcheck.service.IBmsBillAccountInService;

/**
 * 
 * @author chenwenxin
 * 
 */
@Service("bmsBillAccountInService")
public class BmsBillAccountInServiceImpl implements IBmsBillAccountInService {

	private static final Logger logger = Logger.getLogger(BmsBillAccountInServiceImpl.class.getName());
	
	@Autowired
    private IBillAccountInRepository billAccountInRepository;

    @Override
    public PageInfo<BillAccountInEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billAccountInRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BillAccountInEntity findById(Long id) {
        return billAccountInRepository.findById(id);
    }

    @Override
    public BillAccountInEntity save(BillAccountInEntity entity) {
        return billAccountInRepository.save(entity);
    }

    @Override
    public BillAccountInEntity update(BillAccountInEntity entity) {
        return billAccountInRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        billAccountInRepository.delete(id);
    }
	
}
