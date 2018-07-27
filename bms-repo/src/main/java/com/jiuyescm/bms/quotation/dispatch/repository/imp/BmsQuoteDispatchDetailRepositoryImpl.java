/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.BmsQuoteDispatchDetailEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IBmsQuoteDispatchDetailRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bmsQuoteDispatchDetailRepository")
public class BmsQuoteDispatchDetailRepositoryImpl extends MyBatisDao<BmsQuoteDispatchDetailEntity> implements IBmsQuoteDispatchDetailRepository {

	private static final Logger logger = Logger.getLogger(BmsQuoteDispatchDetailRepositoryImpl.class.getName());
	
	@Override
    public PageInfo<BmsQuoteDispatchDetailEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsQuoteDispatchDetailEntity> list = selectList("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsQuoteDispatchDetailEntity> pageInfo = new PageInfo<BmsQuoteDispatchDetailEntity>(list);
        return pageInfo;
    }

	@Override
	public List<BmsQuoteDispatchDetailEntity> queryAllById(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.queryAllById", condition);
	}
	
	@Override
	public Integer getId(String temid) {
		SqlSession session = getSqlSessionTemplate();
		return (Integer)session.selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.getId", temid);
	}

    @Override
    public int save(BmsQuoteDispatchDetailEntity entity) {
    	return insert("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.save", entity);
    }

    @Override
    public int update(BmsQuoteDispatchDetailEntity entity) {
    	return update("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.update", entity);
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.delete", id);
    }

	@Override
	public int deletePriceDistribution(BmsQuoteDispatchDetailEntity entity) {
		return delete("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.deletePriceDistribution", entity);
	}
	
	@Override
	public int removeDispatchByMap(Map<String, Object> condition) {
		return delete("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.removeDispatchByMap", condition);
	}

	@Override
	public int insertBatchTmp(List<BmsQuoteDispatchDetailEntity> list) {
		return insertBatch("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.save", list);
	}

	@Override
	public BmsQuoteDispatchDetailEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BmsQuoteDispatchDetailEntity) selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.BmsQuoteDispatchDetailMapper.queryOne", condition);
	}

}
