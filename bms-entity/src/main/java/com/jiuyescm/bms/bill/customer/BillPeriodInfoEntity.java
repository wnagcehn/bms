package com.jiuyescm.bms.bill.customer;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BillPeriodInfoEntity implements IEntity {

	/**
     * 
     */
    private static final long serialVersionUID = 4771599670874477266L;
    // 自增主键
	private Long id;
	// 主商家id
	private String mkId;
	// 主商家名称
	private String invoiceName;
	// 应收款起算基准
	private String basicCode;
	// 加月数
	private Integer addMonth;
	// 加天数
	private Integer addDay;
	// 创建人
	private String crePerson;
	// 创建人id
	private String crePersonId;
	// 创建时间
	private Timestamp creTime;
	// 修改人
	private String modPerson;
	// 修改人id
	private String modPersonId;
	// ModTime
	private Timestamp modTime;
	// 作废标识
	private String delFlag;

	public BillPeriodInfoEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMkId() {
		return this.mkId;
	}

	public void setMkId(String mkId) {
		this.mkId = mkId;
	}
	
	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	
	public String getBasicCode() {
		return this.basicCode;
	}

	public void setBasicCode(String basicCode) {
		this.basicCode = basicCode;
	}
	
	public Integer getAddMonth() {
		return this.addMonth;
	}

	public void setAddMonth(Integer addMonth) {
		this.addMonth = addMonth;
	}
	
	public Integer getAddDay() {
		return this.addDay;
	}

	public void setAddDay(Integer addDay) {
		this.addDay = addDay;
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
	
	public String getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}
