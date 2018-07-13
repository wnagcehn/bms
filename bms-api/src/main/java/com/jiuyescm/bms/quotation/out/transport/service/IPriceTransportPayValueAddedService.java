/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayValueAddedEntity;

/**
 * 应付运输增值服务报价API
 * @author wubangjun
 */
public interface IPriceTransportPayValueAddedService {

    PageInfo<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    List<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition);
    
    PriceTransportPayValueAddedEntity findById(Long id);

    PriceTransportPayValueAddedEntity save(PriceTransportPayValueAddedEntity entity);
    
    int saveList(List<PriceTransportPayValueAddedEntity> valueAddedList);

    PriceTransportPayValueAddedEntity update(PriceTransportPayValueAddedEntity entity);

    void delete(Long id);

}
