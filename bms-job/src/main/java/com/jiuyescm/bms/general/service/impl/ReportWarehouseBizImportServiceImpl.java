/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

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
	public int updateReport(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.general.mapper.ReportWarehouseBizImportMapper.updateReport",param);
	}
	
}
