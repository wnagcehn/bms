package com.jiuyescm.bms.biz.storage.entity;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 耗材出库明细实体
 * 
 * @author yangshuaishuai
 * 
 */
public class BizOutstockPackmaterialEntity implements IEntity {

	// TODO Change serialVersionUID by eclipse
	private static final long serialVersionUID = -1;

	// id
	private Long id;
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
	// 数量
	private Double num;
	// 重量
	private Double weight;
	// 调整数量
	private Double adjustNum;
	// 费用ID
	private String feesNo;
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

	private String price;

	private String contractCode;

	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	//规格说明
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
	
	private String unitPrice;
	// 是否已计算成本费用 0-未计算 1-已计算 2-计算异常 3-单价缺失 4-多个报价
	private String costIsCalculated;
	private Timestamp costCalculateTime;
	private String costFeesNo;
	private String costRemark;
	//Excel的行号
	private int rowExcelNo;
	private String rowExcelName;
	
	private String materialType;
	// 转寄后运单号
    private String zexpressnum;
	
	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getSpecDesc() {
		return specDesc;
	}

	public void setSpecDesc(String specDesc) {
		this.specDesc = specDesc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWmsId() {
		return wmsId;
	}

	public void setWmsId(String wmsId) {
		this.wmsId = wmsId;
	}

	public String getOutstockNo() {
		return outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getConsumerMaterialCode() {
		return consumerMaterialCode;
	}

	public void setConsumerMaterialCode(String consumerMaterialCode) {
		this.consumerMaterialCode = consumerMaterialCode;
	}

	public String getConsumerMaterialName() {
		return consumerMaterialName;
	}

	public void setConsumerMaterialName(String consumerMaterialName) {
		this.consumerMaterialName = consumerMaterialName;
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

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
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
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
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

	public int getRowExcelNo() {
		return rowExcelNo;
	}

	public void setRowExcelNo(int rowExcelNo) {
		this.rowExcelNo = rowExcelNo;
	}

	public String getCostIsCalculated() {
		return costIsCalculated;
	}

	public void setCostIsCalculated(String costIsCalculated) {
		this.costIsCalculated = costIsCalculated;
	}

	public Timestamp getCostCalculateTime() {
		return costCalculateTime;
	}

	public void setCostCalculateTime(Timestamp costCalculateTime) {
		this.costCalculateTime = costCalculateTime;
	}

	public String getCostFeesNo() {
		return costFeesNo;
	}

	public void setCostFeesNo(String costFeesNo) {
		this.costFeesNo = costFeesNo;
	}

	public String getRowExcelName() {
		return rowExcelName;
	}

	public void setRowExcelName(String rowExcelName) {
		this.rowExcelName = rowExcelName;
	}

	public String getCostRemark() {
		return costRemark;
	}

	public void setCostRemark(String costRemark) {
		this.costRemark = costRemark;
	}

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getZexpressnum() {
        return zexpressnum;
    }

    public void setZexpressnum(String zexpressnum) {
        this.zexpressnum = zexpressnum;
    }
    
    
}
