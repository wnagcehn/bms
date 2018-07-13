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
import com.jiuyescm.bms.bill.receive.vo.BmsBillCountEntityVo;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCustomerCountEntityVo;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsBillInfoRepository {

	public PageInfo<BmsBillInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize);

    public BmsBillInfoEntity findById(Long id);

    public int save(BmsBillInfoEntity entity);

    public int update(BmsBillInfoEntity entity);
    
    public int updateByBillNo(BmsBillInfoEntity entity);

    public void delete(Long id);
    
    public BmsBillInfoEntity queryLastBillTime(Map<String, Object> condition);

	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo);
	
	public int deleteFeesBill(BmsBillInfoEntity entity);

	public void updateDiscountStorageBill(BmsBillInfoEntity billInfoEntity);

	public BmsBillInfoEntity queryEntityByBillNo(String billNo);

	public void refeshAmount(BmsBillSubjectInfoEntity bill);

	/**
	 * 账单统计
	 * @param condition
	 * @return
	 */
	 PageInfo<BmsBillCountEntityVo> countBill(Map<String, Object> condition, int pageNo,
	            int pageSize);
	
	  /**
     * 根据map条件查询账单
     * @param condition
     * @return
     */
	List<BmsBillInfoEntity> queryBmsBill(Map<String, Object> condition);
	
	/**
	 * 商家账单详情
	 * @param condition
	 * @return
	 */
	 BmsBillCustomerCountEntityVo queryCustomerVo(Map<String, Object> condition);

}