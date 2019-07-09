/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayLineRangeRepository;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineRangeService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportPayLineRangeService")
public class PriceTransportPayLineRangeServiceImpl implements IPriceTransportPayLineRangeService {
	
	@Autowired
    private IPriceTransportPayLineRangeRepository priceTransportPayLineRangeRepository;

    @Override
    public PageInfo<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportPayLineRangeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PriceTransportPayLineRangeEntity findById(Long id) {
        return priceTransportPayLineRangeRepository.findById(id);
    }

    @Override
    public PriceTransportPayLineRangeEntity save(PriceTransportPayLineRangeEntity entity) {
        return priceTransportPayLineRangeRepository.save(entity);
    }

    @Override
    public PriceTransportPayLineRangeEntity update(PriceTransportPayLineRangeEntity entity) {
        return priceTransportPayLineRangeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportPayLineRangeRepository.delete(id);
    }

	@Override
	public List<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition) {
		 return priceTransportPayLineRangeRepository.query(condition);
	}

	@Override
	public int saveList(List<PriceTransportPayLineRangeEntity> rangList) {
		return priceTransportPayLineRangeRepository.saveList(rangList);
	}
	
}
