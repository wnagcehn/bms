package com.jiuyescm.bms.fees.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author Wuliangfeng20170602 应收账单关联费用实体
 */
public class FeesBillReceiveEntity implements IEntity {
	
	private static final long serialVersionUID = 5421135322528627844L;

	private String billNo;// 账单编号
	private String billname;// 账单名称
	private String customerid;// 商家ID
	private String customerName;// 商家ID
	private Timestamp billstarttime;// 账单起始时间
	private Timestamp billendtime;// 账单结束时间
	private List<FeesReceiveDeliverEntity> feesDeliverList;// 运输费列表
	private List<FeesReceiveStorageEntity> feesStorageList;// 仓储费
	private List<FeesReceiveDispatchEntity> feesDispatchList;// 配送费
	private List<FeesAbnormalEntity> feesAbnormalList;// 异常费

	
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillname() {
		return billname;
	}

	public void setBillname(String billname) {
		this.billname = billname;
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

	public Timestamp getBillstarttime() {
		return billstarttime;
	}

	public void setBillstarttime(Timestamp billstarttime) {
		this.billstarttime = billstarttime;
	}

	public Timestamp getBillendtime() {
		return billendtime;
	}

	public void setBillendtime(Timestamp billendtime) {
		this.billendtime = billendtime;
	}

	public List<FeesReceiveDeliverEntity> getFeesDeliverList() {
		return feesDeliverList;
	}

	public void setFeesDeliverList(
			List<FeesReceiveDeliverEntity> feesDeliverList) {
		this.feesDeliverList = feesDeliverList;
	}

	public List<FeesReceiveStorageEntity> getFeesStorageList() {
		return feesStorageList;
	}

	public void setFeesStorageList(
			List<FeesReceiveStorageEntity> feesStorageList) {
		this.feesStorageList = feesStorageList;
	}

	public List<FeesReceiveDispatchEntity> getFeesDispatchList() {
		return feesDispatchList;
	}

	public void setFeesDispatchList(List<FeesReceiveDispatchEntity> feesDispatchList) {
		this.feesDispatchList = feesDispatchList;
	}

	public List<FeesAbnormalEntity> getFeesAbnormalList() {
		return feesAbnormalList;
	}

	public void setFeesAbnormalList(List<FeesAbnormalEntity> feesAbnormalList) {
		this.feesAbnormalList = feesAbnormalList;
	}

}
