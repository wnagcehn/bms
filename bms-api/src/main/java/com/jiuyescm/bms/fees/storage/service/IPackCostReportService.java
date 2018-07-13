/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.PackCostReportEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPackCostReportService {

    PageInfo<PackCostReportEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    PackCostReportEntity findById(Long id);

    PackCostReportEntity save(PackCostReportEntity entity);

    PackCostReportEntity update(PackCostReportEntity entity);

    void delete(Long id);

}
