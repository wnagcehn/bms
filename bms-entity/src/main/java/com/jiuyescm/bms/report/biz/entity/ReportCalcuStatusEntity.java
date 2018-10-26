package com.jiuyescm.bms.report.biz.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class ReportCalcuStatusEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6988978439493287766L;
	// 自增标识
	private Long id;
	// 统计年份
	private Integer createYear;
	// 统计月份
	private Integer createMonth;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 业务类型 STORAGE-仓储 DISPATCH-配送 TRANSPORT-干线
	private String bizType;
	// 费用科目
	private String subjectCode;
	// 计算状态
	private String isCalculated;
	// 计算总金额
	private String sumAmount;
	// 计算时间
	private Timestamp reportTime;

	public ReportCalcuStatusEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getCreateYear() {
		return this.createYear;
	}

	public void setCreateYear(Integer createYear) {
		this.createYear = createYear;
	}
	
	public Integer getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getBizType() {
		return this.bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public String getSumAmount() {
		return this.sumAmount;
	}

	public void setSumAmount(String sumAmount) {
		this.sumAmount = sumAmount;
	}
	
	public Timestamp getReportTime() {
		return this.reportTime;
	}

	public void setReportTime(Timestamp reportTime) {
		this.reportTime = reportTime;
	}
    
}
