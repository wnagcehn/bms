package com.jiuyescm.bms.systemCode.service.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class SystemCodeVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8914297335219187004L;
	private String typeCode;
	private String codeName;
	private String code;
	private String codeDesc;
	private String status;
	private Long sortNo;
	private String extattr1;
	private String extattr2;
	private String extattr3;
	private String extattr4;
	private String extattr5;
	private String createId;
	private Timestamp createDt;
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getSortNo() {
		return sortNo;
	}
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}
	public String getExtattr1() {
		return extattr1;
	}
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	public String getExtattr2() {
		return extattr2;
	}
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	public String getExtattr3() {
		return extattr3;
	}
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	public String getExtattr4() {
		return extattr4;
	}
	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	public String getExtattr5() {
		return extattr5;
	}
	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public Timestamp getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	
}
