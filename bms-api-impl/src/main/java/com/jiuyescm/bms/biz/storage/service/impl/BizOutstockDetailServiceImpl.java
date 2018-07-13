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
import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockDetailRepository;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockDetailService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizOutstockDetailService")
public class BizOutstockDetailServiceImpl implements IBizOutstockDetailService {

	private static final Logger logger = Logger.getLogger(BizOutstockDetailServiceImpl.class.getName());
	
	@Autowired
    private IBizOutstockDetailRepository bizOutstockDetailRepository;

    @Override
    public PageInfo<BizOutstockDetailEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizOutstockDetailRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizOutstockDetailEntity findById(Long id) {
        return bizOutstockDetailRepository.findById(id);
    }

    @Override
    public BizOutstockDetailEntity save(BizOutstockDetailEntity entity) {
        return bizOutstockDetailRepository.save(entity);
    }

    @Override
    public int update(BizOutstockDetailEntity entity) {
        return bizOutstockDetailRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizOutstockDetailRepository.delete(id);
    }
	
}
