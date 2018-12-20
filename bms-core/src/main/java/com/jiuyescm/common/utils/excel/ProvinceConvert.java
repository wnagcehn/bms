package com.jiuyescm.common.utils.excel;

public class ProvinceConvert {
	/**
	 * 省地址转换
	 * @param province
	 * @return
	 */
	public static String replaceProvince(String province){ 
		if ("北京市".equals(province)) return "北京";
		if ("上海市".equals(province)) return "上海";
		if ("天津市".equals(province)) return "天津";
		if ("重庆市".equals(province)) return "重庆";
		if (province.startsWith("广西") || province.startsWith("广西省")) return "广西壮族自治区";
		if (province.startsWith("湖北")) return "湖北省";
		if (province.startsWith("湖南")) return "湖南省";
		if (province.startsWith("吉林")) return "吉林省";
		if (province.startsWith("江苏")) return "江苏省";
		if (province.startsWith("江西")) return "江西省";
		if (province.startsWith("辽宁")) return "辽宁省";
		if (province.startsWith("内蒙古")) return "内蒙古自治区";
		if (province.startsWith("宁夏")) return "宁夏回族自治区";
		if (province.startsWith("青海")) return "青海省";
		if (province.startsWith("山东")) return "山东省";
		if (province.startsWith("山西")) return "山西省";
		if (province.startsWith("陕西")) return "陕西省";
		if (province.startsWith("四川")) return "四川省";
		if (province.startsWith("西藏")) return "西藏自治区";
		if (province.startsWith("新疆")) return "新疆维吾尔自治区";
		if (province.startsWith("云南")) return "云南省";
		if (province.startsWith("浙江")) return "浙江省";
		if (province.startsWith("安徽")) return "安徽省";
		if (province.startsWith("福建")) return "福建省";
		if (province.startsWith("甘肃")) return "甘肃省";
		if (province.startsWith("广东")) return "广东省";
		if (province.startsWith("贵州")) return "贵州省";
		if (province.startsWith("河北")) return "河北省";
		if (province.startsWith("河南")) return "河南省";
		if (province.startsWith("河南")) return "河南省";
		if (province.startsWith("海南")) return "海南省";
		if (province.startsWith("黑龙江")) return "黑龙江省";
		return province;
	}
}
