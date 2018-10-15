package com.jiuyescm.bms.base.group.repository;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.base.group.BmsGroupEntity;


public interface IBmsGroupRepository {
	int addGroup(BmsGroupEntity entity);
	int deleteGroup(int id);
	int updateGroup(BmsGroupEntity entity);
	List<BmsGroupEntity> queryAllGroup();
	List<BmsGroupEntity> queryDataByParentId(int parentId);
	int queryChildGroupCount(int id);
	
	List<Integer> queryAllGroupId(int groupId);
	List<BmsGroupEntity> queryDataByParentIdAndBizType(int pid,
			String bizTypeCode);
	boolean checkGroup(BmsGroupEntity entity);
	BmsGroupEntity queryOne(Map<String,Object> condition);
	
	/**
	 * 区域下拉
	 * @param param
	 * @return
	 */
	List<BmsGroupEntity> findAreaEnumList(Map<String, String> param);
}
