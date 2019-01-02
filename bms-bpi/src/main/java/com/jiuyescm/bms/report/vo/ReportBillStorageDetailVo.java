package com.jiuyescm.bms.report.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class ReportBillStorageDetailVo implements IEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5908767788130666565L;
	//组别
	private String area;
	//对账月份
	private Integer createMonth;
	//客户名称
	private String invoiceName;
	//账单名称
	private String billName;
	//账单
	private String bill;
	//一级品类
	private String firstClassName;
	//业务类型
	private String bizTypeName;
	//项目
	private String projectName;
	//销售员
	private String sellerName;
	//项目管理员
	private String projectManagerName;
	//结算员
	private String balanceName;
	//账单状态
	private String billStatus;
	//账单确认日期
	private String billConfirmDate;
	//开票状态
	private String invoiceStatus;
	//备注
	private String remark;
	//差异率超5%说明
	private String instruction;
	//快递单号
	private String waybillNo;
	//开票金额
	private BigDecimal invoiceAmount;
	//开票日期
	private Timestamp invoiceDate;
	//开票明细-仓储
	private BigDecimal invoiceStorage;
	//开票明细-干线
	private BigDecimal invoiceTransport;
	//开票明细-宅配费用
	private BigDecimal invoiceDispatch;
	//开票明细-航空
	private BigDecimal invoiceAir;
	//开票明细-索赔
	private BigDecimal invoiceAbnormal;
	//理赔-仓储
	private BigDecimal abnormalStorage;
	//理赔-干线
	private BigDecimal abnormalTransport;
	//理赔-宅配费用
	private BigDecimal abnormalDispatch;
	//理赔-航空
	private BigDecimal abnormalAir;
	//运输费用-九曳自送（元）
	private BigDecimal jiuyeAmount;
	//运输费用-九曳自送（单量）
	private Double jiuyeCount;
	//运输费用-顺丰（元）
	private BigDecimal shunfengAmount;
	//运输费用-顺丰（单量）
	private Double shunfengCount;
	//运输费用-圆通（元）
	private BigDecimal yuantongAmount;
	//运输费用-圆通（单量）
	private Double yuantongCount;
	//运输费用-中通（元）
	private BigDecimal zhongtongAmount;
	//运输费用-中通（单量）
	private Double zhongtongCount;
	//单量合计
	private Double totalCount;
	//仓储费-存储托盘数
	private Double storagePallet;
	//仓储费-出库托盘数
	private Double outstockPallet;
	//仓储费-仓租费
	private BigDecimal rentFee;
	//仓储费-操作费
	private BigDecimal outstockFee;
	//仓储费-包材费
	private BigDecimal materialFee;
	//仓储费-增值
	private BigDecimal addFee;
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getCreateMonth() {
		return createMonth;
	}
	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	public String getInvoiceName() {
		return invoiceName;
	}
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public String getFirstClassName() {
		return firstClassName;
	}
	public void setFirstClassName(String firstClassName) {
		this.firstClassName = firstClassName;
	}
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getProjectManagerName() {
		return projectManagerName;
	}
	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	public String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getBillConfirmDate() {
		return billConfirmDate;
	}
	public void setBillConfirmDate(String billConfirmDate) {
		this.billConfirmDate = billConfirmDate;
	}
	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public BigDecimal getInvoiceStorage() {
		return invoiceStorage;
	}
	public void setInvoiceStorage(BigDecimal invoiceStorage) {
		this.invoiceStorage = invoiceStorage;
	}
	public BigDecimal getInvoiceTransport() {
		return invoiceTransport;
	}
	public void setInvoiceTransport(BigDecimal invoiceTransport) {
		this.invoiceTransport = invoiceTransport;
	}
	public BigDecimal getInvoiceDispatch() {
		return invoiceDispatch;
	}
	public void setInvoiceDispatch(BigDecimal invoiceDispatch) {
		this.invoiceDispatch = invoiceDispatch;
	}
	public BigDecimal getInvoiceAir() {
		return invoiceAir;
	}
	public void setInvoiceAir(BigDecimal invoiceAir) {
		this.invoiceAir = invoiceAir;
	}
	public BigDecimal getInvoiceAbnormal() {
		return invoiceAbnormal;
	}
	public void setInvoiceAbnormal(BigDecimal invoiceAbnormal) {
		this.invoiceAbnormal = invoiceAbnormal;
	}
	public BigDecimal getAbnormalStorage() {
		return abnormalStorage;
	}
	public void setAbnormalStorage(BigDecimal abnormalStorage) {
		this.abnormalStorage = abnormalStorage;
	}
	public BigDecimal getAbnormalTransport() {
		return abnormalTransport;
	}
	public void setAbnormalTransport(BigDecimal abnormalTransport) {
		this.abnormalTransport = abnormalTransport;
	}
	public BigDecimal getAbnormalDispatch() {
		return abnormalDispatch;
	}
	public void setAbnormalDispatch(BigDecimal abnormalDispatch) {
		this.abnormalDispatch = abnormalDispatch;
	}
	public BigDecimal getAbnormalAir() {
		return abnormalAir;
	}
	public void setAbnormalAir(BigDecimal abnormalAir) {
		this.abnormalAir = abnormalAir;
	}
	
	
	public BigDecimal getJiuyeAmount() {
		return jiuyeAmount;
	}
	public void setJiuyeAmount(BigDecimal jiuyeAmount) {
		this.jiuyeAmount = jiuyeAmount;
	}
	public Double getJiuyeCount() {
		return jiuyeCount;
	}
	public void setJiuyeCount(Double jiuyeCount) {
		this.jiuyeCount = jiuyeCount;
	}
	public BigDecimal getShunfengAmount() {
		return shunfengAmount;
	}
	public void setShunfengAmount(BigDecimal shunfengAmount) {
		this.shunfengAmount = shunfengAmount;
	}
	public Double getShunfengCount() {
		return shunfengCount;
	}
	public void setShunfengCount(Double shunfengCount) {
		this.shunfengCount = shunfengCount;
	}
	public BigDecimal getYuantongAmount() {
		return yuantongAmount;
	}
	public void setYuantongAmount(BigDecimal yuantongAmount) {
		this.yuantongAmount = yuantongAmount;
	}
	public Double getYuantongCount() {
		return yuantongCount;
	}
	public void setYuantongCount(Double yuantongCount) {
		this.yuantongCount = yuantongCount;
	}
	public BigDecimal getZhongtongAmount() {
		return zhongtongAmount;
	}
	public void setZhongtongAmount(BigDecimal zhongtongAmount) {
		this.zhongtongAmount = zhongtongAmount;
	}
	public Double getZhongtongCount() {
		return zhongtongCount;
	}
	public void setZhongtongCount(Double zhongtongCount) {
		this.zhongtongCount = zhongtongCount;
	}
	public Double getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}
	public Double getStoragePallet() {
		return storagePallet;
	}
	public void setStoragePallet(Double storagePallet) {
		this.storagePallet = storagePallet;
	}
	public Double getOutstockPallet() {
		return outstockPallet;
	}
	public void setOutstockPallet(Double outstockPallet) {
		this.outstockPallet = outstockPallet;
	}
	public BigDecimal getRentFee() {
		return rentFee;
	}
	public void setRentFee(BigDecimal rentFee) {
		this.rentFee = rentFee;
	}
	public BigDecimal getOutstockFee() {
		return outstockFee;
	}
	public void setOutstockFee(BigDecimal outstockFee) {
		this.outstockFee = outstockFee;
	}
	public BigDecimal getMaterialFee() {
		return materialFee;
	}
	public void setMaterialFee(BigDecimal materialFee) {
		this.materialFee = materialFee;
	}
	public BigDecimal getAddFee() {
		return addFee;
	}
	public void setAddFee(BigDecimal addFee) {
		this.addFee = addFee;
	}
	
	
}
