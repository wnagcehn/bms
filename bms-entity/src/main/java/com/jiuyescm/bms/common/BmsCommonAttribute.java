package com.jiuyescm.bms.common;

import com.jiuyescm.cfm.domain.IEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity public class BmsCommonAttribute implements IEntity {
	
	private static final long serialVersionUID = 3235134242200295765L;
	/**
	 * 主键标识
	 */
	@Id @GeneratedValue private Long id;
	/**
	 * 创建者
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private java.sql.Timestamp createTime;
	
	/**
	 * 最后修改者
	 */
	private String lastModifier;
	
	/**
	 * 最后修改时间
	 */
	private java.sql.Timestamp lastModifyTime;
	
	/**
	 * 删除标识
	 */
	private String delFlag;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public java.sql.Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(java.sql.Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}
