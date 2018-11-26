package com.jiuyescm.bms.billimport.service;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;

/**
 * ..Service
 * @author liuzhicheng
 * 
 */
public interface IBillFeesReceiveTransportTempService {

    BillFeesReceiveTransportTempEntity findById(Long id);
	
    PageInfo<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition);

    BillFeesReceiveTransportTempEntity save(BillFeesReceiveTransportTempEntity entity);

    BillFeesReceiveTransportTempEntity update(BillFeesReceiveTransportTempEntity entity);

    void delete(Long id);
    
    /**
     * 批量写入
     * @param list
     * @return
     */
    int insertBatchTemp(List<BillFeesReceiveTransportTempEntity> list);
    
    /**
     * 批量删除
     * @param condition
     * @return
     */
	int deleteBatchTemp(Map<String, Object> condition);

}