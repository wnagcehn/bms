package com.jiuyescm.bms.billimport.repository;

import java.util.Map;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;

/**
 * ..Repository
 * @author liuzhicheng
 * 
 */
public interface IBillFeesReceiveAirTempRepository {

    BillFeesReceiveAirTempEntity findById(Long id);
	
	PageInfo<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition);

    BillFeesReceiveAirTempEntity save(BillFeesReceiveAirTempEntity entity);

    BillFeesReceiveAirTempEntity update(BillFeesReceiveAirTempEntity entity);

    void delete(Long id);
    
    /**
     * 批量写入
     * @param list
     * @return
     * @throws Exception
     */
	int insertBatch(List<BillFeesReceiveAirTempEntity> list) throws Exception;
	
	/**
	 * 批量删除
	 * @param condition
	 * @return
	 */
	int deleteBatch(Map<String, Object> condition);

}
