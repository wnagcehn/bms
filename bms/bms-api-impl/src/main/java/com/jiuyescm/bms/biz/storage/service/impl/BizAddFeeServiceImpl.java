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
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizAddFeeRepository;
import com.jiuyescm.bms.biz.storage.service.IBizAddFeeService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizAddFeeService")
public class BizAddFeeServiceImpl implements IBizAddFeeService {

	private static final Logger logger = Logger.getLogger(BizAddFeeServiceImpl.class.getName());
	
	@Autowired
    private IBizAddFeeRepository bizAddFeeRepository;

    @Override
    public PageInfo<BizAddFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizAddFeeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizAddFeeEntity findById(Long id) {
        return bizAddFeeRepository.findById(id);
    }

    @Override
    public BizAddFeeEntity save(BizAddFeeEntity entity) {
        return bizAddFeeRepository.save(entity);
    }

    @Override
    public BizAddFeeEntity update(BizAddFeeEntity entity) {
        return bizAddFeeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizAddFeeRepository.delete(id);
    }
	
}
