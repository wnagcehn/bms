/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.dictionary.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsSubjectInfoService {

    PageInfo<BmsSubjectInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);


    BmsSubjectInfoEntity save(BmsSubjectInfoEntity entity);

    BmsSubjectInfoEntity update(BmsSubjectInfoEntity entity);
    
    List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode);
    List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode,String bizTypeCode);
    List<BmsSubjectInfoEntity> queryAllByDimen1(String inOutTypeCode,String bizTypeCode,String dimen1Code);
    List<BmsSubjectInfoEntity> queryAllByDimen2(String inOutTypeCode,String bizTypeCode,String dimen2Code);
    BmsSubjectInfoEntity queryOne(String inOutTypeCode,String bizTypeCode,String subjectCode);
    BmsSubjectInfoEntity querySubject(Map<String, Object> condition);


	List<BmsSubjectInfoEntity> queryAllSubejct();


	List<BmsSubjectInfoEntity> findAll(String bizTypeCode);

}
