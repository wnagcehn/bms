/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.orgincity.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityElecWarehouseEntity;

/**
 * 始发城市到达电商仓库Service接口
 * @author stevenl
 * 
 */
public interface IPubOriginCityElecWarehouseService {

    PageInfo<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition);

    PubOriginCityElecWarehouseEntity findById(Long id);

    PubOriginCityElecWarehouseEntity save(PubOriginCityElecWarehouseEntity entity);

    PubOriginCityElecWarehouseEntity update(PubOriginCityElecWarehouseEntity entity);

    void delete(Long id);
    
    /**
     * 查询始发城市到达的电商仓库、去重
     * @param condition
     * @return
     */
    List<PubOriginCityElecWarehouseEntity> queryElecName(Map<String, Object> condition);

}
