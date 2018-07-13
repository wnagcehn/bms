package com.jiuyescm.bms.base.config;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsWarehouseConfigEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8915767160023924889L;
	private String warehouseCode;
	private int displayLevel;
	private int isDropDisplay;
	private String creator;
	private Timestamp createTime;
	private String lastModifier;
	private Timestamp lastModifyTime;
	public String getWarehouseCode() {
		return warehouseCode;
	}
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	public int getDisplayLevel() {
		return displayLevel;
	}
	public void setDisplayLevel(int displayLevel) {
		this.displayLevel = displayLevel;
	}
	public int getIsDropDisplay() {
		return isDropDisplay;
	}
	public void setIsDropDisplay(int isDropDisplay) {
		this.isDropDisplay = isDropDisplay;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
}
