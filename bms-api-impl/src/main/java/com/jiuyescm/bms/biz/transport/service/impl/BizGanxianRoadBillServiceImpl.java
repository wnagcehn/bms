/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;
import com.jiuyescm.bms.biz.transport.repository.IBizGanxianRoadBillRepository;
import com.jiuyescm.bms.biz.transport.service.IBizGanxianRoadBillService;

/**
 * 
 * @author wubangjun
 * 
 */
@Service("bizGanxianRoadBillService")
public class BizGanxianRoadBillServiceImpl implements IBizGanxianRoadBillService {

	private static final Logger logger = Logger.getLogger(BizGanxianRoadBillServiceImpl.class.getName());
	
	@Autowired
    private IBizGanxianRoadBillRepository bizGanxianRoadbillRepository;

    @Override
    public PageInfo<BizGanxianRoadBillEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bizGanxianRoadbillRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BizGanxianRoadBillEntity findById(Long id) {
        return bizGanxianRoadbillRepository.findById(id);
    }

    @Override
    public BizGanxianRoadBillEntity save(BizGanxianRoadBillEntity entity) {
        return bizGanxianRoadbillRepository.save(entity);
    }

    @Override
    public BizGanxianRoadBillEntity update(BizGanxianRoadBillEntity entity) {
        return bizGanxianRoadbillRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bizGanxianRoadbillRepository.delete(id);
    }

	@Override
	public void updateList(List<BizGanxianRoadBillEntity> updateList) {
		bizGanxianRoadbillRepository.updateList(updateList);
	}
}
