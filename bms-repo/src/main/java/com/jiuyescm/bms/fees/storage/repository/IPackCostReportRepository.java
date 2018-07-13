/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.PackCostReportEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPackCostReportRepository {

	public PageInfo<PackCostReportEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public PackCostReportEntity findById(Long id);

    public PackCostReportEntity save(PackCostReportEntity entity);

    public PackCostReportEntity update(PackCostReportEntity entity);

    public void delete(Long id);

}
