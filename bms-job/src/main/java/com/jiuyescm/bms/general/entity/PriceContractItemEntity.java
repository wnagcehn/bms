package com.jiuyescm.bms.general.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class PriceContractItemEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 主键ID
	private Integer id;
	// 合同编号
	private String contractCode;
	// 费用科目
	private String subjectId;
	// 计费模板
	private String templateId;
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

	public PriceContractItemEntity() {

	}
	
	/**
     * 主键ID
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * 主键ID
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * 合同编号
     */
	public String getContractCode() {
		return this.contractCode;
	}

    /**
     * 合同编号
     *
     * @param contractCode
     */
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	/**
     * 费用科目
     */
	public String getSubjectId() {
		return this.subjectId;
	}

    /**
     * 费用科目
     *
     * @param subjectId
     */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	/**
     * 计费模板
     */
	public String getTemplateId() {
		return this.templateId;
	}

    /**
     * 计费模板
     *
     * @param templateId
     */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
    
}
