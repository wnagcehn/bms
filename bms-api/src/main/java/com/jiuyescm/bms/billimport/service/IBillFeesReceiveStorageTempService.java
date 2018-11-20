package com.jiuyescm.bms.billimport.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillFeesReceiveStorageTempService {

    BillFeesReceiveStorageTempEntity findById(Long id);
	
    PageInfo<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition);

    BillFeesReceiveStorageTempEntity save(BillFeesReceiveStorageTempEntity entity);

    BillFeesReceiveStorageTempEntity update(BillFeesReceiveStorageTempEntity entity);

    void delete(Long id);
  
    /**
     * 批量写入
     * @param list
     * @return
     */
    int insertBatchTemp(List<BillFeesReceiveStorageTempEntity> list);
    
    /**
     * 批量删除
     * @param condition
     * @return
     */
	int deleteBatchTemp(Map<String, Object> condition);
}
