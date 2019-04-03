package com.jiuyescm.bms.asyn.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsUserCustomerListenerEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3716852335590157133L;
	// 自增标识
	private Long id;
	// 订购人ID
	private String crePersonId;
	// 商家ID
	private String customerId;
	// 订购时间
	private Timestamp createTime;
	// 作废标识  0-未作废 1-已作废
	private String delFlag;

	public BmsUserCustomerListenerEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCrePersonId() {
		return this.crePersonId;
	}

	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
