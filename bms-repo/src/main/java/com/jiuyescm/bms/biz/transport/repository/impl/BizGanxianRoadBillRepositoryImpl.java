/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;
import com.jiuyescm.bms.biz.transport.repository.IBizGanxianRoadBillRepository;

/**
 * 
 * @author wubangjun
 * 
 */
@Repository("bizGanxianRoadBillRepository")
public class BizGanxianRoadBillRepositoryImpl extends MyBatisDao<BizGanxianRoadBillEntity> implements IBizGanxianRoadBillRepository {

	private static final Logger logger = Logger.getLogger(BizGanxianRoadBillRepositoryImpl.class.getName());

	public BizGanxianRoadBillRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BizGanxianRoadBillEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizGanxianRoadBillEntity> list = selectList("com.jiuyescm.bms.biz.transport.mapper.BizGanxianRoadBillEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizGanxianRoadBillEntity> pageInfo = new PageInfo<BizGanxianRoadBillEntity>(list);
        return pageInfo;
    }

    @Override
    public BizGanxianRoadBillEntity findById(Long id) {
        BizGanxianRoadBillEntity entity = selectOne("com.jiuyescm.bms.biz.transport.mapper.BizGanxianRoadBillEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BizGanxianRoadBillEntity save(BizGanxianRoadBillEntity entity) {
        insert("com.jiuyescm.bms.biz.transport.mapper.BizGanxianRoadBillEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BizGanxianRoadBillEntity update(BizGanxianRoadBillEntity entity) {
        update("com.jiuyescm.bms.biz.transport.mapper.BizGanxianRoadBillEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.transport.mapper.BizGanxianRoadBillEntityMapper.delete", id);
    }

	@Override
	public void updateList(List<BizGanxianRoadBillEntity> updateList) {
		updateBatch("com.jiuyescm.bms.biz.transport.mapper.BizGanxianRoadBillEntityMapper.update", updateList);
	}
	
}
