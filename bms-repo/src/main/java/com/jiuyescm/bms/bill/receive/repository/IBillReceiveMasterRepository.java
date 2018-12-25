package com.jiuyescm.bms.bill.receive.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.billcheck.BillReceiveExpectEntity;
import com.jiuyescm.bms.billcheck.ReportBillImportMasterEntity;

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

    int update(BillReceiveMasterEntity entity);

	void delete(String billNo);
	
	/**
	 * 保存预估金额
	 */
	int saveExpect(BillReceiveExpectEntity entity);

	BillReceiveExpectEntity queryExpect(Map<String, Object> condition);
	
	Double getAbnormalMoney(String billNo);
	
	/**
	 * 获取耗材托数、耗材存储费
	 * @return
	 */
	Map<String,BigDecimal> queryMaterial(String billNo);
	
	/**
	 * 获取商品托数、商品存储数
	 * @return
	 */
	Map<String,BigDecimal> queryProduct(String billNo);
	
	/**
	 * 获取仓租费
	 * @param map
	 * @return
	 */
	Double queryStorageRent(String billNo);
	
	int insertReportMaster(ReportBillImportMasterEntity vo);
}
