/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.pub.transport.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 运输始发城市信息实体
 * @author yangss
 */
public class PubTransportOriginCityEntity implements IEntity {

	private static final long serialVersionUID = 1263128352170848222L;

	// 主键ID
	private Integer id;
	// 始发省份
	private String originProvince;
	// 始发城市
	private String originCity;
	// 类型名称-用于扩展
	private String typeName;
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
	// 备注
	private String remark;

	public PubTransportOriginCityEntity() {

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
	 * 始发省份
	 */
	public String getOriginProvince() {
		return this.originProvince;
	}

	/**
	 * 始发省份
	 * 
	 * @param originProvince
	 */
	public void setOriginProvince(String originProvince) {
		this.originProvince = originProvince;
	}

	/**
	 * 始发城市
	 */
	public String getOriginCity() {
		return this.originCity;
	}

	/**
	 * 始发城市
	 * 
	 * @param originCity
	 */
	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	/**
	 * 类型名称-用于扩展
	 */
	public String getTypeName() {
		return this.typeName;
	}

	/**
	 * 类型名称-用于扩展
	 * 
	 * @param typeName
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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

}
