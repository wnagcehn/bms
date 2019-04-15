/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.transport.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IBizGanxianWayBillService {

    PageInfo<BizGanxianWayBillEntity> query(Map<String, Object> condition, int pageNo,
            int pageSize);
    
    BizGanxianWayBillEntity query(Map<String, Object> condition);

    BizGanxianWayBillEntity findById(Long id);

    BizGanxianWayBillEntity save(BizGanxianWayBillEntity entity);

    BizGanxianWayBillEntity update(BizGanxianWayBillEntity entity);
    
    void updateList(List<BizGanxianWayBillEntity> updateList);

    int saveList(List<BizGanxianWayBillEntity> list);

	Properties validRetry(Map<String, Object> param);
	 
	int reCalculate(Map<String, Object> param);
	
	PageInfo<BizGanxianWayBillEntity> queryGroup(Map<String, Object> condition, int pageNo,
	            int pageSize);
	
	List<BizGanxianWayBillEntity> queryDelete(Map<String, Object> condition);
	
	int deleteBatch(List<BizGanxianWayBillEntity> list);
	
	int deleteFees(Map<String, Object> condition);
	
	//同时删除业务数据和费用
	int deleteBizAndFees(Map<String, Object> condition);
	
	 /**
	  * 判断是否有计算异常的数据
	  * @param condition
	  * @return
	  */
	 public BizGanxianWayBillEntity queryExceptionOne(Map<String,Object> condition);

	 
}
