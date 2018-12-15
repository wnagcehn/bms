package com.jiuyescm.bms.report.bill;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class ReportBillBizDetailEntity implements IEntity{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -1300869856650216747L;
	//组别
	private String area;
	//对账月份
	private Integer createMonth;
	//账单名称
	private String billName;
	//账单
	private String bill;
	//开票日期
	private Timestamp invoiceDate;
	//结算员
	private String balanceName;
	//仓库名称
	private String warehouseName;
	//收入小计-仓储
	private BigDecimal receiveStorage;
	//收入小计-宅配
	private BigDecimal receiveDispatch;
	//理赔小计-仓储
	private BigDecimal abnormalStorage;
	//理赔小计-宅配
	private BigDecimal abnormalDispatch;
	//仓库-存储托盘数-常温
	private Double cwPallet;
	//仓库-存储托盘数-恒温
	private Double hwPallet;
	//仓库-存储托盘数-冷冻
	private Double ldPallet;
	//仓库-存储托盘数-冷藏
	private Double lcPallet;
	//仓库-存储托盘数-小计
	private Double totalPallet;
	//仓库-出库托盘数
	private Double outstockPallet;
	//仓库-仓租费-常温
	private BigDecimal cwRent;
	//仓库-仓租费-恒温
	private BigDecimal hwRent;
	//仓库-仓租费-冷冻
	private BigDecimal ldRent;
	//仓库-仓租费-冷藏
	private BigDecimal lcRent;
	//仓库-仓租费-小计
	private BigDecimal totalRent;
	//仓库-操作费
	private BigDecimal outstockFee;
	//仓库-耗材费
	private BigDecimal materialFee;
	//仓库-增值费
	private BigDecimal addFee;
	//仓库-理赔
	private BigDecimal abnormalFee;
	
	//宅配-九曳自送-金额
	private BigDecimal jiuyeAmount;
	//宅配-九曳自送-单量
	private Double jiuyeCount;
	//宅配-顺丰-金额
	private BigDecimal shunfengAmount;
	//宅配-顺丰-单量
	private Double shunfengCount;
	//宅配-跨越-金额
	private BigDecimal kuayueAmount;
	//宅配-跨越-单量
	private Double kuayueCount;
	//宅配-圆通-金额
	private BigDecimal yuantongAmount;
	//宅配-圆通-单量
	private Double yuantongCount;	
	//宅配-中通-金额
	private BigDecimal zhongtongAmount;
	//宅配-中通-单量
	private Double zhongtongCount;
	//宅配-申通-金额
	private BigDecimal shentongAmount;
	//宅配-申通-单量
	private Double shentongCount;
	//宅配-韵达-金额
	private BigDecimal yundaAmount;
	//宅配-韵达-单量
	private Double yundaCount;
	//宅配-小计-金额
	private BigDecimal totalAmount;
	//宅配-小计-单量
	private Double totalCount;	
	//宅配-理赔-金额
	private BigDecimal abnormalAmount;
	//宅配-理赔-单量
	private Double abnormalCount;
	//TB出库箱数
	private Double tbOutstockBox;
	//常温库单价
	private BigDecimal cwPrice;
	//恒温库单价
	private BigDecimal hwPrice;
	//冷冻库单价
	private BigDecimal ldPrice;
	//冷藏库单价
	private BigDecimal lcPrice;
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
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public BigDecimal getReceiveStorage() {
		return receiveStorage;
	}
	public void setReceiveStorage(BigDecimal receiveStorage) {
		this.receiveStorage = receiveStorage;
	}
	public BigDecimal getReceiveDispatch() {
		return receiveDispatch;
	}
	public void setReceiveDispatch(BigDecimal receiveDispatch) {
		this.receiveDispatch = receiveDispatch;
	}
	public BigDecimal getAbnormalStorage() {
		return abnormalStorage;
	}
	public void setAbnormalStorage(BigDecimal abnormalStorage) {
		this.abnormalStorage = abnormalStorage;
	}
	public BigDecimal getAbnormalDispatch() {
		return abnormalDispatch;
	}
	public void setAbnormalDispatch(BigDecimal abnormalDispatch) {
		this.abnormalDispatch = abnormalDispatch;
	}
	public Double getCwPallet() {
		return cwPallet;
	}
	public void setCwPallet(Double cwPallet) {
		this.cwPallet = cwPallet;
	}
	public Double getHwPallet() {
		return hwPallet;
	}
	public void setHwPallet(Double hwPallet) {
		this.hwPallet = hwPallet;
	}
	public Double getLdPallet() {
		return ldPallet;
	}
	public void setLdPallet(Double ldPallet) {
		this.ldPallet = ldPallet;
	}
	public Double getLcPallet() {
		return lcPallet;
	}
	public void setLcPallet(Double lcPallet) {
		this.lcPallet = lcPallet;
	}
	public Double getTotalPallet() {
		return totalPallet;
	}
	public void setTotalPallet(Double totalPallet) {
		this.totalPallet = totalPallet;
	}
	public Double getOutstockPallet() {
		return outstockPallet;
	}
	public void setOutstockPallet(Double outstockPallet) {
		this.outstockPallet = outstockPallet;
	}
	public BigDecimal getCwRent() {
		return cwRent;
	}
	public void setCwRent(BigDecimal cwRent) {
		this.cwRent = cwRent;
	}
	public BigDecimal getHwRent() {
		return hwRent;
	}
	public void setHwRent(BigDecimal hwRent) {
		this.hwRent = hwRent;
	}
	public BigDecimal getLdRent() {
		return ldRent;
	}
	public void setLdRent(BigDecimal ldRent) {
		this.ldRent = ldRent;
	}
	public BigDecimal getLcRent() {
		return lcRent;
	}
	public void setLcRent(BigDecimal lcRent) {
		this.lcRent = lcRent;
	}
	public BigDecimal getTotalRent() {
		return totalRent;
	}
	public void setTotalRent(BigDecimal totalRent) {
		this.totalRent = totalRent;
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
	public BigDecimal getAbnormalFee() {
		return abnormalFee;
	}
	public void setAbnormalFee(BigDecimal abnormalFee) {
		this.abnormalFee = abnormalFee;
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
	public BigDecimal getKuayueAmount() {
		return kuayueAmount;
	}
	public void setKuayueAmount(BigDecimal kuayueAmount) {
		this.kuayueAmount = kuayueAmount;
	}
	public Double getKuayueCount() {
		return kuayueCount;
	}
	public void setKuayueCount(Double kuayueCount) {
		this.kuayueCount = kuayueCount;
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
	public BigDecimal getShentongAmount() {
		return shentongAmount;
	}
	public void setShentongAmount(BigDecimal shentongAmount) {
		this.shentongAmount = shentongAmount;
	}
	public Double getShentongCount() {
		return shentongCount;
	}
	public void setShentongCount(Double shentongCount) {
		this.shentongCount = shentongCount;
	}
	public BigDecimal getYundaAmount() {
		return yundaAmount;
	}
	public void setYundaAmount(BigDecimal yundaAmount) {
		this.yundaAmount = yundaAmount;
	}
	public Double getYundaCount() {
		return yundaCount;
	}
	public void setYundaCount(Double yundaCount) {
		this.yundaCount = yundaCount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Double getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Double totalCount) {
		this.totalCount = totalCount;
	}
	public BigDecimal getAbnormalAmount() {
		return abnormalAmount;
	}
	public void setAbnormalAmount(BigDecimal abnormalAmount) {
		this.abnormalAmount = abnormalAmount;
	}
	public Double getAbnormalCount() {
		return abnormalCount;
	}
	public void setAbnormalCount(Double abnormalCount) {
		this.abnormalCount = abnormalCount;
	}
	public Double getTbOutstockBox() {
		return tbOutstockBox;
	}
	public void setTbOutstockBox(Double tbOutstockBox) {
		this.tbOutstockBox = tbOutstockBox;
	}
	public BigDecimal getCwPrice() {
		return cwPrice;
	}
	public void setCwPrice(BigDecimal cwPrice) {
		this.cwPrice = cwPrice;
	}
	public BigDecimal getHwPrice() {
		return hwPrice;
	}
	public void setHwPrice(BigDecimal hwPrice) {
		this.hwPrice = hwPrice;
	}
	public BigDecimal getLdPrice() {
		return ldPrice;
	}
	public void setLdPrice(BigDecimal ldPrice) {
		this.ldPrice = ldPrice;
	}
	public BigDecimal getLcPrice() {
		return lcPrice;
	}
	public void setLcPrice(BigDecimal lcPrice) {
		this.lcPrice = lcPrice;
	}


}
