package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class PubAddvalueUnitEntity implements IEntity {
    
	/**
     * 
     */
    private static final long serialVersionUID = -1575973247291692670L;
    // 自增主键
	private Long id;
	// 二级类目编码
	private String secondSubjectCode;
	// 二级类目名称
	private String secondSubjectName;
	// 单位
	private String unit;
	// 创建人
	private String crePerson;
	// 创建人id
	private String crePersonId;
	// 创建时间
	private Timestamp creTime;
	// 修改人
	private String modPerson;
	// 修改人id
	private String modPersonId;
	// 修改时间
	private Timestamp modTime;
	// 作废标识
	private String delFlag;

	public PubAddvalueUnitEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSecondSubjectCode() {
		return this.secondSubjectCode;
	}

	public void setSecondSubjectCode(String secondSubjectCode) {
		this.secondSubjectCode = secondSubjectCode;
	}
	
	public String getSecondSubjectName() {
		return this.secondSubjectName;
	}

	public void setSecondSubjectName(String secondSubjectName) {
		this.secondSubjectName = secondSubjectName;
	}
	
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
