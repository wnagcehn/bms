package com.jiuyescm.bms.subject.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.BmsSubjectInfoEntity;
import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;

public interface IBmsSubjectInfoService {
	public List<BmsSubjectInfoVo> querySubject(BmsSubjectInfoVo vo);
	
	PageInfo<BmsSubjectInfoVo> query(
			BmsSubjectInfoVo queryCondition, int pageNo, int pageSize) throws Exception;
	
    BmsSubjectInfoEntity save(BmsSubjectInfoVo entity);

    BmsSubjectInfoEntity update(BmsSubjectInfoVo entity);
}
