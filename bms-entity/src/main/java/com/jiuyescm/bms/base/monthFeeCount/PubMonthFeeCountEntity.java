package com.jiuyescm.bms.base.monthFeeCount;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class PubMonthFeeCountEntity implements IEntity {

 	// TODO please create serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// 自增标识
	private Integer id;
	// 物流商id
	private String carrierId;
	// 月结账号
	private String monthFeeCount;
	// 商家自有标识(0：否，1：是)
	private Integer ownflag;
	// 作废标识(0：启用，1：作废)
	private Integer delflag;
	// 创建人
	private String crePerson;
	// 创建人ID
	private String crePersonId;
	// 创建时间
	private Timestamp creTime;
	// 修改人
	private String modPerson;
	// 修改人ID
	private String modPersonId;
	// 修改时间
	private Timestamp modTime;
	// 写入bms时间
	private Timestamp writeTime;

	public PubMonthFeeCountEntity() {
		super();
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCarrierId() {
		return this.carrierId;
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}
	
	public String getMonthFeeCount() {
		return this.monthFeeCount;
	}

	public void setMonthFeeCount(String monthFeeCount) {
		this.monthFeeCount = monthFeeCount;
	}
	
	public Integer getOwnflag() {
		return this.ownflag;
	}

	public void setOwnflag(Integer ownflag) {
		this.ownflag = ownflag;
	}
	
	public Integer getDelflag() {
		return this.delflag;
	}

	public void setDelflag(Integer delflag) {
		this.delflag = delflag;
	}
	
	public String getCrePerson() {
		return this.crePerson;
	}

	public void setCrePerson(String crePerson) {
		this.crePerson = crePerson;
	}
	
	public String getCrePersonId() {
		return this.crePersonId;
	}

	public void setCrePersonId(String crePersonId) {
		this.crePersonId = crePersonId;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	public String getModPerson() {
		return this.modPerson;
	}

	public void setModPerson(String modPerson) {
		this.modPerson = modPerson;
	}
	
	public String getModPersonId() {
		return this.modPersonId;
	}

	public void setModPersonId(String modPersonId) {
		this.modPersonId = modPersonId;
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
    
}
