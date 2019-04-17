package com.jiuyescm.bms.biz.diapatch.vo;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Vo
 * @author wangchen
 * 
 */
public class BizDispatchPackageVo implements IEntity {

	/**
     * 
     */
    private static final long serialVersionUID = 9222627284729984253L;
    // 主键Id
	private Long id;
	// 运单号
	private String waybillNo;
	// 出库单号
	private String outstockNo;
	// 仓库id
	private String warehouseCode;
	// 仓库名称
	private String warehouseName;
	// 商家id
	private String customerid;
	// 商家名称
	private String customerName;
	// 运输方式
	private String transportType;
	// 配送温区
	private String transportTemperatureType;
	// 保温时效
	private String holdingTime;
	// 操作分类
	private String packOperateType;
	// 季节
	private String season;
	// 泡沫箱型号
	private String packBoxType;
	// 包材组编号
	private String packGroupNo;
	// 包装方案编号
	private String packPlanNo;
	// 费用编号
	private String feesNo;
	// 计算状态
	private String isCalculated;
	// 作废标识
	private String delFlag;
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	// 备注
	private String remark;
	// 创建人
	private String crePerson;
	// 创建人ID
	private String crePersonId;
	// 创建时间
	private Timestamp creTime;
	// 修改人
	private String modPerson;
	// 修改人ID
	private String modPersonId;
	// 修改时间
	private Timestamp modTime;
	
	//金额
	private Double cost;
	
	//包材方案名称
	private String packPlanName;

	public BizDispatchPackageVo() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	public String getOutstockNo() {
		return this.outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
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
	
	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getTransportType() {
		return this.transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	
	public String getTransportTemperatureType() {
		return this.transportTemperatureType;
	}

	public void setTransportTemperatureType(String transportTemperatureType) {
		this.transportTemperatureType = transportTemperatureType;
	}
	
	public String getHoldingTime() {
		return this.holdingTime;
	}

	public void setHoldingTime(String holdingTime) {
		this.holdingTime = holdingTime;
	}
	
	public String getPackOperateType() {
		return this.packOperateType;
	}

	public void setPackOperateType(String packOperateType) {
		this.packOperateType = packOperateType;
	}
	
	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	
	public String getPackBoxType() {
		return this.packBoxType;
	}

	public void setPackBoxType(String packBoxType) {
		this.packBoxType = packBoxType;
	}
	
	public String getPackGroupNo() {
		return this.packGroupNo;
	}

	public void setPackGroupNo(String packGroupNo) {
		this.packGroupNo = packGroupNo;
	}
	
	public String getPackPlanNo() {
		return this.packPlanNo;
	}

	public void setPackPlanNo(String packPlanNo) {
		this.packPlanNo = packPlanNo;
	}
	
	public String getFeesNo() {
		return this.feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public Timestamp getCalculateTime() {
		return this.calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCrePerson() {
		return this.crePerson;
	}

	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	public String getCrePersonId() {
		return this.crePersonId;
	}

	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	public String getModPerson() {
		return this.modPerson;
	}

	public void setModPerson(String modPerson) {
		this.modPerson = modPerson;
	}
	
	public String getModPersonId() {
		return this.modPersonId;
	}

	public void setModPersonId(String modPersonId) {
		this.modPersonId = modPersonId;
	}
	
	public Timestamp getModTime() {
		return this.modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getPackPlanName() {
        return packPlanName;
    }

    public void setPackPlanName(String packPlanName) {
        this.packPlanName = packPlanName;
    }
    
}
