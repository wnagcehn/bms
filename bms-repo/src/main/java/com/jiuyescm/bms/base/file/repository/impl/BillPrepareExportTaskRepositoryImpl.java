package com.jiuyescm.bms.base.file.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.file.entity.BillPrepareExportTaskEntity;
import com.jiuyescm.bms.base.file.repository.IBillPrepareExportTaskRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("billPrepareExportTaskRepository")
public class BillPrepareExportTaskRepositoryImpl extends MyBatisDao implements IBillPrepareExportTaskRepository{

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<BillPrepareExportTaskEntity> queryBillTask(
			Map<String, Object> param, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillPrepareExportTaskEntity> list = selectList("com.jiuyescm.bms.base.file.mapper.BillPrepareExportTaskMapper.queryBillTask", 
				param, new RowBounds(pageNo, pageSize));
	    return new PageInfo<BillPrepareExportTaskEntity>(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillPrepareExportTaskEntity save(BillPrepareExportTaskEntity entity)
			throws Exception {
		// TODO Auto-generated method stub
		 insert("com.jiuyescm.bms.base.file.mapper.BillPrepareExportTaskMapper.save", entity);
	     return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int update(BillPrepareExportTaskEntity entity) {
		// TODO Auto-generated method stub
        return update("com.jiuyescm.bms.base.file.mapper.BillPrepareExportTaskMapper.update", entity);
	}

	@Override
	public boolean checkFileHasDownLoad(Map<String, Object> queryEntity) {
		// TODO Auto-generated method stub
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.file.mapper.BillPrepareExportTaskMapper.checkFileHasDownLoad", queryEntity);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getChildCustomer(String mkId) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list=(List<Map<String, String>>)selectList("com.jiuyescm.bms.base.file.mapper.BillPrepareExportTaskMapper.getChildCustomer", mkId);
		return list;
	}

}
