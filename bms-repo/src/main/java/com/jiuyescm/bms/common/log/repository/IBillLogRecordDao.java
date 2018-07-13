package com.jiuyescm.bms.common.log.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;

public interface IBillLogRecordDao {

	void log(BmsBillLogRecordEntity record);
	
	public PageInfo<BmsBillLogRecordEntity> queryAll(Map<String,Object> parameter,int pageNo, int pageSize);
	
	public List<BmsBillLogRecordEntity> queryList(Map<String,Object> parameter);
	
}
