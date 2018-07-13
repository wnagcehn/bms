package com.jiuyescm.bms.bill.customer.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.vo.BillCustomerInfoVo;

public interface IBillCustomerInfoService {

	PageInfo<BillCustomerInfoVo> queryList(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;

	int insertEntity(BillCustomerInfoVo voEntity) throws Exception;

	int updateEntity(BillCustomerInfoVo voEntity) throws Exception;

	int removeEntity(BillCustomerInfoVo voEntity) throws Exception;
	
	List<BillCustomerInfoVo> queryAll() throws Exception;
	
	int saveBatch(List<BillCustomerInfoVo> list) throws Exception;

	int updateBatch(List<BillCustomerInfoVo> list) throws Exception;

	boolean checkSysCustomerHasBind(String sysCustomerId, String customerId);

	boolean checkCustomerNameExist(String customerName);
}
