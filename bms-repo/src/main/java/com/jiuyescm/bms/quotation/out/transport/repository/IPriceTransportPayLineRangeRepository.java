/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineRangeEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportPayLineRangeRepository {

	public PageInfo<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PriceTransportPayLineRangeEntity> query(Map<String, Object> condition);

    public PriceTransportPayLineRangeEntity findById(Long id);

    public PriceTransportPayLineRangeEntity save(PriceTransportPayLineRangeEntity entity);

    public int saveList(List<PriceTransportPayLineRangeEntity> rangeList);
    
    public PriceTransportPayLineRangeEntity update(PriceTransportPayLineRangeEntity entity);

    public void delete(Long id);

}
