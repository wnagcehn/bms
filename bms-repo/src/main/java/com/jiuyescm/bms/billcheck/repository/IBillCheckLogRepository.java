package com.jiuyescm.bms.billcheck.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.BillCheckLogEntity;

public interface IBillCheckLogRepository {
	PageInfo<BillCheckLogEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	int addCheckLog(BillCheckLogEntity logEntity);

}
