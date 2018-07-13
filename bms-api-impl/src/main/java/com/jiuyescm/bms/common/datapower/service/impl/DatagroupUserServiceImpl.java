package com.jiuyescm.bms.common.datapower.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DataUser;
import com.jiuyescm.bms.common.datapower.entity.UserLimitgroupEntity;
import com.jiuyescm.bms.common.datapower.repository.DatagroupUserDao;
import com.jiuyescm.bms.common.datapower.service.DatagroupUserService;

@Service("datagroupUserService")
public class DatagroupUserServiceImpl implements DatagroupUserService {

	@Resource
	private DatagroupUserDao datagroupUserDao;
	
	@Override
	public PageInfo<UserLimitgroupEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupUserDao.query(map, aPageNo, aPageSize);
	}

	@Override
	public List<UserLimitgroupEntity> queryBydatagroupid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return datagroupUserDao.queryBydatagroupid(map);
	}

	@Override
	public PageInfo<DataUser> queryByUserid(Map<String, Object> map, int aPageNo,
			int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupUserDao.queryByUserid(map, aPageNo, aPageSize);
	}

	@Override
	public int addUser(List<UserLimitgroupEntity> list) {
		// TODO Auto-generated method stub
		return datagroupUserDao.addUser(list);
	}

	@Override
	public int deleteUser(List<UserLimitgroupEntity> list) {
		// TODO Auto-generated method stub
		return datagroupUserDao.deleteUser(list);
	}

}
