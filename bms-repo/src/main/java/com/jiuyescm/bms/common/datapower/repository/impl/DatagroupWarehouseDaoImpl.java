package com.jiuyescm.bms.common.datapower.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.common.datapower.entity.DatagroupWarehouseEntity;
import com.jiuyescm.bms.common.datapower.entity.Ware_houseEntity;
import com.jiuyescm.bms.common.datapower.repository.DatagroupWarehouseDao;

@Repository("datagroupWarehouseDao")
public class DatagroupWarehouseDaoImpl extends MyBatisDao implements
		DatagroupWarehouseDao {

	// 通过数据组id查询出数据组与仓库的信息
	@Override
	public PageInfo<DatagroupWarehouseEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<DatagroupWarehouseEntity> list = selectList(
				"com.jiuye.datapower.entity.DatagroupWarehouseEntityMapper.selectByPrimaryKey",
				map, new RowBounds(aPageNo, aPageSize));
		PageInfo<DatagroupWarehouseEntity> pageinfo = new PageInfo<DatagroupWarehouseEntity>(
				list);
		return pageinfo;
	}

	// 条件查询仓库
	@Override
	public PageInfo<Ware_houseEntity> queryByCkid(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<Ware_houseEntity> list = selectList(
				"com.jiuye.datapower.entity.DatagroupWarehouseEntityMapper.selectByCk",
				map, new RowBounds(aPageNo, aPageSize));
		PageInfo<Ware_houseEntity> pageinfo = new PageInfo<Ware_houseEntity>(
				list);
		return pageinfo;
	}

	@Override
	public List<DatagroupWarehouseEntity> queryBydatagroupid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("com.jiuye.datapower.entity.DatagroupWarehouseEntityMapper.selectByPrimaryKey",map);

	}

	@Override
	public int addCk(List<DatagroupWarehouseEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuye.datapower.entity.DatagroupWarehouseEntityMapper.insert", list);
	}

	@Override
	public int deleteCk(List<DatagroupWarehouseEntity> list) {
		// TODO Auto-generated method stub
		return deleteBatch("com.jiuye.datapower.entity.DatagroupWarehouseEntityMapper.deleteByPrimaryKey", list);
	}

}
