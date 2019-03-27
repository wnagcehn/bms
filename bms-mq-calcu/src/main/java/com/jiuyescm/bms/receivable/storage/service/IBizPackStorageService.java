package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;
import com.jiuyescm.bms.biz.storage.entity.BizPackStorageEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBizPackStorageService {

	public List<BizPackStorageEntity> query(Map<String, Object> condition);

    public void update(BizPackStorageEntity entity);
    
    public void updateBatch(List<BizPackStorageEntity> list);

}
