package com.jiuyescm.bms.report.month.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.report.month.entity.MaterialImportReportEntity;
import com.jiuyescm.bms.report.month.repository.IMaterialReportRepo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("materialReportRepo")
public class MaterialReportRepoImpl extends MyBatisDao<MaterialImportReportEntity> implements IMaterialReportRepo {


	@Override
	public List<MaterialImportReportEntity> dispatchNumReport(Map<String, Object> map) {
		List<MaterialImportReportEntity> list = selectList("com.jiuyescm.report.month.mapper.MaterialReportMapper.dispatchNumReport",map);
		return list;
	}

	@Override
	public List<MaterialImportReportEntity> materialNumReport(Map<String, Object> map) {
		List<MaterialImportReportEntity> list = selectList("com.jiuyescm.report.month.mapper.MaterialReportMapper.materialNumReport",map);
		return list;
	}

}
