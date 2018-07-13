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
import com.jiuyescm.bms.pub.transport.entity.PubTransportOriginCityEntity;
import com.jiuyescm.bms.pub.transport.repository.IPubTransportOriginCityRepository;
import com.jiuyescm.bms.pub.transport.service.IPubTransportOriginCityService;

/**
 * 运输始发城市信息service实现类
 * @author yangss
 *
 */
@Service("pubTransportOriginCityService")
public class PubTransportOriginCityServiceImpl implements IPubTransportOriginCityService {

	private static final Logger logger = Logger.getLogger(PubTransportOriginCityServiceImpl.class.getName());
	
	@Autowired
    private IPubTransportOriginCityRepository pubTransportOriginCityRepository;

    @Override
    public PageInfo<PubTransportOriginCityEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return pubTransportOriginCityRepository.query(condition, pageNo, pageSize);
    }

	@Override
	public List<PubTransportOriginCityEntity> queryList(Map<String, Object> condition) {
		return pubTransportOriginCityRepository.queryList(condition);
	}

    @Override
    public PubTransportOriginCityEntity findById(Long id) {
        return pubTransportOriginCityRepository.findById(id);
    }

    @Override
    public PubTransportOriginCityEntity save(PubTransportOriginCityEntity entity) {
        return pubTransportOriginCityRepository.save(entity);
    }

    @Override
    public PubTransportOriginCityEntity update(PubTransportOriginCityEntity entity) {
        return pubTransportOriginCityRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        pubTransportOriginCityRepository.delete(id);
    }
	
}
