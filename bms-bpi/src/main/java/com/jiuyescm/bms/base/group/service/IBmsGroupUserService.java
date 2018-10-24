package com.jiuyescm.bms.base.group.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.group.BmsGroupUserEntity;
import com.jiuyescm.bms.base.group.vo.BmsGroupUserVo;
import com.jiuyescm.bms.base.group.vo.BmsGroupVo;

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
	
	/**
	 * 通过userId查询所属区域
	 * @param condition
	 * @return
	 */
	//BmsGroupUserVo queryGroupNameByUserId(String userId);
	
	/**
	 * 销售员id和name映射
	 * 
	 * @param param
	 * @return
	 */
	List<BmsGroupUserEntity> queryUserByBizType(Map<String, String> param);
	
	/**
	 * 查询销售区域+人员
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageInfo<BmsGroupUserVo> queryGroupUser(Map<String, Object> condition, int pageNo, int pageSize) throws Exception;
	
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
	 * @throws Exception
	 */
	PageInfo<BmsGroupUserVo> querySaleUser(Map<String, Object> condition, int pageNo, int pageSize) throws Exception;
}
