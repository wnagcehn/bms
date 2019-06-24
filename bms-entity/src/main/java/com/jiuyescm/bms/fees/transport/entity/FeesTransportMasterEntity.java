package com.jiuyescm.bms.fees.transport.entity;

import java.sql.Timestamp;
import java.math.BigDecimal;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class FeesTransportMasterEntity implements IEntity {

    
	/**
     * 
     */
    private static final long serialVersionUID = -991992554937349013L;
    // 编号
	private Long id;
	// omsid
	private Long omsId;
	// 运输订单号
	@ExcelField(title = "订单号", num = 2)
	private String orderNo;
	// 外部订单号(来源：OMS，为出库单号)
	@ExcelField(title = "外部订单号", num = 3)
	private String outstockNo;
	// 温控标准编号
	@ExcelField(title = "温控", num = 4)
	private String temperatureTypeCode;
	// 伙伴编码
	private String customerId;
	// 伙伴名称
	@ExcelField(title = "商家全称", num = 8)
	private String customerName;
	// 承运产品
	@ExcelField(title = "承运产品", num = 9)
	private String projectName;
	// 始发站
	@ExcelField(title = "始发站", num = 10)
	private String sendSite;
	// 始发省份
	@ExcelField(title = "始发省份", num = 11)
	private String sendProvince;
	// 始发城市
	@ExcelField(title = "始发城市", num = 12)
	private String sendCity;
	// 始发区
	@ExcelField(title = "始发区", num = 13)
	private String sendDistrict;
	// 目的站
	@ExcelField(title = "目的站", num = 14)
	private String receiveSite;
	// 目的省份
	@ExcelField(title = "目的省份", num = 15)
	private String receiveProvince;
	// 目的城市
	@ExcelField(title = "目的城市", num = 16)
	private String receiveCity;
	// 目的区
	@ExcelField(title = "目的区", num = 17)
	private String receiveDistrict;
	// 目的地址
	@ExcelField(title = "目的地址", num = 18)
	private String receiveAddress;
	// 订单创建日期
	@ExcelField(title = "订单创建日", num = 1)
	private Timestamp createdDt;
	// 体积
	@ExcelField(title = "体积", num = 19)
	private BigDecimal actualVolume;
	// 重量
	@ExcelField(title = "重量", num = 20)
	private BigDecimal actualWeight;
	// 实发箱数
	@ExcelField(title = "实发箱数", num = 23)
	private BigDecimal actualPackingQty;
	// 实发件数
	@ExcelField(title = "实发件数", num = 24)
	private BigDecimal actualGoodsQty;
	// 实收箱数
	@ExcelField(title = "实收箱数", num = 25)
	private BigDecimal receiptPackingQty;
	// 实收件数
	@ExcelField(title = "实收件数", num = 25)
	private BigDecimal receiptGoodsQty;
	// 发货日期
	@ExcelField(title = "发货日期", num = 27)
	private Timestamp beginTime;
	// 收货日期
	@ExcelField(title = "收货日期", num = 28)
	private Timestamp endTime;
	// 应付总计
	private BigDecimal paymentTotle;
	// 客户是否需要保险
	@ExcelField(title = "客户是否需要保险", num = 57)
	private String needInsurance;
	// 订单来源：OMS、TMS
	private String orderSourceCode;
	// 是否泡货:是:1 ，否:0
	@ExcelField(title = "是否泡货", num = 21)
	private Integer light;
	// 是否退货:是:1 ，否:0
	@ExcelField(title = "是否退货", num = 26)
	private Integer hasBacktrack;
	// 备注
	@ExcelField(title = "备注", num = 58)
	private String remark;
	// TMS上传时间
	private Timestamp uploadTime;
	// 创建时间
	private Timestamp creTime;
	// 创建人
	private String crePerson;
	// N=未同步；Y=已同步；F=同步失败
	private String syncFlag;
	// BMS同步时间
	private Timestamp syncTime;
	// 同步次数
	private Integer syncCount;
	
	//派车单号
	@ExcelField(title = "派车单号", num = 5)
	private String routeNo;
	//调度人
	@ExcelField(title = "调度人", num = 6)
	private String dispatcherName;
	//交接单号
	@ExcelField(title = "运单号", num = 7)
	private String transportNo;
	//车型
	@ExcelField(title = "车型", num = 22)
	private String capacityTypeCode;
	
	//提货费
	@ExcelField(title = "提货费", num = 29)
	private Double tsTakes;
	//运费
	@ExcelField(title = "运费", num = 30)
	private Double tsTransAmount;
	//送货费
	@ExcelField(title = "送货费", num = 31)
	private Double tsSend;
	//装货费
	@ExcelField(title = "装货费", num = 32)
	private Double ysZh;
	//卸货费
	@ExcelField(title = "卸货费", num = 33)
	private Double ysXh;
	//逆向物流费
	@ExcelField(title = "逆向物流费", num = 34)
	private Double tsReverseLogistic;
	//延时等待费
	@ExcelField(title = "延时等待费", num = 35)
	private Double tsDelayWaiting;
	//缠绕膜费
	@ExcelField(title = "缠绕膜费", num = 36)
	private Double tsWrappingFilm;
	//放空费
	@ExcelField(title = "放空费", num = 37)
	private Double tsEmptying;
	//赔付费
	@ExcelField(title = "赔付费", num = 38)
	private Double tsClaim;
	//纸面回单费
	@ExcelField(title = "纸面回单费", num = 39)
	private Double ysZmhd;
	//拆箱费
	@ExcelField(title = "拆箱费", num = 40)
	private Double ysCx;
	//贴码费
	@ExcelField(title = "贴码费", num = 41)
	private Double ysTm;
	//保险费
	@ExcelField(title = "保险费", num = 42)
	private Double tsInsurance;
	//分流费
	@ExcelField(title = "分流费", num = 43)
	private Double tsFl;
	//上楼搬运费
	@ExcelField(title = "上楼搬运费", num = 44)
	private Double tsSlby;
	//过夜制冷费
	@ExcelField(title = "过夜制冷费", num = 45)
	private Double tsGyzl;
	//单据打印费
	@ExcelField(title = "单据打印费", num = 46)
	private Double tsDjdy;
	//加点费
	@ExcelField(title = "加点费", num = 47)
	private Double tsAddSite;
	//中转费
	@ExcelField(title = "中转费", num = 48)
	private Double tsZz;
	//垫付费
	@ExcelField(title = "垫付费", num = 49)
	private Double tsDf;
	//押车费
	@ExcelField(title = "押车费", num = 50)
	private Double tsYc;
	//理货费
	@ExcelField(title = "理货费", num = 51)
	private Double tsTallying;
	//交货费
	@ExcelField(title = "交货费", num = 52)
	private Double tsJh;
	//过路费
	@ExcelField(title = "过路费", num = 53)
	private Double tsGl;
	//码货费
	@ExcelField(title = "码货费", num = 54)
	private Double tsMh;
	//托盘费
	@ExcelField(title = "托盘费", num = 55)
	private Double tsPallet;
	//应付总计
	@ExcelField(title = "应付总计", num = 56)
	private Double payTotalAmount;

	public FeesTransportMasterEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getOmsId() {
		return this.omsId;
	}

	public void setOmsId(Long omsId) {
		this.omsId = omsId;
	}
	
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getOutstockNo() {
		return this.outstockNo;
	}

	public void setOutstockNo(String outstockNo) {
		this.outstockNo = outstockNo;
	}
	
	public String getTemperatureTypeCode() {
		return this.temperatureTypeCode;
	}

	public void setTemperatureTypeCode(String temperatureTypeCode) {
		this.temperatureTypeCode = temperatureTypeCode;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getSendSite() {
		return this.sendSite;
	}

	public void setSendSite(String sendSite) {
		this.sendSite = sendSite;
	}
	
	public String getSendProvince() {
		return this.sendProvince;
	}

	public void setSendProvince(String sendProvince) {
		this.sendProvince = sendProvince;
	}
	
	public String getSendCity() {
		return this.sendCity;
	}

	public void setSendCity(String sendCity) {
		this.sendCity = sendCity;
	}
	
	public String getSendDistrict() {
		return this.sendDistrict;
	}

	public void setSendDistrict(String sendDistrict) {
		this.sendDistrict = sendDistrict;
	}
	
	public String getReceiveSite() {
		return this.receiveSite;
	}

	public void setReceiveSite(String receiveSite) {
		this.receiveSite = receiveSite;
	}
	
	public String getReceiveProvince() {
		return this.receiveProvince;
	}

	public void setReceiveProvince(String receiveProvince) {
		this.receiveProvince = receiveProvince;
	}
	
	public String getReceiveCity() {
		return this.receiveCity;
	}

	public void setReceiveCity(String receiveCity) {
		this.receiveCity = receiveCity;
	}
	
	public String getReceiveDistrict() {
		return this.receiveDistrict;
	}

	public void setReceiveDistrict(String receiveDistrict) {
		this.receiveDistrict = receiveDistrict;
	}
	
	public String getReceiveAddress() {
		return this.receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	
	public Timestamp getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Timestamp createdDt) {
		this.createdDt = createdDt;
	}
	
	public BigDecimal getActualVolume() {
		return this.actualVolume;
	}

	public void setActualVolume(BigDecimal actualVolume) {
		this.actualVolume = actualVolume;
	}
	
	public BigDecimal getActualWeight() {
		return this.actualWeight;
	}

	public void setActualWeight(BigDecimal actualWeight) {
		this.actualWeight = actualWeight;
	}
	
	public BigDecimal getActualPackingQty() {
		return this.actualPackingQty;
	}

	public void setActualPackingQty(BigDecimal actualPackingQty) {
		this.actualPackingQty = actualPackingQty;
	}
	
	public BigDecimal getActualGoodsQty() {
		return this.actualGoodsQty;
	}

	public void setActualGoodsQty(BigDecimal actualGoodsQty) {
		this.actualGoodsQty = actualGoodsQty;
	}
	
	public BigDecimal getReceiptPackingQty() {
		return this.receiptPackingQty;
	}

	public void setReceiptPackingQty(BigDecimal receiptPackingQty) {
		this.receiptPackingQty = receiptPackingQty;
	}
	
	public BigDecimal getReceiptGoodsQty() {
		return this.receiptGoodsQty;
	}

	public void setReceiptGoodsQty(BigDecimal receiptGoodsQty) {
		this.receiptGoodsQty = receiptGoodsQty;
	}
	
	public Timestamp getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public BigDecimal getPaymentTotle() {
		return this.paymentTotle;
	}

	public void setPaymentTotle(BigDecimal paymentTotle) {
		this.paymentTotle = paymentTotle;
	}
	
	public String getNeedInsurance() {
		return this.needInsurance;
	}

	public void setNeedInsurance(String needInsurance) {
		this.needInsurance = needInsurance;
	}
	
	public String getOrderSourceCode() {
		return this.orderSourceCode;
	}

	public void setOrderSourceCode(String orderSourceCode) {
		this.orderSourceCode = orderSourceCode;
	}
	
	public Integer getLight() {
		return this.light;
	}

	public void setLight(Integer light) {
		this.light = light;
	}
	
	public Integer getHasBacktrack() {
		return this.hasBacktrack;
	}

	public void setHasBacktrack(Integer hasBacktrack) {
		this.hasBacktrack = hasBacktrack;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Timestamp getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	public String getCrePerson() {
		return this.crePerson;
	}

	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	
	public Timestamp getSyncTime() {
		return this.syncTime;
	}

	public void setSyncTime(Timestamp syncTime) {
		this.syncTime = syncTime;
	}
	
	public Integer getSyncCount() {
		return this.syncCount;
	}

	public void setSyncCount(Integer syncCount) {
		this.syncCount = syncCount;
	}

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }

    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }

    public String getCapacityTypeCode() {
        return capacityTypeCode;
    }

    public void setCapacityTypeCode(String capacityTypeCode) {
        this.capacityTypeCode = capacityTypeCode;
    }

    public Double getTsTakes() {
        return tsTakes;
    }

    public void setTsTakes(Double tsTakes) {
        this.tsTakes = tsTakes;
    }

    public Double getTsTransAmount() {
        return tsTransAmount;
    }

    public void setTsTransAmount(Double tsTransAmount) {
        this.tsTransAmount = tsTransAmount;
    }

    public Double getTsSend() {
        return tsSend;
    }

    public void setTsSend(Double tsSend) {
        this.tsSend = tsSend;
    }

    public Double getYsZh() {
        return ysZh;
    }

    public void setYsZh(Double ysZh) {
        this.ysZh = ysZh;
    }

    public Double getYsXh() {
        return ysXh;
    }

    public void setYsXh(Double ysXh) {
        this.ysXh = ysXh;
    }

    public Double getTsReverseLogistic() {
        return tsReverseLogistic;
    }

    public void setTsReverseLogistic(Double tsReverseLogistic) {
        this.tsReverseLogistic = tsReverseLogistic;
    }

    public Double getTsDelayWaiting() {
        return tsDelayWaiting;
    }

    public void setTsDelayWaiting(Double tsDelayWaiting) {
        this.tsDelayWaiting = tsDelayWaiting;
    }

    public Double getTsWrappingFilm() {
        return tsWrappingFilm;
    }

    public void setTsWrappingFilm(Double tsWrappingFilm) {
        this.tsWrappingFilm = tsWrappingFilm;
    }

    public Double getTsEmptying() {
        return tsEmptying;
    }

    public void setTsEmptying(Double tsEmptying) {
        this.tsEmptying = tsEmptying;
    }

    public Double getTsClaim() {
        return tsClaim;
    }

    public void setTsClaim(Double tsClaim) {
        this.tsClaim = tsClaim;
    }

    public Double getYsZmhd() {
        return ysZmhd;
    }

    public void setYsZmhd(Double ysZmhd) {
        this.ysZmhd = ysZmhd;
    }

    public Double getYsCx() {
        return ysCx;
    }

    public void setYsCx(Double ysCx) {
        this.ysCx = ysCx;
    }

    public Double getYsTm() {
        return ysTm;
    }

    public void setYsTm(Double ysTm) {
        this.ysTm = ysTm;
    }

    public Double getTsInsurance() {
        return tsInsurance;
    }

    public void setTsInsurance(Double tsInsurance) {
        this.tsInsurance = tsInsurance;
    }

    public Double getTsFl() {
        return tsFl;
    }

    public void setTsFl(Double tsFl) {
        this.tsFl = tsFl;
    }

    public Double getTsSlby() {
        return tsSlby;
    }

    public void setTsSlby(Double tsSlby) {
        this.tsSlby = tsSlby;
    }

    public Double getTsGyzl() {
        return tsGyzl;
    }

    public void setTsGyzl(Double tsGyzl) {
        this.tsGyzl = tsGyzl;
    }

    public Double getTsDjdy() {
        return tsDjdy;
    }

    public void setTsDjdy(Double tsDjdy) {
        this.tsDjdy = tsDjdy;
    }

    public Double getTsAddSite() {
        return tsAddSite;
    }

    public void setTsAddSite(Double tsAddSite) {
        this.tsAddSite = tsAddSite;
    }

    public Double getTsZz() {
        return tsZz;
    }

    public void setTsZz(Double tsZz) {
        this.tsZz = tsZz;
    }

    public Double getTsDf() {
        return tsDf;
    }

    public void setTsDf(Double tsDf) {
        this.tsDf = tsDf;
    }

    public Double getTsYc() {
        return tsYc;
    }

    public void setTsYc(Double tsYc) {
        this.tsYc = tsYc;
    }

    public Double getTsTallying() {
        return tsTallying;
    }

    public void setTsTallying(Double tsTallying) {
        this.tsTallying = tsTallying;
    }

    public Double getTsJh() {
        return tsJh;
    }

    public void setTsJh(Double tsJh) {
        this.tsJh = tsJh;
    }

    public Double getTsGl() {
        return tsGl;
    }

    public void setTsGl(Double tsGl) {
        this.tsGl = tsGl;
    }

    public Double getTsMh() {
        return tsMh;
    }

    public void setTsMh(Double tsMh) {
        this.tsMh = tsMh;
    }

    public Double getTsPallet() {
        return tsPallet;
    }

    public void setTsPallet(Double tsPallet) {
        this.tsPallet = tsPallet;
    }

    public Double getPayTotalAmount() {
        return payTotalAmount;
    }

    public void setPayTotalAmount(Double payTotalAmount) {
        this.payTotalAmount = payTotalAmount;
    }
    
	
}
