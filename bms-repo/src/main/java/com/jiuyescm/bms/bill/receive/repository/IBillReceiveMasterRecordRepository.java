package com.jiuyescm.bms.bill.receive.repository;

import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillReceiveMasterRecordRepository {

    BillReceiveMasterRecordEntity findById(Long id);
	
	PageInfo<BillReceiveMasterRecordEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillReceiveMasterRecordEntity> query(Map<String, Object> condition);

    int save(BillReceiveMasterRecordEntity entity);

    BillReceiveMasterRecordEntity update(BillReceiveMasterRecordEntity entity);

    void delete(Long id);

}
