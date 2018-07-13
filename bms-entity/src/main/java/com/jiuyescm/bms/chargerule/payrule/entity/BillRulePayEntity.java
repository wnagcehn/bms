/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.chargerule.payrule.entity;

import java.sql.Timestamp;

import com.jiuyescm.bms.chargerule.receiverule.entity.Rule;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillRulePayEntity extends Rule implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 报价名称
	private String quotationName;
	// 业务类型
	private String bizTypeCode;
	// SubjectId
	private String subjectId;
	// remark
	private String remark;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;
	//说明
	private String instruction;

	//新增一属性只做记录，不添加到数据库中
	private String subjectName;
	public BillRulePayEntity() {

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
     * 报价名称
     */
	public String getQuotationName() {
		return this.quotationName;
	}

    /**
     * 报价名称
     *
     * @param quotationName
     */
	public void setQuotationName(String quotationName) {
		this.quotationName = quotationName;
	}
	
	/**
     * 业务类型
     */
	public String getBizTypeCode() {
		return this.bizTypeCode;
	}

    /**
     * 业务类型
     *
     * @param bizTypeCode
     */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}
	
	/**
     * SubjectId
     */
	public String getSubjectId() {
		return this.subjectId;
	}

    /**
     * SubjectId
     *
     * @param subjectId
     */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	/**
     * remark
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * remark
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
     * 创建者
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建者
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 创建时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 修改者
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * 修改时间
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * 删除标志
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	
    
}
