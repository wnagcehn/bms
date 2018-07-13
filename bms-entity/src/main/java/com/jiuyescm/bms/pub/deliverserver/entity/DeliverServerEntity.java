package com.jiuyescm.bms.pub.deliverserver.entity;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 宅配商服务配置
 */
public class DeliverServerEntity implements IEntity {

	private static final long serialVersionUID = 2765039881378285618L;
	// 主键
	private String id;
	// 宅配商ID
	private String deliverid;
	// 宅配商Code
	private String delivercode;
	// 状态
	private int state;
	// 接口地址
	private String receiveurl;
	// mkeys
	private String mkeys;
	// 备注
	private String remark;

	public DeliverServerEntity() {

	}

	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getDeliverid() {
		return deliverid;
	}



	public void setDeliverid(String deliverid) {
		this.deliverid = deliverid;
	}



	public String getDelivercode() {
		return delivercode;
	}



	public void setDelivercode(String delivercode) {
		this.delivercode = delivercode;
	}



	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}



	public String getReceiveurl() {
		return receiveurl;
	}



	public void setReceiveurl(String receiveurl) {
		this.receiveurl = receiveurl;
	}



	public String getMkeys() {
		return mkeys;
	}



	public void setMkeys(String mkeys) {
		this.mkeys = mkeys;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.deliverserver.entity.DeliverServerEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("deliverid = " + this.deliverid + ";\n");
		returnString.append("delivercode = " + this.delivercode + ";\n");
		returnString.append("state = " + this.state + ";\n");
		returnString.append("receiveurl = " + this.receiveurl + ";\n");
		returnString.append("mkeys = " + this.mkeys + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (id == null || "".equals(id.trim())) {
			return null;
		}
		return id;
	}
}