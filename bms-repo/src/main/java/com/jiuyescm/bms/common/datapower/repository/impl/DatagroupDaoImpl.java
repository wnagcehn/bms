package com.jiuyescm.bms.common.datapower.repository.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.common.datapower.entity.DatagroupEntity;
import com.jiuyescm.bms.common.datapower.repository.DatagroupDao;


@Repository("datagroupDao")
public class DatagroupDaoImpl extends MyBatisDao<DatagroupEntity> implements DatagroupDao {

	
	//查詢
	@Override
	public PageInfo<DatagroupEntity> query(DatagroupEntity aModel, int aPageNo,
			int aPageSize) {
		// TODO Auto-generated method stub
		List<DatagroupEntity> list =selectList("com.jiuye.datapower.entity.DatagroupEntityMapper.query", aModel, new RowBounds(aPageNo, aPageSize));
		PageInfo<DatagroupEntity> pageinfo=new PageInfo<DatagroupEntity>(list);
		return pageinfo;
	}

	@Override
	public int insert(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return insert("com.jiuye.datapower.entity.DatagroupEntityMapper.insert", aModel);
		//return 0;
	}

	@Override
	public int update(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return update("com.jiuye.datapower.entity.DatagroupEntityMapper.updateByPrimaryKey", aModel);
	}

	@Override
	public int delete(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return delete("com.jiuye.datapower.entity.DatagroupEntityMapper.deleteByPrimaryKey", aModel.getId());
	}

	@Override
	public List<DatagroupEntity> findByName(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return selectList("com.jiuye.datapower.entity.DatagroupEntityMapper.queryByName", aModel);
	}

}
