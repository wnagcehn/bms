package com.jiuyescm.bms.bill.check;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class BillCheckFollowEntity implements IEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598160611721960774L;
	private int id;
	private int billCheckId;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer takesTime;
	private String appointMan;
	private String appointManId;
	private String appointDeptId;
	private String appointDept;
	private String followState;
	private String followMan;
	private String followManId;
	private String followDeptId;
	private String followDept;
	private String delFlag;
	
	public String getAppointManId() {
		return appointManId;
	}
	public void setAppointManId(String appointManId) {
		this.appointManId = appointManId;
	}
	public String getAppointDept() {
		return appointDept;
	}
	public void setAppointDept(String appointDept) {
		this.appointDept = appointDept;
	}
	public String getFollowState() {
		return followState;
	}
	public void setFollowState(String followState) {
		this.followState = followState;
	}
	public String getFollowManId() {
		return followManId;
	}
	public void setFollowManId(String followManId) {
		this.followManId = followManId;
	}
	public String getFollowDept() {
		return followDept;
	}
	public void setFollowDept(String followDept) {
		this.followDept = followDept;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBillCheckId() {
		return billCheckId;
	}
	public void setBillCheckId(int billCheckId) {
		this.billCheckId = billCheckId;
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
	public Integer getTakesTime() {
		return takesTime;
	}
	public void setTakesTime(Integer takesTime) {
		this.takesTime = takesTime;
	}
	public String getAppointMan() {
		return appointMan;
	}
	public void setAppointMan(String appointMan) {
		this.appointMan = appointMan;
	}
	public String getAppointDeptId() {
		return appointDeptId;
	}
	public void setAppointDeptId(String appointDeptId) {
		this.appointDeptId = appointDeptId;
	}
	public String getFollowMan() {
		return followMan;
	}
	public void setFollowMan(String followMan) {
		this.followMan = followMan;
	}
	public String getFollowDeptId() {
		return followDeptId;
	}
	public void setFollowDeptId(String followDeptId) {
		this.followDeptId = followDeptId;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}
