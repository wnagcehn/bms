package com.jiuyescm.bms.general.entity;


import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillCustomerMasterEntity implements IEntity {

    
	/**
     * 
     */
    private static final long serialVersionUID = -6980369369059438900L;
    // 自增标识
	private Integer id;
	// 主商家ID
	private String mkId;
	// 主商家名称
	private String mkInvoiceName;
	// 业务月份
	private Integer createMonth;
	// 是否处理 0-未处理 1-已处理
	private String isCalculated;
	// 写入时间
	private Timestamp creTime;
	// 最后一次更新时间
	private Timestamp modTime;

	public BillCustomerMasterEntity() {
		super();
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMkId() {
		return this.mkId;
	}

	public void setMkId(String mkId) {
		this.mkId = mkId;
	}
	
	public Integer getCreateMonth() {
		return this.createMonth;
	}

	public void setCreateMonth(Integer createMonth) {
		this.createMonth = createMonth;
	}
	
	public String getIsCalculated() {
		return this.isCalculated;
	}

	public void setIsCalculated(String isCalculated) {
		this.isCalculated = isCalculated;
	}
	
	public Timestamp getCreTime() {
		return this.creTime;
	}

	public void setCreTime(Timestamp creTime) {
		this.creTime = creTime;
	}
	
	public Timestamp getModTime() {
		return this.modTime;
	}

	public void setModTime(Timestamp modTime) {
		this.modTime = modTime;
	}

    public String getMkInvoiceName() {
        return this.mkInvoiceName;
    }

    public void setMkInvoiceName(String mkInvoiceName) {
        this.mkInvoiceName = mkInvoiceName;
    }

}
