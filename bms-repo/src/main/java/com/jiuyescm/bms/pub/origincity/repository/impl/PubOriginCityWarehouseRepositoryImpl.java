/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.origincity.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityWarehouseRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 始发城市到达仓库
 * @author yangss
 *
 */
@Repository("pubOriginCityWarehouseRepository")
public class PubOriginCityWarehouseRepositoryImpl extends MyBatisDao<PubOriginCityWarehouseEntity> implements IPubOriginCityWarehouseRepository {

	@Override
    public PageInfo<PubOriginCityWarehouseEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubOriginCityWarehouseEntity> list = selectList("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityWarehouseMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubOriginCityWarehouseEntity> pageInfo = new PageInfo<PubOriginCityWarehouseEntity>(list);
        return pageInfo;
    }
	
	@Override
	public List<PubOriginCityWarehouseEntity> query(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityWarehouseMapper.query", condition);
	}

    @Override
    public PubOriginCityWarehouseEntity findById(Long id) {
        PubOriginCityWarehouseEntity entity = selectOne("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityWarehouseMapper.findById", id);
        return entity;
    }

    @Override
    public PubOriginCityWarehouseEntity save(PubOriginCityWarehouseEntity entity) {
        insert("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityWarehouseMapper.save", entity);
        return entity;
    }

    @Override
    public PubOriginCityWarehouseEntity update(PubOriginCityWarehouseEntity entity) {
        update("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityWarehouseMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityWarehouseMapper.delete", id);
    }
	
}
