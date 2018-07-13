package com.jiuyescm.bms.common.log.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.repository.IBillLogRecordDao;
import com.jiuyescm.bms.common.log.service.IBillLogRecordService;

@Service("billLogRecordServiceImpl")
public class BillLogRecordServiceImpl implements IBillLogRecordService {

	@Resource private IBillLogRecordDao billLogRecordDaoImpl;
	
	@Override
	public void log(BmsBillLogRecordEntity record) {
		billLogRecordDaoImpl.log(record);
	}

	@Override
	public PageInfo<BmsBillLogRecordEntity> queryAll(Map<String, Object> parameter,int pageNo, int pageSize) {
		
		return billLogRecordDaoImpl.queryAll(parameter, pageNo, pageSize);
	}

	@Override
	public List<BmsBillLogRecordEntity> queryList(Map<String, Object> parameter) {
		
		return billLogRecordDaoImpl.queryList(parameter);
	}

}
