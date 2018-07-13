/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.system.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsJiuyeQuotationSystemService {

    PageInfo<BmsJiuyeQuotationSystemEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BmsJiuyeQuotationSystemEntity findById(Long id);

    BmsJiuyeQuotationSystemEntity save(BmsJiuyeQuotationSystemEntity entity);

    BmsJiuyeQuotationSystemEntity update(BmsJiuyeQuotationSystemEntity entity);

    void delete(Long id);
    
    List<BmsJiuyeQuotationSystemEntity>  queryCustomerBmsList(Map<String, Object> condition);
    
}
