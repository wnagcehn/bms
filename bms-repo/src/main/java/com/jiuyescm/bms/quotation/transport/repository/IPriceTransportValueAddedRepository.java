/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportValueAddedEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportValueAddedRepository {

	public PageInfo<PriceTransportValueAddedEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public PriceTransportValueAddedEntity findById(Long id);

    public PriceTransportValueAddedEntity save(PriceTransportValueAddedEntity entity);
    
    int saveList(List<PriceTransportValueAddedEntity> valueAddedList);

    public PriceTransportValueAddedEntity update(PriceTransportValueAddedEntity entity);

    public void delete(Long id);

	List<PriceTransportValueAddedEntity> query(Map<String, Object> condition);
	
	int deleteBatch(Long id);
	
	int deleteBatchList(List<PriceTransportValueAddedEntity> list);

}
