package com.jiuyescm.bms.fees.out.transport.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class FeesPayTransportImportEntity implements IEntity {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6912557326096638158L;
	private String orderno;
	private String waybillno;
	private String customername;
	private String orgaddress;
	private String targetadress;
	private String productdetails;
	private Integer boxnum;
	private Integer ordernum;
	private Double weight;
	private Double volume;
	private String carmodel;
	private Integer quantity;
	private String distributiontype;
	private Timestamp accepttime;
	private Timestamp signtime;
	private Integer backnum;
	private Integer receivenum;
	private String hasreceipt;
	private String pickupcharge;
	private String freight;
	private String deliverycharges;
	private String dischargingcharges;
	private String reverselogisticsfee;
	private String compensation;
	private String forwardername;
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getWaybillno() {
		return waybillno;
	}
	public void setWaybillno(String waybillno) {
		this.waybillno = waybillno;
	}
	public String getCustomername() {
		return customername;
	}
	public void setCustomername(String customername) {
		this.customername = customername;
	}
	public String getOrgaddress() {
		return orgaddress;
	}
	public void setOrgaddress(String orgaddress) {
		this.orgaddress = orgaddress;
	}
	public String getTargetadress() {
		return targetadress;
	}
	public void setTargetadress(String targetadress) {
		this.targetadress = targetadress;
	}
	public String getProductdetails() {
		return productdetails;
	}
	public void setProductdetails(String productdetails) {
		this.productdetails = productdetails;
	}
	public Integer getBoxnum() {
		return boxnum;
	}
	public void setBoxnum(Integer boxnum) {
		this.boxnum = boxnum;
	}
	public Integer getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
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
	public String getCarmodel() {
		return carmodel;
	}
	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getDistributiontype() {
		return distributiontype;
	}
	public void setDistributiontype(String distributiontype) {
		this.distributiontype = distributiontype;
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
	public Integer getBacknum() {
		return backnum;
	}
	public void setBacknum(Integer backnum) {
		this.backnum = backnum;
	}
	public Integer getReceivenum() {
		return receivenum;
	}
	public void setReceivenum(Integer receivenum) {
		this.receivenum = receivenum;
	}
	public String getHasreceipt() {
		return hasreceipt;
	}
	public void setHasreceipt(String hasreceipt) {
		this.hasreceipt = hasreceipt;
	}
	public String getPickupcharge() {
		return pickupcharge;
	}
	public void setPickupcharge(String pickupcharge) {
		this.pickupcharge = pickupcharge;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getDeliverycharges() {
		return deliverycharges;
	}
	public void setDeliverycharges(String deliverycharges) {
		this.deliverycharges = deliverycharges;
	}
	public String getDischargingcharges() {
		return dischargingcharges;
	}
	public void setDischargingcharges(String dischargingcharges) {
		this.dischargingcharges = dischargingcharges;
	}
	public String getReverselogisticsfee() {
		return reverselogisticsfee;
	}
	public void setReverselogisticsfee(String reverselogisticsfee) {
		this.reverselogisticsfee = reverselogisticsfee;
	}
	public String getCompensation() {
		return compensation;
	}
	public void setCompensation(String compensation) {
		this.compensation = compensation;
	}
	public String getForwardername() {
		return forwardername;
	}
	public void setForwardername(String forwardername) {
		this.forwardername = forwardername;
	}
	
}
