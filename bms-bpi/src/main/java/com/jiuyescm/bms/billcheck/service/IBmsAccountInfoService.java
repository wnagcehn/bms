/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillAccountInfoVo;
/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsAccountInfoService {

    PageInfo<BillAccountInfoVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillAccountInfoVo findByCustomerId(String customerId);

    BillAccountInfoVo save(BillAccountInfoVo entity);

    BillAccountInfoVo update(BillAccountInfoVo entity);

    void delete(Long id);

}
