package com.jiuyescm.bms.common.datapower.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupCustomerEntity;
import com.jiuyescm.bms.common.datapower.entity.Pub_Customer;
import com.jiuyescm.bms.common.datapower.repository.DatagroupCustomerDao;
import com.jiuyescm.bms.common.datapower.service.DatagroupCustomerService;

@Service("datagroupCustomerService")
public class DatagroupCustomerServiceImpl implements DatagroupCustomerService {
	
	@Resource
	private DatagroupCustomerDao datagroupCustomerDao;

	@Override
	public PageInfo<DatagroupCustomerEntity> selectByPrimaryKey(
			Map<String, Object> map, int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupCustomerDao.query(map, aPageNo, aPageSize);
	}

	@Override
	public PageInfo<Pub_Customer> queryByCustomerid(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupCustomerDao.queryByCustomerid(map, aPageNo, aPageSize);
	}

	@Override
	public List<DatagroupCustomerEntity> queryBydatagroup(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return datagroupCustomerDao.queryBydatagroup(map);
	}

	@Override
	public int addCustomer(List<DatagroupCustomerEntity> list) {
		// TODO Auto-generated method stub
		return datagroupCustomerDao.addCustomer(list);
	}

	@Override
	public int deleteCustomer(List<DatagroupCustomerEntity> list) {
		// TODO Auto-generated method stub
		//String customerid=datagroupCustomerEntity.getCustomerid();//商品id
		//String dgroupid=datagroupCustomerEntity.getDgroupid();//数据组id
		return datagroupCustomerDao.deleteCustomer(list);
	}

	@Override
	public List<DatagroupCustomerEntity> queryByDataCustomerid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return datagroupCustomerDao.queryByDataCustomerid(map);
	}


}
