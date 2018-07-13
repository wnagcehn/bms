/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.quotation.out.dispatch.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PriceOutDispacthTemplateEntity implements IEntity {

	// TODO Change serialVersionUID by eclipse
	private static final long serialVersionUID = -1;

	// id
	private Long id;
	// 模版编号
	private String templateCode;
	// 模版编号
	private String templateName;
	// 模版类型（S:标准，C:常规）
	private String templateType;
	// 配送商
	private String deliver;
	// 备注
	private String remark;
	// 参数1
	private String param1;
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

	// 宅配商id
	private String deliverId;
	// 宅配商名称
	private String deliverName;

	private List<PriceOutMainDispatchEntity> child;

	public PriceOutDispacthTemplateEntity() {

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
	 * 模版编号
	 */
	public String getTemplateCode() {
		return this.templateCode;
	}

	/**
	 * 模版编号
	 * 
	 * @param templateCode
	 */
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	/**
	 * 模版编号
	 */
	public String getTemplateName() {
		return this.templateName;
	}

	/**
	 * 模版编号
	 * 
	 * @param templateName
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * 计费方式
	 */
	public String getDeliver() {
		return this.deliver;
	}

	/**
	 * 计费方式
	 * 
	 * @param deliver
	 */
	public void setDeliver(String deliver) {
		this.deliver = deliver;
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

	public List<PriceOutMainDispatchEntity> getChild() {
		return child;
	}

	public void setChild(List<PriceOutMainDispatchEntity> child) {
		this.child = child;
	}

	public String getDeliverId() {
		return deliverId;
	}

	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

}
