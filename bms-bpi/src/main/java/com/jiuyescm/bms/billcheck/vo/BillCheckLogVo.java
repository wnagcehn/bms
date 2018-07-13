/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.billcheck.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BillCheckLogVo implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// 对账主表ID
	private Integer billCheckId;
	// 账单状态
	private String billStatusCode;
	// 部门编号
	private String deptId;
	// 部门名称
	private String deptName;
	// 附件名称
	private String fileName;
	// 附件地址
	private String fileUrl;
	// 操作明细
	private String operateDesc;
	// DelFlag
	private String delFlag;
	private String creatorId;
	// 操作人
	private String creator;
	// 操作时间
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	private String lastModifierId;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// 日志类型
	private Integer logType;
	private Integer billFollowId;
	private String billFollowState;

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getLastModifierId() {
		return lastModifierId;
	}

	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}

	public BillCheckLogVo() {

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
     * 对账主表ID
     */
	public Integer getBillCheckId() {
		return this.billCheckId;
	}

    /**
     * 对账主表ID
     *
     * @param billCheckId
     */
	public void setBillCheckId(Integer billCheckId) {
		this.billCheckId = billCheckId;
	}
	
	/**
     * 账单状态
     */
	public String getBillStatusCode() {
		return this.billStatusCode;
	}

    /**
     * 账单状态
     *
     * @param billStatusCode
     */
	public void setBillStatusCode(String billStatusCode) {
		this.billStatusCode = billStatusCode;
	}
	
	/**
     * 部门编号
     */
	public String getDeptId() {
		return this.deptId;
	}

    /**
     * 部门编号
     *
     * @param deptId
     */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	/**
     * 部门名称
     */
	public String getDeptName() {
		return this.deptName;
	}

    /**
     * 部门名称
     *
     * @param deptName
     */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	/**
     * 附件名称
     */
	public String getFileName() {
		return this.fileName;
	}

    /**
     * 附件名称
     *
     * @param fileName
     */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
     * 附件地址
     */
	public String getFileUrl() {
		return this.fileUrl;
	}

    /**
     * 附件地址
     *
     * @param fileUrl
     */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	/**
     * 操作明细
     */
	public String getOperateDesc() {
		return this.operateDesc;
	}

    /**
     * 操作明细
     *
     * @param operateDesc
     */
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
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
     * 操作人
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 操作人
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * 操作时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 操作时间
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
     * 日志类型
     */
	public Integer getLogType() {
		return this.logType;
	}

    /**
     * 日志类型
     *
     * @param logType
     */
	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public Integer getBillFollowId() {
		return billFollowId;
	}

	public void setBillFollowId(Integer billFollowId) {
		this.billFollowId = billFollowId;
	}

	public String getBillFollowState() {
		return billFollowState;
	}

	public void setBillFollowState(String billFollowState) {
		this.billFollowState = billFollowState;
	}
    
}
