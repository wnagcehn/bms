package com.jiuyescm.bms.pub;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.record.repository.IPubRecordLogRepository;

@Service("pubRecordLogService")
public class PubRecordLogServiceImpl implements IPubRecordLogService {

	@Autowired
	private IPubRecordLogRepository pubRecordLogRepository;
	@Override
	public int AddRecordLog(PubRecordLogEntity entity) {
		return pubRecordLogRepository.AddRecordLog(entity);
	}
	@Override
	public PageInfo<PubRecordLogEntity> queryAll(Map<String, Object> parameter,
			int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return pubRecordLogRepository.queryAll(parameter,pageNo,pageSize);
	}

}
