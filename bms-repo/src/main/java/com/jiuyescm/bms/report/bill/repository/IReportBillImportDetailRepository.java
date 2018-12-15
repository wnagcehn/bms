package com.jiuyescm.bms.report.bill.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.bill.ReportBillBizDetailEntity;
import com.jiuyescm.bms.report.bill.ReportBillReceiptDetailEntity;
import com.jiuyescm.bms.report.bill.ReportBillStorageDetailEntity;

public interface IReportBillImportDetailRepository {
	/**
	 * 收入明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportBillReceiptDetailEntity> queryReceipt(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 仓储明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportBillStorageDetailEntity> queryStorage(Map<String, Object> condition, int pageNo, int pageSize);

	/**
	 * 业务明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportBillBizDetailEntity> queryBiz(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 导出收入明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Map<String,Object>> queryReceiptExport(Map<String, Object> condition);

}
