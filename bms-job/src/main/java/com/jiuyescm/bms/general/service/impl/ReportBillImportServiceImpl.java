/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.general.entity.ReportBillImportDetailEntity;
import com.jiuyescm.bms.general.service.IReportBillImportService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@SuppressWarnings("rawtypes")
@Repository("reportBillImportServiceImpl")
public class ReportBillImportServiceImpl extends MyBatisDao implements IReportBillImportService {

	private static final Logger logger = Logger.getLogger(ReportBillImportServiceImpl.class.getName());

	public ReportBillImportServiceImpl(){
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public BillReceiveMasterEntity queryBill(Map<String, Object> map) {
		// TODO Auto-generated method stub
		BillReceiveMasterEntity en=(BillReceiveMasterEntity) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryBill", map);
		return en;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryAllWare(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<String> list=selectList("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryAllWare", map);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal queryStorageNum(Map<String, Object> map) {
		// TODO Auto-generated method stub
		BigDecimal money=(BigDecimal) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryStorageNum", map);
		return money;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal queryStorageAmount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		BigDecimal money=(BigDecimal) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryStorageAmount", map);
		return money;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal queryStorageAdd(Map<String, Object> map) {
		// TODO Auto-generated method stub
		BigDecimal money=(BigDecimal) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryStorageAdd", map);
		return money;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal queryStorageTbBox(Map<String, Object> map) {
		// TODO Auto-generated method stub
		BigDecimal money=(BigDecimal) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryStorageTbBox", map);
		return money;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String,BigDecimal> queryDispatch(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Map<String,BigDecimal> resultMap=new HashMap<>();
		Map<String,Object> result=(Map<String, Object>) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryDispatch", map);
		BigDecimal amount = (BigDecimal)result.get("amount");
		Long count1 = (Long)result.get("count");
		BigDecimal count = new BigDecimal(count1);
		resultMap.put("amount", amount);
		resultMap.put("count", count);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveList(List<ReportBillImportDetailEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.saveList", list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Timestamp getTime() {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String, Object>();
		Timestamp time=(Timestamp) selectOne("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.getTime", map);
		return time;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateEtlTime(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.updateEtlTime", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillReceiveMasterEntity> queryList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<BillReceiveMasterEntity> list=selectList("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.queryList", map);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int updateBill(List<BillReceiveMasterEntity> list) {
		// TODO Auto-generated method stub
		
		return updateBatch("com.jiuyescm.bms.general.mapper.ReportBillImportDetailMapper.updateBillList", list);
	}
	
	
}
