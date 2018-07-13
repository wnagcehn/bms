/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;

/**
 * 应付运输路线表API
 * @author wubangjun
 */
public interface IPriceTransportPayLineService {

    PageInfo<PriceTransportPayLineEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    List<PriceTransportPayLineEntity> query(Map<String, Object> condition);

    PriceTransportPayLineEntity findById(Long id);

    PriceTransportPayLineEntity save(PriceTransportPayLineEntity entity);
    
    int saveList(List<PriceTransportPayLineEntity> lineList);

    PriceTransportPayLineEntity update(PriceTransportPayLineEntity entity);

    void delete(Long id);
    
    Integer findIdByLineNo(String lineNo);

}
