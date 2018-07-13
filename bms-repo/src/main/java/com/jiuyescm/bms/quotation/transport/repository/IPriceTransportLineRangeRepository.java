/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportRegionEntity;
import com.jiuyescm.bms.quotation.transport.entity.PriceTransportLineRangeEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportLineRangeRepository {

	public PageInfo<PriceTransportLineRangeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PriceTransportLineRangeEntity> query(Map<String, Object> condition);

    public PriceTransportLineRangeEntity findById(Long id);

    public PriceTransportLineRangeEntity save(PriceTransportLineRangeEntity entity);

    public int saveList(List<PriceTransportLineRangeEntity> rangeList);
    
    public PriceTransportLineRangeEntity update(PriceTransportLineRangeEntity entity);

    public void delete(Long id);

    public List<PubTransportRegionEntity> queryQuotationRegion(List<String> lineIdList);
    
}
