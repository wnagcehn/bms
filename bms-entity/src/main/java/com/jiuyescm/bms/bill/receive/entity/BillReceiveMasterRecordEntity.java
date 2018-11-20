package com.jiuyescm.bms.bill.receive.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillReceiveMasterRecordEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -6810981838792762376L;
	// id
	private Long id;
	// 任务ID
	private String billNo;
	// 账单操作时间
	private Timestamp createTime;
	// 操作人
	private String creator;
	// 操作人ID
	private String creatorId;
	// 调整金额
	private BigDecimal adjustAmount;
	// 备注
	private String remark;

	public BillReceiveMasterRecordEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public BigDecimal getAdjustAmount() {
		return this.adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
