/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.orgincity.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityWarehouseEntity;

/**
 * 始发城市、仓库对应关系Service接口
 * @author yangss
 *
 */
public interface IPubOriginCityWarehouseService {

    PageInfo<PubOriginCityWarehouseEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PubOriginCityWarehouseEntity> query(Map<String, Object> condition);

    PubOriginCityWarehouseEntity findById(Long id);

    PubOriginCityWarehouseEntity save(PubOriginCityWarehouseEntity entity);

    PubOriginCityWarehouseEntity update(PubOriginCityWarehouseEntity entity);

    void delete(Long id);

}
