package com.jiuyescm.bms.billimport.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillFeesReceiveStorageTempEntity implements IEntity {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8816384002403169646L;
	// 自增主键
	private Long id;
	// 账单编号
	private String billNo;
	// 费用科目
	private String subjectCode;
	// 单据编号 (OMS orderno)
	private String orderNo;
	// 运单号
	private String waybillNo;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 耗材编码
	private String materialCode;
	// 耗材名称
	private String materialName;
	// 单据类型
	private String orderType;
	// 商品类别
	private String productType;
	// 温度类型
	private String tempretureType;
	// 商品编号
	private String productNo;
	// 商品名称
	private String productName;
	// 计费单位
	private String chargeUnit;
	// 数量
	private Integer totalQty;
	// 重量
	private BigDecimal totalWeight;
	// 体积
	private BigDecimal totalVolume;
	// 品种数
	private Integer totalSku;
	// 箱数
	private BigDecimal totalBox;
	// 总金额
	private BigDecimal amount;
	// 折扣金额
	private BigDecimal derateAmount;
	// 业务时间
	private Timestamp createTime;
	// 业务年月 1810
	private Integer createMonth;
	// Excel行号
	private Integer rowExcelNo;
	// Excel列名
	private String rowExcelName;
	// Excel Sheet名称
	private String sheetName;

	public BillFeesReceiveStorageTempEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
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
	
	public String getMaterialCode() {
		return this.materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	public String getMaterialName() {
		return this.materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getTempretureType() {
		return this.tempretureType;
	}

	public void setTempretureType(String tempretureType) {
		this.tempretureType = tempretureType;
	}
	
	public String getProductNo() {
		return this.productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	
	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getChargeUnit() {
		return this.chargeUnit;
	}

	public void setChargeUnit(String chargeUnit) {
		this.chargeUnit = chargeUnit;
	}
	
	public Integer getTotalQty() {
		return this.totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	
	public BigDecimal getTotalWeight() {
		return this.totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public BigDecimal getTotalVolume() {
		return this.totalVolume;
	}

	public void setTotalVolume(BigDecimal totalVolume) {
		this.totalVolume = totalVolume;
	}
	
	public Integer getTotalSku() {
		return this.totalSku;
	}

	public void setTotalSku(Integer totalSku) {
		this.totalSku = totalSku;
	}
	
	public BigDecimal getTotalBox() {
		return this.totalBox;
	}

	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = totalBox;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public BigDecimal getDerateAmount() {
		return this.derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public Integer getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	public Integer getRowExcelNo() {
		return this.rowExcelNo;
	}

	public void setRowExcelNo(Integer rowExcelNo) {
		this.rowExcelNo = rowExcelNo;
	}
	
	public String getRowExcelName() {
		return this.rowExcelName;
	}

	public void setRowExcelName(String rowExcelName) {
		this.rowExcelName = rowExcelName;
	}
	
	public String getSheetName() {
		return this.sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
    
}
