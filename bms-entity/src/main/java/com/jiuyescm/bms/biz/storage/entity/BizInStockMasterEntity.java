package com.jiuyescm.bms.biz.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 入库单主表
 * 
 * @author yangshuaishuai
 * 
 */
public class BizInStockMasterEntity implements IEntity {

	private static final long serialVersionUID = 5789412584082639885L;

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

	/** 合同编号 */
	private String contractCode;
	/** 冗余价格 */
	private BigDecimal price;

	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;	
	
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
	
	public BizInStockMasterEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOmsId() {
		return omsId;
	}

	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}

	public String getInstockNo() {
		return instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
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


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getExternalNum() {
		return externalNum;
	}

	public void setExternalNum(String externalNum) {
		this.externalNum = externalNum;
	}

	public String getInstockType() {
		return instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}

	public Timestamp getInstockDate() {
		return instockDate;
	}

	public void setInstockDate(Timestamp instockDate) {
		this.instockDate = instockDate;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public Double getAdjustNum() {
		return adjustNum;
	}

	public void setAdjustNum(Double adjustNum) {
		this.adjustNum = adjustNum;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		if (StringUtils.isNotEmpty(remark) && remark.length() > 128) {
			this.remark = remark.substring(0, 100);
		}else {
			this.remark = remark;
		}
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

	public String getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	public Timestamp getCalculateTime() {
		return calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}

	public String getextattr1() {
		return extattr1;
	}

	public void setextattr1(String extattr1) {
		this.extattr1 = extattr1;
	}

	public String getextattr2() {
		return extattr2;
	}

	public void setextattr2(String extattr2) {
		this.extattr2 = extattr2;
	}

	public String getextattr3() {
		return extattr3;
	}

	public void setextattr3(String extattr3) {
		this.extattr3 = extattr3;
	}

	public String getextattr4() {
		return extattr4;
	}

	public void setextattr4(String extattr4) {
		this.extattr4 = extattr4;
	}

	public String getextattr5() {
		return extattr5;
	}

	public void setextattr5(String extattr5) {
		this.extattr5 = extattr5;
	}

	
}
