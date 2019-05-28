package com.jiuyescm.bms.base.dict.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class PubPackageDictEntity implements IEntity {

    
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    // 自增id
	private Long id;
	// 组合编码
	private String packMark;
	// 季节
	private String season;
	// 配送温区
	private String transportTemperatureType;
	// 运输方式
	private String transportType;
	// 保温时效
	private String holdingTime;
	// 操作分类
	private String packOperateType;
	// 耗材类型
	private String materialType;
	// 耗材类型名称
	private String materialTypeName;
	// 创建人
	private String crePerson;
	// 创建人ID
	private String crePersonId;
	// 创建时间
	private Timestamp creTime;
	// 修改人
	private String modPerson;
	// 修改人ID
	private String modPersonId;
	// 修改时间
	private Timestamp modTime;
	// 作废标识
	private String delFlag;

	public PubPackageDictEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPackMark() {
		return this.packMark;
	}

	public void setPackMark(String packMark) {
		this.packMark = packMark;
	}
	
	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
	public String getTransportTemperatureType() {
		return this.transportTemperatureType;
	}

	public void setTransportTemperatureType(String transportTemperatureType) {
		this.transportTemperatureType = transportTemperatureType;
	}
	
	public String getTransportType() {
		return this.transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	
	public String getHoldingTime() {
		return this.holdingTime;
	}

	public void setHoldingTime(String holdingTime) {
		this.holdingTime = holdingTime;
	}
	
	public String getPackOperateType() {
		return this.packOperateType;
	}

	public void setPackOperateType(String packOperateType) {
		this.packOperateType = packOperateType;
	}
	
	public String getMaterialType() {
		return this.materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}
	
	public String getMaterialTypeName() {
		return this.materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}
	
	public String getCrePerson() {
		return this.crePerson;
	}

	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	public String getCrePersonId() {
		return this.crePersonId;
	}

	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	public String getModPerson() {
		return this.modPerson;
	}

	public void setModPerson(String modPerson) {
		this.modPerson = modPerson;
	}
	
	public String getModPersonId() {
		return this.modPersonId;
	}

	public void setModPersonId(String modPersonId) {
		this.modPersonId = modPersonId;
	}
	
	public Timestamp getModTime() {
		return this.modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
