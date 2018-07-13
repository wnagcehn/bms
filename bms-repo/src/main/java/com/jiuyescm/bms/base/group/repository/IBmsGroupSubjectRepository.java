package com.jiuyescm.bms.base.group.repository;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.entity.BmsGroupSubEntity;
import com.jiuyescm.bms.base.group.BmsGroupSubjectEntity;

public interface IBmsGroupSubjectRepository {

	List<BmsGroupSubjectEntity> queryAllByGroupId(int groupId);

	int addBatch(List<BmsGroupSubjectEntity> list);

	int delGroupSubject(BmsGroupSubjectEntity subjectEntity);

	int updateGroupSubject(BmsGroupSubjectEntity entity);

	PageInfo<BmsGroupSubEntity> queryGroupSubject(
			BmsGroupSubjectEntity queryCondition, int pageNo, int pageSize);

	List<String> checkSubjectCodeExist(int groupId, List<String> subjectCodeList);

	int querySubjectCountByGroupId(int groupId);

}
