package com.jiuyescm.bms.common.log.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsBillLogRecordVo implements IEntity {

	// TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 账单编号
	private String billNo;
	// 操作类型（账单生成，对账，科目折扣，开票...)
	private String operateType;
	// 操作内容（折扣金额134元...)
	private String operateDesc;
	// 操作时间
	private Timestamp createTime;
	// 操作人
	private String creator;
	// 备注（此处可用来存储操作异常信息）
	private String remark;

	public BmsBillLogRecordVo() {

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
     * 账单编号
     */
	public String getBillNo() {
		return this.billNo;
	}

    /**
     * 账单编号
     *
     * @param billNo
     */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	/**
     * 操作类型（账单生成，对账，科目折扣，开票...)
     */
	public String getOperateType() {
		return this.operateType;
	}

    /**
     * 操作类型（账单生成，对账，科目折扣，开票...)
     *
     * @param operateType
     */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	
	/**
     * 操作内容（折扣金额134元...)
     */
	public String getOperateDesc() {
		return this.operateDesc;
	}

    /**
     * 操作内容（折扣金额134元...)
     *
     * @param operateDesc
     */
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}
	
	/**
     * 操作时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 操作时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 操作人
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 操作人
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * 备注（此处可用来存储操作异常信息）
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注（此处可用来存储操作异常信息）
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
