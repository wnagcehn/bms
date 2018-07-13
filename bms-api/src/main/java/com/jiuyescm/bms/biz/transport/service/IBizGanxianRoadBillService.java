/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IBizGanxianRoadBillService {

    PageInfo<BizGanxianRoadBillEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizGanxianRoadBillEntity findById(Long id);

    BizGanxianRoadBillEntity save(BizGanxianRoadBillEntity entity);

    BizGanxianRoadBillEntity update(BizGanxianRoadBillEntity entity);
    
    void updateList(List<BizGanxianRoadBillEntity> updateList);

    void delete(Long id);

}
