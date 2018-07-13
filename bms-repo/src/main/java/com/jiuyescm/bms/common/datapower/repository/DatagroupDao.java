package com.jiuyescm.bms.common.datapower.repository;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupEntity;

public interface DatagroupDao {

	// 根据数组实体查询出数组集合
	public PageInfo<DatagroupEntity> query(DatagroupEntity aModel, int aPageNo,
			int aPageSize);
	//增加数据组
	public int insert(DatagroupEntity aModel);
	//修改数据组
	public int update(DatagroupEntity aModel);
	//删除数据组
	public int delete(DatagroupEntity aModel);
	//根据数据组名称查找
	public List<DatagroupEntity> findByName(DatagroupEntity aModel);

}
