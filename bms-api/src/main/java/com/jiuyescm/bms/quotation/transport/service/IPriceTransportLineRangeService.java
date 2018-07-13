/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportRegionEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;

/**
 * 运输梯度报价API
 * @author wubangjun
 */
public interface IPriceTransportLineRangeService {

    PageInfo<PriceTransportLineRangeEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PriceTransportLineRangeEntity> query(Map<String, Object> condition);

    PriceTransportLineRangeEntity findById(Long id);

    PriceTransportLineRangeEntity save(PriceTransportLineRangeEntity entity);

    int saveList(List<PriceTransportLineRangeEntity> rangList);
    
    PriceTransportLineRangeEntity update(PriceTransportLineRangeEntity entity);

    void delete(Long id);
    
    List<PubTransportRegionEntity> queryQuotationRegion(List<String> lineIdList);

}
