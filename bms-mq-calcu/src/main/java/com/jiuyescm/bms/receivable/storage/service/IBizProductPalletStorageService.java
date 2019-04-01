package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBizProductPalletStorageService {

	public List<BizProductPalletStorageEntity> query(Map<String, Object> condition);

    public void update(BizProductPalletStorageEntity entity);

    public void updateBatch(List<BizProductPalletStorageEntity> list);

}
