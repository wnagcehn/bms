/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBillAccountInfoRepository {

	public PageInfo<BillAccountInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BillAccountInfoEntity findByCustomerId(String customerId);

    public BillAccountInfoEntity save(BillAccountInfoEntity entity);

    public BillAccountInfoEntity update(BillAccountInfoEntity entity);

    public void delete(Long id);

}
