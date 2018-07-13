package com.jiuyescm.bms.base.dictionary.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class SystemCodeEntity implements IEntity {

	// id
	private Long id;
	// timeflag
	private Timestamp timeflag;
	// 类型编码
	private String typeCode;
	// 名称
	private String codeName;
	// 编码
	private String code;
	// 描述
	private String codeDesc;
	// 状态
	private String status;
	// 创建人ID
	private String createId;
	// 创建时间
	private Timestamp createDt;
	// 修改人ID
	private String updateId;
	// 修改时间
	private Timestamp updateDt;
	// 序号
	private Long sortNo;
	// 扩展字段1
	private String extattr1;
	// 扩展字段2
	private String extattr2;
	// 扩展字段3
	private String extattr3;
	// 扩展字段4
	private String extattr4;
	// 扩展字段5
	private String extattr5;
	// 删除人
	private String deleteId;
	// 删除时间
	private Timestamp deleteDt;
	
	private String contractNo;//合同号

	public SystemCodeEntity() {

	}
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * timeflag
     */
	public Timestamp getTimeflag() {
		return this.timeflag;
	}

    /**
     * timeflag
     *
     * @param timeflag
     */
	public void setTimeflag(Timestamp timeflag) {
		this.timeflag = timeflag;
	}
	
	/**
     * 类型编码
     */
	public String getTypeCode() {
		return this.typeCode;
	}

    /**
     * 类型编码
     *
     * @param typeCode
     */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	/**
     * 名称
     */
	public String getCodeName() {
		return this.codeName;
	}

    /**
     * 名称
     *
     * @param codeName
     */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	/**
     * 编码
     */
	public String getCode() {
		return this.code;
	}

    /**
     * 编码
     *
     * @param code
     */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
     * 描述
     */
	public String getCodeDesc() {
		return this.codeDesc;
	}

    /**
     * 描述
     *
     * @param codeDesc
     */
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	
	/**
     * 状态
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态
     *
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
     * 创建人ID
     */
	public String getCreateId() {
		return this.createId;
	}

    /**
     * 创建人ID
     *
     * @param createrId
     */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCreateDt() {
		return this.createDt;
	}

    /**
     * 创建时间
     *
     * @param createrDt
     */
	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}
	
	/**
     * 修改人ID
     */
	public String getUpdateId() {
		return this.updateId;
	}

    /**
     * 修改人ID
     *
     * @param updateId
     */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getUpdateDt() {
		return this.updateDt;
	}

    /**
     * 修改时间
     *
     * @param updateDt
     */
	public void setUpdateDt(Timestamp updateDt) {
		this.updateDt = updateDt;
	}
	
	/**
     * 序号
     */
	public Long getSortNo() {
		return this.sortNo;
	}

    /**
     * 序号
     *
     * @param sortNo
     */
	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}
	
	/**
     * 扩展字段1
     */
	public String getExtattr1() {
		return this.extattr1;
	}

    /**
     * 扩展字段1
     *
     * @param extattr1
     */
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	/**
     * 扩展字段2
     */
	public String getExtattr2() {
		return this.extattr2;
	}

    /**
     * 扩展字段2
     *
     * @param extattr2
     */
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	/**
     * 扩展字段3
     */
	public String getExtattr3() {
		return this.extattr3;
	}

    /**
     * 扩展字段3
     *
     * @param extattr3
     */
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	/**
     * 扩展字段4
     */
	public String getExtattr4() {
		return this.extattr4;
	}

    /**
     * 扩展字段4
     *
     * @param extattr4
     */
	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	
	/**
     * 扩展字段5
     */
	public String getExtattr5() {
		return this.extattr5;
	}

    /**
     * 扩展字段5
     *
     * @param extattr5
     */
	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
	/**
     * 删除人
     */
	public String getDeleteId() {
		return this.deleteId;
	}

    /**
     * 删除人
     *
     * @param deleteId
     */
	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}
	
	/**
     * 删除时间
     */
	public Timestamp getDeleteDt() {
		return this.deleteDt;
	}

    /**
     * 删除时间
     *
     * @param deleteDt
     */
	public void setDeleteDt(Timestamp deleteDt) {
		this.deleteDt = deleteDt;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	
	
	
    
}
