/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.base.dictionary.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BmsSubjectInfoEntity implements IEntity {

   
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 4704309441292350302L;
	
	private Long id;
	// 收支类型（IN-收入 OUT-支出）
	private String inOutTypecode;
	// 业务类型（仓，干，配）
	private String bizTypecode;
	// 费用类型 BASE-基础费 ADD-增值费 APP-分摊费 OTHER-其他费
	private String feesType;
	// 费用科目名称
	private String subjectName;
	// 费用科目编码
	private String subjectCode;
	// 维度1编码
	private String dimen1Code;
	// 维度1序号
	private Integer dimen1Sortno;
	// Dimen2Code
	private String dimen2Code;
	// Dimen2Sortno
	private Integer dimen2Sortno;
	// Dimen3Code
	private String dimen3Code;
	// Dimen3Sortno
	private Integer dimen3Sortno;
	// Dimen4Code
	private String dimen4Code;
	// Dimen4Sortno
	private Integer dimen4Sortno;
	// Dimen5Code
	private String dimen5Code;
	// Dimen5Sortno
	private Integer dimen5Sortno;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// DelFlag
	private String delFlag;
	// 备注
	private String remark;
	public BmsSubjectInfoEntity() {

	}
	
	/**
     * 收支类型（IN-收入 OUT-支出）
     */
	public String getInOutTypecode() {
		return this.inOutTypecode;
	}

    /**
     * 收支类型（IN-收入 OUT-支出）
     *
     * @param inOutTypecode
     */
	public void setInOutTypecode(String inOutTypecode) {
		this.inOutTypecode = inOutTypecode;
	}
	
	/**
     * 业务类型（仓，干，配）
     */
	public String getBizTypecode() {
		return this.bizTypecode;
	}

    /**
     * 业务类型（仓，干，配）
     *
     * @param bizTypecode
     */
	public void setBizTypecode(String bizTypecode) {
		this.bizTypecode = bizTypecode;
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
     * 费用科目编码
     */
	public String getSubjectCode() {
		return this.subjectCode;
	}

    /**
     * 费用科目编码
     *
     * @param subjectCode
     */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
     * 维度1编码
     */
	public String getDimen1Code() {
		return this.dimen1Code;
	}

    /**
     * 维度1编码
     *
     * @param dimen1Code
     */
	public void setDimen1Code(String dimen1Code) {
		this.dimen1Code = dimen1Code;
	}
	
	/**
     * 维度1序号
     */
	public Integer getDimen1Sortno() {
		return this.dimen1Sortno;
	}

    /**
     * 维度1序号
     *
     * @param dimen1Sortno
     */
	public void setDimen1Sortno(Integer dimen1Sortno) {
		this.dimen1Sortno = dimen1Sortno;
	}
	
	/**
     * Dimen2Code
     */
	public String getDimen2Code() {
		return this.dimen2Code;
	}

    /**
     * Dimen2Code
     *
     * @param dimen2Code
     */
	public void setDimen2Code(String dimen2Code) {
		this.dimen2Code = dimen2Code;
	}
	
	/**
     * Dimen2Sortno
     */
	public Integer getDimen2Sortno() {
		return this.dimen2Sortno;
	}

    /**
     * Dimen2Sortno
     *
     * @param dimen2Sortno
     */
	public void setDimen2Sortno(Integer dimen2Sortno) {
		this.dimen2Sortno = dimen2Sortno;
	}
	
	/**
     * Dimen3Code
     */
	public String getDimen3Code() {
		return this.dimen3Code;
	}

    /**
     * Dimen3Code
     *
     * @param dimen3Code
     */
	public void setDimen3Code(String dimen3Code) {
		this.dimen3Code = dimen3Code;
	}
	
	/**
     * Dimen3Sortno
     */
	public Integer getDimen3Sortno() {
		return this.dimen3Sortno;
	}

    /**
     * Dimen3Sortno
     *
     * @param dimen3Sortno
     */
	public void setDimen3Sortno(Integer dimen3Sortno) {
		this.dimen3Sortno = dimen3Sortno;
	}
	
	/**
     * Dimen4Code
     */
	public String getDimen4Code() {
		return this.dimen4Code;
	}

    /**
     * Dimen4Code
     *
     * @param dimen4Code
     */
	public void setDimen4Code(String dimen4Code) {
		this.dimen4Code = dimen4Code;
	}
	
	/**
     * Dimen4Sortno
     */
	public Integer getDimen4Sortno() {
		return this.dimen4Sortno;
	}

    /**
     * Dimen4Sortno
     *
     * @param dimen4Sortno
     */
	public void setDimen4Sortno(Integer dimen4Sortno) {
		this.dimen4Sortno = dimen4Sortno;
	}
	
	/**
     * Dimen5Code
     */
	public String getDimen5Code() {
		return this.dimen5Code;
	}

    /**
     * Dimen5Code
     *
     * @param dimen5Code
     */
	public void setDimen5Code(String dimen5Code) {
		this.dimen5Code = dimen5Code;
	}
	
	/**
     * Dimen5Sortno
     */
	public Integer getDimen5Sortno() {
		return this.dimen5Sortno;
	}

    /**
     * Dimen5Sortno
     *
     * @param dimen5Sortno
     */
	public void setDimen5Sortno(Integer dimen5Sortno) {
		this.dimen5Sortno = dimen5Sortno;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeesType() {
		return feesType;
	}

	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
	
}
