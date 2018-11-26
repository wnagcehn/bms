package com.jiuyescm.bms.billimport.repository;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;

/**
 * ..Repository
 * @author liuzhicheng
 * 
 */
public interface IBillFeesReceiveTransportTempRepository {

    BillFeesReceiveTransportTempEntity findById(Long id);
	
	PageInfo<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition);

    BillFeesReceiveTransportTempEntity save(BillFeesReceiveTransportTempEntity entity);

    BillFeesReceiveTransportTempEntity update(BillFeesReceiveTransportTempEntity entity);

    void delete(Long id);
    
    /**
     * 批量写入
     * @param list
     * @return
     * @throws Exception
     */
	int insertBatch(List<BillFeesReceiveTransportTempEntity> list) throws Exception;
	
	/**
	 * 批量删除
	 * @param condition
	 * @return
	 */
	int deleteBatch(Map<String, Object> condition);

}
