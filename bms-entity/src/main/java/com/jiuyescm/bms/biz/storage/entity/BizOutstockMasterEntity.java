/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizOutstockMasterEntity implements IEntity {

  
	private static final long serialVersionUID = 8344936944721528484L;   
    
	// id
	private Long id;
	// oms_id
	private String omsId;
	// 出库单号
	private String outstockNo;
	// 外部单号
	private String externalNo;
	// 仓库编号
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家编号
	private String customerid;
	// 商家名称
	private String customerName;
	// 物流商编号
	private String carrierId;
	// 物流商名称
	private String carrierName;
	// 宅配商编号
	private String deliverId;
	// 宅配商名称
	private String deliverName;
	// 宅配商名称
	private String chyunCode;
	// 承运商名称
	private String chyunName;
	// 运单号
	private String waybillNo;
	// 是否拆箱
	private String unpacking;
	// 发货日期
	private Timestamp sendTime;
	// 温度类型
	private String temperatureTypeCode;
	// 温度类型名称
	private String temperatureTypeName;
	// 单据类型编码
	private String billTypeCode;
	// 单据类型名称
	private String billTypeName;
	// B2B标识
	private String b2bFlag;
	// 总重量
	private Double totalWeight;
	// 调整重量
	private Double resizeWeight;
	// 总数量
	private Double totalQuantity;
	// 总品种数
	private Double totalVarieties;
	// 是否拆单
	private Double splitSingle;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-其他
	private String isCalculated;
	// 费用ID
	private String feesNo;
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
	//合同
    private String contractCode;
    //单量 在试算中用到,只为试算而生
    private Double billNum;
    //试算用 只是显示
  	private String price;
  	//调整数量
  	private Double resizeNum;

  	// 写入BMS时间
 	private Timestamp writeTime;
 	// 费用计算时间
 	private Timestamp calculateTime;
 	//商品明细
 	private String productDetail;
  	
 	
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
	
	private Integer boxnum;
	
	private Integer adjustBoxnum;
	
	public Integer getBoxnum() {
		return boxnum;
	}

	public void setBoxnum(Integer boxnum) {
		this.boxnum = boxnum;
	}

	public Integer getAdjustBoxnum() {
		return adjustBoxnum;
	}

	public void setAdjustBoxnum(Integer adjustBoxnum) {
		this.adjustBoxnum = adjustBoxnum;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public Double getResizeNum() {
		return resizeNum;
	}

	public void setResizeNum(Double resizeNum) {
		this.resizeNum = resizeNum;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public Double getBillNum() {
		return billNum;
	}

	public void setBillNum(Double billNum) {
		this.billNum = billNum;
	}

	public BizOutstockMasterEntity() {

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
     * oms_id
     */
	public String getOmsId() {
		return this.omsId;
	}

    /**
     * oms_id
     *
     * @param omsId
     */
	public void setOmsId(String omsId) {
		this.omsId = omsId;
	}
	
	/**
     * 出库单号
     */
	public String getOutstockNo() {
		return this.outstockNo;
	}

    /**
     * 出库单号
     *
     * @param outstockNo
     */
	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}
	
	/**
     * 外部单号
     */
	public String getExternalNo() {
		return this.externalNo;
	}

    /**
     * 外部单号
     *
     * @param externalNo
     */
	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	
	/**
     * 仓库编号
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * 仓库编号
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
     * 商家编号
     */
	public String getCustomerid() {
		return this.customerid;
	}

    /**
     * 商家编号
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
     * 物流商编号
     */
	public String getCarrierId() {
		return this.carrierId;
	}

    /**
     * 物流商编号
     *
     * @param carrierId
     */
	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	/**
     * 物流商名称
     */
	public String getCarrierName() {
		return this.carrierName;
	}

    /**
     * 物流商名称
     *
     * @param carrierName
     */
	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	/**
     * 宅配商编号
     */
	public String getDeliverId() {
		return this.deliverId;
	}

    /**
     * 宅配商编号
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
     * 宅配商名称
     */
	public String getChyunCode() {
		return this.chyunCode;
	}

    /**
     * 宅配商名称
     *
     * @param chyunCode
     */
	public void setChyunCode(String chyunCode) {
		this.chyunCode = chyunCode;
	}
	
	/**
     * 承运商名称
     */
	public String getChyunName() {
		return this.chyunName;
	}

    /**
     * 承运商名称
     *
     * @param chyunName
     */
	public void setChyunName(String chyunName) {
		this.chyunName = chyunName;
	}
	
	/**
     * 运单号
     */
	public String getWaybillNo() {
		return this.waybillNo;
	}

    /**
     * 运单号
     *
     * @param waybillNo
     */
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	/**
     * 是否拆箱
     */
	public String getUnpacking() {
		return this.unpacking;
	}

    /**
     * 是否拆箱
     *
     * @param unpacking
     */
	public void setUnpacking(String unpacking) {
		this.unpacking = unpacking;
	}
	
	/**
     * 发货日期
     */
	public Timestamp getSendTime() {
		return this.sendTime;
	}

    /**
     * 发货日期
     *
     * @param sendTime
     */
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
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
     * 温度类型名称
     */
	public String getTemperatureTypeName() {
		return this.temperatureTypeName;
	}

    /**
     * 温度类型名称
     *
     * @param temperatureTypeName
     */
	public void setTemperatureTypeName(String temperatureTypeName) {
		this.temperatureTypeName = temperatureTypeName;
	}
	
	/**
     * 单据类型编码
     */
	public String getBillTypeCode() {
		return this.billTypeCode;
	}

    /**
     * 单据类型编码
     *
     * @param billTypeCode
     */
	public void setBillTypeCode(String billTypeCode) {
		this.billTypeCode = billTypeCode;
	}
	
	/**
     * 单据类型名称
     */
	public String getBillTypeName() {
		return this.billTypeName;
	}

    /**
     * 单据类型名称
     *
     * @param billTypeName
     */
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	
	/**
     * B2B标识
     */
	public String getB2bFlag() {
		return this.b2bFlag;
	}

    /**
     * B2B标识
     *
     * @param b2bFlag
     */
	public void setB2bFlag(String b2bFlag) {
		this.b2bFlag = b2bFlag;
	}
	
	/**
     * 总重量
     */
	public Double getTotalWeight() {
		return this.totalWeight;
	}

    /**
     * 总重量
     *
     * @param totalWeight
     */
	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/**
     * 调整重量
     */
	public Double getResizeWeight() {
		return this.resizeWeight;
	}

    /**
     * 调整重量
     *
     * @param resizeWeight
     */
	public void setResizeWeight(Double resizeWeight) {
		this.resizeWeight = resizeWeight;
	}
	
	/**
     * 总数量
     */
	public Double getTotalQuantity() {
		return this.totalQuantity;
	}

    /**
     * 总数量
     *
     * @param totalQuantity
     */
	public void setTotalQuantity(Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	/**
     * 总品种数
     */
	public Double getTotalVarieties() {
		return this.totalVarieties;
	}

    /**
     * 总品种数
     *
     * @param totalVarieties
     */
	public void setTotalVarieties(Double totalVarieties) {
		this.totalVarieties = totalVarieties;
	}
	
	/**
     * 是否拆单
     */
	public Double getSplitSingle() {
		return this.splitSingle;
	}

    /**
     * 是否拆单
     *
     * @param splitSingle
     */
	public void setSplitSingle(Double splitSingle) {
		this.splitSingle = splitSingle;
	}
	
	/**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-其他
     */
	public String getIsCalculated() {
		return this.isCalculated;
	}

    /**
     * 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-其他
     *
     * @param isCalculated
     */
	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	/**
     * 费用ID
     */
	public String getFeesNo() {
		return this.feesNo;
	}

    /**
     * 费用ID
     *
     * @param feesNo
     */
	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
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
		if (StringUtils.isNotEmpty(remark) && remark.length() > 128) {
			this.remark = remark.substring(0, 100);
		}else {
			this.remark = remark;
		}
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
