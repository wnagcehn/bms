package com.jiuyescm.bms.common.datapower.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DataUser;
import com.jiuyescm.bms.common.datapower.entity.UserLimitgroupEntity;

public interface DatagroupUserService {
	// 通过数据组id查找出用户与数据组的
	public PageInfo<UserLimitgroupEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize);

	// 通过数据组id查找出用户与数据组的 返回list
	public List<UserLimitgroupEntity> queryBydatagroupid(Map<String, Object> map);

	// 通过用户id查找出用户单表
	public PageInfo<DataUser> queryByUserid(Map<String, Object> map,
			int aPageNo, int aPageSize);

	// 增加
	public int addUser(List<UserLimitgroupEntity> list);

	// 删除
	public int deleteUser(List<UserLimitgroupEntity> list);
}
