package com.jiuyescm.bms.base.address.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class PubAddressVo implements IEntity {

    private static final long serialVersionUID = -1;
    
	// 主键ID
	private Long id;
	// 区域编号
	private String province;
	// 区域名称
	private String provinceCode;
	// 所属层级
	private String city;
	// 父级区域编号
	private String cityCode;
	// 大区编号
	private String district;
	// 创建者编号
	private String districtCode;
	// 创建时间
	private Timestamp createTime;
	// 创建者编号
	private String creatorCode;
	// 修改者编号
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;

	public PubAddressVo() {

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
    
}
