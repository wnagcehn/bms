package com.jiuyescm.bms.base.group.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.base.entity.BmsGroupSubEntity;
import com.jiuyescm.bms.base.group.BmsGroupSubjectEntity;
import com.jiuyescm.bms.base.group.repository.IBmsGroupSubjectRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsGroupSubjectRepository")
public class BmsGroupSubjectRepositoryImpl extends MyBatisDao<BmsGroupSubjectEntity> implements IBmsGroupSubjectRepository{

	@Override
	public List<BmsGroupSubjectEntity> queryAllByGroupId(int groupId) {
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.queryAllByGroupId", groupId);
	}
	
	@Override
	public List<BmsGroupSubjectEntity> queryAllByGroupIdAndBizTypeCode(BmsGroupSubjectEntity entity) {
		return this.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.queryAllByGroupIdAndBizTypeCode", entity);
	}

	@Override
	public int addBatch(List<BmsGroupSubjectEntity> list) {
		return this.insertBatch("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.insertEntity", list);
	}

	@Override
	public int delGroupSubject(BmsGroupSubjectEntity entity) {
		return this.update("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.deleteEntity", entity);
	}

	@Override
	public int updateGroupSubject(BmsGroupSubjectEntity entity) {
		// TODO Auto-generated method stub
		return this.update("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.update", entity);
	}

	@Override
	public PageInfo<BmsGroupSubEntity> queryGroupSubject(
			BmsGroupSubjectEntity queryCondition, int pageNo, int pageSize) {
		SqlSession session=this.getSqlSessionTemplate();
		List<BmsGroupSubEntity> list = session.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.queryGroupSubject", 
				queryCondition, new RowBounds(pageNo, pageSize));
		return new PageInfo<BmsGroupSubEntity>(list);
	}

	@Override
	public List<String> checkSubjectCodeExist(int groupId,
			List<String> subjectCodeList) {
		Map<String,Object> map=Maps.newHashMap();
		map.put("groupId", groupId);
		map.put("subjectCodeList", subjectCodeList);
		SqlSession session = getSqlSessionTemplate();
		return session.selectList("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.checkSubjectCodeExist", map);
	}

	@Override
	public int querySubjectCountByGroupId(int groupId) {
		int obj=selectOneForInt("com.jiuyescm.bms.base.group.mapper.BmsGroupSubjectMapper.querySubjectCountByGroupId", groupId);
		return obj;
	}

}
