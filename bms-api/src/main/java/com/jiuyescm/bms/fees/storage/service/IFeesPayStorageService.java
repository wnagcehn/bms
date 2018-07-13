/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.FeesPayStorageEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesPayStorageService {

    PageInfo<FeesPayStorageEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    FeesPayStorageEntity findById(Long id);

    FeesPayStorageEntity save(FeesPayStorageEntity entity);

    FeesPayStorageEntity update(FeesPayStorageEntity entity);

    void delete(Long id);

}
