package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;
/**
 * 
 * @author Wuliangfeng
 *
 */
public class BizCustomerImportQueryEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5415087615296799913L;

	//仓库ID
	private String wareHouseId;
	//开始时间
	private Timestamp startTime;
	//结束时间
	private Timestamp endTime;
	//状态
	private int status;
	//商家Id
	private String customerId;
	public String getWareHouseId() {
		return wareHouseId;
	}
	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
