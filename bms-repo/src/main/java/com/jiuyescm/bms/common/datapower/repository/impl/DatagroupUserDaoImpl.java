package com.jiuyescm.bms.common.datapower.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.common.datapower.entity.DataUser;
import com.jiuyescm.bms.common.datapower.entity.UserLimitgroupEntity;
import com.jiuyescm.bms.common.datapower.repository.DatagroupUserDao;

//数据组用户
@Repository("datagroupUserDao")
public class DatagroupUserDaoImpl extends MyBatisDao implements
		DatagroupUserDao {

	
	@Override
	public PageInfo<UserLimitgroupEntity> query(Map<String, Object> map,
			int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<UserLimitgroupEntity> list=selectList("com.jiuye.datapower.entity.UserLimitgroupEntityMapper.selectByPrimaryKey", map, new RowBounds(aPageNo, aPageSize));
		PageInfo<UserLimitgroupEntity> pageinfo=new PageInfo<UserLimitgroupEntity>(list);
		return pageinfo;
	}

	@Override
	public List<UserLimitgroupEntity> queryBydatagroupid(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("com.jiuye.datapower.entity.UserLimitgroupEntityMapper.selectByPrimaryKey", map);
	}

	@Override
	public PageInfo<DataUser> queryByUserid(Map<String, Object> map, int aPageNo,
			int aPageSize) {
		// TODO Auto-generated method stub
		List<DataUser> list=selectList("com.jiuye.datapower.entity.UserLimitgroupEntityMapper.selectByUserid", map, new RowBounds(aPageNo, aPageSize));
		PageInfo<DataUser> pageinfo=new PageInfo<DataUser>(list);
		return pageinfo;
	}

	@Override
	public int addUser(List<UserLimitgroupEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuye.datapower.entity.UserLimitgroupEntityMapper.insert", list);
	}

	@Override
	public int deleteUser(List<UserLimitgroupEntity> list) {
		// TODO Auto-generated method stub
		
		return deleteBatch("com.jiuye.datapower.entity.UserLimitgroupEntityMapper.deleteByPrimaryKey", list);
	}

}
