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
    private static final long serialVersionUID = -631612165123969426L;
    // 编号
	private Long id;
	// 运输订单号
	private String orderNo;
	// 外部订单号(来源：OMS，为出库单号)
	private String transferNo;
	// 温控标准编号
	private String temperatureControlNo;
	// 伙伴编码
	private String partnerCode;
	// 伙伴名称
	private String partnerName;
	// 承运产品
	private String projectName;
	// 始发站
	private String originSiteNo;
	// 始发省份
	private String originProvince;
	// 始发城市
	private String origin;
	// 始发区
	private String originDistrict;
	// 目的站
	private String destinationSiteNo;
	// 目的省份
	private String destinationProvince;
	// 目的城市
	private String destination;
	// 目的区
	private String destinationDistrict;
	// 目的地址
	private String receiverAddress;
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
	
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getTransferNo() {
		return this.transferNo;
	}

	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}
	
	public String getTemperatureControlNo() {
		return this.temperatureControlNo;
	}

	public void setTemperatureControlNo(String temperatureControlNo) {
		this.temperatureControlNo = temperatureControlNo;
	}
	
	public String getPartnerCode() {
		return this.partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	
	public String getPartnerName() {
		return this.partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getOriginSiteNo() {
		return this.originSiteNo;
	}

	public void setOriginSiteNo(String originSiteNo) {
		this.originSiteNo = originSiteNo;
	}
	
	public String getOriginProvince() {
		return this.originProvince;
	}

	public void setOriginProvince(String originProvince) {
		this.originProvince = originProvince;
	}
	
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public String getOriginDistrict() {
		return this.originDistrict;
	}

	public void setOriginDistrict(String originDistrict) {
		this.originDistrict = originDistrict;
	}
	
	public String getDestinationSiteNo() {
		return this.destinationSiteNo;
	}

	public void setDestinationSiteNo(String destinationSiteNo) {
		this.destinationSiteNo = destinationSiteNo;
	}
	
	public String getDestinationProvince() {
		return this.destinationProvince;
	}

	public void setDestinationProvince(String destinationProvince) {
		this.destinationProvince = destinationProvince;
	}
	
	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String getDestinationDistrict() {
		return this.destinationDistrict;
	}

	public void setDestinationDistrict(String destinationDistrict) {
		this.destinationDistrict = destinationDistrict;
	}
	
	public String getReceiverAddress() {
		return this.receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
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
