package com.jiuyescm.bms.bill.receive.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillReceiveMasterRepository {

    BillReceiveMasterEntity findById(Long id);
	
	PageInfo<BillReceiveMasterEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillReceiveMasterEntity> query(Map<String, Object> condition);

    int save(BillReceiveMasterEntity entity);

    BillReceiveMasterEntity update(BillReceiveMasterEntity entity);

    void delete(Long id);

}
