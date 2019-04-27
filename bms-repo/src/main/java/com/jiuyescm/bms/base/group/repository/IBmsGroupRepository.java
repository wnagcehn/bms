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
	
	/**
	 * 查询销售区域的id
	 * @return
	 */
	BmsGroupEntity queryIdByBizType();
	
	/**
	 * 获取指定节点下的二级节点
	 * @author caojianwei
	 * @date 2019年4月17日 下午12:57:57
	 * @param param bizType-业务类型  groupCode-指定节点编码
	 * @return
	 */
	List<BmsGroupEntity> queryNodesForNode(Map<String, String> param);
}
