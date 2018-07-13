package com.jiuyescm.bms.report.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.report.vo.MaterialImportReportVo;

public interface IMaterialReportService {

	/**
	 * 获取耗材导入报表
	 * @param map
	 * @return
	 */
	List<MaterialImportReportVo> materialImportReport(Map<String, Object> map) throws Exception;;
	
}
