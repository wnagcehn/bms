/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayValueAddedEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayValueAddedRepository;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayValueAddedService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportPayValueAddedService")
public class PriceTransportPayValueAddedServiceImpl implements IPriceTransportPayValueAddedService {

	private static final Logger logger = Logger.getLogger(PriceTransportPayValueAddedServiceImpl.class.getName());
	
	@Autowired
    private IPriceTransportPayValueAddedRepository priceTransportPayValueAddedRepository;

    @Override
    public PageInfo<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportPayValueAddedRepository.query(condition, pageNo, pageSize);
    }
    @Override
    public List<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition) {
        return priceTransportPayValueAddedRepository.query(condition);
    }

    @Override
    public PriceTransportPayValueAddedEntity findById(Long id) {
        return priceTransportPayValueAddedRepository.findById(id);
    }

    @Override
    public PriceTransportPayValueAddedEntity save(PriceTransportPayValueAddedEntity entity) {
        return priceTransportPayValueAddedRepository.save(entity);
    }

    @Override
    public PriceTransportPayValueAddedEntity update(PriceTransportPayValueAddedEntity entity) {
        return priceTransportPayValueAddedRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportPayValueAddedRepository.delete(id);
    }

	@Override
	public int saveList(List<PriceTransportPayValueAddedEntity> valueAddedList) {
		return priceTransportPayValueAddedRepository.saveList(valueAddedList);
	}
	
}
