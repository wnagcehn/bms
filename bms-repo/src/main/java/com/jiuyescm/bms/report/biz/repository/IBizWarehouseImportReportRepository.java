/**
 * 
 */
package com.jiuyescm.bms.report.biz.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.biz.entity.BizWarehouseImportReportEntity;
import com.jiuyescm.bms.report.biz.entity.BizWarehouseNotImportEntity;

/**
 * @author yangss
 */
public interface IBizWarehouseImportReportRepository {

	PageInfo<BizWarehouseImportReportEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<BizWarehouseImportReportEntity> queryAll(Map<String, Object> condition);
	
}
