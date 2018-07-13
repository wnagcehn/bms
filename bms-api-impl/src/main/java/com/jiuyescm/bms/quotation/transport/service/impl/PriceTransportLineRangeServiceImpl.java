/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportRegionEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportLineRangeRepository;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportLineRangeService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportLineRangeService")
public class PriceTransportLineRangeServiceImpl implements IPriceTransportLineRangeService {

	private static final Logger logger = Logger.getLogger(PriceTransportLineRangeServiceImpl.class.getName());
	
	@Autowired
    private IPriceTransportLineRangeRepository priceTransportLineRangeRepository;

    @Override
    public PageInfo<PriceTransportLineRangeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportLineRangeRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PriceTransportLineRangeEntity findById(Long id) {
        return priceTransportLineRangeRepository.findById(id);
    }

    @Override
    public PriceTransportLineRangeEntity save(PriceTransportLineRangeEntity entity) {
        return priceTransportLineRangeRepository.save(entity);
    }

    @Override
    public PriceTransportLineRangeEntity update(PriceTransportLineRangeEntity entity) {
        return priceTransportLineRangeRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportLineRangeRepository.delete(id);
    }

	@Override
	public List<PriceTransportLineRangeEntity> query(Map<String, Object> condition) {
		 return priceTransportLineRangeRepository.query(condition);
	}

	@Override
	public int saveList(List<PriceTransportLineRangeEntity> rangList) {
		return priceTransportLineRangeRepository.saveList(rangList);
	}

	@Override
	public List<PubTransportRegionEntity> queryQuotationRegion(List<String> lineIdList) {
		return priceTransportLineRangeRepository.queryQuotationRegion(lineIdList);
	}
	
}
