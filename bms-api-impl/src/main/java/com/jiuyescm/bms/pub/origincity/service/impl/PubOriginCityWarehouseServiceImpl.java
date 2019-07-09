/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.origincity.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.orgincity.service.IPubOriginCityWarehouseService;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityWarehouseRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Service("pubOriginCityWarehouseService")
public class PubOriginCityWarehouseServiceImpl implements IPubOriginCityWarehouseService {
	
	@Autowired
    private IPubOriginCityWarehouseRepository pubOriginCityWarehouseRepository;

    @Override
    public PageInfo<PubOriginCityWarehouseEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubOriginCityWarehouseRepository.query(condition, pageNo, pageSize);
    }

	@Override
	public List<PubOriginCityWarehouseEntity> query(Map<String, Object> condition) {
		return pubOriginCityWarehouseRepository.query(condition);
	}
	
    @Override
    public PubOriginCityWarehouseEntity findById(Long id) {
        return pubOriginCityWarehouseRepository.findById(id);
    }

    @Override
    public PubOriginCityWarehouseEntity save(PubOriginCityWarehouseEntity entity) {
        return pubOriginCityWarehouseRepository.save(entity);
    }

    @Override
    public PubOriginCityWarehouseEntity update(PubOriginCityWarehouseEntity entity) {
        return pubOriginCityWarehouseRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        pubOriginCityWarehouseRepository.delete(id);
    }
	
}
