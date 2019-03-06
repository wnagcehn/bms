package com.jiuyescm.bms.biz.pallet.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizPalletInfoTempEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3821450436992687003L;
	// id
	private Long id;
	// 任务ID
	private String taskId;
	// 日期(2018-01-01)
	private Date curTime;
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
	// 创建者
	private String creator;
	// 创建者id
	private String creatorId;
	// 创建时间
	private Timestamp createTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// excel行号
	private Integer rowExcelNo;
	// excel列名
	private String rowExcelName;
	
	//系统托数
	private Double sysPalletNum;
	//计费来源
	private String chargeSource;
	public BizPalletInfoTempEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public Date getCurTime() {
		return curTime;
	}

	public void setCurTime(Date curTime) {
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
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
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

	public Double getSysPalletNum() {
		return sysPalletNum;
	}

	public void setSysPalletNum(Double sysPalletNum) {
		this.sysPalletNum = sysPalletNum;
	}

	public String getChargeSource() {
		return chargeSource;
	}

	public void setChargeSource(String chargeSource) {
		this.chargeSource = chargeSource;
	}
    
}
