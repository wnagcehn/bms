package com.jiuyescm.bms.base.group.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;

public interface IBmsGroupUserService {
	
	int addGroupUser(BmsGroupUserVo voEntity) throws Exception;
	int deleteGroupUser(int id);
	int updateGroupUser(BmsGroupUserVo voEntity) throws Exception;
	List<BmsGroupUserVo> queryAllGroupUser() throws Exception;
	PageInfo<BmsGroupUserVo> query(Map<String, Object> condition,
            int pageNo, int pageSize) throws Exception;
	String checkExistGroupName(String userId);
	int queryUserCountByGroupId(int groupId);
	List<String> queryContainUserIds(
			String userId);
	BmsGroupUserVo queryEntityByUserId(String userId);
	
	List<String> queryContainUserIds(BmsGroupUserVo voEntity);
	
	/**
	 * 查询区域组ID（销售区域管理使用）
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	BmsGroupUserVo queryAreaGroupId(Map<String, Object> condition) throws Exception;
	
	/**
	 * 检查username是否存在其他权限组
	 * @param param
	 * @return
	 */
	String checkUserGroupName(Map<String, Object> param);
}
