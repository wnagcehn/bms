package com.jiuyescm.bms.general.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizPalletInfoEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -2633861698566608026L;
	// id
	private Long id;
	// 日期(2018-01-01)
	private Timestamp curTime;
	// 业务类型 product-商品存储托数 material-耗材存储托数  instock-入库托数 outstock-出库托数
	private String bizType;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家Id
	private String customerId;
	// 商家名称
	private String customerName;
	// 温度编码
	private String temperatureTypeCode;
	// 托数
	private Double palletNum;
	// 调整托数
	private Double adjustPalletNum;
	// 费用编码
	private String feesNo;
	// 创建者
	private String creator;
	// 创建者id
	private String creatorId;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改者id
	private String lastModifierId;
	// 修改时间
	private Timestamp lastModifyTime;
	// 计算状态
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// 作废标志 0-未作废 1-已作废
	private String delFlag;
	// 备注
	private String remark;
	
	private Double cost;
	
	private String subjectCode;
	private String subjectName;
	
	private String chargeSource;
	private Double sysPalletNum;

	public BizPalletInfoEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Timestamp getCurTime() {
		return this.curTime;
	}

	public void setCurTime(Timestamp curTime) {
		this.curTime = curTime;
	}
	
	public String getBizType() {
		return this.bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
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
	
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	public Double getPalletNum() {
		return palletNum;
	}

	public void setPalletNum(Double palletNum) {
		this.palletNum = palletNum;
	}

	public Double getAdjustPalletNum() {
		return adjustPalletNum;
	}

	public void setAdjustPalletNum(Double adjustPalletNum) {
		this.adjustPalletNum = adjustPalletNum;
	}

	public String getFeesNo() {
		return this.feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
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
	
	public String getLastModifierId() {
		return this.lastModifierId;
	}

	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
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
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getChargeSource() {
		return chargeSource;
	}

	public void setChargeSource(String chargeSource) {
		this.chargeSource = chargeSource;
	}

	public Double getSysPalletNum() {
		return sysPalletNum;
	}

	public void setSysPalletNum(Double sysPalletNum) {
		this.sysPalletNum = sysPalletNum;
	}
    
}
