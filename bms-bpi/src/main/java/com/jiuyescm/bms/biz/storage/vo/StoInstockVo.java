package com.jiuyescm.bms.biz.storage.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 入库单业务对象
 * @author caojianwei
 *
 */
public class StoInstockVo extends StoFeeGeneralVo implements IEntity {

	private static final long serialVersionUID = 8491828091386068860L;
	
	private Long id;
	// 费用编号
	private String feesNo;
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
	// 入库日期
	private Timestamp instockDate;
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
	// 写入BMS时间
	private Timestamp writeTime;
	// 备注
	private String remark;

	public StoInstockVo() {

	}
	
	/**
     * 自增ID
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增ID
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 费用编号
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * 费用编号
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	/**
     * 入库单号
     */
	public String getInstockNo() {
		return this.instockNo;
	}

    /**
     * 入库单号
     *
     * @param instockNo
     */
	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}
	
	/**
     * 外部单号
     */
	public String getExternalNum() {
		return this.externalNum;
	}

    /**
     * 外部单号
     *
     * @param externalNum
     */
	public void setExternalNum(String externalNum) {
		this.externalNum = externalNum;
	}
	
	/**
     * 仓库编码
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库编码
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * 仓库名称
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * 仓库名称
     *
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * 商家id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家id
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 商家
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 入库类型
     */
	public String getInstockType() {
		return this.instockType;
	}

    /**
     * 入库类型
     *
     * @param instockType
     */
	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}
	
	/**
     * 入库日期
     */
	public Timestamp getInstockDate() {
		return this.instockDate;
	}

    /**
     * 入库日期
     *
     * @param instockDate
     */
	public void setInstockDate(Timestamp instockDate) {
		this.instockDate = instockDate;
	}
	
	/**
     * 商品数量
     */
	public BigDecimal getTotalQty() {
		return this.totalQty;
	}

    /**
     * 商品数量
     *
     * @param totalQty
     */
	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	
	/**
     * 商品箱数
     */
	public BigDecimal getTotalBox() {
		return this.totalBox;
	}

    /**
     * 商品箱数
     *
     * @param totalBox
     */
	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = totalBox;
	}
	
	/**
     * 商品重量
     */
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 商品重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 收货人
     */
	public String getReceiver() {
		return this.receiver;
	}

    /**
     * 收货人
     *
     * @param receiver
     */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	/**
     * 创建者
     */
	public String getCreator() {
		return this.creator;
	}

    /**
     * 创建者
     *
     * @param creator
     */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
     * 业务时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 业务时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	/**
     * 商品调整数量
     */
	public BigDecimal getAdjustQty() {
		return this.adjustQty;
	}

    /**
     * 商品调整数量
     *
     * @param adjustQty
     */
	public void setAdjustQty(BigDecimal adjustQty) {
		this.adjustQty = adjustQty;
	}
	
	/**
     * 商品调整箱数
     */
	public BigDecimal getAdjustBox() {
		return this.adjustBox;
	}

    /**
     * 商品调整箱数
     *
     * @param adjustBox
     */
	public void setAdjustBox(BigDecimal adjustBox) {
		this.adjustBox = adjustBox;
	}
	
	/**
     * 商品调整重量
     */
	public BigDecimal getAdjustWeight() {
		return this.adjustWeight;
	}

    /**
     * 商品调整重量
     *
     * @param adjustWeight
     */
	public void setAdjustWeight(BigDecimal adjustWeight) {
		this.adjustWeight = adjustWeight;
	}
	
	/**
     * 修改者id
     */
	public String getLastModifierId() {
		return this.lastModifierId;
	}

    /**
     * 修改者id
     *
     * @param lastModifierId
     */
	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	/**
     * 修改者
     */
	public String getLastModifier() {
		return this.lastModifier;
	}

    /**
     * 修改者
     *
     * @param lastModifier
     */
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

    /**
     * 修改时间
     *
     * @param lastModifyTime
     */
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	/**
     * 作废标识 0-未作废 1-已作废
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 作废标识 0-未作废 1-已作废
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	/**
     * 0-未计算 1-已计算 99-待重算
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 0-未计算 1-已计算 99-待重算
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 费用计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 费用计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	/**
     * 写入BMS时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入BMS时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * 备注
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
