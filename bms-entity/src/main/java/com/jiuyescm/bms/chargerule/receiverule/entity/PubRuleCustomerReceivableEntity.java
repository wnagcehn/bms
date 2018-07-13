/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.chargerule.receiverule.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 规则商家映射关系实体类
 * 
 * @author yangshuaishuai
 */
public class PubRuleCustomerReceivableEntity implements IEntity {

	private static final long serialVersionUID = 6752501983557999678L;

	// id
	private Integer id;
	// 商家编号
	private String customerid;
	// 商家名称
	private String customerName;
	// 规则编号
	private String ruleNo;
	// 费用科目id
	private String subjectId;
	// remark
	private String remark;
	// creator
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// 作废标识 0-启用 1-作废
	private Integer delFlag;

	public PubRuleCustomerReceivableEntity() {

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

	/**
	 * 商家编号
	 */
	public String getCustomerid() {
		return this.customerid;
	}

	/**
	 * 商家编号
	 * 
	 * @param customerid
	 */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * 规则编号
	 */
	public String getRuleNo() {
		return this.ruleNo;
	}

	/**
	 * 规则编号
	 * 
	 * @param ruleNo
	 */
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getSubjectId() {
		return subjectId;
	}

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
	 * 作废标识 0-启用 1-作废
	 */
	public Integer getDelFlag() {
		return this.delFlag;
	}

	/**
	 * 作废标识 0-启用 1-作废
	 * 
	 * @param delFlag
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

}
