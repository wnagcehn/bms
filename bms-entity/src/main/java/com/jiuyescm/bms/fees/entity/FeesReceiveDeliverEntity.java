package com.jiuyescm.bms.fees.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 运输费用实体
 * 
 * @author Wuliangfeng add 20170527
 */
public class FeesReceiveDeliverEntity implements IEntity {

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
	// 商家Id
	private String customerid;
	// CustomerName
	private String customerName;
	// 承运商id
	private String forwarderId;
	// 承运商名称
	private String forwarderName;
	// 费用类型 INPUT-应收 OUTPUT-应付
	private String costType;
	// 费用科目
	private String subjectCode;
	// 增值费用科目
	private String otherSubjectCode;
	// 线路名称
	private String linename;
	// 订单号
	private String orderno;
	// 运单号
	private String waybillNo;
	// 始发省
	private String originProvince;
	// 始发市
	private String originatingcity;
	// 始发区
	private String originatingdistrict;
	// 目的仓
	private String targetwarehouse;
	//目的省份
	private String targetProvince;
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
	private int islight;
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
	// 是否已计算费用 0-未结算 1-已结算 2-结算异常 3-合同不存在 4-不在操作范围 5-其他
	private String isCalculated;
	// 费用计算时间
	private Timestamp calculateTime;
	// 有无回单
	private String isReceiveOrder;
	// 实收件数
	private String receiptNumber;
	// 退货
	private String returnGoods;
	// 配送类型
	private String dispatchType;
	// 货物类型
	private String goodType;
	// 订单件数
	private String orderNumber;
	// 订单箱数
	private String orderBoxno;
	// 产品
	private String productType;
	// 到达地
	private String receiveAddress;
	// 发货地
	private String sendAddress;
	// 路单号
	private String roadbillNo;
	// 外部订单号
	private String externalNo;
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
	//合计值
	private Double totalCost;
	// 发货时间
	private Timestamp sendTime;
	// 收货时间
	private Timestamp signTime;
	// 总重量
	private Double totalWeight;
	// 调整重量
	private Double adjustWeight;
	// 总体积
	private Double totalVolume;
	// 总件数
	private Double totalPackage;
	/*// 产品类型
    private String productType;*/
	// 总箱数
 	private Double totalBox; 	
	// 作废标识 0-未作废 1-已作废
	private String delFlag;
 	
	public Double getTotalBox() {
		return totalBox;
	}

	public void setTotalBox(Double totalBox) {
		this.totalBox = totalBox;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public Timestamp getSignTime() {
		return signTime;
	}

	public void setSignTime(Timestamp signTime) {
		this.signTime = signTime;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Double getAdjustWeight() {
		return adjustWeight;
	}

	public void setAdjustWeight(Double adjustWeight) {
		this.adjustWeight = adjustWeight;
	}

	public Double getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(Double totalVolume) {
		this.totalVolume = totalVolume;
	}

	public Double getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(Double totalPackage) {
		this.totalPackage = totalPackage;
	}

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

	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
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

	public int getIslight() {
		return islight;
	}

	public void setIslight(int islight) {
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

	public String getOriginProvince() {
		return originProvince;
	}

	public void setOriginProvince(String originProvince) {
		this.originProvince = originProvince;
	}

	public String getTargetProvince() {
		return targetProvince;
	}

	public void setTargetProvince(String targetProvince) {
		this.targetProvince = targetProvince;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
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

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getIsReceiveOrder() {
		return isReceiveOrder;
	}

	public void setIsReceiveOrder(String isReceiveOrder) {
		this.isReceiveOrder = isReceiveOrder;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getReturnGoods() {
		return returnGoods;
	}

	public void setReturnGoods(String returnGoods) {
		this.returnGoods = returnGoods;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderBoxno() {
		return orderBoxno;
	}

	public void setOrderBoxno(String orderBoxno) {
		this.orderBoxno = orderBoxno;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
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
	
	
}
