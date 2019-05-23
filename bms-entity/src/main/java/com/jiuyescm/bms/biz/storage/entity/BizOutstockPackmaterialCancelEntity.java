package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizOutstockPackmaterialCancelEntity implements IEntity {

    
	/**
     * 
     */
    private static final long serialVersionUID = 4900452320232120963L;
    // 自增id
	private Long id;
	// 导入运单号
	private String waybillNoImport;
	// 包材组运单号
	private String waybillNoPackage;
	// 任务状态
	private String status;
	// 描述
	private String descrip;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 仓库编码
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 写入时间
	private Timestamp writeTime;
	// 修改时间
	private Timestamp modTime;
	// 运单号
	private String waybillNo;
	
	//合同归属
	private String contractAttr;

	public BizOutstockPackmaterialCancelEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getWaybillNoImport() {
		return this.waybillNoImport;
	}

	public void setWaybillNoImport(String waybillNoImport) {
		this.waybillNoImport = waybillNoImport;
	}
	
	public String getWaybillNoPackage() {
		return this.waybillNoPackage;
	}

	public void setWaybillNoPackage(String waybillNoPackage) {
		this.waybillNoPackage = waybillNoPackage;
	}
	
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getDescrip() {
		return this.descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
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
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public Timestamp getModTime() {
		return this.modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

    public String getContractAttr() {
        return contractAttr;
    }

    public void setContractAttr(String contractAttr) {
        this.contractAttr = contractAttr;
    }
    
}
