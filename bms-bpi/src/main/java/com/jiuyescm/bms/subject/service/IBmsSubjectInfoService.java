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
}
