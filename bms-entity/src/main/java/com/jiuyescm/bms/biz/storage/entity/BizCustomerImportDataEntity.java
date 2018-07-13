package com.jiuyescm.bms.biz.storage.entity;

import com.jiuyescm.cfm.domain.IEntity;
/**
 * 
 * @author Wuliangfeng
 *
 */
public class BizCustomerImportDataEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3093957591848457792L;
	private String customerName;
	//商品按托存储状态  未签约  已导入  未导入  无需导入
	private String palletStatus;
	//按托存储单量
	private Integer palletCount;
	//耗材库存状态   未签约  已导入  未导入 不需导入
	private String packStorageStatus;
	//耗材库存存储单量
	private Integer packstorageCount;
	//耗材出库 状态  未签约  已导入  未导入  不需导入
	private String packoutstorageStatus;
	//耗材出库导入单量
	private Integer packoutstorageCount;
	//出库单量
	private Integer outCount;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getPalletStatus() {
		return palletStatus;
	}
	public void setPalletStatus(String palletStatus) {
		this.palletStatus = palletStatus;
	}
	public Integer getPalletCount() {
		return palletCount;
	}
	public void setPalletCount(Integer palletCount) {
		this.palletCount = palletCount;
	}
	public String getPackStorageStatus() {
		return packStorageStatus;
	}
	public void setPackStorageStatus(String packStorageStatus) {
		this.packStorageStatus = packStorageStatus;
	}
	public Integer getPackstorageCount() {
		return packstorageCount;
	}
	public void setPackstorageCount(Integer packstorageCount) {
		this.packstorageCount = packstorageCount;
	}
	public String getPackoutstorageStatus() {
		return packoutstorageStatus;
	}
	public void setPackoutstorageStatus(String packoutstorageStatus) {
		this.packoutstorageStatus = packoutstorageStatus;
	}
	public Integer getPackoutstorageCount() {
		return packoutstorageCount;
	}
	public void setPackoutstorageCount(Integer packoutstorageCount) {
		this.packoutstorageCount = packoutstorageCount;
	}
	public Integer getOutCount() {
		return outCount;
	}
	public void setOutCount(Integer outCount) {
		this.outCount = outCount;
	}
	
}
