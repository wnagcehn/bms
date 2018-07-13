/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;

/**
 * 运输产品类型信息
 * @author yangss
 */
public interface IPubTransportProductTypeRepository {

	public PageInfo<PubTransportProductTypeEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PubTransportProductTypeEntity> query(Map<String, Object> condition);

    public PubTransportProductTypeEntity findById(Long id);

    public PubTransportProductTypeEntity save(PubTransportProductTypeEntity entity);

    public PubTransportProductTypeEntity update(PubTransportProductTypeEntity entity);

    public void delete(Long id);
    
}
