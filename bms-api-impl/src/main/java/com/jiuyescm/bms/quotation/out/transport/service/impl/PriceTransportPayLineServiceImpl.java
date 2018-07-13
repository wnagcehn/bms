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
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayLineRepository;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayLineService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportPayLineService")
public class PriceTransportPayLineServiceImpl implements IPriceTransportPayLineService {

	private static final Logger logger = Logger.getLogger(PriceTransportPayLineServiceImpl.class.getName());
	
	@Autowired
    private IPriceTransportPayLineRepository priceTransportPayLineRepository;

    @Override
    public PageInfo<PriceTransportPayLineEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportPayLineRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PriceTransportPayLineEntity findById(Long id) {
        return priceTransportPayLineRepository.findById(id);
    }

    @Override
    public PriceTransportPayLineEntity save(PriceTransportPayLineEntity entity) {
        return priceTransportPayLineRepository.save(entity);
    }

    @Override
    public PriceTransportPayLineEntity update(PriceTransportPayLineEntity entity) {
        return priceTransportPayLineRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportPayLineRepository.delete(id);
    }

	@Override
	public List<PriceTransportPayLineEntity> query(Map<String, Object> condition) {
		return priceTransportPayLineRepository.query(condition);
	}

	@Override
	public int saveList(List<PriceTransportPayLineEntity> lineList) {
		return priceTransportPayLineRepository.saveList(lineList);
	}

	@Override
	public Integer findIdByLineNo(String lineNo) {
		return priceTransportPayLineRepository.findIdByLineNo(lineNo);
	}
	
}
