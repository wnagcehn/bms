/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.dictionary.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.base.dictionary.repository.IBmsSubjectInfoRepository;
import com.jiuyescm.bms.base.dictionary.service.IBmsSubjectInfoService;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bmsSubjectInfoService")
public class BmsSubjectInfoServiceImpl implements IBmsSubjectInfoService {

	//private static final Logger logger = Logger.getLogger(BmsSubjectInfoServiceImpl.class.getName());
	
	@Autowired
    private IBmsSubjectInfoRepository bmsSubjectInfoRepository;

    @Override
    public PageInfo<BmsSubjectInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsSubjectInfoRepository.query(condition, pageNo, pageSize);
    }


    @Override
    public BmsSubjectInfoEntity save(BmsSubjectInfoEntity entity) {
        return bmsSubjectInfoRepository.save(entity);
    }

    @Override
    public BmsSubjectInfoEntity update(BmsSubjectInfoEntity entity) {
        return bmsSubjectInfoRepository.update(entity);
    }


	@Override
	public List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode) {
		return bmsSubjectInfoRepository.queryAll(inOutTypeCode);
	}


	@Override
	public List<BmsSubjectInfoEntity> queryAll(String inOutTypeCode,
			String bizTypeCode) {
		return bmsSubjectInfoRepository.queryAll(inOutTypeCode,bizTypeCode);
	}


	@Override
	public List<BmsSubjectInfoEntity> queryAllByDimen1(String inOutTypeCode,
			String bizTypeCode, String dimen1Code) {
		return bmsSubjectInfoRepository.queryAllByDimen1(inOutTypeCode,bizTypeCode,dimen1Code);
	}


	@Override
	public List<BmsSubjectInfoEntity> queryAllByDimen2(String inOutTypeCode,
			String bizTypeCode, String dimen2Code) {
		return bmsSubjectInfoRepository.queryAllByDimen2(inOutTypeCode,bizTypeCode,dimen2Code);
	}


	@Override
	public BmsSubjectInfoEntity queryOne(String inOutTypeCode,
			String bizTypeCode, String subjectCode) {
		return bmsSubjectInfoRepository.queryOne(inOutTypeCode,bizTypeCode,subjectCode);
	}


	@Override
	public BmsSubjectInfoEntity querySubject(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsSubjectInfoRepository.querySubject(condition);
	}


	@Override
	public List<BmsSubjectInfoEntity> queryAllSubejct() {
		return bmsSubjectInfoRepository.queryAllSubejct();
	}

	
}
