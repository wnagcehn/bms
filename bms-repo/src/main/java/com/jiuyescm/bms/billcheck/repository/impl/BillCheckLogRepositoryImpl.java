package com.jiuyescm.bms.billcheck.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;
import com.jiuyescm.bms.billcheck.repository.IBillCheckLogRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("billCheckLogRepository")
public class BillCheckLogRepositoryImpl extends MyBatisDao<BillCheckLogEntity> implements IBillCheckLogRepository{

	@Override
	public PageInfo<BillCheckLogEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<BillCheckLogEntity> list=selectList("com.jiuyescm.bms.billcheck.mapper.BillCheckLogMapper.query", condition,new RowBounds(pageNo,pageSize));
		PageInfo<BillCheckLogEntity> page=new PageInfo<>(list);
		return page;
	}

	@Override
	public int addCheckLog(BillCheckLogEntity logEntity) {
		
		return this.insert("com.jiuyescm.bms.billcheck.mapper.BillCheckLogMapper.addCheckLog", logEntity);
	}
}
