/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBillAccountInRepository {

	public PageInfo<BillAccountInEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BillAccountInEntity findById(Long id);

    public BillAccountInEntity save(BillAccountInEntity entity);

    public BillAccountInEntity update(BillAccountInEntity entity);

    public void delete(Long id);

}
