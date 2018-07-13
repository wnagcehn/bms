package com.jiuyescm.bms.report.month.repository;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.month.entity.MaterialImportReportEntity;


public interface IMaterialReportRepo {

	/**
	 * 对配送运单统计 获取 仓库->商家->单量
	 * @param map
	 * @return
	 */
	List<MaterialImportReportEntity> dispatchNumReport(Map<String, Object> map);
	
	
	/**
	 * 对导入耗材统计 获取 仓库->商家->单量
	 * @param map
	 * @return
	 */
	List<MaterialImportReportEntity> materialNumReport(Map<String, Object> map);
	
}
