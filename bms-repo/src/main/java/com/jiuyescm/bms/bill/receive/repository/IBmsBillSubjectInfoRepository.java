/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsBillSubjectInfoRepository {

	public PageInfo<BmsBillSubjectInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BmsBillSubjectInfoEntity findById(Long id);

    public BmsBillSubjectInfoEntity save(BmsBillSubjectInfoEntity entity);
    
    public int saveBillSubjectList(List<BmsBillSubjectInfoEntity> list);

    public BmsBillSubjectInfoEntity update(BmsBillSubjectInfoEntity entity);
    
    public int updateBillSubject(Map<String, Object> condition);
    
    public int deleteFeesBill(BmsBillInfoEntity entity);

    public void delete(Long id);

	public void updateDiscountStorageBill(BmsBillSubjectInfoEntity bill);

	public List<BmsBillSubjectInfoEntity> queryAllByBillNoAndwarehouse(
			Map<String, Object> parameter);

	List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo,
		String feesType);

	//查询账单科目状态
	public List<BmsBillSubjectInfoEntity> queryBillSubjectStatus(Map<String, Object> parameter);
	
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo);

	public void deleteStorageBill(BmsBillSubjectInfoEntity bill);

	public void updateStatus(String billNo, String warehouseCode,
			String subjectCode, String status);

	public void deleteSubjectBill(BmsBillSubjectInfoEntity bill);
	
	BmsBillSubjectInfoEntity queryTransportSum(Map<String,Object> param);
	
	//根据map条件更新减免费用
	public int updateDerateFee(Map<String, Object> condition);
	
	/**
	 * 批量更新账单费用科目明细
	 */
	int updateSubjectList(List<BmsBillSubjectInfoEntity> condition);

	public void updateAbnormalTransportBillSubject(
			Map<String, Object> conditionMap);

	public void updateTransportBillSubject(Map<String, Object> conditionMap);
}
