/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.PackCostReportEntity;
import com.jiuyescm.bms.fees.storage.repository.IPackCostReportRepository;
import com.jiuyescm.bms.fees.storage.service.IPackCostReportService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("packCostReportService")
public class PackCostReportServiceImpl implements IPackCostReportService {
	
	@Autowired
    private IPackCostReportRepository packCostReportRepository;

    @Override
    public PageInfo<PackCostReportEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return packCostReportRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public PackCostReportEntity findById(Long id) {
        return packCostReportRepository.findById(id);
    }

    @Override
    public PackCostReportEntity save(PackCostReportEntity entity) {
        return packCostReportRepository.save(entity);
    }

    @Override
    public PackCostReportEntity update(PackCostReportEntity entity) {
        return packCostReportRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        packCostReportRepository.delete(id);
    }
	
}
