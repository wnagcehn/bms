package com.jiuyescm.constants;

public class MQConstants {
	
	/**
	 * BMS文件导出
	 */
	public static final String BMS_FILE_EXPORT_QUEUE = "BMS_FILE_EXPORT_QUEUE";
	
	/**
	 * 耗材导入(系统模板)
	 */
	public static final String PACKMATERIALIMPORTBATCH_TASK = "BMS.QUEUE.PACKMATERIALIMPORTBATCH.TASK";
	
	/**
	 * 耗材导入(WMS模板)
	 */
	public static final String PACKMATERIALIMPORTWMS_TASK = "BMS.QUEUE.PACKMATERIALIMPORTWMS.TASK";
	
	/**
	 * 托数管理导入
	 */
	public static final String PALLET_STORAGE_IMPORT_TASK = "BMS.QUEUE.PALLET_STORAGE_IMPORT.TASK";
	
	/**
	 * 宅配运单导出
	 */
	public static final String DISPATCH_BILL_EXPORT = "BMS.QUEUE.DISPATCH_BILL_EXPORT";
	
	/**
	 * 应付运单导出
	 */
	public static final String DISPATCH_BILL_PAY_EXPORT = "BMS.QUEUE.DISPATCH_BILL_PAY_EXPORT";
	
	/**
	 * 耗材明细导出
	 */
	public static final String OUTSTOCK_PACKMATERIAL_EXPORT = "BMS.QUEUE.OUTSTOCK_PACKMATERIAL_EXPORT";
	
	/**
	 * 原始耗材导出
	 */
	public static final String OUTSTOCK_PACKMATERIAL_ORIGIN_EXPORT = "BMS.QUEUE.OUTSTOCK_PACKMATERIAL_ORIGIN_EXPORT";
	
	/**
	 * 预账单导出
	 */
	public static final String BUINESSDATA_EXPORT = "BMS.QUEUE.BUINESSDATA_EXPORT";
	
}
