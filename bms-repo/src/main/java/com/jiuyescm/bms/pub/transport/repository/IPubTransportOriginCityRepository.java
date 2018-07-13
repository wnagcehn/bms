/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportOriginCityEntity;

/**
 * 运输始发城市信息
 * @author yangss
 */
public interface IPubTransportOriginCityRepository {

	public PageInfo<PubTransportOriginCityEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public List<PubTransportOriginCityEntity> queryList(Map<String, Object> condition);

    public PubTransportOriginCityEntity findById(Long id);

    public PubTransportOriginCityEntity save(PubTransportOriginCityEntity entity);

    public PubTransportOriginCityEntity update(PubTransportOriginCityEntity entity);

    public void delete(Long id);

}
