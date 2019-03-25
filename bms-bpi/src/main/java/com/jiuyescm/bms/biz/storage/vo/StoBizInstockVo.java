package com.jiuyescm.bms.biz.storage.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 入库单业务对象
 * @author caojianwei
 *
 */
public class StoBizInstockVo implements IEntity {

	private static final long serialVersionUID = 6043089073055573290L;
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
	// 费用
	private Double feeAmount;
	// 总数量
	private Double num;
	// 调整数量
	private Double adjustNum;
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

	public StoBizInstockVo() {

	}
	
	/**
     * id
     */
	public Long getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * OmsId
     */
	public String getOmsId() {
		return this.omsId;
	}

    /**
     * OmsId
     *
     * @param omsId
     */
	public void setOmsId(String omsId) {
		this.omsId = omsId;
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
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家id
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
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
     * FeesNo
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * FeesNo
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	/**
     * 费用
     */
	public Double getFeeAmount() {
		return this.feeAmount;
	}

    /**
     * 费用
     *
     * @param feeAmount
     */
	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	/**
     * 总数量
     */
	public Double getNum() {
		return this.num;
	}

    /**
     * 总数量
     *
     * @param num
     */
	public void setNum(Double num) {
		this.num = num;
	}
	
	/**
     * 调整数量
     */
	public Double getAdjustNum() {
		return this.adjustNum;
	}

    /**
     * 调整数量
     *
     * @param adjustNum
     */
	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}
	
	/**
     * 结算状态 0-未结算 1-已结算 2-结算异常 3-其他
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 结算状态 0-未结算 1-已结算 2-结算异常 3-其他
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
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
     * 创建时间
     */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

    /**
     * 创建时间
     *
     * @param createTime
     */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
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
     * 删除标志
     */
	public String getDelFlag() {
		return this.delFlag;
	}

    /**
     * 删除标志
     *
     * @param delFlag
     */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}	
}
