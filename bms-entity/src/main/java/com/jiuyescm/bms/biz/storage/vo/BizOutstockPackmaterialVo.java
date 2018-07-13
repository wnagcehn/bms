package com.jiuyescm.bms.biz.storage.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author stevenl
 * 
 */
public class BizOutstockPackmaterialVo implements IEntity {

	private static final long serialVersionUID = 264147301793138046L;
	
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
	// 调整数量
	private Double adjustNum;
	// 费用ID
	private String feesNo;
	// 所属DB的名称
	private String dbname;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
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

	public BizOutstockPackmaterialVo() {

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
     * 数量
     */
	public Double getNum() {
		return this.num;
	}

    /**
     * 数量
     *
     * @param num
     */
	public void setNum(Double num) {
		this.num = num;
	}
	
	/**
     * 调整数量
     */
	public Double getAdjustNum() {
		return this.adjustNum;
	}

    /**
     * 调整数量
     *
     * @param adjustNum
     */
	public void setAdjustNum(Double adjustNum) {
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
    
}
