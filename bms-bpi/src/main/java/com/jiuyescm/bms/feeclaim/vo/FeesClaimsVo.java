package com.jiuyescm.bms.feeclaim.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.bms.excel.annotation.ExcelAnnotation.ExcelField;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class FeesClaimsVo implements IEntity {

 	// TODO please create serialVersionUID by eclipse
    private static final long serialVersionUID = -1L;
     
	// 唯一标识，自增
	private Long id;
    // 账单年份
    @ExcelField(title = "账单年份", num = 1)
    private String billYear;
    // 账单月份
    @ExcelField(title = "账单月份", num = 2)
    private String billMonth;  
    // 商家Id
    private String customerId;
    // 商家名称
    @ExcelField(title = "商家", num = 3)
    private String customerName;
    // 仓库Id
    private String warehouseCode;
    // 仓库名称
    @ExcelField(title = "仓库", num = 4)
    private String warehouseName;
    // 承运商名称Id
    private String carrierId;
    // 承运商名称
    @ExcelField(title = "承运商", num = 5)
    private String carrierName;
    // 宅配商Id
    private String deliverId;
    // 宅配商名称
    @ExcelField(title = "宅配商", num = 6)
    private String deliverName;
    // 工单编号
    @ExcelField(title = "工单号", num = 7)
    private String workOrderNo;
    // 出库单号
    @ExcelField(title = "出库单号", num = 8)
    private String outstockNo;
    // 运单号
    @ExcelField(title = "运单号", num = 9)
    private String waybillNo;
    // 外部订单号
    @ExcelField(title = "外部订单号", num = 10)
    private String externalNo;
    // 责任方
    @ExcelField(title = "责任方", num = 11)
    private String dutyType;
    // 赔付类型
    @ExcelField(title = "赔付类型", num = 12)
    private String payType;
    // 备注
    @ExcelField(title = "备注", num = 13)
    private String remark;
	// SCP主业务表ID
	private Long masterId;
	// 赔付ID
	private String payId;
	// 赔付方向 J2C-九曳赔偿商家 C2J-商家赔偿九曳 J2D-九曳赔承运商 D2J-承运商赔九曳
	private String payDirection;
	// 是否免运费 0-不免运费 1-免运费
    @ExcelField(title = "是否免运费", num = 15)
	private String isDeliveryFree;
	// 理赔商品金额
    @ExcelField(title = "理赔商品金额", num = 14)
	private Double productAmount;
	// 改地址退件费
    @ExcelField(title = "改地址退件费", num = 16)
	private Double returnedAmount;
    // 创建人
    @ExcelField(title = "创建人", num = 17)
    private String crePerson;
	// 处罚金额
	private BigDecimal amerceAmount;
	// 赔付日期
	@ExcelField(title = "客诉确认时间", num = 18)
	private Timestamp createTime;
	// 运单创建时间
	@ExcelField(title = "运单创建时间", num = 19)
    private Timestamp waybillTime;
	// 创建人ID
	private String crePersonId;


	// 修改人ID
	private String modPersonId;
	// 修改人
	private String modPerson;
	// 修改时间
	private Timestamp modTime;
	// 写入BMS时间
	private Timestamp writeTime;
	// 同步OMS时间
	private Timestamp syncTime;

	// 作废标识 0-未作废 1-已作废
	private String delFlag;

	public FeesClaimsVo() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getMasterId() {
		return this.masterId;
	}

	public void setMasterId(Long masterId) {
		this.masterId = masterId;
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
	
	public String getExternalNo() {
		return this.externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}
	
	public String getWorkOrderNo() {
		return this.workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	
	public String getPayId() {
		return this.payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}
	
	public String getDutyType() {
		return this.dutyType;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	
	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
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
	
	public String getCarrierId() {
		return this.carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	public String getCarrierName() {
		return this.carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	public String getDeliverId() {
		return this.deliverId;
	}

	public void setDeliverId(String deliverId) {
		this.deliverId = deliverId;
	}
	
	public String getDeliverName() {
		return this.deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}
	
	public String getPayDirection() {
		return this.payDirection;
	}

	public void setPayDirection(String payDirection) {
		this.payDirection = payDirection;
	}
		
	public String getIsDeliveryFree() {
        return isDeliveryFree;
    }

    public void setIsDeliveryFree(String isDeliveryFree) {
        this.isDeliveryFree = isDeliveryFree;
    }

	
	public Double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Double productAmount) {
        this.productAmount = productAmount;
    }

    public Double getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(Double returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public BigDecimal getAmerceAmount() {
		return this.amerceAmount;
	}

	public void setAmerceAmount(BigDecimal amerceAmount) {
		this.amerceAmount = amerceAmount;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getCrePersonId() {
		return this.crePersonId;
	}

	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	public String getCrePerson() {
		return this.crePerson;
	}

	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getModPersonId() {
		return this.modPersonId;
	}

	public void setModPersonId(String modPersonId) {
		this.modPersonId = modPersonId;
	}
	
	public String getModPerson() {
		return this.modPerson;
	}

	public void setModPerson(String modPerson) {
		this.modPerson = modPerson;
	}
	
	public Timestamp getModTime() {
		return this.modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}
	
	public Timestamp getWriteTime() {
		return this.writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}
	
	public Timestamp getSyncTime() {
		return this.syncTime;
	}

	public void setSyncTime(Timestamp syncTime) {
		this.syncTime = syncTime;
	}
	
	public String getBillYear() {
		return this.billYear;
	}

	public void setBillYear(String billYear) {
		this.billYear = billYear;
	}
	
	public String getBillMonth() {
		return this.billMonth;
	}

	public void setBillMonth(String billMonth) {
		this.billMonth = billMonth;
	}
	
	public Timestamp getWaybillTime() {
		return this.waybillTime;
	}

	public void setWaybillTime(Timestamp waybillTime) {
		this.waybillTime = waybillTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
