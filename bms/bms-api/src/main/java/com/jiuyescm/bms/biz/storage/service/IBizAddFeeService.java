/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizAddFeeService {

    PageInfo<BizAddFeeEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BizAddFeeEntity findById(Long id);

    BizAddFeeEntity save(BizAddFeeEntity entity);

    BizAddFeeEntity update(BizAddFeeEntity entity);

    void delete(Long id);

}
