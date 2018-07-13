package com.jiuyescm.bms.bill.customer.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerInfoEntity;

public interface IBillCustomerInfoRepository {

	PageInfo<BillCustomerInfoEntity> queryList(Map<String, Object> condition,
            int pageNo, int pageSize);

	int insertEntity(BillCustomerInfoEntity entity);

	int updateEntity(BillCustomerInfoEntity entity);
	
	int deleteEntity(BillCustomerInfoEntity entity);

	List<BillCustomerInfoEntity> queryAll();

	int saveBatch(List<BillCustomerInfoEntity> list);

	int updateBatch(List<BillCustomerInfoEntity> list);

	boolean checkSysCustomerHasBind(String sysCustomerId, String customerId);

	boolean checkCustomerNameExist(String customerName);
}
