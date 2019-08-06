package com.jiuyescm.bms.report.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.vo.MaterailOutReportVo;
import com.jiuyescm.bms.report.vo.MaterialImportReportVo;
import com.jiuyescm.exception.BizException;

public interface IMaterialReportService {

	/**
	 * 获取耗材导入报表
	 * @param map
	 * @return
	 */
	List<MaterialImportReportVo> materialImportReport(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 * <功能描述>
	 * 耗材出库报表
	 * @author caojianwei
	 * @date 2019年8月6日 上午11:43:15
	 *
	 * @param condition  year-年       month-月    customerId-商家ID   warehouseCode-仓库编码
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws BizException
	 */
	PageInfo<MaterailOutReportVo> query(Map<String, Object> condition, int pageNo,int pageSize) throws BizException;
	
}
