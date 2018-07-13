package com.jiuyescm.bms.pub;

import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface IPubRecordLogService {

	public int AddRecordLog(PubRecordLogEntity entity);

	public PageInfo<PubRecordLogEntity> queryAll(Map<String, Object> parameter,
			int pageNo, int pageSize);
}
