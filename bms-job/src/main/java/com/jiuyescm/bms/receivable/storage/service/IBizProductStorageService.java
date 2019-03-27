package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.entity.BizProductStorageEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBizProductStorageService {

	public List<BizProductStorageEntity> query(Map<String, Object> condition);

    public void update(BizProductStorageEntity entity);

    public void updateBatch(List<BizProductStorageEntity> list);

	void updateProductStorageById(
			List<com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity> list);
}
