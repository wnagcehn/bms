/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizOutstockDetailService {

    PageInfo<BizOutstockDetailEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizOutstockDetailEntity findById(Long id);

    BizOutstockDetailEntity save(BizOutstockDetailEntity entity);

    int update(BizOutstockDetailEntity entity);

    void delete(Long id);

}
