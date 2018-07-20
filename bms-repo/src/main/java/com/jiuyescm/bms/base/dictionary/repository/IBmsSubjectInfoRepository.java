/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.dictionary.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsSubjectInfoRepository {

	public PageInfo<BmsSubjectInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	
	public PageInfo<BmsSubjectInfoEntity> query(BmsSubjectInfoEntity queryCondition,
            int pageNo, int pageSize);

    public BmsSubjectInfoEntity save(BmsSubjectInfoEntity entity);

    public BmsSubjectInfoEntity update(BmsSubjectInfoEntity entity);

	public List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode);

	public List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode,
			String bizTypeCode);

	public List<BmsSubjectInfoEntity> queryAllByDimen1(String inOutTypeCode,
			String bizTypeCode, String dimen1Code);

	public List<BmsSubjectInfoEntity> queryAllByDimen2(String inOutTypeCode,
			String bizTypeCode, String dimen2Code);

	public BmsSubjectInfoEntity queryOne(String inOutTypeCode,
			String bizTypeCode, String subjectCode);
	
	BmsSubjectInfoEntity querySubject(Map<String, Object> condition);

	public List<BmsSubjectInfoEntity> queryAllSubejct();
	
	public List<BmsSubjectInfoEntity> querySubject(BmsSubjectInfoEntity entity);
}
