package com.jiuyescm.bms.billimport.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author liuzhicheng
 * 
 */
public class BillFeesReceiveAirTempEntity implements IEntity {

	private static final long serialVersionUID = -6091192321282996791L;
	// 自增主键
	private Long id;
	// 账单编号
	private String billNo;
	// 费用类型 BASE-基础费 ADD-增值费 OTHER-其他费
	private String feesType;
	// 费用科目
	private String subjectCode;
	// 运单号
	private String waybillNo;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 仓库(区域仓)编号
	private String warehouseCode;
	// 仓库(区域仓)名称
	private String warehouseName;
	// 始发站
	private String sendSite;
	// 目的站
	private String receiveSite;
	// 重量
	private BigDecimal totalWeight;
	// 业务时间(发货日期)
	private Timestamp createTime;
	// 业务年月 1810
	private Integer createMonth;
	// 总金额
	private BigDecimal amount;
	// 折扣金额
	private BigDecimal derateAmount;
	// 写入bms时间
	private Timestamp writeTime;
	// Excel行号
	private Integer rowExcelNo;
	// Excel列名
	private String rowExcelName;

	public BillFeesReceiveAirTempEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getFeesType() {
		return this.feesType;
	}

	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}
	
	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
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
	
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	public String getWarehouseName() {
		return this.warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	public String getSendSite() {
		return this.sendSite;
	}

	public void setSendSite(String sendSite) {
		this.sendSite = sendSite;
	}
	
	public String getReceiveSite() {
		return this.receiveSite;
	}

	public void setReceiveSite(String receiveSite) {
		this.receiveSite = receiveSite;
	}
	
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Integer getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getDerateAmount() {
		return this.derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public Integer getRowExcelNo() {
		return this.rowExcelNo;
	}

	public void setRowExcelNo(Integer rowExcelNo) {
		this.rowExcelNo = rowExcelNo;
	}
	
	public String getRowExcelName() {
		return this.rowExcelName;
	}

	public void setRowExcelName(String rowExcelName) {
		this.rowExcelName = rowExcelName;
	}
    
}
