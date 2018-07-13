/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.dictionary.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.repository.IBmsSubjectInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bmsSubjectInfoRepository")
public class BmsSubjectInfoRepositoryImpl extends MyBatisDao<BmsSubjectInfoEntity> implements IBmsSubjectInfoRepository {

	private static final Logger logger = Logger.getLogger(BmsSubjectInfoRepositoryImpl.class.getName());

	public BmsSubjectInfoRepositoryImpl() {
		super();
	}
	
	@Override
    public PageInfo<BmsSubjectInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsSubjectInfoEntity> list = selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BmsSubjectInfoEntity> pageInfo = new PageInfo<BmsSubjectInfoEntity>(list);
        return pageInfo;
    }

    @Override
    public BmsSubjectInfoEntity save(BmsSubjectInfoEntity entity) {
        insert("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.save", entity);
        return entity;
    }

    @Override
    public BmsSubjectInfoEntity update(BmsSubjectInfoEntity entity) {
        update("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.update", entity);
        return entity;
    }

	@Override
	public List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("inOutTypeCode", inOutTypeCode);
		return this.selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryAll", map);
	}

	@Override
	public List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode,
			String bizTypeCode) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("inOutTypeCode", inOutTypeCode);
		map.put("bizTypeCode", bizTypeCode);
		return this.selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryAll", map);
	}

	@Override
	public List<BmsSubjectInfoEntity> queryAllByDimen1(String inOutTypeCode,
			String bizTypeCode, String dimen1Code) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("inOutTypeCode", inOutTypeCode);
		map.put("bizTypeCode", bizTypeCode);
		map.put("dimen1Code", dimen1Code);
		return this.selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryAll", map);
	}

	@Override
	public List<BmsSubjectInfoEntity> queryAllByDimen2(String inOutTypeCode,
			String bizTypeCode, String dimen2Code) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("inOutTypeCode", inOutTypeCode);
		map.put("bizTypeCode", bizTypeCode);
		map.put("dimen2Code", dimen2Code);
		return this.selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryAll", map);
	}

	@Override
	public BmsSubjectInfoEntity queryOne(String inOutTypeCode,
			String bizTypeCode, String subjectCode) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("inOutTypeCode", inOutTypeCode);
		map.put("bizTypeCode", bizTypeCode);
		map.put("subjectCode", subjectCode);
		return (BmsSubjectInfoEntity)this.selectOne("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryAll", map);
	}

	@Override
	public BmsSubjectInfoEntity querySubject(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BmsSubjectInfoEntity) selectOne("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.querySubject", condition);
	}

	@Override
	public List<BmsSubjectInfoEntity> queryAllSubejct() {
		return this.selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryAllSubejct", null);
	}

	@Override
	public List<BmsSubjectInfoEntity> querySubject(BmsSubjectInfoEntity entity) {
		// TODO Auto-generated method stub
		return this.selectList("com.jiuyescm.bms.base.dictionary.BmsSubjectInfoMapper.queryByEntity", entity);
	}

	
}
