package com.jiuyescm.bms.general.entity;




import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

import java.math.BigDecimal;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizInstockZxEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4784828995498746836L;
	// id
	private Long id;
	// OmsId
	private String omsId;
	// 入库单号
	private String instockNo;
	// 仓库编码
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家id
	private String customerid;
	// 商家
	private String customerName;
	// 外部单号
	private String externalNum;
	// 入库类型
	private String instockType;
	// 入库日期
	private Timestamp instockDate;
	// FeesNo
	private String feesNo;
	// FeeAmount
	private BigDecimal feeAmount;
	// num
	private BigDecimal num;
	// AdjustNum
	private BigDecimal adjustNum;
	// 结算状态 0-未结算 1-已结算 2-结算异常 3-其他
	private String isCalculated;
	// 收货人
	private String receiver;
	// 备注
	private String remark;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 删除标志
	private String delFlag;
	// 费用计算时间
	private Timestamp calculateTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// 拓展字段1
	private String extattr1;
	// 拓展字段2
	private String extattr2;
	// 拓展字段3
	private String extattr3;
	// 拓展字段4
	private String extattr4;
	// 拓展字段5
	private String extattr5;
	// weight
	private BigDecimal weight;
	// AdjustWeight
	private BigDecimal adjustWeight;

	public BizInstockZxEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOmsId() {
		return this.omsId;
	}

	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}
	
	public String getInstockNo() {
		return this.instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
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
	
	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getExternalNum() {
		return this.externalNum;
	}

	public void setExternalNum(String externalNum) {
		this.externalNum = externalNum;
	}
	
	public String getInstockType() {
		return this.instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}
	
	public Timestamp getInstockDate() {
		return this.instockDate;
	}

	public void setInstockDate(Timestamp instockDate) {
		this.instockDate = instockDate;
	}
	
	public String getFeesNo() {
		return this.feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	public BigDecimal getFeeAmount() {
		return this.feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	public BigDecimal getNum() {
		return this.num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}
	
	public BigDecimal getAdjustNum() {
		return this.adjustNum;
	}

	public void setAdjustNum(BigDecimal adjustNum) {
		this.adjustNum = adjustNum;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public String getExtattr1() {
		return this.extattr1;
	}

	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	public String getExtattr2() {
		return this.extattr2;
	}

	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	public String getExtattr3() {
		return this.extattr3;
	}

	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	public String getExtattr4() {
		return this.extattr4;
	}

	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	
	public String getExtattr5() {
		return this.extattr5;
	}

	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
	public BigDecimal getWeight() {
		return this.weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	public BigDecimal getAdjustWeight() {
		return this.adjustWeight;
	}

	public void setAdjustWeight(BigDecimal adjustWeight) {
		this.adjustWeight = adjustWeight;
	}
}
