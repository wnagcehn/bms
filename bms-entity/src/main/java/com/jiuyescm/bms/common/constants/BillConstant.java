package com.jiuyescm.bms.common.constants;

/**
 * 账单常量类
 * @author yangss
 *
 */
public class BillConstant {

	public static final String FEES_NOTFOUND_MSG = "未查询到生成账单的费用信息！";
	
	public static final String GENER_BILL_LOCK_MSG = "该商家正在生成账单，请稍后重试!";
	
	public static final String GENER_BILLNO_FAIL_MSG = "生成账单编号失败，请稍后重试!";
	
	public static final String BMS_BILL_SAVE_FAIL_MSG = "新增账单失败!";
	
	public static final String BMS_BILL_NOTFOUND_MSG = "未查询到账单信息!";
	
	public static final String BMS_BILL_SETTLE_FAIL_MSG = "账单当前状态不允许结账!";
	
	public static final String BMS_BILL_INVOCE_NOTFOUND_MSG = "未查询到账单开票信息!";
	
	public static final String BMS_BILL_SUBJECT_SAVE_FAIL_MSG = "新增账单科目费用失败!";
	
	public static final String FEE_STRO_UPDATE_FAIL_MSG = "更新仓储费账单编号失败!";
	
	public static final String FEE_TRAN_UPDATE_FAIL_MSG = "更新运输费账单编号失败！";
	
	public static final String FEE_DISP_UPDATE_FAIL_MSG = "更新配送费账单编号失败！";
	
	public static final String FEE_ABNO_UPDATE_FAIL_MSG = "更新异常费账单编号失败！";
	
	public static final String BILL_SUBJECT_UPDATED_MSG = "账单已更新，请在科目明细中重新计算账单，再确认!";
	
	public static final String BILL_INVOCE_NOTENOUGH_MSG = "账单未开发票金额不足!";
	
	public static final String INVOCE_GT_BILL_AMOUNT_MSG = "开票金额不能大于账单应收金额!";
	
	public static final String BILL_OPENINVOCE_FAIL_MSG = "账单开票失败!";
	
	public static final String RECEIPT_GT_INVOCE_AMOUNT_MSG = "收款金额不能大于开票金额!";
	
	public static final String RECEIPT_GT_BILL_AMOUNT_MSG = "收款金额不能大于账单应收金额!";
	
	
}
