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
 * @author liuzhicheng
 */
public interface IBizWarehouseNotImportRepository {
	
	PageInfo<BizWarehouseNotImportEntity> queryNotImport(Map<String, Object> condition,int pageNo, int pageSize);
	
}
