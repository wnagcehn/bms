/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialTempEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialTempRepository;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bizOutstockPackmaterialTempRepository")
public class BizOutstockPackmaterialTempRepositoryImpl extends MyBatisDao<BizOutstockPackmaterialTempEntity> implements IBizOutstockPackmaterialTempRepository {

	private static final Logger logger = Logger.getLogger(BizOutstockPackmaterialTempRepositoryImpl.class.getName());

	@Override
    public PageInfo<BizOutstockPackmaterialTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizOutstockPackmaterialTempEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizOutstockPackmaterialTempEntity> pageInfo = new PageInfo<BizOutstockPackmaterialTempEntity>(list);
        return pageInfo;
    }

    @Override
    public BizOutstockPackmaterialTempEntity findById(Long id) {
        BizOutstockPackmaterialTempEntity entity = (BizOutstockPackmaterialTempEntity) selectOne("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BizOutstockPackmaterialTempEntity save(BizOutstockPackmaterialTempEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BizOutstockPackmaterialTempEntity update(BizOutstockPackmaterialTempEntity entity) {
        update("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.delete", id);
    }

	@Override
	public int saveBatch(List<BizOutstockPackmaterialTempEntity> list) {
		SqlSession session = getSqlSessionTemplate();
		int ret = session.insert("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.save", list);
		logger.info("保存行数【"+ret+"】");
		return ret;
	}

	@Override
	public void deleteBybatchNum(String batchNum) {
		Map<String,String> map=Maps.newHashMap();
		map.put("batchNum", batchNum);
		int k=this.delete("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.deleteBybatchNum", map);
		logger.info("删除耗材临时表 行数【"+k+"】,批次号【"+batchNum+"】");
	}

	@Override
	public List<BizOutstockPackmaterialTempEntity> querySameData(String batchNum) {
		Map<String,String> map=Maps.newHashMap();
		map.put("batchNum", batchNum);
		return this.selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.querySameData", map);
	}

	@Override
	public List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("batchNum", batchNum);
		return this.selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.queryContainsList", map);
	}

	@Override
	public List<BizOutstockPackmaterialTempEntity> queryContainsList(
			String batchNum, int errorCount) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("batchNum", batchNum);
		map.put("errorCount", errorCount);
		return this.selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialTempEntityMapper.queryContainsListCount", map);

	}
	
}
