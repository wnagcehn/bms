package com.jiuyescm.bms.common.enumtype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件导出任务类型枚举
 * @author yangss
 */
public enum FileTaskTypeEnum {
	
	REC_BILL("recbill", "应收账单"),
	PAY_BILL("paybill", "应付宅配账单"),
	PAY_TRANS_BILL("paytranbill", "应付运输账单"),
	FEE_STORAGE("storage", "仓储费"),
	FEE_PAY_STORAGE("pay_storage","应付仓储费"),
	FEE_TRANSPORT("transport", "运输费"),
	FEE_REC_DISPATCH("recdispatch", "应收配送费"),
	FEE_PAY_DISPATCH("paydispatch", "应付配送费"),
	FEE_ABNORMAL("abnormal", "异常费"),
	BIZ_PRODUCT("product", "商品库存"),
	BIZ_PRODUCT_PALLET("pro_pallet", "商品按托库存"),
	BIZ_INSTOCK_INFO("instock_info", "入库单信息"),
	BIZ_PACK("pack", "耗材库存"),
	BIZ_PRO_OUTSTOCK("pro_outstock", "商品出库单"),
	BIZ_PACK_OUTSTOCK("pack_outstock", "耗材出库明细"),
	BIZ_PACKAGE_OUTSTOCK("package_outstock", "标准包装方案"),
	BIZ_TRANSPORT("transport", "干线"),
	BIZ_INSTOCK("instock", "入库单"),
	BIZ_INSTOCK_HANDWORK("instockhandwork", "入库卸货单"),
	BIZ_OUTSTOCK("outstock","出库数据"),
	BIZ_WAYBILL("waybill", "干线运单"),
	BIZ_ROADBILL("roadbill", "干线路单"),
	BIZ_REC_DIS("recdis", "应收运单"),
	BIZ_PAY_DIS("paydis", "应付运单"),
	BIZ_PAY_DIS_ORIGIN_DATA("origin_paydis", "原始耗材出库明细"),
	BIZ_TIHUO("tihuo", "配送提货"),
	BILL_REC_DISPATCH("billrecdispatch","账单应收配送费"),
	CHECK_RECEIPT("check_receipt","回款追踪报表"),
	BILL_OTHER_STORAGE("other_storage","仓储其它费用"),
	BILL_ABNORMAL_STORAGE("abnormal_storage","仓储异常费用"),
	BILL_ABNORMAL_DISPATCH("abnormal_dispatch","配送异常费用"),
	BILL_PAY_DISPATCH_DISTINCT("pay_dispatch_distinct","配送异常费用"),
	BILL_RE_DOWN("bill_re_down","账单预下载"),
	Calcu_Error("calcu_error", "计费异常数据下载"),
	BIZ_BMS_OUTSTOCK("biz_bms_outstock","BMS原始出库数据"),
	FEES_CLAIM("fees_claim","理赔费用"),
	MATERIAL_OUT("material_out", "耗材出库统计"),
	BIZ_WMS_OUTSTOCK("biz_wms_outstock","WMS原始结算数据");
	private String code;
	private String desc;
	
	private FileTaskTypeEnum(String code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}
	
	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}
	
	private static Map<String,String> maps = new LinkedHashMap<String,String>();
	static{
		maps.put(REC_BILL.getCode(), REC_BILL.getDesc());
		maps.put(PAY_BILL.getCode(), PAY_BILL.getDesc());
		maps.put(FEE_STORAGE.getCode(), FEE_STORAGE.getDesc());
		maps.put(FEE_TRANSPORT.getCode(), FEE_TRANSPORT.getDesc());
		maps.put(FEE_REC_DISPATCH.getCode(), FEE_REC_DISPATCH.getDesc());
		maps.put(FEE_PAY_DISPATCH.getCode(), FEE_PAY_DISPATCH.getDesc());
		maps.put(FEE_ABNORMAL.getCode(), FEE_ABNORMAL.getDesc());
		maps.put(BIZ_PRODUCT.getCode(), BIZ_PRODUCT.getDesc());
		maps.put(BIZ_PRODUCT_PALLET.getCode(), BIZ_PRODUCT_PALLET.getDesc());
		maps.put(BIZ_PACK.getCode(), BIZ_PACK.getDesc());
		maps.put(BIZ_PRO_OUTSTOCK.getCode(), BIZ_PRO_OUTSTOCK.getDesc());
		maps.put(BIZ_PACK_OUTSTOCK.getCode(), BIZ_PACK_OUTSTOCK.getDesc());
		maps.put(BIZ_PACKAGE_OUTSTOCK.getCode(), BIZ_PACKAGE_OUTSTOCK.getDesc());
		maps.put(BIZ_INSTOCK.getCode(), BIZ_INSTOCK.getDesc());
		maps.put(BIZ_OUTSTOCK.getCode(), BIZ_OUTSTOCK.getDesc());
		maps.put(BIZ_WAYBILL.getCode(), BIZ_WAYBILL.getDesc());
		maps.put(BIZ_ROADBILL.getCode(), BIZ_ROADBILL.getDesc());
		maps.put(BIZ_REC_DIS.getCode(), BIZ_REC_DIS.getDesc());
		maps.put(BIZ_PAY_DIS.getCode(), BIZ_PAY_DIS.getDesc());
		maps.put(BIZ_TIHUO.getCode(), BIZ_TIHUO.getDesc());
	    maps.put(MATERIAL_OUT.getCode(), MATERIAL_OUT.getDesc());	
	}
	
	public static Map<String,String> getMap(){
		return maps;
	}
	
	public static String getDesc(String code)
	{
		if (maps.containsKey(code))
		{
			return maps.get(code);
		}
		return null;
	}
	
}
