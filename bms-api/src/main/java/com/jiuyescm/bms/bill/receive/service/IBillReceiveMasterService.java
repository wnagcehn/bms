package com.jiuyescm.bms.bill.receive.service;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillReceiveMasterService {

    BillReceiveMasterEntity findById(Long id);
	
    PageInfo<BillReceiveMasterEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillReceiveMasterEntity> query(Map<String, Object> condition);

    int save(BillReceiveMasterEntity entity);

    BillReceiveMasterEntity update(BillReceiveMasterEntity entity);

    void delete(Long id);

}
