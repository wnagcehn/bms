package com.jiuyescm.bms.fees.out.transport.vo;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应付运输费用实体
 * @author wubangjun add 20170627
 */
public class FeesPayTransportVo implements IEntity {

	private static final long serialVersionUID = 4089978354310963123L;

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
	// 是否轻货 0-不是 1-是
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
	private String feessubjectname;// 费用科目名称

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeesNo() {
		return feesNo;
	}

	public void setFeesNo(String feesNo) {
		this.feesNo = feesNo;
	}

	public Timestamp getOperationtime() {
		return operationtime;
	}

	public void setOperationtime(Timestamp operationtime) {
		this.operationtime = operationtime;
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

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public String getOriginatingcity() {
		return originatingcity;
	}

	public void setOriginatingcity(String originatingcity) {
		this.originatingcity = originatingcity;
	}

	public String getOriginatingdistrict() {
		return originatingdistrict;
	}

	public void setOriginatingdistrict(String originatingdistrict) {
		this.originatingdistrict = originatingdistrict;
	}

	public String getTargetwarehouse() {
		return targetwarehouse;
	}

	public void setTargetwarehouse(String targetwarehouse) {
		this.targetwarehouse = targetwarehouse;
	}

	public String getTargetcity() {
		return targetcity;
	}

	public void setTargetcity(String targetcity) {
		this.targetcity = targetcity;
	}

	public String getTargetdistrict() {
		return targetdistrict;
	}

	public void setTargetdistrict(String targetdistrict) {
		this.targetdistrict = targetdistrict;
	}

	public String getTemperaturetype() {
		return temperaturetype;
	}

	public void setTemperaturetype(String temperaturetype) {
		this.temperaturetype = temperaturetype;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getKilometers() {
		return kilometers;
	}

	public void setKilometers(Double kilometers) {
		this.kilometers = kilometers;
	}

	public Double getSpendtime() {
		return spendtime;
	}

	public void setSpendtime(Double spendtime) {
		this.spendtime = spendtime;
	}

	public String getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}

	public String getTemplatenum() {
		return templatenum;
	}

	public void setTemplatenum(String templatenum) {
		this.templatenum = templatenum;
	}

	public Long getIslight() {
		return islight;
	}

	public void setIslight(Long islight) {
		this.islight = islight;
	}

	public Double getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Double unitprice) {
		this.unitprice = unitprice;
	}

	public Double getTotleprice() {
		return totleprice;
	}

	public void setTotleprice(Double totleprice) {
		this.totleprice = totleprice;
	}

	public Timestamp getAccepttime() {
		return accepttime;
	}

	public void setAccepttime(Timestamp accepttime) {
		this.accepttime = accepttime;
	}

	public Timestamp getSigntime() {
		return signtime;
	}

	public void setSigntime(Timestamp signtime) {
		this.signtime = signtime;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getRuleno() {
		return ruleno;
	}

	public void setRuleno(String ruleno) {
		this.ruleno = ruleno;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreperson() {
		return creperson;
	}

	public void setCreperson(String creperson) {
		this.creperson = creperson;
	}

	public String getCrepersonname() {
		return crepersonname;
	}

	public void setCrepersonname(String crepersonname) {
		this.crepersonname = crepersonname;
	}

	public Timestamp getCretime() {
		return cretime;
	}

	public void setCretime(Timestamp cretime) {
		this.cretime = cretime;
	}

	public String getModperson() {
		return modperson;
	}

	public void setModperson(String modperson) {
		this.modperson = modperson;
	}

	public String getModpersonname() {
		return modpersonname;
	}

	public void setModpersonname(String modpersonname) {
		this.modpersonname = modpersonname;
	}

	public Timestamp getModtime() {
		return modtime;
	}

	public void setModtime(Timestamp modtime) {
		this.modtime = modtime;
	}

	public String getExtarr1() {
		return extarr1;
	}

	public void setExtarr1(String extarr1) {
		this.extarr1 = extarr1;
	}

	public String getExtarr2() {
		return extarr2;
	}

	public void setExtarr2(String extarr2) {
		this.extarr2 = extarr2;
	}

	public String getExtarr3() {
		return extarr3;
	}

	public void setExtarr3(String extarr3) {
		this.extarr3 = extarr3;
	}

	public String getExtarr4() {
		return extarr4;
	}

	public void setExtarr4(String extarr4) {
		this.extarr4 = extarr4;
	}

	public String getExtarr5() {
		return extarr5;
	}

	public void setExtarr5(String extarr5) {
		this.extarr5 = extarr5;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getFeessubjectname() {
		return feessubjectname;
	}

	public void setFeessubjectname(String feessubjectname) {
		this.feessubjectname = feessubjectname;
	}
}
