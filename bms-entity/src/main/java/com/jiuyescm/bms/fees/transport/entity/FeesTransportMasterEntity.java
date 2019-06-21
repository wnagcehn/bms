package com.jiuyescm.bms.fees.transport.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class FeesTransportMasterEntity implements IEntity {

    
	/**
     * 
     */
    private static final long serialVersionUID = -991992554937349013L;
    // 编号
	private Long id;
	// omsid
	private Long omsId;
	// 运输订单号
	private String orderNo;
	// 外部订单号(来源：OMS，为出库单号)
	private String outstockNo;
	// 温控标准编号
	private String temperatureTypeCode;
	// 伙伴编码
	private String customerId;
	// 伙伴名称
	private String customerName;
	// 承运产品
	private String projectName;
	// 始发站
	private String sendSite;
	// 始发省份
	private String sendProvince;
	// 始发城市
	private String sendCity;
	// 始发区
	private String sendDistrict;
	// 目的站
	private String receiveSite;
	// 目的省份
	private String receiveProvince;
	// 目的城市
	private String receiveCity;
	// 目的区
	private String receiveDistrict;
	// 目的地址
	private String receiveAddress;
	// 订单创建日期
	private Timestamp createdDt;
	// 体积
	private BigDecimal actualVolume;
	// 重量
	private BigDecimal actualWeight;
	// 实发箱数
	private BigDecimal actualPackingQty;
	// 实发件数
	private BigDecimal actualGoodsQty;
	// 实收箱数
	private BigDecimal receiptPackingQty;
	// 实收件数
	private BigDecimal receiptGoodsQty;
	// 发货日期
	private Timestamp beginTime;
	// 收货日期
	private Timestamp endTime;
	// 应付总计
	private BigDecimal paymentTotle;
	// 客户是否需要保险
	private String needInsurance;
	// 订单来源：OMS、TMS
	private String orderSourceCode;
	// 是否泡货:是:1 ，否:0
	private Integer light;
	// 是否退货:是:1 ，否:0
	private Integer hasBacktrack;
	// 备注
	private String remark;
	// TMS上传时间
	private Timestamp uploadTime;
	// 创建时间
	private Timestamp creTime;
	// 创建人
	private String crePerson;
	// N=未同步；Y=已同步；F=同步失败
	private String syncFlag;
	// BMS同步时间
	private Timestamp syncTime;
	// 同步次数
	private Integer syncCount;

	public FeesTransportMasterEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getOmsId() {
		return this.omsId;
	}

	public void setOmsId(Long omsId) {
		this.omsId = omsId;
	}
	
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getOutstockNo() {
		return this.outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}
	
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
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
	
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
	
	public String getSendDistrict() {
		return this.sendDistrict;
	}

	public void setSendDistrict(String sendDistrict) {
		this.sendDistrict = sendDistrict;
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
	
	public String getReceiveDistrict() {
		return this.receiveDistrict;
	}

	public void setReceiveDistrict(String receiveDistrict) {
		this.receiveDistrict = receiveDistrict;
	}
	
	public String getReceiveAddress() {
		return this.receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	
	public Timestamp getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Timestamp createdDt) {
		this.createdDt = createdDt;
	}
	
	public BigDecimal getActualVolume() {
		return this.actualVolume;
	}

	public void setActualVolume(BigDecimal actualVolume) {
		this.actualVolume = actualVolume;
	}
	
	public BigDecimal getActualWeight() {
		return this.actualWeight;
	}

	public void setActualWeight(BigDecimal actualWeight) {
		this.actualWeight = actualWeight;
	}
	
	public BigDecimal getActualPackingQty() {
		return this.actualPackingQty;
	}

	public void setActualPackingQty(BigDecimal actualPackingQty) {
		this.actualPackingQty = actualPackingQty;
	}
	
	public BigDecimal getActualGoodsQty() {
		return this.actualGoodsQty;
	}

	public void setActualGoodsQty(BigDecimal actualGoodsQty) {
		this.actualGoodsQty = actualGoodsQty;
	}
	
	public BigDecimal getReceiptPackingQty() {
		return this.receiptPackingQty;
	}

	public void setReceiptPackingQty(BigDecimal receiptPackingQty) {
		this.receiptPackingQty = receiptPackingQty;
	}
	
	public BigDecimal getReceiptGoodsQty() {
		return this.receiptGoodsQty;
	}

	public void setReceiptGoodsQty(BigDecimal receiptGoodsQty) {
		this.receiptGoodsQty = receiptGoodsQty;
	}
	
	public Timestamp getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public BigDecimal getPaymentTotle() {
		return this.paymentTotle;
	}

	public void setPaymentTotle(BigDecimal paymentTotle) {
		this.paymentTotle = paymentTotle;
	}
	
	public String getNeedInsurance() {
		return this.needInsurance;
	}

	public void setNeedInsurance(String needInsurance) {
		this.needInsurance = needInsurance;
	}
	
	public String getOrderSourceCode() {
		return this.orderSourceCode;
	}

	public void setOrderSourceCode(String orderSourceCode) {
		this.orderSourceCode = orderSourceCode;
	}
	
	public Integer getLight() {
		return this.light;
	}

	public void setLight(Integer light) {
		this.light = light;
	}
	
	public Integer getHasBacktrack() {
		return this.hasBacktrack;
	}

	public void setHasBacktrack(Integer hasBacktrack) {
		this.hasBacktrack = hasBacktrack;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Timestamp getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	public String getCrePerson() {
		return this.crePerson;
	}

	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	
	public Timestamp getSyncTime() {
		return this.syncTime;
	}

	public void setSyncTime(Timestamp syncTime) {
		this.syncTime = syncTime;
	}
	
	public Integer getSyncCount() {
		return this.syncCount;
	}

	public void setSyncCount(Integer syncCount) {
		this.syncCount = syncCount;
	}
    
}
