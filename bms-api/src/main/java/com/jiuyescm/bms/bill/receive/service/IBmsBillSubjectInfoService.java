/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsBillSubjectInfoService {

    PageInfo<BmsBillSubjectInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BmsBillSubjectInfoEntity findById(Long id);

    BmsBillSubjectInfoEntity save(BmsBillSubjectInfoEntity entity);

    BmsBillSubjectInfoEntity update(BmsBillSubjectInfoEntity entity);
    
    int updateBillSubject(Map<String, Object> condition);

    void delete(Long id);

	void discountBill(BmsBillSubjectInfoEntity bill) throws Exception;

	List<BmsBillSubjectInfoEntity> queryAllByBillNoAndwarehouse(
			Map<String, Object> parameter);

	List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo,
			String feesType);

	List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo);

	PageInfo<FeesAbnormalEntity> queryAbnormalDetailGroupPage(
			Map<String, Object> parameter, int pageNo, int pageSize);

	void deleteStorageBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountStorageDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountStorageUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void deleteTransportBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountTransportDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountTransportUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception;
	
	void deleteDispatchBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountDispatchDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountDispatchUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void deleteAbnormalBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountAbnormalUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception;

	void reCountAbnormalDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception;
	
	List<String>  queryBillWarehouse(Map<String,Object> param);
	
	void export(BmsBillInfoEntity paramer, String taskId)throws Exception;

	/**
	 * 批量更新账单费用科目明细
	 */
	int updateSubjectList(List<BmsBillSubjectInfoEntity> condition);


	void updateAbnormalTransportBillSubject(Map<String, Object> conditionMap);

	void updateTransportBillSubject(Map<String, Object> conditionMap);
}
