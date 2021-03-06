package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class ReportBillImportMasterVo implements IEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -954666443111192136L;
	// id
	private Long id;
	// BillNo
	private String billNo;
	// 总计金额
	private BigDecimal totalMoney;
	// 仓储总金额
	private BigDecimal totalStorageMoney;
	// 配送总金额
	private BigDecimal totalDispatchMoney;
	// 干线总金额
	private BigDecimal totalTransportMoney;
	// 航空总金额
	private BigDecimal totalAirMoney;
	// 异常金额
	private BigDecimal totalAbnormalMoney;
	// 创建人
	private String creator;
	// 任务创建时间
	private Timestamp createTime;
	// 作废标识
	private String delFlag;
	// 耗材托数
	private BigDecimal totalMaterialStorage;
	// 耗材存储费
	private BigDecimal totalMaterialStorageMoney;
	// 商品存储费
	private BigDecimal totalProductStorage;
	// 商品存储费
	private BigDecimal totalProductStorageMoney;
	// 仓租费
	private BigDecimal totalRentMoney;

	// 干线理赔费用
	private BigDecimal totalTransportAbnormlMoney;
	// 航空理赔费用
	private BigDecimal totalAirAbnormlMoney;
	// 仓储理赔费用
	private BigDecimal totalStorageAbnormalMoney;
	// 配送理赔费用
	private BigDecimal totalDispatchAbnormalMoney;
	public ReportBillImportMasterVo() {
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
	
	public BigDecimal getTotalMoney() {
		return this.totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	public BigDecimal getTotalStorageMoney() {
		return this.totalStorageMoney;
	}

	public void setTotalStorageMoney(BigDecimal totalStorageMoney) {
		this.totalStorageMoney = totalStorageMoney;
	}
	
	public BigDecimal getTotalDispatchMoney() {
		return this.totalDispatchMoney;
	}

	public void setTotalDispatchMoney(BigDecimal totalDispatchMoney) {
		this.totalDispatchMoney = totalDispatchMoney;
	}
	
	public BigDecimal getTotalTransportMoney() {
		return this.totalTransportMoney;
	}

	public void setTotalTransportMoney(BigDecimal totalTransportMoney) {
		this.totalTransportMoney = totalTransportMoney;
	}
	
	public BigDecimal getTotalAirMoney() {
		return this.totalAirMoney;
	}

	public void setTotalAirMoney(BigDecimal totalAirMoney) {
		this.totalAirMoney = totalAirMoney;
	}
	
	public BigDecimal getTotalAbnormalMoney() {
		return this.totalAbnormalMoney;
	}

	public void setTotalAbnormalMoney(BigDecimal totalAbnormalMoney) {
		this.totalAbnormalMoney = totalAbnormalMoney;
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
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public BigDecimal getTotalMaterialStorage() {
		return totalMaterialStorage;
	}

	public void setTotalMaterialStorage(BigDecimal totalMaterialStorage) {
		this.totalMaterialStorage = totalMaterialStorage;
	}

	public BigDecimal getTotalMaterialStorageMoney() {
		return totalMaterialStorageMoney;
	}

	public void setTotalMaterialStorageMoney(BigDecimal totalMaterialStorageMoney) {
		this.totalMaterialStorageMoney = totalMaterialStorageMoney;
	}

	public BigDecimal getTotalProductStorage() {
		return totalProductStorage;
	}

	public void setTotalProductStorage(BigDecimal totalProductStorage) {
		this.totalProductStorage = totalProductStorage;
	}

	public BigDecimal getTotalProductStorageMoney() {
		return totalProductStorageMoney;
	}

	public void setTotalProductStorageMoney(BigDecimal totalProductStorageMoney) {
		this.totalProductStorageMoney = totalProductStorageMoney;
	}

	public BigDecimal getTotalRentMoney() {
		return totalRentMoney;
	}

	public void setTotalRentMoney(BigDecimal totalRentMoney) {
		this.totalRentMoney = totalRentMoney;
	}

	public BigDecimal getTotalTransportAbnormlMoney() {
		return totalTransportAbnormlMoney;
	}

	public void setTotalTransportAbnormlMoney(BigDecimal totalTransportAbnormlMoney) {
		this.totalTransportAbnormlMoney = totalTransportAbnormlMoney;
	}

	public BigDecimal getTotalAirAbnormlMoney() {
		return totalAirAbnormlMoney;
	}

	public void setTotalAirAbnormlMoney(BigDecimal totalAirAbnormlMoney) {
		this.totalAirAbnormlMoney = totalAirAbnormlMoney;
	}

	public BigDecimal getTotalStorageAbnormalMoney() {
		return totalStorageAbnormalMoney;
	}

	public void setTotalStorageAbnormalMoney(BigDecimal totalStorageAbnormalMoney) {
		this.totalStorageAbnormalMoney = totalStorageAbnormalMoney;
	}

	public BigDecimal getTotalDispatchAbnormalMoney() {
		return totalDispatchAbnormalMoney;
	}

	public void setTotalDispatchAbnormalMoney(BigDecimal totalDispatchAbnormalMoney) {
		this.totalDispatchAbnormalMoney = totalDispatchAbnormalMoney;
	}

    
}
