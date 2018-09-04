package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsBizInstockOriginEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 7218687788071682641L;
	// id
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

	public BmsBizInstockOriginEntity() {
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
	
	public String getInstockNo() {
		return this.instockNo;
	}

	public void setInstockNo(String instockNo) {
		this.instockNo = instockNo;
	}
	
	public String getExternalNum() {
		return this.externalNum;
	}

	public void setExternalNum(String externalNum) {
		this.externalNum = externalNum;
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
	
	public String getInstockType() {
		return this.instockType;
	}

	public void setInstockType(String instockType) {
		this.instockType = instockType;
	}
	
	public BigDecimal getTotalQty() {
		return this.totalQty;
	}

	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}
	
	public BigDecimal getTotalBox() {
		return this.totalBox;
	}

	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = totalBox;
	}
	
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
    
}
