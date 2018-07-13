package com.jiuyescm.bms.report.month.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.biz.storage.entity.BizWmsOutstockPackmaterialEntity;
import com.jiuyescm.bms.report.month.entity.BmsMaterialReportEntity;
import com.jiuyescm.bms.report.month.repository.IBmsMaterialReportRepository;
import com.jiuyescm.bms.report.month.service.IBmsMaterialReportService;

@Service("bmsMaterialReportService")
public class BmsMaterialReportServiceImp implements IBmsMaterialReportService{
	@Autowired
	private IBmsMaterialReportRepository bmsMaterialReportRepository;

	@Override
	public PageInfo<BmsMaterialReportEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bmsMaterialReportRepository.query(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BizOutstockPackmaterialEntity> queryBmsMaterial(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bmsMaterialReportRepository.queryBmsMaterial(condition, pageNo, pageSize);
	}

	@Override
	public PageInfo<BizWmsOutstockPackmaterialEntity> queryWmsMaterial(
			Map<String, Object> condition, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return bmsMaterialReportRepository.queryWmsMaterial(condition, pageNo, pageSize);
	}
}
