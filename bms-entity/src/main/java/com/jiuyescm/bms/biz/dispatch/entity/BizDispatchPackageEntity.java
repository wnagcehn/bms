package com.jiuyescm.bms.biz.dispatch.entity;

import java.sql.Timestamp;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BizDispatchPackageEntity implements IEntity {

	/**
     * 
     */
    private static final long serialVersionUID = 9222627284729984253L;
    // 主键Id
	private Long id;
	
	// 商家名称
	@ExcelField(title = "商家", num = 1)
	private String customerName;
	
	// 仓库名称
	@ExcelField(title = "仓库", num = 2)
	private String warehouseName;
	
	// 出库单号
	@ExcelField(title = "出库单号", num = 3)
	private String outstockNo;
	
	// 运单号
	@ExcelField(title = "运单号", num = 4)
	private String waybillNo;
	
	// 包装方案编号
	@ExcelField(title = "包装方案编号", num = 5)
    private String packPlanNo;
	
	// 泡沫箱型号
	@ExcelField(title = "泡沫箱型号", num = 6)
	private String packBoxType;
	
	// 操作分类
	@ExcelField(title = "操作分类", num = 7)
    private String packOperateType;
	
	// 运输方式
	@ExcelField(title = "操作分类", num = 8)
    private String transportType;
	
	// 配送温区
	@ExcelField(title = "配送温区", num = 9)
    private String transportTemperatureType;
	
	// 保温时效
	@ExcelField(title = "保温时效", num = 10)
    private String holdingTime;
	
	// 季节
	@ExcelField(title = "季节", num = 11)
    private String season;
	
	// 包材组编号
	@ExcelField(title = "包材组编号", num = 12)
    private String packGroupNo;
	
	// 包装方案名称
	@ExcelField(title = "包装方案名称", num = 13)
    private String packPlanName;
	
	// 费用编号
	@ExcelField(title = "费用编号", num = 14)
    private String feesNo;
	
	//费用
	@ExcelField(title = "金额", num = 15)
    private Double cost;
	
	// 创建时间
	@ExcelField(title = "创建时间", num = 16)
    private String creTimeExport;
	
	// 计算状态
	@ExcelField(title = "计算状态", num = 17)
    private String isCalculated;
	
	// 备注
	@ExcelField(title = "计算备注", num = 18)
    private String remark;
	
	// 仓库id
	private String warehouseCode;	
	// 商家id
	private String customerid;
	// 作废标识
	private String delFlag;
	// 写入BMS时间
	private Timestamp writeTime;
	// 费用计算时间
	private Timestamp calculateTime;
	// 创建人
	private String crePerson;
	// 创建人ID
	private String crePersonId;

	private Timestamp creEndTime;
	// 修改人
	private String modPerson;
	// 修改人ID
	private String modPersonId;
	// 修改时间
	private Timestamp modTime;
	private Timestamp creTime;
	


	public BizDispatchPackageEntity() {
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

    public Timestamp getCreEndTime() {
        return creEndTime;
    }

    public void setCreEndTime(Timestamp creEndTime) {
        this.creEndTime = creEndTime;
    }

    public String getPackPlanName() {
        return packPlanName;
    }

    public void setPackPlanName(String packPlanName) {
        this.packPlanName = packPlanName;
    }

    public String getCreTimeExport() {
        return creTimeExport;
    }

    public void setCreTimeExport(String creTimeExport) {
        this.creTimeExport = creTimeExport;
    }
    
}
