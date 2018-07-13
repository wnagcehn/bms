/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizAddFeeEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizAddFeeRepository {

	public PageInfo<BizAddFeeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizAddFeeEntity findById(Long id);

    public BizAddFeeEntity save(BizAddFeeEntity entity);

    public BizAddFeeEntity update(BizAddFeeEntity entity);

    public void delete(Long id);

}
