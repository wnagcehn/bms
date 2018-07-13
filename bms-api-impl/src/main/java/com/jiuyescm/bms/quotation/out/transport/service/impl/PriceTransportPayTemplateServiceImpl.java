/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayTemplateEntity;
import com.jiuyescm.bms.quotation.out.transport.repository.IPriceTransportPayTemplateRepository;
import com.jiuyescm.bms.quotation.out.transport.service.IPriceTransportPayTemplateService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("priceTransportPayTemplateService")
public class PriceTransportPayTemplateServiceImpl implements IPriceTransportPayTemplateService {

	private static final Logger logger = Logger.getLogger(PriceTransportPayTemplateServiceImpl.class.getName());
	
	@Autowired
    private IPriceTransportPayTemplateRepository priceTransportPayTemplateRepository;

    @Override
    public PageInfo<PriceTransportPayTemplateEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return priceTransportPayTemplateRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PriceTransportPayTemplateEntity findById(Long id) {
        return priceTransportPayTemplateRepository.findById(id);
    }

    @Override
    public PriceTransportPayTemplateEntity save(PriceTransportPayTemplateEntity entity) {
        return priceTransportPayTemplateRepository.save(entity);
    }

    @Override
    public PriceTransportPayTemplateEntity update(PriceTransportPayTemplateEntity entity) {
        return priceTransportPayTemplateRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceTransportPayTemplateRepository.delete(id);
    }

	@Override
	public Integer findIdByTemplateCode(String templateCode) {
		// TODO Auto-generated method stub
		return priceTransportPayTemplateRepository.findIdByTemplateCode(templateCode);
	}
	
}
