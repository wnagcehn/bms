package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class BizProductStorageEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Long id;
	// WmsId
	private String wmsId;
	// DataNum
	private String dataNum;
	// 日期(当前日期)
	private String curDay;
	// 日期(当前日期)
	private Timestamp curTime;
	// 仓库号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家Id
	private String customerid;
	// 商家名称
	private String customerName;
	// SKU Id
	private String productId;
	// SKU名称
	private String productName;
	// 库存类型编码
	private String stockPlaceCode;
	// 库存类型
	private String stockPlace;
	// 批次编码
	private String batchCode;
	// 数量
	private Double aqty;
	// 生产日期
	private Timestamp productDate;
	// 到期日期
	private Timestamp expiryDate;
	// 入库时间
	private Timestamp inTime;
	// 费用编码
	private String feesNo;
	// 所属哪个仓库的数据库
	private String dbname;
	// temperature
	private String temperature;
	// weight
	private Double weight;
	// volume
	private Double volume;
	// PalletNum
	private Double palletNum;
	// PieceNum
	private Double pieceNum;
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
	
	//试算用 只是显示
	private String price;
	
	//试算用 只是显示
	private String feeUnitCode;
	
	//试算用 只是显示
	private String contractCode;
	
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
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
	public String getFeeUnitCode() {
		return feeUnitCode;
	}

	public void setFeeUnitCode(String feeUnitCode) {
		this.feeUnitCode = feeUnitCode;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public BizProductStorageEntity() {

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
     * WmsId
     */
	public String getWmsId() {
		return this.wmsId;
	}

    /**
     * WmsId
     *
     * @param wmsId
     */
	public void setWmsId(String wmsId) {
		this.wmsId = wmsId;
	}
	
	/**
     * DataNum
     */
	public String getDataNum() {
		return this.dataNum;
	}

    /**
     * DataNum
     *
     * @param dataNum
     */
	public void setDataNum(String dataNum) {
		this.dataNum = dataNum;
	}
	
	/**
     * 日期(当前日期)
     */
	public String getCurDay() {
		return this.curDay;
	}

    /**
     * 日期(当前日期)
     *
     * @param curDay
     */
	public void setCurDay(String curDay) {
		this.curDay = curDay;
	}
	
	/**
     * 日期(当前日期)
     */
	public Timestamp getCurTime() {
		return this.curTime;
	}

    /**
     * 日期(当前日期)
     *
     * @param curTime
     */
	public void setCurTime(Timestamp curTime) {
		this.curTime = curTime;
	}
	
	/**
     * 仓库号
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库号
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
     * 商家Id
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家Id
     *
     * @param customerid
     */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
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
     * SKU Id
     */
	public String getProductId() {
		return this.productId;
	}

    /**
     * SKU Id
     *
     * @param productId
     */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
     * SKU名称
     */
	public String getProductName() {
		return this.productName;
	}

    /**
     * SKU名称
     *
     * @param productName
     */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
     * 库存类型编码
     */
	public String getStockPlaceCode() {
		return this.stockPlaceCode;
	}

    /**
     * 库存类型编码
     *
     * @param stockPlaceCode
     */
	public void setStockPlaceCode(String stockPlaceCode) {
		this.stockPlaceCode = stockPlaceCode;
	}
	
	/**
     * 库存类型
     */
	public String getStockPlace() {
		return this.stockPlace;
	}

    /**
     * 库存类型
     *
     * @param stockPlace
     */
	public void setStockPlace(String stockPlace) {
		this.stockPlace = stockPlace;
	}
	
	/**
     * 批次编码
     */
	public String getBatchCode() {
		return this.batchCode;
	}

    /**
     * 批次编码
     *
     * @param batchCode
     */
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	
	/**
     * 数量
     */
	public Double getAqty() {
		if(this.aqty != null){
			return this.aqty;
		}else{
			return 0.0d;
		}
	}

    /**
     * 数量
     *
     * @param aqty
     */
	public void setAqty(Double aqty) {
		this.aqty = aqty;
	}
	
	/**
     * 生产日期
     */
	public Timestamp getProductDate() {
		return this.productDate;
	}

    /**
     * 生产日期
     *
     * @param productDate
     */
	public void setProductDate(Timestamp productDate) {
		this.productDate = productDate;
	}
	
	/**
     * 到期日期
     */
	public Timestamp getExpiryDate() {
		return this.expiryDate;
	}

    /**
     * 到期日期
     *
     * @param expiryDate
     */
	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	/**
     * 入库时间
     */
	public Timestamp getInTime() {
		return this.inTime;
	}

    /**
     * 入库时间
     *
     * @param inTime
     */
	public void setInTime(Timestamp inTime) {
		this.inTime = inTime;
	}
	
	/**
     * 费用编码
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * 费用编码
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	/**
     * 所属哪个仓库的数据库
     */
	public String getDbname() {
		return this.dbname;
	}

    /**
     * 所属哪个仓库的数据库
     *
     * @param dbname
     */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	
	/**
     * temperature
     */
	public String getTemperature() {
		return this.temperature;
	}

    /**
     * temperature
     *
     * @param temperature
     */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	/**
     * weight
     */
	public Double getWeight() {
		if(this.weight != null){
			return this.weight;
		}else{
			return 0.0d;
		}
	}

    /**
     * weight
     *
     * @param weight
     */
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	/**
     * volume
     */
	public Double getVolume() {
		return this.volume;
	}

    /**
     * volume
     *
     * @param volume
     */
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	/**
     * PalletNum
     */
	public Double getPalletNum() {
		return this.palletNum;
	}

    /**
     * PalletNum
     *
     * @param palletNum
     */
	public void setPalletNum(Double palletNum) {
		this.palletNum = palletNum;
	}
	
	/**
     * PieceNum
     */
	public Double getPieceNum() {
		return this.pieceNum;
	}

    /**
     * PieceNum
     *
     * @param pieceNum
     */
	public void setPieceNum(Double pieceNum) {
		this.pieceNum = pieceNum;
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
	
}
