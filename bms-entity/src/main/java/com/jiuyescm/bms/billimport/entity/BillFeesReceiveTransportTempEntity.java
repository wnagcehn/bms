package com.jiuyescm.bms.billimport.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author liuzhicheng
 * 
 */
public class BillFeesReceiveTransportTempEntity implements IEntity {

    
	private static final long serialVersionUID = -5629109340743057514L;
	// 自增主键
	private Long id;
	// 账单编号
	private String billNo;
	// 费用科目编码
	private String subjectCode;
	// 费用科目名称
	private String subjectName;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 运单号
	private String waybillNo;
	// 订单号
	private String orderNo;
	// 派车单号
	private String sendNo;
	// 温控
	private String temperatureType;
	// 温控编码
	private String temperatureCode;
	// 业务类型
	private String bizType;
	// 始发站
	private String sendSite;
	// 始发省份
	private String sendProvince;
	// 始发城市
	private String sendCity;
	// 始发区
	private String sendDistinct;
	// 目的站
	private String receiveSite;
	// 目的省份
	private String receiveProvince;
	// 目的城市
	private String receiveCity;
	// 目的区
	private String receiveDistinct;
	// 重量
	private BigDecimal totalWeight;
	// 体积
	private BigDecimal totalVolumn;
	// 是否抛货
	private String isLight;
	// 车型
	private String carModel;
	// 金额
	private BigDecimal amount;
	// 折扣金额
	private BigDecimal derateAmount;
	// 业务时间
	private Timestamp createTime;
	// 业务年月 1810
	private Integer createMonth;
	// 写入bms时间
	private Timestamp writeTime;
	// Excel行号
	private Integer rowExcelNo;
	// Excel列名
	private String rowExcelName;

	public BillFeesReceiveTransportTempEntity() {
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
	
	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getSendNo() {
		return this.sendNo;
	}

	public void setSendNo(String sendNo) {
		this.sendNo = sendNo;
	}
	
	public String getTemperatureType() {
		return this.temperatureType;
	}

	public void setTemperatureType(String temperatureType) {
		this.temperatureType = temperatureType;
	}
	
	public String getTemperatureCode() {
		return this.temperatureCode;
	}

	public void setTemperatureCode(String temperatureCode) {
		this.temperatureCode = temperatureCode;
	}
	
	public String getBizType() {
		return this.bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
	public String getSendSite() {
		return this.sendSite;
	}

	public void setSendSite(String sendSite) {
		this.sendSite = sendSite;
	}
	
	public String getSendProvince() {
		return this.sendProvince;
	}

	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}
	
	public String getSendCity() {
		return this.sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	
	public String getSendDistinct() {
		return this.sendDistinct;
	}

	public void setSendDistinct(String sendDistinct) {
		this.sendDistinct = sendDistinct;
	}
	
	public String getReceiveSite() {
		return this.receiveSite;
	}

	public void setReceiveSite(String receiveSite) {
		this.receiveSite = receiveSite;
	}
	
	public String getReceiveProvince() {
		return this.receiveProvince;
	}

	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}
	
	public String getReceiveCity() {
		return this.receiveCity;
	}

	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	
	public String getReceiveDistinct() {
		return this.receiveDistinct;
	}

	public void setReceiveDistinct(String receiveDistinct) {
		this.receiveDistinct = receiveDistinct;
	}
	
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public BigDecimal getTotalVolumn() {
		return this.totalVolumn;
	}

	public void setTotalVolumn(BigDecimal totalVolumn) {
		this.totalVolumn = totalVolumn;
	}
	
	public String getIsLight() {
		return this.isLight;
	}

	public void setIsLight(String isLight) {
		this.isLight = isLight;
	}
	
	public String getCarModel() {
		return this.carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
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
