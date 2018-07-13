/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.paymanage.payimport;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesPayImportEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 所在月份
	// 所在月份
	private Integer createMonth;
	// 业务类型（仓储，干线，配送 ，总部管理费）
	private String bizType;
	// 仓库编码
	private String warehouseCode;
	// 费用科目
	private String subjectCode;
	// 费用科目名称
	private String subjectName;
	// 金额
	private BigDecimal amount;
	// 审核状态 0未审核  1审核
	private String state;
	// DelFlag
	private String delFlag;
	// 备注
	private String remark;
	// 扩展字段1
	private String extarr1;
	// 扩展字段2
	private String extarr2;
	// 扩展字段3
	private String extarr3;
	// 扩展字段4
	private String extarr4;
	// 扩展字段5
	private String extarr5;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;

	public FeesPayImportEntity() {

	}
	
	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public Integer getCreateMonth() {
		return createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}

	/**
     * 业务类型（仓储，干线，配送 ，总部管理费）
     */
	public String getBizType() {
		return this.bizType;
	}

    /**
     * 业务类型（仓储，干线，配送 ，总部管理费）
     *
     * @param bizType
     */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	/**
     * 仓库编码
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库编码
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * 费用科目
     */
	public String getSubjectCode() {
		return this.subjectCode;
	}

    /**
     * 费用科目
     *
     * @param subjectCode
     */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
     * 费用科目名称
     */
	public String getSubjectName() {
		return this.subjectName;
	}

    /**
     * 费用科目名称
     *
     * @param subjectName
     */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	/**
     * 金额
     */
	public BigDecimal getAmount() {
		return this.amount;
	}

    /**
     * 金额
     *
     * @param amount
     */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
     * DelFlag
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * DelFlag
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 备注
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
     * 扩展字段1
     */
	public String getExtarr1() {
		return this.extarr1;
	}

    /**
     * 扩展字段1
     *
     * @param extarr1
     */
	public void setExtarr1(String extarr1) {
		this.extarr1 = extarr1;
	}
	
	/**
     * 扩展字段2
     */
	public String getExtarr2() {
		return this.extarr2;
	}

    /**
     * 扩展字段2
     *
     * @param extarr2
     */
	public void setExtarr2(String extarr2) {
		this.extarr2 = extarr2;
	}
	
	/**
     * 扩展字段3
     */
	public String getExtarr3() {
		return this.extarr3;
	}

    /**
     * 扩展字段3
     *
     * @param extarr3
     */
	public void setExtarr3(String extarr3) {
		this.extarr3 = extarr3;
	}
	
	/**
     * 扩展字段4
     */
	public String getExtarr4() {
		return this.extarr4;
	}

    /**
     * 扩展字段4
     *
     * @param extarr4
     */
	public void setExtarr4(String extarr4) {
		this.extarr4 = extarr4;
	}
	
	/**
     * 扩展字段5
     */
	public String getExtarr5() {
		return this.extarr5;
	}

    /**
     * 扩展字段5
     *
     * @param extarr5
     */
	public void setExtarr5(String extarr5) {
		this.extarr5 = extarr5;
	}
	
	/**
     * creator
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * creator
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * CreateTime
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * CreateTime
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * LastModifier
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * LastModifier
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * LastModifyTime
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * LastModifyTime
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
}
