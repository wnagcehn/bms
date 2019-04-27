/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.biz.storage.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizOutstockPackmaterialTempEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -3364103119884878642L;
	// id
	private int id;
	// 批次号
	private String batchNum;
	// wms_id
	private String wmsId;
	// 出库单号
	private String outstockNo;
	// 运单号
	private String waybillNo;
	// 商家id
	private String customerId;
	// 商家名称
	private String customerName;
	// 耗材编码
	private String consumerMaterialCode;
	// 耗材名称
	private String consumerMaterialName;
	// WarehouseCode
	private String warehouseCode;
	// WarehouseName
	private String warehouseName;
	// num
	private BigDecimal num;
	// AdjustNum
	private BigDecimal adjustNum;
	// 费用ID
	private String feesNo;
	// 成本费用编号
	private String costFeesNo;
	// 所属DB的名称
	private String dbname;
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
	// 费用计算时间
	private Timestamp calculateTime;
	// 成本计算状态
	private String costIsCalculated;
	// 成本费用计算时间
	private Timestamp costCalculateTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// weight
	private BigDecimal weight;
	// SpecDesc
	private String specDesc;
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
	// 成本费用计算备注
	private String costRemark;
	//Excel的行号
	private int rowExcelNo;
	private String rowExcelName;
	
	//包材组编码
	private String packGroupNo;

	public BizOutstockPackmaterialTempEntity() {

	}
	
	/**
     * id
     */
	public int getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * 批次号
     */
	public String getBatchNum() {
		return this.batchNum;
	}

    /**
     * 批次号
     *
     * @param batchNum
     */
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	
	/**
     * wms_id
     */
	public String getWmsId() {
		return this.wmsId;
	}

    /**
     * wms_id
     *
     * @param wmsId
     */
	public void setWmsId(String wmsId) {
		this.wmsId = wmsId;
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
     * 商家id
     */
	public String getCustomerId() {
		return this.customerId;
	}

    /**
     * 商家id
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
     * 耗材编码
     */
	public String getConsumerMaterialCode() {
		return this.consumerMaterialCode;
	}

    /**
     * 耗材编码
     *
     * @param consumerMaterialCode
     */
	public void setConsumerMaterialCode(String consumerMaterialCode) {
		this.consumerMaterialCode = consumerMaterialCode;
	}
	
	/**
     * 耗材名称
     */
	public String getConsumerMaterialName() {
		return this.consumerMaterialName;
	}

    /**
     * 耗材名称
     *
     * @param consumerMaterialName
     */
	public void setConsumerMaterialName(String consumerMaterialName) {
		this.consumerMaterialName = consumerMaterialName;
	}
	
	/**
     * WarehouseCode
     */
	public String getWarehouseCode() {
		return this.warehouseCode;
	}

    /**
     * WarehouseCode
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
     * num
     */
	public BigDecimal getNum() {
		return this.num;
	}

    /**
     * num
     *
     * @param num
     */
	public void setNum(BigDecimal num) {
		this.num = num;
	}
	
	/**
     * AdjustNum
     */
	public BigDecimal getAdjustNum() {
		return this.adjustNum;
	}

    /**
     * AdjustNum
     *
     * @param adjustNum
     */
	public void setAdjustNum(BigDecimal adjustNum) {
		this.adjustNum = adjustNum;
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
     * 成本费用编号
     */
	public String getCostFeesNo() {
		return this.costFeesNo;
	}

    /**
     * 成本费用编号
     *
     * @param costFeesNo
     */
	public void setCostFeesNo(String costFeesNo) {
		this.costFeesNo = costFeesNo;
	}
	
	/**
     * 所属DB的名称
     */
	public String getDbname() {
		return this.dbname;
	}

    /**
     * 所属DB的名称
     *
     * @param dbname
     */
	public void setDbname(String dbname) {
		this.dbname = dbname;
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
	
	/**
     * 费用计算时间
     */
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

    /**
     * 费用计算时间
     *
     * @param calculateTime
     */
	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	/**
     * 成本计算状态
     */
	public String getCostIsCalculated() {
		return this.costIsCalculated;
	}

    /**
     * 成本计算状态
     *
     * @param costIsCalculated
     */
	public void setCostIsCalculated(String costIsCalculated) {
		this.costIsCalculated = costIsCalculated;
	}
	
	/**
     * 成本费用计算时间
     */
	public Timestamp getCostCalculateTime() {
		return this.costCalculateTime;
	}

    /**
     * 成本费用计算时间
     *
     * @param costCalculateTime
     */
	public void setCostCalculateTime(Timestamp costCalculateTime) {
		this.costCalculateTime = costCalculateTime;
	}
	
	/**
     * 写入BMS时间
     */
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

    /**
     * 写入BMS时间
     *
     * @param writeTime
     */
	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	/**
     * weight
     */
	public BigDecimal getWeight() {
		return this.weight;
	}

    /**
     * weight
     *
     * @param weight
     */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
	/**
     * SpecDesc
     */
	public String getSpecDesc() {
		return this.specDesc;
	}

    /**
     * SpecDesc
     *
     * @param specDesc
     */
	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}
	
	/**
     * 拓展字段1
     */
	public String getExtattr1() {
		return this.extattr1;
	}

    /**
     * 拓展字段1
     *
     * @param extattr1
     */
	public void setExtattr1(String extattr1) {
		this.extattr1 = extattr1;
	}
	
	/**
     * 拓展字段2
     */
	public String getExtattr2() {
		return this.extattr2;
	}

    /**
     * 拓展字段2
     *
     * @param extattr2
     */
	public void setExtattr2(String extattr2) {
		this.extattr2 = extattr2;
	}
	
	/**
     * 拓展字段3
     */
	public String getExtattr3() {
		return this.extattr3;
	}

    /**
     * 拓展字段3
     *
     * @param extattr3
     */
	public void setExtattr3(String extattr3) {
		this.extattr3 = extattr3;
	}
	
	/**
     * 拓展字段4
     */
	public String getExtattr4() {
		return this.extattr4;
	}

    /**
     * 拓展字段4
     *
     * @param extattr4
     */
	public void setExtattr4(String extattr4) {
		this.extattr4 = extattr4;
	}
	
	/**
     * 拓展字段5
     */
	public String getExtattr5() {
		return this.extattr5;
	}

    /**
     * 拓展字段5
     *
     * @param extattr5
     */
	public void setExtattr5(String extattr5) {
		this.extattr5 = extattr5;
	}
	
	/**
     * 成本费用计算备注
     */
	public String getCostRemark() {
		return this.costRemark;
	}

    /**
     * 成本费用计算备注
     *
     * @param costRemark
     */
	public void setCostRemark(String costRemark) {
		this.costRemark = costRemark;
	}

	public int getRowExcelNo() {
		return rowExcelNo;
	}

	public void setRowExcelNo(int rowExcelNo) {
		this.rowExcelNo = rowExcelNo;
	}

	public String getRowExcelName() {
		return rowExcelName;
	}

	public void setRowExcelName(String rowExcelName) {
		this.rowExcelName = rowExcelName;
	}

    public String getPackGroupNo() {
        return packGroupNo;
    }

    public void setPackGroupNo(String packGroupNo) {
        this.packGroupNo = packGroupNo;
    }
    
}
