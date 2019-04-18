package com.jiuyescm.bms.receivable.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchPackageEntity;


/**
 * 
 * @author cjw
 * 
 */
public interface IBizDispatchPackageService {

	public List<BizDispatchPackageEntity> query(Map<String, Object> condition);

    public void updateBatch(List<BizDispatchPackageEntity> list);

}
