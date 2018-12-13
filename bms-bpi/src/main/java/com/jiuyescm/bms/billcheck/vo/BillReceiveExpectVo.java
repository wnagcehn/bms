package com.jiuyescm.bms.billcheck.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class BillReceiveExpectVo implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = -9159079217667915424L;
	// id
	private Long id;
	//账单编号
	private String billNo;
	//业务月份
	private Integer createMonth;
	//账单名称
	private String billName;
	// 预计金额
	private BigDecimal expectMoney;
	// 预估仓储
	private BigDecimal expectStorageMoney;
	// 预估配送
	private BigDecimal expectDispatchMoney;
	// 预估干线
	private BigDecimal expectTransportMoney;
	// 预估航空
	private BigDecimal expectAirMoney;
	// 创建人
	private String creator;
	// 任务创建时间
	private Timestamp createTime;
	// 作废标识
	private String delFlag;

	public BillReceiveExpectVo() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getExpectMoney() {
		return this.expectMoney;
	}

	public void setExpectMoney(BigDecimal expectMoney) {
		this.expectMoney = expectMoney;
	}
	
	public BigDecimal getExpectStorageMoney() {
		return this.expectStorageMoney;
	}

	public void setExpectStorageMoney(BigDecimal expectStorageMoney) {
		this.expectStorageMoney = expectStorageMoney;
	}
	
	public BigDecimal getExpectDispatchMoney() {
		return this.expectDispatchMoney;
	}

	public void setExpectDispatchMoney(BigDecimal expectDispatchMoney) {
		this.expectDispatchMoney = expectDispatchMoney;
	}
	
	public BigDecimal getExpectTransportMoney() {
		return this.expectTransportMoney;
	}

	public void setExpectTransportMoney(BigDecimal expectTransportMoney) {
		this.expectTransportMoney = expectTransportMoney;
	}
	
	public BigDecimal getExpectAirMoney() {
		return this.expectAirMoney;
	}

	public void setExpectAirMoney(BigDecimal expectAirMoney) {
		this.expectAirMoney = expectAirMoney;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
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
    
}
