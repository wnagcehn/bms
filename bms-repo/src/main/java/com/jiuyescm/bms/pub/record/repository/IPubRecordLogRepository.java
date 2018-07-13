package com.jiuyescm.bms.pub.record.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.pub.PubRecordLogEntity;

public interface IPubRecordLogRepository {

	int AddRecordLog(PubRecordLogEntity entity);

	PageInfo<PubRecordLogEntity> queryAll(Map<String, Object> parameter,
			int pageNo, int pageSize);

}
