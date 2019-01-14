/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.reportWarehouse.ReportWarehouseCustomerEntity;
import com.jiuyescm.bms.general.entity.ReportWarehouseBizImportEntity;
import com.jiuyescm.bms.general.service.IReportWarehouseBizImportService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author stevenl
 * 
 */
@Service("reportWarehouseBizImportService")
public class ReportWarehouseBizImportServiceImpl  extends MyBatisDao implements IReportWarehouseBizImportService {

	private static final Logger logger = Logger.getLogger(ReportWarehouseBizImportServiceImpl.class.getName());
	
    @Override
    public int save(ReportWarehouseBizImportEntity entity) {
    	return insert("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.save", entity);
    }

    @Override
    public int update(ReportWarehouseBizImportEntity entity) {
    	 return update("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.update", entity);
    }

	@Override
	public int upsertPalletStorage(Map<String, Object> param) {
		return insert("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.upsertPalletStorage", param);
	}

	@Override
	public int upsertPackMaterial(Map<String, Object> param) {
		return insert("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.upsertPackMaterial", param);
	}

	@Override
	public List<ReportWarehouseCustomerEntity> queryWareList(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.queryWareList", param);
	}

	@Override
	public int updateReport(List<ReportWarehouseCustomerEntity> list) {
		// TODO Auto-generated method stub
		return updateBatch("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.updateReport", list);
	}

	@Override
	public int insertReport(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.insertReport", param);
	}

	@Override
	public int deletetReport(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.deletetReport", param);
	}

	@Override
	public List<ReportWarehouseBizImportEntity> queryImport(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.queryImport", param);
	}

	
}
