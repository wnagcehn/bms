package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsBizInstockInfoEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 5976461500856513768L;
	// 自增ID
	private Long id;
	// 费用编号
	private String feesNo;
	// 商品调整数量
	private Double adjustQty;
	// 商品调整箱数
	private Double adjustBox;
	// 商品调整重量
	private Double adjustWeight;
	// 修改者id
	private String lastModifierId;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 作废标识 0-未作废 1-已作废
	private String delFlag;
	// 0-未计算 1-已计算 99-待重算
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	// 备注
	private String remark;
	
	// 入库单号
	private String instockNo;
	// 外部单号
	private String externalNum;
	// 仓库编码
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家id
	private String customerId;
	// 商家
	private String customerName;
	// 入库类型
	private String instockType;
	// 商品数量
	private BigDecimal totalQty;
	// 商品箱数
	private BigDecimal totalBox;
	// 商品重量
	private BigDecimal totalWeight;
	// 收货人
	private String receiver;
	// 创建者
	private String creator;
	// 业务时间
	private Timestamp createTime;
	// 写入BMS时间
	private Timestamp writeTime;
	
	// 费用科目
	private String subjectCode;
	// 计费重量
	private BigDecimal chargeWeight;
	// 计费箱数
	private BigDecimal chargeBox;
	// 计费商品数量
	private BigDecimal chargeQty;
	// 计费sku数量
	private BigDecimal chargeSku;
	// 计费物流商id
	private String chargeCarrierId;
	// 计费报价ID
	private String quoteId;
	// 金额
	private BigDecimal amount;
	// 减免金额
	private BigDecimal derateAmount;
	// 计算状态二级编码 1-合同不存在 2-报价缺失 3-数据异常 4-系统错误
	private String calcuStatus;
	// 计算说明
	private String calcuMsg;
	
	private BigDecimal unitPrice;
	private BigDecimal firstNum;
	private BigDecimal firstPrice;
	private BigDecimal continueNum;
	private BigDecimal continuePrice;
	private BigDecimal chargeUnit;
	
	private String modReason;

	public BmsBizInstockInfoEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFeesNo() {
		return this.feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	public Double getAdjustQty() {
		return adjustQty;
	}

	public void setAdjustQty(Double adjustQty) {
		this.adjustQty = adjustQty;
	}

	public Double getAdjustBox() {
		return adjustBox;
	}

	public void setAdjustBox(Double adjustBox) {
		this.adjustBox = adjustBox;
	}

	public Double getAdjustWeight() {
		return adjustWeight;
	}

	public void setAdjustWeight(Double adjustWeight) {
		this.adjustWeight = adjustWeight;
	}

	public String getLastModifierId() {
		return this.lastModifierId;
	}

	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInstockNo() {
		return instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}

	public String getExternalNum() {
		return externalNum;
	}

	public void setExternalNum(String externalNum) {
		this.externalNum = externalNum;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInstockType() {
		return instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}

	public BigDecimal getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}

	public BigDecimal getTotalBox() {
		return totalBox;
	}

	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = totalBox;
	}

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public BigDecimal getChargeWeight() {
		return chargeWeight;
	}

	public void setChargeWeight(BigDecimal chargeWeight) {
		this.chargeWeight = chargeWeight;
	}

	public BigDecimal getChargeBox() {
		return chargeBox;
	}

	public void setChargeBox(BigDecimal chargeBox) {
		this.chargeBox = chargeBox;
	}

	public BigDecimal getChargeQty() {
		return chargeQty;
	}

	public void setChargeQty(BigDecimal chargeQty) {
		this.chargeQty = chargeQty;
	}

	public BigDecimal getChargeSku() {
		return chargeSku;
	}

	public void setChargeSku(BigDecimal chargeSku) {
		this.chargeSku = chargeSku;
	}

	public String getChargeCarrierId() {
		return chargeCarrierId;
	}

	public void setChargeCarrierId(String chargeCarrierId) {
		this.chargeCarrierId = chargeCarrierId;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}

	public String getCalcuStatus() {
		return calcuStatus;
	}

	public void setCalcuStatus(String calcuStatus) {
		this.calcuStatus = calcuStatus;
	}

	public String getCalcuMsg() {
		return calcuMsg;
	}

	public void setCalcuMsg(String calcuMsg) {
		this.calcuMsg = calcuMsg;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(BigDecimal firstNum) {
		this.firstNum = firstNum;
	}

	public BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public BigDecimal getContinueNum() {
		return continueNum;
	}

	public void setContinueNum(BigDecimal continueNum) {
		this.continueNum = continueNum;
	}

	public BigDecimal getContinuePrice() {
		return continuePrice;
	}

	public void setContinuePrice(BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

	public BigDecimal getChargeUnit() {
		return chargeUnit;
	}

	public void setChargeUnit(BigDecimal chargeUnit) {
		this.chargeUnit = chargeUnit;
	}

	public String getModReason() {
		return modReason;
	}

	public void setModReason(String modReason) {
		this.modReason = modReason;
	}
    
}
