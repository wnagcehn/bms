package com.jiuyescm.bms.general.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class FeesReceiveTransportEntity implements IEntity {

    // TODO Change serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增主键
	private Long id;
	// 费用编号
	private String feesNo;
	// 操作时间
	private Timestamp operationtime;
	// 仓库Id
	private String warehouseCode;
	// WarehouseName
	private String warehouseName;
	// 商家Id
	private String customerid;
	// CustomerName
	private String customerName;
	// 费用类型 INPUT-应收 OUTPUT-应付
	private String costType;
	// 费用科目
	private String subjectCode;
	private String otherSubjectCode;
	// 线路名称
	private String linename;
	// 订单号
	private String orderno;
	// 运单号
	private String waybillNo;
	// 始发市
	private String originatingcity;
	// 始发区
	private String originatingdistrict;
	// 目的仓
	private String targetwarehouse;
	// 目的市
	private String targetcity;
	// 目的区
	private String targetdistrict;
	// 温度类型
	private String temperaturetype;
	// 品类
	private String category;
	// 重量
	private Double weight;
	// 体积
	private Double volume;
	// 公里数
	private Double kilometers;
	// 花费时间
	private Double spendtime;
	// 车型
	private String carmodel;
	// 模板编号
	private String templatenum;
	// 是否轻货 1是;0不是
	private Long islight;
	// 单价
	private Double unitprice;
	// 金额
	private Double totleprice;
	// 揽收时间
	private Timestamp accepttime;
	// 签收时间
	private Timestamp signtime;
	// 账单编号
	private String billno;
	// 规则编号
	private String ruleno;
	// 状态
	private String state;
	// 创建人ID
	private String creperson;
	// 创建人姓名
	private String crepersonname;
	// 创建时间
	private Timestamp cretime;
	// 修改人ID
	private String modperson;
	// 修改人姓名
	private String modpersonname;
	// 修改时间
	private Timestamp modtime;
	// 状态(0 未过账 1已过账)
	private String status;
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	// 扩展字段1
	private String extarr1;
	// 扩展字段2
	private String extarr2;
	// 扩展字段3
	private String extarr3;
	// 扩展字段4
	private String extarr4;
	// 扩展字段5
	private String extarr5;
	// 数量
	private Integer quantity;
	//减免金额
	private BigDecimal derateAmount;
	//作废标识 0-未作废 1-已作废
	private String delFlag;
	
	public FeesReceiveTransportEntity() {

	}
	
	/**
     * 自增主键
     */
	public Long getId() {
		return this.id;
	}

    /**
     * 自增主键
     *
     * @param id
     */
	public void setId(Long id) {
		this.id = id;
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
     * 操作时间
     */
	public Timestamp getOperationtime() {
		return this.operationtime;
	}

    /**
     * 操作时间
     *
     * @param operationtime
     */
	public void setOperationtime(Timestamp operationtime) {
		this.operationtime = operationtime;
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
     * 费用类型 INPUT-应收 OUTPUT-应付
     */
	public String getCostType() {
		return this.costType;
	}

    /**
     * 费用类型 INPUT-应收 OUTPUT-应付
     *
     * @param costType
     */
	public void setCostType(String costType) {
		this.costType = costType;
	}
	
	/**
     * 费用科目
     */
	public String getSubjectCode() {
		return this.subjectCode;
	}

    /**
     * 费用科目
     *
     * @param subjectCode
     */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
     * 线路名称
     */
	public String getLinename() {
		return this.linename;
	}

    /**
     * 线路名称
     *
     * @param linename
     */
	public void setLinename(String linename) {
		this.linename = linename;
	}
	
	/**
     * 订单号
     */
	public String getOrderno() {
		return this.orderno;
	}

    /**
     * 订单号
     *
     * @param orderno
     */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
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
     * 始发市
     */
	public String getOriginatingcity() {
		return this.originatingcity;
	}

    /**
     * 始发市
     *
     * @param originatingcity
     */
	public void setOriginatingcity(String originatingcity) {
		this.originatingcity = originatingcity;
	}
	
	/**
     * 始发区
     */
	public String getOriginatingdistrict() {
		return this.originatingdistrict;
	}

    /**
     * 始发区
     *
     * @param originatingdistrict
     */
	public void setOriginatingdistrict(String originatingdistrict) {
		this.originatingdistrict = originatingdistrict;
	}
	
	/**
     * 目的仓
     */
	public String getTargetwarehouse() {
		return this.targetwarehouse;
	}

    /**
     * 目的仓
     *
     * @param targetwarehouse
     */
	public void setTargetwarehouse(String targetwarehouse) {
		this.targetwarehouse = targetwarehouse;
	}
	
	/**
     * 目的市
     */
	public String getTargetcity() {
		return this.targetcity;
	}

    /**
     * 目的市
     *
     * @param targetcity
     */
	public void setTargetcity(String targetcity) {
		this.targetcity = targetcity;
	}
	
	/**
     * 目的区
     */
	public String getTargetdistrict() {
		return this.targetdistrict;
	}

    /**
     * 目的区
     *
     * @param targetdistrict
     */
	public void setTargetdistrict(String targetdistrict) {
		this.targetdistrict = targetdistrict;
	}
	
	/**
     * 温度类型
     */
	public String getTemperaturetype() {
		return this.temperaturetype;
	}

    /**
     * 温度类型
     *
     * @param temperaturetype
     */
	public void setTemperaturetype(String temperaturetype) {
		this.temperaturetype = temperaturetype;
	}
	
	/**
     * 品类
     */
	public String getCategory() {
		return this.category;
	}

    /**
     * 品类
     *
     * @param category
     */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
     * 重量
     */
	public Double getWeight() {
		return this.weight;
	}

    /**
     * 重量
     *
     * @param weight
     */
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	/**
     * 体积
     */
	public Double getVolume() {
		return this.volume;
	}

    /**
     * 体积
     *
     * @param volume
     */
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	/**
     * 公里数
     */
	public Double getKilometers() {
		return this.kilometers;
	}

    /**
     * 公里数
     *
     * @param kilometers
     */
	public void setKilometers(Double kilometers) {
		this.kilometers = kilometers;
	}
	
	/**
     * 花费时间
     */
	public Double getSpendtime() {
		return this.spendtime;
	}

    /**
     * 花费时间
     *
     * @param spendtime
     */
	public void setSpendtime(Double spendtime) {
		this.spendtime = spendtime;
	}
	
	/**
     * 车型
     */
	public String getCarmodel() {
		return this.carmodel;
	}

    /**
     * 车型
     *
     * @param carmodel
     */
	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}
	
	/**
     * 模板编号
     */
	public String getTemplatenum() {
		return this.templatenum;
	}

    /**
     * 模板编号
     *
     * @param templatenum
     */
	public void setTemplatenum(String templatenum) {
		this.templatenum = templatenum;
	}
	
	/**
     * 是否轻货 0-不是 1-是
     */
	public Long getIslight() {
		return this.islight;
	}

    /**
     * 是否轻货 0-不是 1-是
     *
     * @param islight
     */
	public void setIslight(Long islight) {
		this.islight = islight;
	}
	
	/**
     * 单价
     */
	public Double getUnitprice() {
		return this.unitprice;
	}

    /**
     * 单价
     *
     * @param unitprice
     */
	public void setUnitprice(Double unitprice) {
		this.unitprice = unitprice;
	}
	
	/**
     * 金额
     */
	public Double getTotleprice() {
		return this.totleprice;
	}

    /**
     * 金额
     *
     * @param totleprice
     */
	public void setTotleprice(Double totleprice) {
		this.totleprice = totleprice;
	}
	
	/**
     * 揽收时间
     */
	public Timestamp getAccepttime() {
		return this.accepttime;
	}

    /**
     * 揽收时间
     *
     * @param accepttime
     */
	public void setAccepttime(Timestamp accepttime) {
		this.accepttime = accepttime;
	}
	
	/**
     * 签收时间
     */
	public Timestamp getSigntime() {
		return this.signtime;
	}

    /**
     * 签收时间
     *
     * @param signtime
     */
	public void setSigntime(Timestamp signtime) {
		this.signtime = signtime;
	}
	
	/**
     * 账单编号
     */
	public String getBillno() {
		return this.billno;
	}

    /**
     * 账单编号
     *
     * @param billno
     */
	public void setBillno(String billno) {
		this.billno = billno;
	}
	
	/**
     * 规则编号
     */
	public String getRuleno() {
		return this.ruleno;
	}

    /**
     * 规则编号
     *
     * @param ruleno
     */
	public void setRuleno(String ruleno) {
		this.ruleno = ruleno;
	}
	
	/**
     * 状态
     */
	public String getState() {
		return this.state;
	}

    /**
     * 状态
     *
     * @param state
     */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
     * 创建人ID
     */
	public String getCreperson() {
		return this.creperson;
	}

    /**
     * 创建人ID
     *
     * @param creperson
     */
	public void setCreperson(String creperson) {
		this.creperson = creperson;
	}
	
	/**
     * 创建人姓名
     */
	public String getCrepersonname() {
		return this.crepersonname;
	}

    /**
     * 创建人姓名
     *
     * @param crepersonname
     */
	public void setCrepersonname(String crepersonname) {
		this.crepersonname = crepersonname;
	}
	
	/**
     * 创建时间
     */
	public Timestamp getCretime() {
		return this.cretime;
	}

    /**
     * 创建时间
     *
     * @param cretime
     */
	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}
	
	/**
     * 修改人ID
     */
	public String getModperson() {
		return this.modperson;
	}

    /**
     * 修改人ID
     *
     * @param modperson
     */
	public void setModperson(String modperson) {
		this.modperson = modperson;
	}
	
	/**
     * 修改人姓名
     */
	public String getModpersonname() {
		return this.modpersonname;
	}

    /**
     * 修改人姓名
     *
     * @param modpersonname
     */
	public void setModpersonname(String modpersonname) {
		this.modpersonname = modpersonname;
	}
	
	/**
     * 修改时间
     */
	public Timestamp getModtime() {
		return this.modtime;
	}

    /**
     * 修改时间
     *
     * @param modtime
     */
	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}
	
	/**
     * 状态(0 未过账 1已过账)
     */
	public String getStatus() {
		return this.status;
	}

    /**
     * 状态(0 未过账 1已过账)
     *
     * @param status
     */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
     * 扩展字段1
     */
	public String getExtarr1() {
		return this.extarr1;
	}

    /**
     * 扩展字段1
     *
     * @param extarr1
     */
	public void setExtarr1(String extarr1) {
		this.extarr1 = extarr1;
	}
	
	/**
     * 扩展字段2
     */
	public String getExtarr2() {
		return this.extarr2;
	}

    /**
     * 扩展字段2
     *
     * @param extarr2
     */
	public void setExtarr2(String extarr2) {
		this.extarr2 = extarr2;
	}
	
	/**
     * 扩展字段3
     */
	public String getExtarr3() {
		return this.extarr3;
	}

    /**
     * 扩展字段3
     *
     * @param extarr3
     */
	public void setExtarr3(String extarr3) {
		this.extarr3 = extarr3;
	}
	
	/**
     * 扩展字段4
     */
	public String getExtarr4() {
		return this.extarr4;
	}

    /**
     * 扩展字段4
     *
     * @param extarr4
     */
	public void setExtarr4(String extarr4) {
		this.extarr4 = extarr4;
	}
	
	/**
     * 扩展字段5
     */
	public String getExtarr5() {
		return this.extarr5;
	}

    /**
     * 扩展字段5
     *
     * @param extarr5
     */
	public void setExtarr5(String extarr5) {
		this.extarr5 = extarr5;
	}
	
	/**
     * 数量
     */
	public Integer getQuantity() {
		return this.quantity;
	}

    /**
     * 数量
     *
     * @param quantity
     */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}

	public String getIsCalculated() {
		return isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}

	public Timestamp getCalculateTime() {
		return calculateTime;
	}

	public void setCalculateTime(Timestamp calculateTime) {
		this.calculateTime = calculateTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}
    
}
