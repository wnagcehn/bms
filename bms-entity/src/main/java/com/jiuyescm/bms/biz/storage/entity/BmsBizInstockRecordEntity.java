package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsBizInstockRecordEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3674717687410422934L;
	// id
	private Long id;
	// FeesNo
	private String feesNo;
	// 商品调整数量
	private BigDecimal adjustQty;
	// 商品调整箱数
	private BigDecimal adjustBox;
	// 商品调整重量
	private BigDecimal adjustWeight;
	// 修改者id
	private String lastModifierId;
	// 修改者名称
	private String lastModifier;
	// 修改时间
	private Timestamp lastModifyTime;
	// 修改原因
	private String modReason;
	// 作废标识 0-未作废 1-已作废
	private String delFlag;
	// 0-未计算 1-已计算 99-待重算
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	// 备注
	private String remark;

	public BmsBizInstockRecordEntity() {
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
	
	public String getModReason() {
		return this.modReason;
	}

	public void setModReason(String modReason) {
		this.modReason = modReason;
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
    
}
