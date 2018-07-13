package com.jiuyescm.bms.base.ewarehouse.entity;

public class NewEWareHouseEntity {
	//id
	private int wareid;
	//电商仓库编号
	private String eWareHouseId;
	//仓库名称
	private String eWareHouseName;
	//电商名称
	private String eleBizName;
	//省
	private String province;
	//市
	private String city;
	//区
	private String county;
	//详细地址
	private String detailAddress;
	//备注
	private String remark;
	public String geteWareHouseId() {
		return eWareHouseId;
	}
	public void seteWareHouseId(String eWareHouseId) {
		this.eWareHouseId = eWareHouseId;
	}
	public String geteWareHouseName() {
		return eWareHouseName;
	}
	public void seteWareHouseName(String eWareHouseName) {
		this.eWareHouseName = eWareHouseName;
	}
	public String getEleBizName() {
		return eleBizName;
	}
	public void setEleBizName(String eleBizName) {
		this.eleBizName = eleBizName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getWareid() {
		return wareid;
	}
	public void setWareid(int wareid) {
		this.wareid = wareid;
	}
	
	
	
}
