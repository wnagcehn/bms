package com.jiuyescm.bms.subject.service;

import java.util.List;

import com.jiuyescm.bms.subject.vo.BmsSubjectInfoVo;

public interface IBmsSubjectInfoService {
	public List<BmsSubjectInfoVo> querySubject(BmsSubjectInfoVo vo);
}
