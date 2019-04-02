/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizProductStorageRepository;
import com.jiuyescm.bms.biz.storage.service.IBizProductStorageService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizProductStorageService")
public class BizProductStorageServiceImpl implements IBizProductStorageService {

	private static final Logger logger = Logger.getLogger(BizProductStorageServiceImpl.class.getName());
	
	@Autowired
    private IBizProductStorageRepository bizProductStorageRepository;

    @Override
    public PageInfo<BizProductStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizProductStorageRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizProductStorageEntity findById(Long id) {
        return bizProductStorageRepository.findById(id);
    }

    @Override
    public BizProductStorageEntity save(BizProductStorageEntity entity) {
        return bizProductStorageRepository.save(entity);
    }

    @Override
    public BizProductStorageEntity update(BizProductStorageEntity entity) {
        return bizProductStorageRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizProductStorageRepository.delete(id);
    }

	@Override
	public BizProductStorageEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizProductStorageRepository.queryExceptionOne(condition);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return bizProductStorageRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return bizProductStorageRepository.reCalculate(param);
	}
	
    @Override
    public List<BizProductStorageEntity> queryList(Map<String, Object> condition) {
        return bizProductStorageRepository.queryList(condition);
    }
	
}