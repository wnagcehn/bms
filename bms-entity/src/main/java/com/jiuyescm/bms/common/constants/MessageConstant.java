package com.jiuyescm.bms.common.constants;

/**
 * 提示信息 常量
 * @author yangss
 */
public class MessageConstant {
	
	/***********通用 常量信息***************/
	public static final String SESSION_INVALID_MSG = "长时间未操作，用户已失效，请重新登录再试！";
	
	public static final String OPERATOR_SUCCESS_MSG = "操作成功！";
	
	public static final String OPERATOR_FAIL_MSG = "操作失败！";
	
	public static final String SYSTEM_ERROR_MSG = "系统异常，请稍后重试！";
	
	public static final String PAGE_PARAM_ERROR_MSG = "页面传递参数有误！";
	
	public static final String DELETE_INFO_NULL_MSG = "请选择需要删除的记录!";
	
	public static final String DELETE_INFO_SUCCESS_MSG = "删除成功!";
	
	public static final String DELETE_INFO_FAIL_MSG = "删除失败!";
	
	public static final String CUSTOMER_INFO_ISNULL_MSG = "商家信息不能为空！";
	
	public static final String SUBJECTID_NULL_MSG = "费用科目不能为空！";
	
	public static final String QUOTETYPE_NULL_MSG = "报价类型不能为空！";
	
	/***********EXCEL 常量信息***************/
	public static final String EXCEL_FORMAT_ERROR_MSG = "Excel导入格式错误请参考标准模板检查!";
	
	public static final String EXCEL_NULL_MSG = "导入的Excel数据为空或者数据格式不对!";
	
	/***********账单 常量信息***************/
	public static final String BILL_INFO_ISNULL_MSG = "账单信息不能为空！";
	
	public static final String BILL_NO_ISNULL_MSG = "账单编号不能为空！";
	public static final String BILL_DELIVER_ISNULL_MSG = "宅配商ID不能为空！";
	
	public static final String BILL_FILE_ISEXIST_MSG = "该账单已经生成过账单文件,请在[账单下载]页面下载！";
	
	public static final String BILL_MSG = "账单费用";
	
	public static final String BILL_STORAGE_GROUP_MSG = "仓储统计费用";
	
	public static final String BILL_PACKMATERIAL_DETAIL_MSG = "耗材使用明细费用";
	
	public static final String BILL_PRODUCTPALLET_DETAIL_MSG = "商品存储明细费用";
	
	public static final String BILL_PACKPALLET_DETAIL_MSG = "耗材存储明细费用";
	
	public static final String BILL_B2BOUTSTOCK_DETAIL_MSG = "B2B出库明细费用";
	
	public static final String BILL_STORAGE_DETAIL_MSG = "仓储明细费用";
	
	public static final String BILL_TRANSPORT_GROUP_MSG = "运输统计费用";
	
	public static final String BILL_TRANSPORT_DETAIL_MSG = "运输明细费用";
	
	public static final String BILL_DISPATCH_GROUP_MSG = "配送统计费用";
	
	public static final String BILL_DISPATCH_DETAIL_MSG = "配送明细费用";
	
	public static final String BILL_ABNORMAL_GROUP_MSG = "异常统计费用";
	
	public static final String BILL_ABNORMAL_DETAIL_MSG = "异常明细费用";
	
	public static final String PAY_DISPATCH_DISTINCT = "应付宅配对账差异";
	/***********下载任务 常量信息***************/
	public static final String FILE_EXPORT_TASK_RECE_BILL_MSG = "系统正在处理,请稍后在[账单下载]页面下载!";
	
	public static final String FILE_EXPORT_TASKNAME_NULL_MSG = "任务名称不能为空!";
	
	public static final String FILE_EXPORT_FILEPATH_NULL_MSG = "任务路径不能为空!";
	
	public static final String FILE_EXPORT_NOTEXISTS_MSG = "未找到符合条件的可下载文件!";
	
	public static final String FILE_ISEXIST_MSG = "该费用已经生成过费用文件,请在[费用下载]页面下载！";	
	
	public static final String EXPORT_TASK_RECE_BILL_MSG = "系统正在处理,请稍后在[费用下载]页面下载!";
	
	public static final String EXPORT_TASK_BIZ_MSG = ",系统正在处理,请稍后在[数据下载]页面下载!";
	
	public static final String QUERY_PARAM_NULL_MSG = "查询信息不能为空！";
	
	public static final String DISPATCH_FEES_INFO_ISNULL_MSG = "配送费用信息不能为空！";
	
	public static final String STORAGE_FEES_INFO_ISNULL_MSG = "仓储费用信息不能为空！";
	
	public static final String ABNORAML_FEES_INFO_ISNULL_MSG = "异常费用信息不能为空！";
	
	public static final String DELETE_FILE_TASK_FAIL_MSG = "删除下载任务失败!";

	/***********报价 常量信息***************/
	public static final String QUOTE_TYPE_NULL_MSG = "报价类型不能为空！";
	
	public static final String QUOTE_STANDARD_EXIST_MSG = "标准报价已存在，不允许操作！";
	
	public static final String BIZ_BMS_OUTSTOCK_MSG = "BMS原始出库数据";
	
	public static final String BILL_CHECK_MSG = "对账账单信息";
}
