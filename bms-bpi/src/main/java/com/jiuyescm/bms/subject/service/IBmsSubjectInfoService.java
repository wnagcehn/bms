/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.subject.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;

public interface IBmsSubjectInfoService {
	public List<BmsSubjectInfoVo> querySubject(BmsSubjectInfoVo vo);
	
	PageInfo<BmsSubjectInfoVo> query(
			BmsSubjectInfoVo queryCondition, int pageNo, int pageSize);
	
	BmsSubjectInfoVo save(BmsSubjectInfoVo entity);

	BmsSubjectInfoVo update(BmsSubjectInfoVo entity);
    
	BmsSubjectInfoVo queryOne(Long id);
	
	/**
	 * 根据编码获取费用科目
	 * @param inOutTypecode 收支类型  INPUT-收入  OUTPUT-支出
	 * @param bizType 业务类型 STORAGE-仓储  DISPATCH-宅配 TRANSPORT-干线 AIRTRANSPORT-航空
	 * @param subjectCode 科目编码
	 * @return
	 */
	BmsSubjectInfoVo querySubjectByCode(String inOutTypecode,String bizType,String subjectCode);
	
	/**
	 * 根据名称获取费用科目
	 * @param inOutTypecode 收支类型  INPUT-收入  OUTPUT-支出
	 * @param bizType 业务类型 STORAGE-仓储  DISPATCH-宅配 TRANSPORT-干线 AIRTRANSPORT-航空
	 * @param subjectName 科目名称
	 * @return
	 */
	BmsSubjectInfoVo querySubjectByName(String inOutTypecode,String bizType,String subjectName);
	
	BmsSubjectInfoVo queryReceiveByCode(String subjectCode);
	
}
