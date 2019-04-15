/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportValueAddedEntity;

/**
 * 运输增值服务报价API
 * @author wubangjun
 */
public interface IPriceTransportValueAddedService {

    PageInfo<PriceTransportValueAddedEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    List<PriceTransportValueAddedEntity> query(Map<String, Object> condition);
    
    PriceTransportValueAddedEntity findById(Long id);

    PriceTransportValueAddedEntity save(PriceTransportValueAddedEntity entity);
    
    int saveList(List<PriceTransportValueAddedEntity> valueAddedList);

    PriceTransportValueAddedEntity update(PriceTransportValueAddedEntity entity);

    void delete(Long id);
    
    int deleteBatch(Long id);
    
    int deleteBatchList(List<PriceTransportValueAddedEntity> list);

}
