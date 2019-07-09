/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportValueAddedEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportValueAddedRepository;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportValueAddedService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportValueAddedService")
public class PriceTransportValueAddedServiceImpl implements IPriceTransportValueAddedService {
	
	@Autowired
    private IPriceTransportValueAddedRepository priceTransportValueAddedRepository;

    @Override
    public PageInfo<PriceTransportValueAddedEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportValueAddedRepository.query(condition, pageNo, pageSize);
    }
    @Override
    public List<PriceTransportValueAddedEntity> query(Map<String, Object> condition) {
        return priceTransportValueAddedRepository.query(condition);
    }

    @Override
    public PriceTransportValueAddedEntity findById(Long id) {
        return priceTransportValueAddedRepository.findById(id);
    }

    @Override
    public PriceTransportValueAddedEntity save(PriceTransportValueAddedEntity entity) {
        return priceTransportValueAddedRepository.save(entity);
    }

    @Override
    public PriceTransportValueAddedEntity update(PriceTransportValueAddedEntity entity) {
        return priceTransportValueAddedRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportValueAddedRepository.delete(id);
    }

	@Override
	public int saveList(List<PriceTransportValueAddedEntity> valueAddedList) {
		return priceTransportValueAddedRepository.saveList(valueAddedList);
	}
	@Override
	public int deleteBatch(Long id) {
		return priceTransportValueAddedRepository.deleteBatch(id);
	}
	@Override
	public int deleteBatchList(List<PriceTransportValueAddedEntity> list) {
		return priceTransportValueAddedRepository.deleteBatchList(list);
	}
	
	
	
}
