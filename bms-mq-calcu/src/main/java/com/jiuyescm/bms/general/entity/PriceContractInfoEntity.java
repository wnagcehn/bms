package com.jiuyescm.bms.general.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class PriceContractInfoEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 主键ID
	private Integer id;
	// 合同编号
	private String contractCode;
	// 合同类型(供应商合同/客户合同)
	private String contractTypeCode;
	// 商家Id
	private String customerId;
	// 商家名称
	private String customerName;
	// 合同对象
	private String contractObj;
	// 纸质合同编号
	private String paperContractNo;
	// 生效日期
	private Timestamp startDate;
	// 失效日期
	private Timestamp expireDate;
	// 合同描述
	private String description;
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

	public PriceContractInfoEntity() {

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
     * 合同编号
     */
	public String getContractCode() {
		return this.contractCode;
	}

    /**
     * 合同编号
     *
     * @param contractCode
     */
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	
	/**
     * 合同类型(供应商合同/客户合同)
     */
	public String getContractTypeCode() {
		return this.contractTypeCode;
	}

    /**
     * 合同类型(供应商合同/客户合同)
     *
     * @param contractTypeCode
     */
	public void setContractTypeCode(String contractTypeCode) {
		this.contractTypeCode = contractTypeCode;
	}
	
	/**
     * 商家Id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家Id
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 商家名称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家名称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 合同对象
     */
	public String getContractObj() {
		return this.contractObj;
	}

    /**
     * 合同对象
     *
     * @param contractObj
     */
	public void setContractObj(String contractObj) {
		this.contractObj = contractObj;
	}
	
	/**
     * 纸质合同编号
     */
	public String getPaperContractNo() {
		return this.paperContractNo;
	}

    /**
     * 纸质合同编号
     *
     * @param paperContractNo
     */
	public void setPaperContractNo(String paperContractNo) {
		this.paperContractNo = paperContractNo;
	}
	
	/**
     * 生效日期
     */
	public Timestamp getStartDate() {
		return this.startDate;
	}

    /**
     * 生效日期
     *
     * @param startDate
     */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	
	/**
     * 失效日期
     */
	public Timestamp getExpireDate() {
		return this.expireDate;
	}

    /**
     * 失效日期
     *
     * @param expireDate
     */
	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}
	
	/**
     * 合同描述
     */
	public String getDescription() {
		return this.description;
	}

    /**
     * 合同描述
     *
     * @param description
     */
	public void setDescription(String description) {
		this.description = description;
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
    
}
