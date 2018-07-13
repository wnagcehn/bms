package com.jiuyescm.bms.fees.bill.out.entity;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.entity.FeesPayAbnormalEntity;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;
import com.jiuyescm.bms.fees.out.transport.entity.FeesPayTransportEntity;
import com.jiuyescm.bms.pub.deliver.entity.DeliverEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 应付账单封装实体
 * 
 * @author yangshuaishuai
 */
public class FeesPayBillEncapEntity implements IEntity {

	private static final long serialVersionUID = 5421135322528627844L;

	private String billNo;// 账单编号
	private String billName;// 账单名称
	// 承运商ID
	private String forwarderId;
	// 承运商名称
	private String forwarderName;
	private String deliveryid;// 宅配商ID
	// 宅配商名称
	private String deliverName;
	private Timestamp startTime;// 账单起始时间
	private Timestamp endTime;// 账单结束时间
	private List<FeesPayTransportEntity> feesTransportList;// 运输费列表
	private List<FeesPayDispatchEntity> feesDispatchList;// 配送费
	private List<FeesPayAbnormalEntity> feesAbnormalList;// 异常费
	private List<FeesPayTransportEntity> feesAbnormalTransportList;//运输异常费

	private String deliverSelectlist;
	private List<DeliverEntity> deliverList;
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
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

	public String getDeliveryid() {
		return deliveryid;
	}

	public void setDeliveryid(String deliveryid) {
		this.deliveryid = deliveryid;
	}

	public String getDeliverName() {
		return deliverName;
	}

	public void setDeliverName(String deliverName) {
		this.deliverName = deliverName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public List<FeesPayTransportEntity> getFeesTransportList() {
		return feesTransportList;
	}

	public void setFeesTransportList(
			List<FeesPayTransportEntity> feesTransportList) {
		this.feesTransportList = feesTransportList;
	}

	public List<FeesPayDispatchEntity> getFeesDispatchList() {
		return feesDispatchList;
	}

	public void setFeesDispatchList(List<FeesPayDispatchEntity> feesDispatchList) {
		this.feesDispatchList = feesDispatchList;
	}

	public List<FeesPayAbnormalEntity> getFeesAbnormalList() {
		return feesAbnormalList;
	}

	public void setFeesAbnormalList(List<FeesPayAbnormalEntity> feesAbnormalList) {
		this.feesAbnormalList = feesAbnormalList;
	}

	public List<DeliverEntity> getDeliverList() {
		return deliverList;
	}

	public void setDeliverList(List<DeliverEntity> deliverList) {
		this.deliverList = deliverList;
	}

	public String getDeliverSelectlist() {
		return deliverSelectlist;
	}

	public void setDeliverSelectlist(String deliverSelectlist) {
		this.deliverSelectlist = deliverSelectlist;
	}

	public List<FeesPayTransportEntity> getFeesAbnormalTransportList() {
		return feesAbnormalTransportList;
	}

	public void setFeesAbnormalTransportList(
			List<FeesPayTransportEntity> feesAbnormalTransportList) {
		this.feesAbnormalTransportList = feesAbnormalTransportList;
	}

}
