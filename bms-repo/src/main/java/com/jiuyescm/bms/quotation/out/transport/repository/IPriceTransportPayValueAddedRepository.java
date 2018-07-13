/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayValueAddedEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportPayValueAddedRepository {

	public PageInfo<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public PriceTransportPayValueAddedEntity findById(Long id);

    public PriceTransportPayValueAddedEntity save(PriceTransportPayValueAddedEntity entity);
    
    int saveList(List<PriceTransportPayValueAddedEntity> valueAddedList);

    public PriceTransportPayValueAddedEntity update(PriceTransportPayValueAddedEntity entity);

    public void delete(Long id);

	List<PriceTransportPayValueAddedEntity> query(Map<String, Object> condition);

}
