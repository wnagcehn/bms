/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportTemplateEntity;
import com.jiuyescm.bms.quotation.transport.repository.IPriceTransportTemplateRepository;
import com.jiuyescm.bms.quotation.transport.service.IPriceTransportTemplateService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportTemplateService")
public class PriceTransportTemplateServiceImpl implements IPriceTransportTemplateService {

	private static final Logger logger = Logger.getLogger(PriceTransportTemplateServiceImpl.class.getName());
	
	@Autowired
    private IPriceTransportTemplateRepository priceTransportTemplateRepository;

    @Override
    public PageInfo<PriceTransportTemplateEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportTemplateRepository.query(condition, pageNo, pageSize);
    }
    
	@Override
	public PriceTransportTemplateEntity query(Map<String, Object> condition) {
		return priceTransportTemplateRepository.query(condition);
	}

    @Override
    public PriceTransportTemplateEntity findById(Long id) {
        return priceTransportTemplateRepository.findById(id);
    }

    @Override
    public PriceTransportTemplateEntity save(PriceTransportTemplateEntity entity) {
        return priceTransportTemplateRepository.save(entity);
    }

    @Override
    public PriceTransportTemplateEntity update(PriceTransportTemplateEntity entity) {
        return priceTransportTemplateRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportTemplateRepository.delete(id);
    }

	@Override
	public Integer findIdByTemplateCode(String templateCode) {
		return priceTransportTemplateRepository.findIdByTemplateCode(templateCode);
	}
}
