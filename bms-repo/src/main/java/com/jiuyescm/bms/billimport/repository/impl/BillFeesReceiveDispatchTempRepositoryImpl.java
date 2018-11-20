/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billimport.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveDispatchTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveDispatchTempRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("billFeesReceiveDispatchTempRepository")
public class BillFeesReceiveDispatchTempRepositoryImpl extends MyBatisDao<BillFeesReceiveDispatchTempEntity> implements IBillFeesReceiveDispatchTempRepository {

	private static final Logger logger = Logger.getLogger(BillFeesReceiveDispatchTempRepositoryImpl.class.getName());

	public BillFeesReceiveDispatchTempRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BillFeesReceiveDispatchTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillFeesReceiveDispatchTempEntity> list = selectList("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BillFeesReceiveDispatchTempEntity> pageInfo = new PageInfo<BillFeesReceiveDispatchTempEntity>(list);
        return pageInfo;
    }

    @Override
    public BillFeesReceiveDispatchTempEntity findById(Long id) {
        BillFeesReceiveDispatchTempEntity entity = selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BillFeesReceiveDispatchTempEntity save(BillFeesReceiveDispatchTempEntity entity) {
        insert("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BillFeesReceiveDispatchTempEntity update(BillFeesReceiveDispatchTempEntity entity) {
        update("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billimport.BillFeesReceiveDispatchTempEntityMapper.delete", id);
    }
	
}
