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

    /**
     * 导出查询
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月24日 上午11:13:19
     *
     * @param condition
     * @param pageNo
     * @param pageSize
     * @return
     */
    PageInfo<FeesTransportMasterEntity> queryToExport(Map<String, Object> condition, int pageNo, int pageSize);

    /**
     * 预账单干线导出
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月25日 下午2:13:30
     *
     * @param condition
     * @return
     */
    List<FeesTransportMasterEntity> queryForPrepareBill(Map<String, Object> condition);

}
