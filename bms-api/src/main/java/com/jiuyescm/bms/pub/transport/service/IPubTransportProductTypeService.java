/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;

/**
 * 运输产品类型信息service接口
 * @author yangss
 *
 */
public interface IPubTransportProductTypeService {

    PageInfo<PubTransportProductTypeEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PubTransportProductTypeEntity> query(Map<String, Object> condition);

    PubTransportProductTypeEntity findById(Long id);

    PubTransportProductTypeEntity save(PubTransportProductTypeEntity entity);

    PubTransportProductTypeEntity update(PubTransportProductTypeEntity entity);

    void delete(Long id);

}
