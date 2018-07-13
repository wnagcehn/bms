/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.origincity.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.orgincity.service.IPubOriginCityElecWarehouseService;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityElecWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityElecWarehouseRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Service("pubOriginCityElecWarehouseService")
public class PubOriginCityElecWarehouseServiceImpl implements IPubOriginCityElecWarehouseService {

	private static final Logger logger = Logger.getLogger(PubOriginCityElecWarehouseServiceImpl.class.getName());
	
	@Autowired
    private IPubOriginCityElecWarehouseRepository pubOriginCityElecWarehouseRepository;

    @Override
    public PageInfo<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubOriginCityElecWarehouseRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public List<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition) {
    	return pubOriginCityElecWarehouseRepository.query(condition);
    }

    @Override
    public PubOriginCityElecWarehouseEntity findById(Long id) {
        return pubOriginCityElecWarehouseRepository.findById(id);
    }

    @Override
    public PubOriginCityElecWarehouseEntity save(PubOriginCityElecWarehouseEntity entity) {
        return pubOriginCityElecWarehouseRepository.save(entity);
    }

    @Override
    public PubOriginCityElecWarehouseEntity update(PubOriginCityElecWarehouseEntity entity) {
        return pubOriginCityElecWarehouseRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        pubOriginCityElecWarehouseRepository.delete(id);
    }

	@Override
	public List<PubOriginCityElecWarehouseEntity> queryElecName(Map<String, Object> condition) {
		return pubOriginCityElecWarehouseRepository.queryElecName(condition);
	}
	
}
