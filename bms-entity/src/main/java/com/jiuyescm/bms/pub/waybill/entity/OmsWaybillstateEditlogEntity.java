package com.jiuyescm.bms.pub.waybill.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 
 * @author cjw
 * 
 */
public class OmsWaybillstateEditlogEntity implements IEntity {

    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// oms订单号
	private String orderno;
	
	// 运单号
	private String waybillno;
	// 新状态
	private String newstatus;
	// 修改前状态
	private String oldstatus;
	// 修改时间
	private Timestamp writeTime;
	
	// 修改时间
	private Timestamp endpointtime;
	
	// 修改人用户id
	private String updatePersonid;
	// 同步到dms状态 0-未同步 1-已同步 2-同步失败
	private Long senddmsState;
	// 同步到dms时间
	private Timestamp senddmsTime;
	// 备注
	private String remark;
	
	private String operateDesc;

	public OmsWaybillstateEditlogEntity() {

	}
	
	/**
     * id
     */
	public Integer getId() {
		return this.id;
	}

    /**
     * id
     *
     * @param id
     */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
     * oms订单号
     */
	public String getOrderno() {
		return this.orderno;
	}

    /**
     * oms订单号
     *
     * @param orderno
     */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	
	/**
     * 新状态
     */
	public String getNewstatus() {
		return this.newstatus;
	}

    /**
     * 新状态
     *
     * @param newstatus
     */
	public void setNewstatus(String newstatus) {
		this.newstatus = newstatus;
	}
	
	/**
     * 修改前状态
     */
	public String getOldstatus() {
		return this.oldstatus;
	}

    /**
     * 修改前状态
     *
     * @param oldstatus
     */
	public void setOldstatus(String oldstatus) {
		this.oldstatus = oldstatus;
	}
	
	/**
     * 修改人用户id
     */
	public String getUpdatePersonid() {
		return this.updatePersonid;
	}

    /**
     * 修改人用户id
     *
     * @param updatePersonid
     */
	public void setUpdatePersonid(String updatePersonid) {
		this.updatePersonid = updatePersonid;
	}
	
	/**
     * 同步到dms状态 0-未同步 1-已同步 2-同步失败
     */
	public Long getSenddmsState() {
		return this.senddmsState;
	}

    /**
     * 同步到dms状态 0-未同步 1-已同步 2-同步失败
     *
     * @param senddmsState
     */
	public void setSenddmsState(Long senddmsState) {
		this.senddmsState = senddmsState;
	}
	
	/**
     * 同步到dms时间
     */
	public Timestamp getSenddmsTime() {
		return this.senddmsTime;
	}

    /**
     * 同步到dms时间
     *
     * @param senddmsTime
     */
	public void setSenddmsTime(Timestamp senddmsTime) {
		this.senddmsTime = senddmsTime;
	}
	
	/**
     * 备注
     */
	public String getRemark() {
		return this.remark;
	}

    /**
     * 备注
     *
     * @param remark
     */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWaybillno() {
		return waybillno;
	}

	public void setWaybillno(String waybillno) {
		this.waybillno = waybillno;
	}

	public String getOperateDesc() {
		return operateDesc;
	}

	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}

	public Timestamp getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(Timestamp writeTime) {
		this.writeTime = writeTime;
	}

	public Timestamp getEndpointtime() {
		return endpointtime;
	}

	public void setEndpointtime(Timestamp endpointtime) {
		this.endpointtime = endpointtime;
	}
    
}
