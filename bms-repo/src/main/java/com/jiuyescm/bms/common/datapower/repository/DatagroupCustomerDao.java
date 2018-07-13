package com.jiuyescm.bms.common.datapower.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupCustomerEntity;
import com.jiuyescm.bms.common.datapower.entity.Pub_Customer;


//数据组和商家
public interface DatagroupCustomerDao {

	//通过数据组id查找出商家与数据组的
	public PageInfo<DatagroupCustomerEntity> query(Map<String,Object>map, int aPageNo,
			int aPageSize);
	
	//通过商家id查找出商家与数据组的
		public List<DatagroupCustomerEntity> queryByDataCustomerid(Map<String,Object>map);
	
	//通过数据组id查找出商家与数据组的
		public List<DatagroupCustomerEntity> queryBydatagroup(Map<String,Object>map);
	
	//通过商家id查找出商家 单表
	public PageInfo<Pub_Customer> queryByCustomerid(Map<String,Object>map, int aPageNo,
			int aPageSize);
	//增加
	public int addCustomer(List<DatagroupCustomerEntity> list);
	//删除
	public int deleteCustomer(List<DatagroupCustomerEntity> list);
}
