/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillAccountInEntity;

/**
 * 
 * @author chenwenxin	
 * 
 */
public interface IBmsBillAccountInService {

    PageInfo<BillAccountInEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillAccountInEntity findById(Long id);

    BillAccountInEntity save(BillAccountInEntity entity);

    BillAccountInEntity update(BillAccountInEntity entity);

    void delete(Long id);

}
