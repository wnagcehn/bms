/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.transport.entity.PriceTransportPayLineEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IPriceTransportPayLineRepository {

	public PageInfo<PriceTransportPayLineEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PriceTransportPayLineEntity> query(Map<String, Object> condition);

    public PriceTransportPayLineEntity findById(Long id);

    public PriceTransportPayLineEntity save(PriceTransportPayLineEntity entity);
    
    public int saveList(List<PriceTransportPayLineEntity> lineList);

    public PriceTransportPayLineEntity update(PriceTransportPayLineEntity entity);

    public void delete(Long id);
    
    public Integer findIdByLineNo(String lineNo);

}
