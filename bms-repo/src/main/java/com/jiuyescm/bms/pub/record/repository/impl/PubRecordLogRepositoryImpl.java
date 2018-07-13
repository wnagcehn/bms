package com.jiuyescm.bms.pub.record.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.PubRecordLogEntity;
import com.jiuyescm.bms.pub.record.repository.IPubRecordLogRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("pubRecordLogRepository")
public class PubRecordLogRepositoryImpl extends MyBatisDao<PubRecordLogEntity> implements IPubRecordLogRepository{

	@Override
	public int AddRecordLog(PubRecordLogEntity entity) {
		return this.insert("com.jiuyescm.bms.pub.record.mapper.PubRecordLogMapper.addEntity", entity);
	}

	@Override
	public PageInfo<PubRecordLogEntity> queryAll(Map<String, Object> parameter,
			int pageNo, int pageSize) {
		
		 List<PubRecordLogEntity> list = selectList("com.jiuyescm.bms.pub.record.mapper.PubRecordLogMapper.queryAll", parameter, new RowBounds(
	                pageNo, pageSize));
	     PageInfo<PubRecordLogEntity> pageInfo = new PageInfo<PubRecordLogEntity>(list);
	     return pageInfo;
	}

}
