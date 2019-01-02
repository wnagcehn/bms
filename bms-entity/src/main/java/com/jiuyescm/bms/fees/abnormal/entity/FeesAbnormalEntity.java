/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.fees.abnormal.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class FeesAbnormalEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
	private String projectId;//项目ID
	private String projectName;//项目名称
	// 唯一标识，自增
	private Long id;
	// 费用编号
	private String feeNo;
	// 出库单号
	private String outstockNo;
	// 外部订单号
	private String reference;
	// 运单号
	private String expressnum;
	// 内部订单号
	private String orderNo;
	// 客诉原因ID
	private Long reasonId;
	// 客诉原因
	private String reason;
	// 客诉原因详细
	private String reasonDetail;
	// 商家名称Id
	private String customerId;
	// 商家名称
	private String customerName;
	// 承运商名称Id
	private String carrierId;
	// 承运商名称
	private String carrierName;
	// 宅配商Id
	private String deliverId;
	// 宅配商名称
	private String deliverName;
	// 仓库Id
	private String warehouseId;
	// 仓库名称
	private String warehouseName;
	// 创建人ID
	private String createPerson;
	// 创建人
	private String createPersonName;
	// 备注
	private String remark;
	// 是否赔付 0-不赔付 1-赔付
	private Long ispay;
	// 创建时间
	private Timestamp createTime;
	// 赔付金额
	private Double payMoney;
	// 是否争议
	private Long isConflict;
	// 账单编号
	private String billNo;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常
	private String isCalculated;
	// 操作时间
	private Timestamp operateTime;
	//是否赔付
	private String isPayStr;
	// 是否争议
	private String isConflictStr;
	//理赔小计
	private Double totalPay;
	//是否免费运费 1-不免运费  0是免运费
    private String isDeliveryFree;
    // 运费
    private Double amerceAmount;//罚款金额
	private Double deliveryCost;
	private Double derateAmount;//减免金额
	private Double receiptAmount;//实收金额
	private Double num;
	
	
	// 九曳赔偿商家 商品金额 
	private Double productAmountJ2c;
	// 承运商赔偿九曳 商品金额 
	private Double productAmountD2j;
	// 承运商赔偿九曳 是否免运费 
	private String isDeliveryFreeD2j;
	// 商家赔偿九曳 改地址退件费
	private Double returnedAmountC2j;
	// 九曳赔偿承运商 改地址退件费
	private Double returnedAmountJ2d;
	// 单据状态 1-待确认 2-已确认 3-已关账
	private String orderStatus;
	// 责任方
	private String dutyType;
	// 赔付类型
	private String payType;
	// 客诉确认时间
	private Timestamp kesuConfirmTime;
	// 九曳赔偿商家 是否免运费
	private String isDeliveryFreeJ2c;
	// 确认理赔金额
	private Double confirmPayAmount;
	// 确认是否免运费
	private String confirmIsDeliveryFree;
	// 确认年份
	private String confirmYear;
	// 确认月份
	private String confirmMonth;
	// 导入确认时间
	private Timestamp importConfirmTime;
	// 关账时间
	private Timestamp closeTime;
	
	//退货单号
	private String returnOrderno;
	//应收是否赔付
	private String isPay1;
	//应付是否赔付
	private String isPay2;
	//承运商赔偿九曳 商品金额 最终确认额
	private Double confirmProductAmountD2j;
	//承运商赔偿九曳 处罚金额 最终确认额
	private Double confirmAmerceAmount;
	//九曳赔偿承运商 改地址退件费 最终确认额
	private Double confirmReturnedAmountJ2d;
	
	public Double getConfirmProductAmountD2j() {
		return confirmProductAmountD2j;
	}

	public void setConfirmProductAmountD2j(Double confirmProductAmountD2j) {
		this.confirmProductAmountD2j = confirmProductAmountD2j;
	}

	public Double getConfirmAmerceAmount() {
		return confirmAmerceAmount;
	}

	public void setConfirmAmerceAmount(Double confirmAmerceAmount) {
		this.confirmAmerceAmount = confirmAmerceAmount;
	}

	public Double getConfirmReturnedAmountJ2d() {
		return confirmReturnedAmountJ2d;
	}

	public void setConfirmReturnedAmountJ2d(Double confirmReturnedAmountJ2d) {
		this.confirmReturnedAmountJ2d = confirmReturnedAmountJ2d;
	}
	
	public String getReturnOrderno() {
		return returnOrderno;
	}

	public void setReturnOrderno(String returnOrderno) {
		this.returnOrderno = returnOrderno;
	}

	public String getIsPay1() {
		return isPay1;
	}

	public void setIsPay1(String isPay1) {
		this.isPay1 = isPay1;
	}

	public String getIsPay2() {
		return isPay2;
	}

	public void setIsPay2(String isPay2) {
		this.isPay2 = isPay2;
	}
	
	public String getIsDeliveryFree() {
		return isDeliveryFree;
	}

	public void setIsDeliveryFree(String isDeliveryFree) {
		this.isDeliveryFree = isDeliveryFree;
	}

	public Double getDeliveryCost() {
		return deliveryCost;
	}

	public void setDeliveryCost(Double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}


	public Double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(Double totalPay) {
		this.totalPay = totalPay;
	}

	public String getIsPayStr() {
		return isPayStr;
	}

	public void setIsPayStr(String isPayStr) {
		this.isPayStr = isPayStr;
	}

	public String getIsConflictStr() {
		return isConflictStr;
	}

	public void setIsConflictStr(String isConflictStr) {
		this.isConflictStr = isConflictStr;
	}

	public FeesAbnormalEntity() {

	}
	
	/**
     * 唯一标识，自增
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 唯一标识，自增
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
     * 费用编号
     */
	public String getFeeNo() {
		return this.feeNo;
	}

    /**
     * 费用编号
     *
     * @param feeNo
     */
	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}
	
	/**
     * 外部订单号
     */
	public String getReference() {
		return this.reference;
	}

    /**
     * 外部订单号
     *
     * @param reference
     */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	/**
     * 运单号
     */
	public String getExpressnum() {
		return this.expressnum;
	}

    /**
     * 运单号
     *
     * @param expressnum
     */
	public void setExpressnum(String expressnum) {
		this.expressnum = expressnum;
	}
	
	/**
     * 内部订单号
     */
	public String getOrderNo() {
		return this.orderNo;
	}

    /**
     * 内部订单号
     *
     * @param orderNo
     */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
     * 客诉原因ID
     */
	public Long getReasonId() {
		return this.reasonId;
	}

    /**
     * 客诉原因ID
     *
     * @param reasonId
     */
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}
	
	/**
     * 客诉原因
     */
	public String getReason() {
		return this.reason;
	}

    /**
     * 客诉原因
     *
     * @param reason
     */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
     * 客诉原因详细
     */
	public String getReasonDetail() {
		return this.reasonDetail;
	}

    /**
     * 客诉原因详细
     *
     * @param reasonDetail
     */
	public void setReasonDetail(String reasonDetail) {
		this.reasonDetail = reasonDetail;
	}
	
	/**
     * 商家名称Id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家名称Id
     *
     * @param customerId
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
     * 商家名称
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * 商家名称
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 承运商名称Id
     */
	public String getCarrierId() {
		return this.carrierId;
	}

    /**
     * 承运商名称Id
     *
     * @param carrierId
     */
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	/**
     * 承运商名称
     */
	public String getCarrierName() {
		return this.carrierName;
	}

    /**
     * 承运商名称
     *
     * @param carrierName
     */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	/**
     * 宅配商Id
     */
	public String getDeliverId() {
		return this.deliverId;
	}

    /**
     * 宅配商Id
     *
     * @param deliverId
     */
	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}
	
	/**
     * 宅配商名称
     */
	public String getDeliverName() {
		return this.deliverName;
	}

    /**
     * 宅配商名称
     *
     * @param deliverName
     */
	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	/**
     * 仓库Id
     */
	public String getWarehouseId() {
		return this.warehouseId;
	}

    /**
     * 仓库Id
     *
     * @param warehouseId
     */
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
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
     * 创建人ID
     */
	public String getCreatePerson() {
		return this.createPerson;
	}

    /**
     * 创建人ID
     *
     * @param createPerson
     */
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	
	/**
     * 创建人
     */
	public String getCreatePersonName() {
		return this.createPersonName;
	}

    /**
     * 创建人
     *
     * @param createPersonName
     */
	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
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
     * 是否赔付 0-不赔付 1-赔付
     */
	public Long getIspay() {
		return this.ispay;
	}

    /**
     * 是否赔付 0-不赔付 1-赔付
     *
     * @param ispay
     */
	public void setIspay(Long ispay) {
		this.ispay = ispay;
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
     * 赔付金额
     */
	public Double getPayMoney() {
		return this.payMoney;
	}

    /**
     * 赔付金额
     *
     * @param payMoney
     */
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	
	/**
     * 是否争议
     */
	public Long getIsConflict() {
		return this.isConflict;
	}

    /**
     * 是否争议
     *
     * @param isConflict
     */
	public void setIsConflict(Long isConflict) {
		this.isConflict = isConflict;
	}
	
	/**
     * 账单编号
     */
	public String getBillNo() {
		return this.billNo;
	}

    /**
     * 账单编号
     *
     * @param billNo
     */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	/**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 操作时间
     */
	public Timestamp getOperateTime() {
		return this.operateTime;
	}

    /**
     * 操作时间
     *
     * @param operateTime
     */
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Double getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(Double derateAmount) {
		this.derateAmount = derateAmount;
	}

	public Double getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public Double getProductAmountJ2c() {
		return productAmountJ2c;
	}

	public void setProductAmountJ2c(Double productAmountJ2c) {
		this.productAmountJ2c = productAmountJ2c;
	}

	public Double getProductAmountD2j() {
		return productAmountD2j;
	}

	public void setProductAmountD2j(Double productAmountD2j) {
		this.productAmountD2j = productAmountD2j;
	}

	public String getIsDeliveryFreeD2j() {
		return isDeliveryFreeD2j;
	}

	public void setIsDeliveryFreeD2j(String isDeliveryFreeD2j) {
		this.isDeliveryFreeD2j = isDeliveryFreeD2j;
	}

	public Double getReturnedAmountC2j() {
		return returnedAmountC2j;
	}

	public void setReturnedAmountC2j(Double returnedAmountC2j) {
		this.returnedAmountC2j = returnedAmountC2j;
	}

	public Double getReturnedAmountJ2d() {
		return returnedAmountJ2d;
	}

	public void setReturnedAmountJ2d(Double returnedAmountJ2d) {
		this.returnedAmountJ2d = returnedAmountJ2d;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Timestamp getKesuConfirmTime() {
		return kesuConfirmTime;
	}

	public void setKesuConfirmTime(Timestamp kesuConfirmTime) {
		this.kesuConfirmTime = kesuConfirmTime;
	}

	public String getIsDeliveryFreeJ2c() {
		return isDeliveryFreeJ2c;
	}

	public void setIsDeliveryFreeJ2c(String isDeliveryFreeJ2c) {
		this.isDeliveryFreeJ2c = isDeliveryFreeJ2c;
	}

	public Double getConfirmPayAmount() {
		return confirmPayAmount;
	}

	public void setConfirmPayAmount(Double confirmPayAmount) {
		this.confirmPayAmount = confirmPayAmount;
	}

	public String getConfirmIsDeliveryFree() {
		return confirmIsDeliveryFree;
	}

	public void setConfirmIsDeliveryFree(String confirmIsDeliveryFree) {
		this.confirmIsDeliveryFree = confirmIsDeliveryFree;
	}

	public String getConfirmYear() {
		return confirmYear;
	}

	public void setConfirmYear(String confirmYear) {
		this.confirmYear = confirmYear;
	}

	public String getConfirmMonth() {
		return confirmMonth;
	}

	public void setConfirmMonth(String confirmMonth) {
		this.confirmMonth = confirmMonth;
	}

	public Timestamp getImportConfirmTime() {
		return importConfirmTime;
	}

	public void setImportConfirmTime(Timestamp importConfirmTime) {
		this.importConfirmTime = importConfirmTime;
	}

	public Timestamp getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Timestamp closeTime) {
		this.closeTime = closeTime;
	}

	public String getOutstockNo() {
		return outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}

	public Double getAmerceAmount() {
		return amerceAmount;
	}

	public void setAmerceAmount(Double amerceAmount) {
		this.amerceAmount = amerceAmount;
	}
	
	
}
