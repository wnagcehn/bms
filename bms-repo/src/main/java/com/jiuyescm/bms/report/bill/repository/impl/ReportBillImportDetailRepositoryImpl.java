package com.jiuyescm.bms.report.bill.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.bill.ReportBillBizDetailEntity;
import com.jiuyescm.bms.report.bill.ReportBillReceiptDetailEntity;
import com.jiuyescm.bms.report.bill.ReportBillStorageDetailEntity;
import com.jiuyescm.bms.report.bill.repository.IReportBillImportDetailRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@SuppressWarnings("rawtypes")
@Repository("reportBillImportDetailRepository")
public class ReportBillImportDetailRepositoryImpl extends MyBatisDao implements IReportBillImportDetailRepository{

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<ReportBillReceiptDetailEntity> queryReceipt(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<ReportBillReceiptDetailEntity> list = selectList("com.jiuyescm.bms.report.bill.mapper.ReportBillImportDetailMapper.queryReceipt", condition, new RowBounds(pageNo, pageSize));
		PageInfo<ReportBillReceiptDetailEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<ReportBillStorageDetailEntity> queryStorage(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<ReportBillStorageDetailEntity> list = selectList("com.jiuyescm.bms.report.bill.mapper.ReportBillImportDetailMapper.queryStorage", condition, new RowBounds(pageNo, pageSize));
		PageInfo<ReportBillStorageDetailEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo<ReportBillBizDetailEntity> queryBiz(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		List<ReportBillBizDetailEntity> list = selectList("com.jiuyescm.bms.report.bill.mapper.ReportBillImportDetailMapper.queryBiz", condition, new RowBounds(pageNo, pageSize));
		PageInfo<ReportBillBizDetailEntity> pageInfo=new PageInfo<>(list);
		return pageInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryReceiptExport(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = selectList("com.jiuyescm.bms.report.bill.mapper.ReportBillImportDetailMapper.queryReceiptExport", condition);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryStorageExport(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = selectList("com.jiuyescm.bms.report.bill.mapper.ReportBillImportDetailMapper.queryStorageExport", condition);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryBizExport(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = selectList("com.jiuyescm.bms.report.bill.mapper.ReportBillImportDetailMapper.queryBizExport", condition);
		return list;
	}
	
}
