package com.jiuyescm.bms.fees.transport.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IFeesTransportMasterService {

    FeesTransportMasterEntity findById(Long id);
	
    PageInfo<FeesTransportMasterEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<FeesTransportMasterEntity> query(Map<String, Object> condition);

    FeesTransportMasterEntity save(FeesTransportMasterEntity entity);

    FeesTransportMasterEntity update(FeesTransportMasterEntity entity);

    void delete(Long id);

}
