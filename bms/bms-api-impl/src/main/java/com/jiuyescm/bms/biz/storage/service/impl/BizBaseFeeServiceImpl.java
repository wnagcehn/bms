/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.Map;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizBaseFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizBaseFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IBizBaseFeeService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizBaseFeeService")
public class BizBaseFeeServiceImpl implements IBizBaseFeeService {

	private static final Logger logger = Logger.getLogger(BizBaseFeeServiceImpl.class.getName());
	
	@Autowired
    private IBizBaseFeeRepository bizBaseFeeRepository;

    @Override
    public PageInfo<BizBaseFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizBaseFeeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizBaseFeeEntity findById(Long id) {
        return bizBaseFeeRepository.findById(id);
    }

    @Override
    public BizBaseFeeEntity save(BizBaseFeeEntity entity) {
        return bizBaseFeeRepository.save(entity);
    }

    @Override
    public BizBaseFeeEntity update(BizBaseFeeEntity entity) {
        return bizBaseFeeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizBaseFeeRepository.delete(id);
    }
	
}
