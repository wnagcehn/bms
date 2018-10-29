/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInfoEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsAccountInfoService {

    PageInfo<BillAccountInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillAccountInfoEntity findByCustomerId(Long customerId);

    BillAccountInfoEntity save(BillAccountInfoEntity entity);

    BillAccountInfoEntity update(BillAccountInfoEntity entity);

    void delete(Long id);

}
