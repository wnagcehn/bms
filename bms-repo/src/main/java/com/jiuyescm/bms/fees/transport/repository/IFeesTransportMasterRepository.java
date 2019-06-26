package com.jiuyescm.bms.fees.transport.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IFeesTransportMasterRepository {

    FeesTransportMasterEntity findById(Long id);
	
	PageInfo<FeesTransportMasterEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<FeesTransportMasterEntity> query(Map<String, Object> condition);

    FeesTransportMasterEntity save(FeesTransportMasterEntity entity);

    FeesTransportMasterEntity update(FeesTransportMasterEntity entity);

    void delete(Long id);

    /**
     * 预账单干线导出查询
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月25日 下午2:12:56
     *
     * @param condition
     * @return
     */
    List<FeesTransportMasterEntity> queryForPrepareBill(Map<String, Object> condition);

}
