/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountOutEntity;

/**
 * 
 * @author liuzhicheng
 * 
 */
public interface IBillAccountOutRepository {

	public PageInfo<BillAccountOutEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BillAccountOutEntity findById(Long id);

    public BillAccountOutEntity save(BillAccountOutEntity entity);

    public BillAccountOutEntity update(BillAccountOutEntity entity);

    public void delete(Long id);

}
