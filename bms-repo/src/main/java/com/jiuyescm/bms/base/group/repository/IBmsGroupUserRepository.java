package com.jiuyescm.bms.base.group.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupEntity;
import com.jiuyescm.bms.base.group.BmsGroupUserEntity;

public interface IBmsGroupUserRepository {
	int addGroupUser(BmsGroupUserEntity entity);
	int deleteGroupUser(int id);
	int updateGroupUser(BmsGroupUserEntity entity);
	List<BmsGroupUserEntity> queryAllGroupUser();
	PageInfo<BmsGroupUserEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);
	String checkExistGroupName(String userId);
	int queryUserCountByGroupId(int groupId);
	BmsGroupUserEntity queryEntityByUserId(String userId);
	List<BmsGroupUserEntity> queryAllByGroupId(List<Integer> groupIds);
	
	/**
	 * 为了添加area_group_id（销售区域管理使用）
	 * @param condition
	 * @return
	 */
	BmsGroupUserEntity queryAreaGroupId(Map<String, Object> condition);
	/**
	 * 检查username是否存在别的权限组
	 * @param param
	 * @return
	 */
	String checkUserGroupName(Map<String, Object> param);
}
