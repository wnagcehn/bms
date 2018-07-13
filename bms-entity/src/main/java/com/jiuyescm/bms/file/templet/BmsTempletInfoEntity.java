package com.jiuyescm.bms.file.templet;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

public class BmsTempletInfoEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 模板编号
	private String templetCode;
	// 业务类型（仓储，干线，配送）
	private String bizType;
	// 模板类型 upload-导入  download-下载
	private String templetType;
	// 模板名称
	private String templetName;
	// 模板下载地址
	private String url;
	//模板Excel名称
	private String excelName;
	// 备注
	private String remark;
	// 创建人
	private String creator;
	// CreateTime
	private Timestamp createTime;
	// LastModifier
	private String lastModifier;
	// LastModifyTime
	private Timestamp lastModifyTime;
	// 作废标识  0-启用  1-作废
	private String delFlag;
	// 扩展1
	private String param1;
	// param2
	private String param2;
	// param3
	private String param3;

	public BmsTempletInfoEntity() {

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
     * 模板编号
     */
	public String getTempletCode() {
		return this.templetCode;
	}

    /**
     * 模板编号
     *
     * @param templetCode
     */
	public void setTempletCode(String templetCode) {
		this.templetCode = templetCode;
	}
	
	/**
     * 业务类型（仓储，干线，配送）
     */
	public String getBizType() {
		return this.bizType;
	}

    /**
     * 业务类型（仓储，干线，配送）
     *
     * @param bizType
     */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	/**
     * 模板类型 upload-导入  download-下载
     */
	public String getTempletType() {
		return this.templetType;
	}

    /**
     * 模板类型 upload-导入  download-下载
     *
     * @param templetType
     */
	public void setTempletType(String templetType) {
		this.templetType = templetType;
	}
	
	/**
     * 模板名称
     */
	public String getTempletName() {
		return this.templetName;
	}

    /**
     * 模板名称
     *
     * @param templetName
     */
	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}
	
	/**
     * 模板下载地址
     */
	public String getUrl() {
		return this.url;
	}

    /**
     * 模板下载地址
     *
     * @param url
     */
	public void setUrl(String url) {
		this.url = url;
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
     * 作废标识  0-启用  1-作废
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 作废标识  0-启用  1-作废
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 扩展1
     */
	public String getParam1() {
		return this.param1;
	}

    /**
     * 扩展1
     *
     * @param param1
     */
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	/**
     * param2
     */
	public String getParam2() {
		return this.param2;
	}

    /**
     * param2
     *
     * @param param2
     */
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	
	/**
     * param3
     */
	public String getParam3() {
		return this.param3;
	}

    /**
     * param3
     *
     * @param param3
     */
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}
    
}
