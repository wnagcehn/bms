/**
 * 
 */
package com.jiuyescm.bms.report.bill;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.report.vo.ReportBillBizDetailVo;
import com.jiuyescm.bms.report.vo.ReportBillReceiptDetailVo;
import com.jiuyescm.bms.report.vo.ReportBillStorageDetailVo;

/**
 * @author yangss
 */
public interface IReportBillImportDetailService {
	
	/**
	 * 收入明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportBillReceiptDetailVo> queryReceipt(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 导出收入明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Map<String,Object>> queryReceiptExport(Map<String, Object> condition);
	
	/**
	 * 仓储明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportBillStorageDetailVo> queryStorage(Map<String, Object> condition, int pageNo, int pageSize);

	/**
	 * 业务明细报表
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<ReportBillBizDetailVo> queryBiz(Map<String, Object> condition, int pageNo, int pageSize);

}
