package com.jiuyescm.bms.base.address.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class PubAddressEntity implements IEntity {

    private static final long serialVersionUID = -1;
    
	// 主键ID
	private Long id;
	// 区域编号
	private String areaCode;
	// 区域名称
	private String areaName;
	// 所属层级
	private String levelNum;
	// 父级区域编号
	private String parentAreaCode;
	// 大区编号
	private String regionCode;
	// 创建者编号
	private String creatorCode;
	// 创建时间
	private Timestamp createTime;
	// 修改者编号
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;

	public PubAddressEntity() {

	}
	
	/**
     * 主键ID
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 主键ID
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 区域编号
     */
	public String getAreaCode() {
		return this.areaCode;
	}

    /**
     * 区域编号
     *
     * @param areaCode
     */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	/**
     * 区域名称
     */
	public String getAreaName() {
		return this.areaName;
	}

    /**
     * 区域名称
     *
     * @param areaName
     */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	/**
     * 所属层级
     */
	public String getLevelNum() {
		return this.levelNum;
	}

    /**
     * 所属层级
     *
     * @param levelNum
     */
	public void setLevelNum(String levelNum) {
		this.levelNum = levelNum;
	}
	
	/**
     * 父级区域编号
     */
	public String getParentAreaCode() {
		return this.parentAreaCode;
	}

    /**
     * 父级区域编号
     *
     * @param parentAreaCode
     */
	public void setParentAreaCode(String parentAreaCode) {
		this.parentAreaCode = parentAreaCode;
	}
	
	/**
     * 大区编号
     */
	public String getRegionCode() {
		return this.regionCode;
	}

    /**
     * 大区编号
     *
     * @param regionCode
     */
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	
	/**
     * 创建者编号
     */
	public String getCreatorCode() {
		return this.creatorCode;
	}

    /**
     * 创建者编号
     *
     * @param creatorCode
     */
	public void setCreatorCode(String creatorCode) {
		this.creatorCode = creatorCode;
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
     * 修改者编号
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者编号
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
