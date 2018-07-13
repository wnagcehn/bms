/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.system.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;
import com.jiuyescm.bms.quotation.system.repository.IBmsJiuyeQuotationSystemRepository;
import com.jiuyescm.bms.quotation.system.service.IBmsJiuyeQuotationSystemService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bmsJiuyeQuotationSystemService")
public class BmsJiuyeQuotationSystemServiceImpl implements IBmsJiuyeQuotationSystemService {

	private static final Logger logger = Logger.getLogger(BmsJiuyeQuotationSystemServiceImpl.class.getName());
	
	@Autowired
    private IBmsJiuyeQuotationSystemRepository bmsJiuyeQuotationSystemRepository;

    @Override
    public PageInfo<BmsJiuyeQuotationSystemEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsJiuyeQuotationSystemRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BmsJiuyeQuotationSystemEntity findById(Long id) {
        return bmsJiuyeQuotationSystemRepository.findById(id);
    }

    @Override
    public BmsJiuyeQuotationSystemEntity save(BmsJiuyeQuotationSystemEntity entity) {
        return bmsJiuyeQuotationSystemRepository.save(entity);
    }

    @Override
    public BmsJiuyeQuotationSystemEntity update(BmsJiuyeQuotationSystemEntity entity) {
        return bmsJiuyeQuotationSystemRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bmsJiuyeQuotationSystemRepository.delete(id);
    }

	@Override
	public List<BmsJiuyeQuotationSystemEntity> queryCustomerBmsList(Map<String, Object> condition) {
		return bmsJiuyeQuotationSystemRepository.queryCustomerBmsList(condition);
	}
	
}
