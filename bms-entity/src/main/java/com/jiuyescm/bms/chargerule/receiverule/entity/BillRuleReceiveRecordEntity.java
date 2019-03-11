package com.jiuyescm.bms.chargerule.receiverule.entity;


import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillRuleReceiveRecordEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1102962216270200858L;
	// id
	private Long id;
	// 规则编号
	private String quotationNo;
	// 操作人
	private String creator;
	// 操作时间
	private Timestamp createTime;
	// 规则
	private String rule;

	public BillRuleReceiveRecordEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getQuotationNo() {
		return this.quotationNo;
	}

	public void setQuotationNo(String quotationNo) {
		this.quotationNo = quotationNo;
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
	
	public String getRule() {
		return this.rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
    
}
