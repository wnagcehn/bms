package com.jiuyescm.bms.common.datapower.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.datapower.entity.DatagroupEntity;
import com.jiuyescm.bms.common.datapower.repository.DatagroupDao;
import com.jiuyescm.bms.common.datapower.service.DatagroupService;

@Service("datagroupService")
public class DatagroupServiceImpl implements DatagroupService {

	private DatagroupDao datagroupDao;

	public DatagroupDao getDatagroupDao() {
		return datagroupDao;
	}
	@Autowired
	public void setDatagroupDao(DatagroupDao datagroupDao) {
		this.datagroupDao = datagroupDao;
	}

	@Override
	public PageInfo<DatagroupEntity> queryDatagroup(DatagroupEntity aModel, int aPageNo,
			int aPageSize) {
		// TODO Auto-generated method stub
		return datagroupDao.query(aModel, aPageNo, aPageSize);
	}
	@Override
	public int insertDatagroup(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return datagroupDao.insert(aModel);
	}
	@Override
	public int updateDatagroup(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return datagroupDao.update(aModel);
	}
	@Override
	public int deleteDatagroup(DatagroupEntity aModel) {
		// TODO Auto-generated method stub
		return datagroupDao.delete(aModel);
	}
	@Override
	public List<DatagroupEntity> findByName(String dgroupname) {
		// TODO Auto-generated method stub
		DatagroupEntity data=new DatagroupEntity();
		data.setDgroupname(dgroupname);
		return datagroupDao.findByName(data);
	}

}
