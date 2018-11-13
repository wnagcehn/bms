/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillAccountInVo;

/**
 * 
 * @author chenwenxin	
 * 
 */
public interface IBmsBillAccountInService {

    PageInfo<BillAccountInVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BillAccountInVo findById(Long id);

    BillAccountInVo save(BillAccountInVo entity);

    BillAccountInVo update(BillAccountInVo entity);

    void delete(Long id);

}
