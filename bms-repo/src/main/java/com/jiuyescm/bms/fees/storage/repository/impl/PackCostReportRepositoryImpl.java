/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.storage.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.fees.storage.entity.PackCostReportEntity;
import com.jiuyescm.bms.fees.storage.repository.IPackCostReportRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("packCostReportRepository")
public class PackCostReportRepositoryImpl extends MyBatisDao<PackCostReportEntity> implements IPackCostReportRepository {

	private static final Logger logger = Logger.getLogger(PackCostReportRepositoryImpl.class.getName());

	public PackCostReportRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<PackCostReportEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PackCostReportEntity> list = selectList("com.jiuyescm.bms.fees.storage.PackCostReportEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<PackCostReportEntity> pageInfo = new PageInfo<PackCostReportEntity>(list);
        return pageInfo;
    }

    @Override
    public PackCostReportEntity findById(Long id) {
        PackCostReportEntity entity = selectOne("com.jiuyescm.bms.fees.storage.PackCostReportEntityMapper.findById", id);
        return entity;
    }

    @Override
    public PackCostReportEntity save(PackCostReportEntity entity) {
        insert("com.jiuyescm.bms.fees.storage.PackCostReportEntityMapper.save", entity);
        return entity;
    }

    @Override
    public PackCostReportEntity update(PackCostReportEntity entity) {
        update("com.jiuyescm.bms.fees.storage.PackCostReportEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.fees.storage.PackCostReportEntityMapper.delete", id);
    }
	
}
