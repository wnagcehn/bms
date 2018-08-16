package com.jiuyescm.bms.base.group;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsGroupChargeunitEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -3198799294073948564L;
	// id
	private Integer id;
	// GroupId
	private Integer groupId;
	private List<Integer> groupIds;
	// 科目id
	private Integer unitId;
	// UnitCode
	private String unitCode;
	// UnitName
	private String unitName;
	// 序号
	private Integer sortNo;
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

	public BmsGroupChargeunitEntity() {
		super();
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	public Integer getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
	public String getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public Integer getSortNo() {
		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public List<Integer> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Integer> groupIds) {
		this.groupIds = groupIds;
	}
    
}
