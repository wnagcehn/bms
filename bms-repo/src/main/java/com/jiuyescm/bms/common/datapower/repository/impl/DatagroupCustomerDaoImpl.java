package com.jiuyescm.bms.common.datapower.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.common.datapower.entity.DatagroupCustomerEntity;
import com.jiuyescm.bms.common.datapower.entity.Pub_Customer;
import com.jiuyescm.bms.common.datapower.repository.DatagroupCustomerDao;

@Repository("datagroupCustomerDao")
public class DatagroupCustomerDaoImpl extends MyBatisDao implements DatagroupCustomerDao{
	
	//通过数据组id查找出商家与数据组的
	@Override
	public PageInfo<DatagroupCustomerEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<DatagroupCustomerEntity> list =selectList("com.jiuye.datapower.entity.DatagroupCustomerEntityMapper.selectByPrimaryKey", map, new RowBounds(aPageNo, aPageSize));
		PageInfo<DatagroupCustomerEntity> pageinfo=new PageInfo<DatagroupCustomerEntity>(list);
		return pageinfo;
	}
	//通过商家id查找出商家与数据组的
	@Override
	public List<DatagroupCustomerEntity> queryByDataCustomerid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("com.jiuye.datapower.entity.DatagroupCustomerEntityMapper.selectByDataCustomerid", map);
		
	}

	//通过商家id查找出商家
	@Override
	public PageInfo<Pub_Customer> queryByCustomerid(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<Pub_Customer> list =selectList("com.jiuye.datapower.entity.DatagroupCustomerEntityMapper.selectByCustomerid", map, new RowBounds(aPageNo, aPageSize));
		PageInfo<Pub_Customer> pageinfo=new PageInfo<Pub_Customer>(list);
		return pageinfo;
	}

	@Override
	public List<DatagroupCustomerEntity> queryBydatagroup(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("com.jiuye.datapower.entity.DatagroupCustomerEntityMapper.selectByPrimaryKey", map);
	}

	@Override
	public int addCustomer(List<DatagroupCustomerEntity> list) {
		// TODO Auto-generated method stub
		//com.jiuye.datapower.entity.DatagroupCustomerEntityMapper
		
		return insertBatch("com.jiuye.datapower.entity.DatagroupCustomerEntityMapper.insert", list);
	}

	@Override
	public int deleteCustomer(List<DatagroupCustomerEntity> list) {
		// TODO Auto-generated method stub
		return deleteBatch("com.jiuye.datapower.entity.DatagroupCustomerEntityMapper.deleteByPrimaryKey", list);
	}

	

	

}
