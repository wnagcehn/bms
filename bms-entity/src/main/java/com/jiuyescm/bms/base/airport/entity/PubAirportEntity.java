
package com.jiuyescm.bms.base.airport.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class PubAirportEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 机场编号（A00001）
	private String airportId;
	// 机场名称
	private String airportName;
	// 省
	private String province;
	// 市
	private String city;
	// 区
	private String district;
	// 创建人
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改人
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标识（0-未删除 1-已删除）
	private String delFlag;
	
	private String remark;

	public PubAirportEntity() {

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
     * 机场编号（A00001）
     */
	public String getAirportId() {
		return this.airportId;
	}

    /**
     * 机场编号（A00001）
     *
     * @param airportId
     */
	public void setAirportId(String airportId) {
		this.airportId = airportId;
	}
	
	/**
     * 机场名称
     */
	public String getAirportName() {
		return this.airportName;
	}

    /**
     * 机场名称
     *
     * @param airportName
     */
	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}
	
	/**
     * 省
     */
	public String getProvince() {
		return this.province;
	}

    /**
     * 省
     *
     * @param province
     */
	public void setProvince(String province) {
		this.province = province;
	}
	
	/**
     * 市
     */
	public String getCity() {
		return this.city;
	}

    /**
     * 市
     *
     * @param city
     */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
     * 区
     */
	public String getDistrict() {
		return this.district;
	}

    /**
     * 区
     *
     * @param district
     */
	public void setDistrict(String district) {
		this.district = district;
	}
	
	/**
     * 创建人
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建人
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
     * 修改人
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改人
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
     * 删除标识（0-未删除 1-已删除）
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标识（0-未删除 1-已删除）
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
