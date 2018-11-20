/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billimport.service.impl;

import java.util.Map;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveDispatchTempRepository;
import com.jiuyescm.bms.billimport.service.IBillFeesReceiveDispatchTempService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("billFeesReceiveDispatchTempService")
public class BillFeesReceiveDispatchTempServiceImpl implements IBillFeesReceiveDispatchTempService {

	private static final Logger logger = Logger.getLogger(BillFeesReceiveDispatchTempServiceImpl.class.getName());
	
	@Autowired
    private IBillFeesReceiveDispatchTempRepository billFeesReceiveDispatchTempRepository;

    @Override
    public PageInfo<BillFeesReceiveDispatchTempEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billFeesReceiveDispatchTempRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BillFeesReceiveDispatchTempEntity findById(Long id) {
        return billFeesReceiveDispatchTempRepository.findById(id);
    }

    @Override
    public BillFeesReceiveDispatchTempEntity save(BillFeesReceiveDispatchTempEntity entity) {
        return billFeesReceiveDispatchTempRepository.save(entity);
    }

    @Override
    public BillFeesReceiveDispatchTempEntity update(BillFeesReceiveDispatchTempEntity entity) {
        return billFeesReceiveDispatchTempRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        billFeesReceiveDispatchTempRepository.delete(id);
    }
	
}
