/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockDetailEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBizOutstockDetailRepository {

	public PageInfo<BizOutstockDetailEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BizOutstockDetailEntity findById(Long id);

    public BizOutstockDetailEntity save(BizOutstockDetailEntity entity);

    public int update(BizOutstockDetailEntity entity);

    public void delete(Long id);

}
