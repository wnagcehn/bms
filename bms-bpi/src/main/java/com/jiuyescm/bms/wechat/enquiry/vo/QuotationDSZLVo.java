package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 航鲜达返回实体vo
 * 
 * @author yangss
 * 
 */
public class QuotationDSZLVo implements Serializable {

	private static final long serialVersionUID = -951953316587837519L;

	// 电商仓库名称
	private String warehouseName;
	// 时效
	private String timeLiness;
	private List<TemperatureCarModelPirceVo> list;

	public QuotationDSZLVo() {
		super();
	}

	public QuotationDSZLVo(String warehouseName, String timeLiness,
			List<TemperatureCarModelPirceVo> list) {
		super();
		this.warehouseName = warehouseName;
		this.timeLiness = timeLiness;
		this.list = list;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getTimeLiness() {
		return timeLiness;
	}

	public void setTimeLiness(String timeLiness) {
		this.timeLiness = timeLiness;
	}

	public List<TemperatureCarModelPirceVo> getList() {
		return list;
	}

	public void setList(List<TemperatureCarModelPirceVo> list) {
		this.list = list;
	}

}
