/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.system.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsJiuyeQuotationSystemRepository {

	public PageInfo<BmsJiuyeQuotationSystemEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BmsJiuyeQuotationSystemEntity findById(Long id);

    public BmsJiuyeQuotationSystemEntity save(BmsJiuyeQuotationSystemEntity entity);

    public BmsJiuyeQuotationSystemEntity update(BmsJiuyeQuotationSystemEntity entity);

    public void delete(Long id);
    
    List<BmsJiuyeQuotationSystemEntity>  queryCustomerBmsList(Map<String, Object> condition);

}
