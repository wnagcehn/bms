package com.jiuyescm.bms.fees.out.transport.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.cfm.domain.IEntity;

public class FeesPayTransportQueryEntity implements IEntity {

	private static final long serialVersionUID = -7109713514421711957L;
	private List<String> customerIdList;
	private String projectId;//项目ID
	private String projectName;//项目名称
	private String feesNo;// 费用编号
	private Timestamp operationtime;// 操作时间
	private String warehouseCode;// 仓库Id
	private String forwarderId;// 承运商Id
	private String forwarderName;//承运商名称
	private String costType;// 费用类型
	private String subjectCode;// 费用科目
	private String subjectName;//费用名称
	private String otherSubjectCode;//费用科目明细
	private String otherSubjectName;//费用科目明细名称
	private String linename;// 线路名称
	private String orderno;// 订单号
	private String waybillNo;// 运单号
	private String originatingcity;// 始发市
	private String originatingdistrict;// 始发区
	private String targetwarehouse;// 目的仓
	private String targetcity;// 目的市
	private String targetdistrict;// 目的区
	private String temperaturetype;// 温度类别
	private String category;// 品类
	private double weight;// 重量
	private double volume;// 体积
	private double kilometers;// 公里数
	private double spendtime;// 花费时间
	private String carmodel;// 车型
	private String templatenum;// 模板编号
	private int islight;// 是否轻货 0-不是 1-是
	private double unitprice;// 单价
	private double totleprice;// 金额
	private Timestamp accepttime;// 揽收时间
	private Timestamp signtime;// 签收时间
	private String billno;// 账单编号
	private String ruleno;// 规则编号
	private String state;// 状态
	private String creperson;// 创建人ID
	private String crepersonname;// 创建人姓名
	private Timestamp cretime;
	private Timestamp crestime;// 创建时间
	private Timestamp creetime;// 创建时间止
	
	
	private Timestamp operationstime;
	private Timestamp operationetime;

	private List<String> ordernoList;//
	private List<String> waybillNolist;
	private List<String> billnolist;
	private List<String> rulenolist;
	private Timestamp signstime;
	private Timestamp signetime;
	private String customerId;
	private String customerName;
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getKilometers() {
		return kilometers;
	}

	public void setKilometers(double kilometers) {
		this.kilometers = kilometers;
	}

	public double getSpendtime() {
		return spendtime;
	}

	public void setSpendtime(double spendtime) {
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

	public double getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(double unitprice) {
		this.unitprice = unitprice;
	}

	public double getTotleprice() {
		return totleprice;
	}

	public void setTotleprice(double totleprice) {
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

	public Timestamp getCrestime() {
		return crestime;
	}

	public void setCrestime(Timestamp crestime) {
		this.crestime = crestime;
	}

	public Timestamp getCreetime() {
		return creetime;
	}

	public void setCreetime(Timestamp creetime) {
		this.creetime = creetime;
	}

	public List<String> getOrdernoList() {
		return ordernoList;
	}

	public void setOrdernoList(List<String> ordernoList) {
		this.ordernoList = ordernoList;
	}

	public List<String> getWaybillNolist() {
		return waybillNolist;
	}

	public void setWaybillNolist(List<String> waybillNolist) {
		this.waybillNolist = waybillNolist;
	}

	public List<String> getBillnolist() {
		return billnolist;
	}

	public void setBillnolist(List<String> billnolist) {
		this.billnolist = billnolist;
	}

	public List<String> getRulenolist() {
		return rulenolist;
	}

	public void setRulenolist(List<String> rulenolist) {
		this.rulenolist = rulenolist;
	}

	public Timestamp getSignstime() {
		return signstime;
	}

	public void setSignstime(Timestamp signstime) {
		this.signstime = signstime;
	}

	public Timestamp getSignetime() {
		return signetime;
	}

	public void setSignetime(Timestamp signetime) {
		this.signetime = signetime;
	}


	public String getForwarderId() {
		return forwarderId;
	}

	public void setForwarderId(String forwarderId) {
		this.forwarderId = forwarderId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getOtherSubjectCode() {
		return otherSubjectCode;
	}

	public void setOtherSubjectCode(String otherSubjectCode) {
		this.otherSubjectCode = otherSubjectCode;
	}

	public String getOtherSubjectName() {
		return otherSubjectName;
	}

	public void setOtherSubjectName(String otherSubjectName) {
		this.otherSubjectName = otherSubjectName;
	}

	public Timestamp getOperationstime() {
		return operationstime;
	}

	public void setOperationstime(Timestamp operationstime) {
		this.operationstime = operationstime;
	}

	public Timestamp getOperationetime() {
		return operationetime;
	}

	public void setOperationetime(Timestamp operationetime) {
		this.operationetime = operationetime;
	}

	public String getForwarderName() {
		return forwarderName;
	}

	public void setForwarderName(String forwarderName) {
		this.forwarderName = forwarderName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public List<String> getCustomerIdList() {
		return customerIdList;
	}

	public void setCustomerIdList(List<String> customerIdList) {
		this.customerIdList = customerIdList;
	}

}
