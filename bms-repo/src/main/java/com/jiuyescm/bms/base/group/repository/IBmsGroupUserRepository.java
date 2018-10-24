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
	
	/**
	 * 通过userId查询所属区域
	 * @param condition
	 * @return
	 */
	//BmsGroupUserEntity queryGroupNameByUserId(String userId);
	
	/**
	 * 销售员id和name映射
	 * @param param
	 * @return
	 */
	List<BmsGroupUserEntity> queryUserByBizType(Map<String, String> param);
	
	/**
	 * 查询组成员+销售区域
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsGroupUserEntity> queryGroupUser(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 检查用户是否重复
	 * @param param
	 * @return
	 */
	String checkSaleUser(Map<String, Object> param);
	
	/**
	 * 更新时检查用户是否重复
	 * @param param
	 * @return
	 */
	String checkSaleUserIgnoreId(Map<String, Object> param);
	
	/**
	 * 查询销售人员
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsGroupUserEntity> querySaleUser(Map<String, Object> condition, int pageNo, int pageSize);
}
