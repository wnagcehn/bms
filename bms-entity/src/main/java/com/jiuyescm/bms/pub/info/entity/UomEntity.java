package com.jiuyescm.bms.pub.info.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 度量单位
 */
public class UomEntity implements IEntity {

	private static final long serialVersionUID = 8168599889215963983L;
	// 度量单位ID
	private String uomid;
	// 度量单位名称
	private String uomname;
	// 度量单位缩写
	private String uomabbvr;
	// 备注
	private String remark;

	public UomEntity() {

	}

	public String getUomid() {
		return this.uomid;
	}

	public void setUomid(String aUomid) {
		this.uomid = aUomid;
	}

	public String getUomname() {
		return this.uomname;
	}

	public void setUomname(String aUomname) {
		this.uomname = aUomname;
	}

	public String getUomabbvr() {
		return this.uomabbvr;
	}

	public void setUomabbvr(String aUomabbvr) {
		this.uomabbvr = aUomabbvr;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String aRemark) {
		this.remark = aRemark;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.info.entity.UomEntity[");
		returnString.append("uomid = " + this.uomid + ";\n");
		returnString.append("uomname = " + this.uomname + ";\n");
		returnString.append("uomabbvr = " + this.uomabbvr + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (uomid == null || "".equals(uomid.trim())) {
			return null;
		}
		return uomid;
	}
}