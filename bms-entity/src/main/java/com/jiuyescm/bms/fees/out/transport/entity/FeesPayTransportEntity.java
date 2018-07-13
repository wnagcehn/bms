package com.jiuyescm.bms.fees.out.transport.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应付运输费用实体
 * @author wubangjun add 20170627
 */
public class FeesPayTransportEntity implements IEntity {

	private static final long serialVersionUID = 4089978354310963123L;

	private String projectId;//项目ID
	private String projectName;//项目名称
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
	// 承运商Code
	private String forwarderId;
	// 承运商Name
	private String forwarderName;
	// 费用类型 INPUT-应收 OUTPUT-应付
	private String costType;
	// 费用科目
	private String subjectCode;
	private String subjectcodename;
	
	private String otherSubjectCode;
	private String otherSubjectCodeName;
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
	private Integer islight;
	//是否轻货 名称
	private String isLightName;
	//泡重
	private Double bubbleWeight;
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
	private String customerId;//商家id
	private String customerName;//商家名称
	
	private String productdetails;//商品明细
	private String boxnum;//订单箱数
	private String ordernum;//订单件数
	private String backnum;//退货数量
	private String receivenum;//实收件数
	private String hasreceipt;//有无 回单 1-有  0-无
	
	// 配送类型
	private String dispatchType;
	// 货物类型
	private String goodType;
	// 产品
	private String productType;
	// 路单号
	private String roadbillNo;
	// 外部订单号
	private String externalNo;
	
	private String orgaddress;//始发地
	private String targetaddress;//目的地
	
	private String distributiontype;//业务类型
	private String hasreceiptname;//

	//合计值
	private Double totalCost;
	//作废标识 0-未作废 1-已作废
	private String delFlag;
	//减免金额
	private BigDecimal derateAmount;
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

	public Integer getIslight() {
		return islight;
	}

	public void setIslight(Integer islight) {
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

	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}

	public Double getBubbleWeight() {
		return bubbleWeight;
	}

	public void setBubbleWeight(Double bubbleWeight) {
		this.bubbleWeight = bubbleWeight;
	}

	public String getProductdetails() {
		return productdetails;
	}

	public void setProductdetails(String productdetails) {
		this.productdetails = productdetails;
	}

	public String getBoxnum() {
		return boxnum;
	}

	public void setBoxnum(String boxnum) {
		this.boxnum = boxnum;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getBacknum() {
		return backnum;
	}

	public void setBacknum(String backnum) {
		this.backnum = backnum;
	}

	public String getReceivenum() {
		return receivenum;
	}

	public void setReceivenum(String receivenum) {
		this.receivenum = receivenum;
	}

	public String getHasreceipt() {
		return hasreceipt;
	}

	public void setHasreceipt(String hasreceipt) {
		this.hasreceipt = hasreceipt;
	}

	public String getDistributiontype() {
		return distributiontype;
	}

	public void setDistributiontype(String distributiontype) {
		this.distributiontype = distributiontype;
	}

	public String getOtherSubjectCodeName() {
		return otherSubjectCodeName;
	}

	public void setOtherSubjectCodeName(String otherSubjectCodeName) {
		this.otherSubjectCodeName = otherSubjectCodeName;
	}

	public String getSubjectcodename() {
		return subjectcodename;
	}

	public void setSubjectcodename(String subjectcodename) {
		this.subjectcodename = subjectcodename;
	}

	public String getHasreceiptname() {
		return hasreceiptname;
	}

	public void setHasreceiptname(String hasreceiptname) {
		this.hasreceiptname = hasreceiptname;
	}

	public String getTargetaddress() {
		return targetaddress;
	}

	public void setTargetaddress(String targetaddress) {
		this.targetaddress = targetaddress;
	}

	public String getOrgaddress() {
		return orgaddress;
	}

	public void setOrgaddress(String orgaddress) {
		this.orgaddress = orgaddress;
	}

	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
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

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getIsLightName() {
		return isLightName;
	}

	public void setIsLightName(String isLightName) {
		this.isLightName = isLightName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public String getDispatchType() {
		return dispatchType;
	}

	public void setDispatchType(String dispatchType) {
		this.dispatchType = dispatchType;
	}

	public String getGoodType() {
		return goodType;
	}

	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getRoadbillNo() {
		return roadbillNo;
	}

	public void setRoadbillNo(String roadbillNo) {
		this.roadbillNo = roadbillNo;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	
}
