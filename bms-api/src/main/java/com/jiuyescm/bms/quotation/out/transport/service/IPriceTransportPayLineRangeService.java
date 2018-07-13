/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;

/**
 * 应付运输梯度报价API
 * @author wubangjun
 */
public interface IPriceTransportPayLineRangeService {

    PageInfo<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition);

    PriceTransportPayLineRangeEntity findById(Long id);

    PriceTransportPayLineRangeEntity save(PriceTransportPayLineRangeEntity entity);

    int saveList(List<PriceTransportPayLineRangeEntity> rangList);
    
    PriceTransportPayLineRangeEntity update(PriceTransportPayLineRangeEntity entity);

    void delete(Long id);
    

}
