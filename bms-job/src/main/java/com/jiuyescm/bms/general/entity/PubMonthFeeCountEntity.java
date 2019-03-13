package com.jiuyescm.bms.general.entity;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author zhaofeng
 * 
 */
public class PubMonthFeeCountEntity implements IEntity {

 	// TODO please create serialVersionUID by eclipse
    private static final long serialVersionUID = -1;
    
	// id
	private Integer id;
	// 物流商id
	private String carrierId;
	// 月结账号
	private String monthFeeCount;
	// 商家自有标识(0：否，1：是)
	private Integer ownflag;
	// 作废标识(0：启用，1：作废)
	private Integer delflag;

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
    
}
