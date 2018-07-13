/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportOriginCityEntity;

/**
 * 运输始发城市信息service接口
 * @author yangss
 *
 */
public interface IPubTransportOriginCityService {

    PageInfo<PubTransportOriginCityEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PubTransportOriginCityEntity> queryList(Map<String, Object> condition);

    PubTransportOriginCityEntity findById(Long id);

    PubTransportOriginCityEntity save(PubTransportOriginCityEntity entity);

    PubTransportOriginCityEntity update(PubTransportOriginCityEntity entity);

    void delete(Long id);

}
