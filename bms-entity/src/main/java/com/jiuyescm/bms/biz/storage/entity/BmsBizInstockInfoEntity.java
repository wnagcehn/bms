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
	private BigDecimal adjustQty;
	// 商品调整箱数
	private BigDecimal adjustBox;
	// 商品调整重量
	private BigDecimal adjustWeight;
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
	
	public BigDecimal getAdjustQty() {
		return this.adjustQty;
	}

	public void setAdjustQty(BigDecimal adjustQty) {
		this.adjustQty = adjustQty;
	}
	
	public BigDecimal getAdjustBox() {
		return this.adjustBox;
	}

	public void setAdjustBox(BigDecimal adjustBox) {
		this.adjustBox = adjustBox;
	}
	
	public BigDecimal getAdjustWeight() {
		return this.adjustWeight;
	}

	public void setAdjustWeight(BigDecimal adjustWeight) {
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
    
}
