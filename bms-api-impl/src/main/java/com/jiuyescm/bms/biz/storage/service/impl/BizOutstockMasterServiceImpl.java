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
import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillPayEntity;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockMasterEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockMasterRepository;
import com.jiuyescm.bms.biz.storage.service.IBizOutstockMasterService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bizOutstockMasterService")
public class BizOutstockMasterServiceImpl implements IBizOutstockMasterService {

	private static final Logger logger = Logger.getLogger(BizOutstockMasterServiceImpl.class.getName());
	
	@Autowired
    private IBizOutstockMasterRepository bizOutstockMasterRepository;

    @Override
    public PageInfo<BizOutstockMasterEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizOutstockMasterRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizOutstockMasterEntity findById(Long id) {
        return bizOutstockMasterRepository.findById(id);
    }

    @Override
    public BizOutstockMasterEntity save(BizOutstockMasterEntity entity) {
        return bizOutstockMasterRepository.save(entity);
    }

    @Override
    public int update(BizOutstockMasterEntity entity) {
        return bizOutstockMasterRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizOutstockMasterRepository.delete(id);
    }

	@Override
	public PageInfo<BizOutstockMasterEntity> queryGroup(Map<String, Object> condition, int pageNo, int pageSize) {
		return bizOutstockMasterRepository.queryGroup(condition, pageNo, pageSize);
	}

	@Override
	public Properties validRetry(Map<String, Object> param) {
		return bizOutstockMasterRepository.validRetry(param);
	}

	@Override
	public int reCalculate(Map<String, Object> param) {
		return bizOutstockMasterRepository.reCalculate(param);
	}

	@Override
	public BizOutstockMasterEntity queryExceptionOne(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bizOutstockMasterRepository.queryExceptionOne(condition);
	}

	@Override
	public List<String> queryAllWarehouseId(Map<String, Object> condition) {
		
		return bizOutstockMasterRepository.queryAllWarehouseId(condition);
	}

	

	
}
