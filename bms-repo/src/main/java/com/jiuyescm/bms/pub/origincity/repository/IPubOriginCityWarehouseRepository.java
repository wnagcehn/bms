/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.origincity.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityWarehouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPubOriginCityWarehouseRepository {

	public PageInfo<PubOriginCityWarehouseEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PubOriginCityWarehouseEntity> query(Map<String, Object> condition);

    public PubOriginCityWarehouseEntity findById(Long id);

    public PubOriginCityWarehouseEntity save(PubOriginCityWarehouseEntity entity);

    public PubOriginCityWarehouseEntity update(PubOriginCityWarehouseEntity entity);

    public void delete(Long id);

}
