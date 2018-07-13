/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.origincity.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityElecWarehouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IPubOriginCityElecWarehouseRepository {

	public PageInfo<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition);

    public PubOriginCityElecWarehouseEntity findById(Long id);

    public PubOriginCityElecWarehouseEntity save(PubOriginCityElecWarehouseEntity entity);

    public PubOriginCityElecWarehouseEntity update(PubOriginCityElecWarehouseEntity entity);

    public void delete(Long id);

    public List<PubOriginCityElecWarehouseEntity> queryElecName(Map<String, Object> condition);
}
