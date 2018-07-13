package com.jiuyescm.bms.common.constants;

/**
 * 运输相关的常量
 * @author yangss
 */
public class TransportMessageConstant {

	public static final String FROMPROVINCE_NULL_MSG = "始发省份不能为空!";
	public static final String FROMCITY_NULL_MSG = "始发城市不能为空!";
	public static final String FROMDISTRICT_NULL_MSG = "始发区不能为空!";
	
	public static final String FROM_PC_MATCH_ERR_MSG = "始发省、市没有在系统中维护!";
	
	public static final String FROMWAREHOUSE_NULL_MSG = "始发仓不能为空!";
	
	public static final String WAREHOUSE_ERROR_MSG = "仓库名没有在仓库表中维护!";
	
	public static final String TOPROVINCE_NULL_MSG = "目的省份不能为空!";
	public static final String TOCITY_NULL_MSG = "目的城市不能为空!";
	public static final String TODISTRICT_NULL_MSG = "目的区不能为空!";
	
	public static final String TO_PC_MATCH_ERR_MSG = "目的省、市没有在系统中维护!";
	
	public static final String STARTSTATION_NULL_MSG = "始发站点不能为空!";
	
	public static final String ENDSTATION_NULL_MSG = "目的站点不能为空!";
	
	public static final String TRANSPORTTYPE_NULL_MSG = "业务类型不能为空!";
	public static final String TRANSPORTTYPE_ERROR_MSG = "业务类型没有在系统中维护!";
	
	
	public static final String ORDERNO_NULL_MSG = "订单号不能为空!";
	
	public static final String SENDTIME_NULL_MSG = "发货日期不能为空!";
	
	public static final String CARMODEL_NULL_MSG = "车型不能为空!";
	public static final String CARMODEL_ERROR_MSG = "车型没有在系统中维护!";
	
	public static final String ISLIGHT_NULL_MSG = "是否泡货不能为空!";
	public static final String ISLIGHT_ERROR_MSG = "是否泡货类型没有在系统中维护!";
	
	public static final String WEIGHT_NULL_MSG = "重量不能为空!";
	public static final String WEIGHT_GT0_MSG = "重量要大于0!";
	
	public static final String WEIGHT_NULL_GT0_MSG = "重量不能为空,且要大于0!";
	
	public static final String VOLUME_NULL_MSG = "体积不能为空!";
	public static final String VOLUME_GT0_MSG = "体积要大于0!";
	
	public static final String WEIGHT_VOLUME_GT0_MSG = "重量或体积不能为空,且要大于0!";
	
	public static final String WAYBILL_SAVE_FAIL_MSG = "干线运单业务数据写入失败!";
	
	public static final String LINEID_NULL_MSG = "请先选择一条运输路线!";
	
	public static final String LINEID_NOTFIND_MSG = "未查询到该运输路线!";
	
	public static final String LINE_TRANSPORTTYPE_NULL_MSG = "运输路线业务类型不能为空!";
	
	public static final String UNITPRICE_NULL_GT0_MSG = "单价不能为空,且要大于0!";
	
	public static final String WEIGHT_UPPER_MSG = "重量上限不能小于下限!";
	
	/***********运输 RPC信息***************/
	public static final String BIZ_NULL_MSG = "业务数据不能为空!";
	
	public static final String BIZ_DATA_EXIST_MSG = "业务数据已存在!";
	
	public static final String BIZ_DATA_NOT_EXIST_MSG = "业务数据不存在!";
	
	public static final String BIZ_FEESNO_NOT_EXIST_MSG = "业务数据暂未生成费用!";
	
	public static final String FEES_BILLNO_EXIST_MSG = "账单已生成,不允许操作!";
	
	public static final String TMS_ID_NULL_MSG = "tmsId不能为空!";
	
	public static final String CHARGETYPE_NULL_MSG = "计费类型不能为空!";
	
	public static final String CHARGETYPE_ERR_MSG = "计费类型没有在系统中维护!";
	
	public static final String INTERFACETYPE_NULL_MSG = "接口类型不能为空!";
	
	public static final String INTERFACETYPE_ERR_MSG = "接口类型没有在系统中维护!";
	
	public static final String BIZ_CANCEL_FAIL_MSG = "作废业务数据失败!";
	
	public static final String PIRCE_LINE_NOTFOUND_MSG = "未查询到报价!";
	
}
