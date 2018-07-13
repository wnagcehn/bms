/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IBizGanxianRoadBillRepository {

	public PageInfo<BizGanxianRoadBillEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizGanxianRoadBillEntity findById(Long id);

    public BizGanxianRoadBillEntity save(BizGanxianRoadBillEntity entity);

    public BizGanxianRoadBillEntity update(BizGanxianRoadBillEntity entity);
    
    public void updateList(List<BizGanxianRoadBillEntity> updateList);

    public void delete(Long id);

}
