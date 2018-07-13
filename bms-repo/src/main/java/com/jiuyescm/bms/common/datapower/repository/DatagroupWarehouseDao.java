package com.jiuyescm.bms.common.datapower.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupWarehouseEntity;
import com.jiuyescm.bms.common.datapower.entity.Ware_houseEntity;

public interface DatagroupWarehouseDao {
	// 通过数据组id查找出仓库与数据组的
	public PageInfo<DatagroupWarehouseEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize);

	// 通过数据组id查找出仓库与数据组的 返回list
	public List<DatagroupWarehouseEntity> queryBydatagroupid(
			Map<String, Object> map);

	// 通过仓库id查找出仓库单表
	public PageInfo<Ware_houseEntity> queryByCkid(Map<String, Object> map,
			int aPageNo, int aPageSize);

	// 增加
	public int addCk(List<DatagroupWarehouseEntity> list);

	// 删除
	public int deleteCk(List<DatagroupWarehouseEntity> list);
}
