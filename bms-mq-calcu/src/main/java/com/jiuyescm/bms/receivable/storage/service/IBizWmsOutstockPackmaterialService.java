package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;

public interface IBizWmsOutstockPackmaterialService {

	List<BizOutstockPackmaterialEntity> query(Map<String, Object> map);

	int updateBatch(List<BizOutstockPackmaterialEntity> billList);

}
