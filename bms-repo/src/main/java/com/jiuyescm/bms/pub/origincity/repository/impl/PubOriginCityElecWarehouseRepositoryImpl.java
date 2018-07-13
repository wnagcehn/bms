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
import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityElecWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityElecWarehouseRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 始发城市到达电商仓库
 * @author yangss
 *
 */
@Repository("pubOriginCityElecWarehouseRepository")
public class PubOriginCityElecWarehouseRepositoryImpl extends MyBatisDao<PubOriginCityElecWarehouseEntity> implements IPubOriginCityElecWarehouseRepository {

	@Override
    public PageInfo<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubOriginCityElecWarehouseEntity> list = selectList("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PubOriginCityElecWarehouseEntity> pageInfo = new PageInfo<PubOriginCityElecWarehouseEntity>(list);
        return pageInfo;
    }
	
	@Override
	public List<PubOriginCityElecWarehouseEntity> query(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.query", condition);
	}

    @Override
    public PubOriginCityElecWarehouseEntity findById(Long id) {
        PubOriginCityElecWarehouseEntity entity = selectOne("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.findById", id);
        return entity;
    }

    @Override
    public PubOriginCityElecWarehouseEntity save(PubOriginCityElecWarehouseEntity entity) {
        insert("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.save", entity);
        return entity;
    }

    @Override
    public PubOriginCityElecWarehouseEntity update(PubOriginCityElecWarehouseEntity entity) {
        update("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.delete", id);
    }

	@Override
	public List<PubOriginCityElecWarehouseEntity> queryElecName(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.pub.origincity.mapper.PubOriginCityElecWarehouseMapper.queryElecName", condition);
	}
	
}
