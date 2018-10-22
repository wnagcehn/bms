package com.jiuyescm.bms.base.group.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.base.group.vo.BmsGroupVo;

public interface IBmsGroupService {

	int addGroup(BmsGroupVo voEntity) throws Exception;
	int deleteGroup(int id);
	int updateGroup(BmsGroupVo voEntity) throws Exception;
	List<BmsGroupVo> queryAllGroup() throws Exception;
	List<BmsGroupVo> queryDataByParentId(int parentId) throws Exception;
	int queryChildGroupCount(int id);
	List<BmsGroupVo> queryDataByParentIdAndBizType(int pid, String bizTypeCode) throws Exception;
	List<BmsGroupVo> queryBmsSubjectGroupByParentId(int parentId) throws Exception;
	boolean checkGroup(BmsGroupVo voEntity) throws Exception;
	BmsGroupVo queryOne(Map<String,Object> condition);
	/**
	 * 查询销售区域
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	List<BmsGroupVo> querySaleAreaDataByParentId(int parentId) throws Exception;
	
	/**
	 * 区域下拉
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<BmsGroupVo> findAreaEnumList(Map<String, String> param) throws Exception;
}
