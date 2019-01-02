/**
 * 
 */
package com.jiuyescm.bms.report.biz.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.vo.BizWarehouseImportReportVo;
import com.jiuyescm.bms.report.vo.BizWarehouseNotImportVo;

/**
 * @author yangss&liuzhicheng
 */
public interface IBizWarehouseImportReportService {
	
	PageInfo<BizWarehouseImportReportVo> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<BizWarehouseImportReportVo> queryAll(Map<String, Object> condition);
	
	/**
	 * 查询未导入商家
	 * @param condition
	 * @return
	 */
	PageInfo<BizWarehouseNotImportVo> queryNotImport(Map<String, Object> condition,int pageNo, int pageSize);
	
}
