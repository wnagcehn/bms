/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.service.impl;

import java.util.Map;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.FeesPayStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesPayStorageRepository;
import com.jiuyescm.bms.fees.storage.service.IFeesPayStorageService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("feesPayStorageService")
public class FeesPayStorageServiceImpl implements IFeesPayStorageService {

	private static final Logger logger = Logger.getLogger(FeesPayStorageServiceImpl.class.getName());
	
	@Autowired
    private IFeesPayStorageRepository feesPayStorageRepository;

    @Override
    public PageInfo<FeesPayStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return feesPayStorageRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public FeesPayStorageEntity findById(Long id) {
        return feesPayStorageRepository.findById(id);
    }

    @Override
    public FeesPayStorageEntity save(FeesPayStorageEntity entity) {
        return feesPayStorageRepository.save(entity);
    }

    @Override
    public FeesPayStorageEntity update(FeesPayStorageEntity entity) {
        return feesPayStorageRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        feesPayStorageRepository.delete(id);
    }
	
}
