package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizProductPalletStorageEntity implements IEntity {
    
	private static final long serialVersionUID = -7397351675206854410L;
	
	// id
	private Long id;
	// 数据编号
	private String dataNum;
	// 库存日期
	private Timestamp stockTime;
	// 仓库Id
	private String warehouseCode;
	// WarehouseName
	private String warehouseName;
	// 商家Id
	private String customerId;
	// CustomerName
	private String customerName;
	// 温度类型
	private String temperatureTypeCode;
	// TemperatureTypeName
	private String temperatureTypeName;
	// 托数
	private Double palletNum;
	// 费用编号
	private String feesNo;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
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
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	//调整托数
	private Double adjustPalletNum;
	
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
	
	// 商品件数
	private Double aqty;
	// 是否差异
	private String isDifferent;
	
	public Double getAdjustPalletNum() {
		return adjustPalletNum;
	}

	public void setAdjustPalletNum(Double adjustPalletNum) {
		this.adjustPalletNum = adjustPalletNum;
	}

	private Double num;
	
	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public BizProductPalletStorageEntity() {

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
     * 数据编号
     */
	public String getDataNum() {
		return this.dataNum;
	}

    /**
     * 数据编号
     *
     * @param dataNum
     */
	public void setDataNum(String dataNum) {
		this.dataNum = dataNum;
	}
	
	/**
     * 库存日期
     */
	public Timestamp getStockTime() {
		return this.stockTime;
	}

    /**
     * 库存日期
     *
     * @param stockTime
     */
	public void setStockTime(Timestamp stockTime) {
		this.stockTime = stockTime;
	}
	
	/**
     * 仓库Id
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库Id
     *
     * @param warehouseCode
     */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}
	
	/**
     * WarehouseName
     */
	public String getWarehouseName() {
		return this.warehouseName;
	}

    /**
     * WarehouseName
     *
     * @param warehouseName
     */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	/**
     * 商家Id
     */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	/**
     * CustomerName
     */
	public String getCustomerName() {
		return this.customerName;
	}

    /**
     * CustomerName
     *
     * @param customerName
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
     * 温度类型
     */
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

    /**
     * 温度类型
     *
     * @param temperatureTypeCode
     */
	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	/**
     * TemperatureTypeName
     */
	public String getTemperatureTypeName() {
		return this.temperatureTypeName;
	}

    /**
     * TemperatureTypeName
     *
     * @param temperatureTypeName
     */
	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}
	
	/**
     * 托数
     */
	public Double getPalletNum() {
		return this.palletNum;
	}

    /**
     * 托数
     *
     * @param palletNum
     */
	public void setPalletNum(Double palletNum) {
		this.palletNum = palletNum;
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
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
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

	public Double getAqty() {
		return aqty;
	}

	public void setAqty(Double aqty) {
		this.aqty = aqty;
	}

	public String getIsDifferent() {
		return isDifferent;
	}

	public void setIsDifferent(String isDifferent) {
		this.isDifferent = isDifferent;
	}
    
}
