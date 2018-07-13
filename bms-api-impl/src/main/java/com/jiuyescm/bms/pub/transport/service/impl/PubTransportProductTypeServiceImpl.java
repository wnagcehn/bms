/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportProductTypeRepository;
import com.jiuyescm.bms.pub.transport.service.IPubTransportProductTypeService;

/**
 * 运输产品类型信息service实现类
 * @author yangss
 *
 */
@Service("pubTransportProductTypeService")
public class PubTransportProductTypeServiceImpl implements IPubTransportProductTypeService {

	private static final Logger logger = Logger.getLogger(PubTransportProductTypeServiceImpl.class.getName());
	
	@Autowired
    private IPubTransportProductTypeRepository pubTransportProductTypeRepository;

    @Override
    public PageInfo<PubTransportProductTypeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubTransportProductTypeRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public List<PubTransportProductTypeEntity> query(Map<String, Object> condition) {
    	return pubTransportProductTypeRepository.query(condition);
    }

    @Override
    public PubTransportProductTypeEntity findById(Long id) {
        return pubTransportProductTypeRepository.findById(id);
    }

    @Override
    public PubTransportProductTypeEntity save(PubTransportProductTypeEntity entity) {
        return pubTransportProductTypeRepository.save(entity);
    }

    @Override
    public PubTransportProductTypeEntity update(PubTransportProductTypeEntity entity) {
        return pubTransportProductTypeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        pubTransportProductTypeRepository.delete(id);
    }
	
}
