/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.storage.entity.FeesPayStorageEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IFeesPayStorageRepository {

	public PageInfo<FeesPayStorageEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public FeesPayStorageEntity findById(Long id);

    public FeesPayStorageEntity save(FeesPayStorageEntity entity);

    public FeesPayStorageEntity update(FeesPayStorageEntity entity);

    public void delete(Long id);

}
