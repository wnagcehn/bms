package com.jiuyescm.bms.base.group.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class BmsGroupSubjectVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8001250134626217405L;
	//ID 主键
	private int id;
	//分组ID
	private int groupId;
	private List<Integer> groupIds;
	//分钟名称
	private String groupName;
	// 费用编码
	private Long subjectId;
	// 费用编码
	private String subjectCode;
	//费用名称
	private String subjectName;
	//创建人
	private String creator;
	//创建时间
	private Timestamp createTime;
	//修改人
	private String lastModifier;
	//修改时间
	private Timestamp lastModifyTime;
	//作废标记
	private String delFlag;
	
	//业务类型
	private String bizType;
	private String groupCode;
	// 序号
	private Integer sortNo;
	//收支类型
	// 收支类型（IN-收入 OUT-支出）
	private String inOutTypecode;
	// 业务类型（仓，干，配）
	private String bizTypecode;
	// 费用类型 BASE-基础费 ADD-增值费 APP-分摊费 OTHER-其他费
	private String feesType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public List<Integer> getGroupIds() {
		return groupIds;
	}
	public void setGroupIds(List<Integer> groupIds) {
		this.groupIds = groupIds;
	}
	public String getInOutTypecode() {
		return inOutTypecode;
	}
	public void setInOutTypecode(String inOutTypecode) {
		this.inOutTypecode = inOutTypecode;
	}
	public String getBizTypecode() {
		return bizTypecode;
	}
	public void setBizTypecode(String bizTypecode) {
		this.bizTypecode = bizTypecode;
	}
	public String getFeesType() {
		return feesType;
	}
	public void setFeesType(String feesType) {
		this.feesType = feesType;
	}
	public Integer getSortNo() {
		return sortNo;
	}
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public Long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
	
	
}
