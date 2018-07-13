package com.jiuyescm.bms.common.datapower.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupWarehouseEntity;
import com.jiuyescm.bms.common.datapower.entity.Ware_houseEntity;
import com.jiuyescm.bms.common.datapower.repository.DatagroupWarehouseDao;
import com.jiuyescm.bms.common.datapower.service.DatagroupWarehouseService;

@Service("datagroupWarehouseService")
public class DatagroupWarehouseServiceImpl implements DatagroupWarehouseService {
	
	@Resource
	private DatagroupWarehouseDao datagroupWarehouseDao;
	
	@Override
	public PageInfo<DatagroupWarehouseEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupWarehouseDao.query(map, aPageNo, aPageSize);
	}

	@Override
	public PageInfo<Ware_houseEntity> queryByCkid(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupWarehouseDao.queryByCkid(map, aPageNo, aPageSize);
	}

	@Override
	public List<DatagroupWarehouseEntity> queryBydatagroupid(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return datagroupWarehouseDao.queryBydatagroupid(map);
	}

	@Override
	public int addCk(List<DatagroupWarehouseEntity> list) {
		// TODO Auto-generated method stub
		return datagroupWarehouseDao.addCk(list);
	}

	@Override
	public int deleteCk(List<DatagroupWarehouseEntity> list) {
		// TODO Auto-generated method stub
		return datagroupWarehouseDao.deleteCk(list);
	}

}
