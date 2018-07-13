/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCountEntityVo;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCustomerCountEntityVo;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;

/**
 * 
 * @author stevenl
 * 
 */
public interface IBmsBillInfoService {

    PageInfo<BmsBillInfoEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);

    BmsBillInfoEntity findById(Long id);

    int save(BmsBillInfoEntity entity);

    int update(BmsBillInfoEntity entity);

    void delete(Long id);
	List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo);
    
    //生成账单
    ResponseVo generReceiveBill(BmsBillInfoEntity entity) throws Exception;
    
    //获取商家 最后一次生成账单 时间
  	BmsBillInfoEntity queryLastBillTime(Map<String, Object> condition);
  	
  	//确认账单
  	ResponseVo confirmFeesBill(BmsBillInfoEntity entity);
  	
  	//删除账单
    ResponseVo deleteReceiveBill(BmsBillInfoEntity entity);
    
    //结账
    ResponseVo sellteBill(BmsBillInfoEntity entity);
    
    //账单统计
    PageInfo<BmsBillCountEntityVo> countBill(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    //商家账单详情
    BmsBillCustomerCountEntityVo queryCustomerVo(Map<String, Object> condition);
    
    /**
     * 根据map条件查询账单
     * @param condition
     * @return
     */
    List<BmsBillInfoEntity> queryBmsBill(Map<String, Object> condition);
}
