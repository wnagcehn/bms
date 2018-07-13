package com.jiuyescm.bms.common.datapower.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupEntity;

public interface DatagroupService {

	// 根据数组实体查询出数组集合
	public PageInfo<DatagroupEntity> queryDatagroup(DatagroupEntity aModel,
			int aPageNo, int aPageSize);

	// 增加数据组
	public int insertDatagroup(DatagroupEntity aModel);

	// 修改数据组
	public int updateDatagroup(DatagroupEntity aModel);

	// 删除数据组
	public int deleteDatagroup(DatagroupEntity aModel);

	// 根据数据组名称查找
	public List<DatagroupEntity> findByName(String dgroupname);

}
