/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.general.entity.ReportBillImportDetailEntity;


/**
 * 
 * @author stevenl
 * o
 */
public interface IReportBillImportService {
	
	
	/**
	 * 根据时间查询最早成功的账单
	 */
	BillReceiveMasterEntity queryBill(Map<String,Object> map);
	
	List<BillReceiveMasterEntity> queryList(Map<String,Object> map);
	
	/**
	 * 更新账单跟踪
	 * 
	 */
	int updateBill(List<BillReceiveMasterEntity> list);
	
	/**
	 * 统计出所有的仓库
	 */
	List<String> queryAllWare(Map<String,Object> map);
	
	/**
	 * 统计数量
	 * @param map
	 * @return
	 */
	BigDecimal  queryStorageNum(Map<String,Object> map);
	
	/**
	 * 统计金额
	 * @param map
	 * @return
	 */
	BigDecimal  queryStorageAmount(Map<String,Object> map);
	
	/**
	 * 仓储增值费
	 * @param map
	 * @return
	 */
	BigDecimal 	queryStorageAdd(Map<String,Object> map);
	
	/**
	 * 仓储Tb箱数
	 * @param map
	 * @return
	 */
	BigDecimal  queryStorageTbBox(Map<String,Object> map);
	
	/**
	 * 配送金额单量
	 */
	Map<String,BigDecimal> queryDispatch(Map<String,Object> map);
	
	/**
	 * 批量保存
	 * @param list
	 * @return
	 */
	int saveList(List<ReportBillImportDetailEntity> list);
	
	/**
	 * 获取增量时间
	 * @return
	 */
	Timestamp getTime();
	
	/**
	 * 更新增量时间
	 * @param map
	 * @return
	 */
	int updateEtlTime(Map<String,Object> map);
}
