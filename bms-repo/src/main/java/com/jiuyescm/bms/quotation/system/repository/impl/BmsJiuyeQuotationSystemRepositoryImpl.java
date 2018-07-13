/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.system.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.quotation.system.entity.BmsJiuyeQuotationSystemEntity;
import com.jiuyescm.bms.quotation.system.repository.IBmsJiuyeQuotationSystemRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bmsJiuyeQuotationSystemRepository")
public class BmsJiuyeQuotationSystemRepositoryImpl extends MyBatisDao<BmsJiuyeQuotationSystemEntity> implements IBmsJiuyeQuotationSystemRepository {

	private static final Logger logger = Logger.getLogger(BmsJiuyeQuotationSystemRepositoryImpl.class.getName());

	public BmsJiuyeQuotationSystemRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BmsJiuyeQuotationSystemEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsJiuyeQuotationSystemEntity> list = selectList("com.jiuyescm.bms.quotation.system.BmsJiuyeQuotationSystemEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsJiuyeQuotationSystemEntity> pageInfo = new PageInfo<BmsJiuyeQuotationSystemEntity>(list);
        return pageInfo;
    }

    @Override
    public BmsJiuyeQuotationSystemEntity findById(Long id) {
        BmsJiuyeQuotationSystemEntity entity = selectOne("com.jiuyescm.bms.quotation.system.BmsJiuyeQuotationSystemEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BmsJiuyeQuotationSystemEntity save(BmsJiuyeQuotationSystemEntity entity) {
        insert("com.jiuyescm.bms.quotation.system.BmsJiuyeQuotationSystemEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BmsJiuyeQuotationSystemEntity update(BmsJiuyeQuotationSystemEntity entity) {
        update("com.jiuyescm.bms.quotation.system.BmsJiuyeQuotationSystemEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.system.BmsJiuyeQuotationSystemEntityMapper.delete", id);
    }

	@Override
	public List<BmsJiuyeQuotationSystemEntity> queryCustomerBmsList(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.system.BmsJiuyeQuotationSystemEntityMapper.query",condition);
	}
	
}
