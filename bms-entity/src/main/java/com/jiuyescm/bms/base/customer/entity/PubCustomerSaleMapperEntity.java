package com.jiuyescm.bms.base.customer.entity;

import java.sql.Timestamp;
import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class PubCustomerSaleMapperEntity implements IEntity {

	private static final long serialVersionUID = 455982683995900769L;
	// 自增标识
	private Integer id;
	// 商家ID
	private String customerId;
	// 商家名称
	private String customerName;
	// 原始销售ID
	private String originSellerId;
	// 原始销售名称
	private String originSellerName;
	// 创建者
	private String creatorId;
	// 创建者
	private String creator;
	// 创建时间
	private Timestamp createTime;
	// 修改者
	private String lastModifier;
	// 修改者
	private String lastModifierId;
	// 修改时间
	private Timestamp lastModifyTime;

	public PubCustomerSaleMapperEntity() {
		super();
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getOriginSellerId() {
		return this.originSellerId;
	}

	public void setOriginSellerId(String originSellerId) {
		this.originSellerId = originSellerId;
	}
	
	public String getOriginSellerName() {
		return this.originSellerName;
	}

	public void setOriginSellerName(String originSellerName) {
		this.originSellerName = originSellerName;
	}
	
	public String getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getLastModifier() {
		return this.lastModifier;
	}

	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	
	public String getLastModifierId() {
		return this.lastModifierId;
	}

	public void setLastModifierId(String lastModifierId) {
		this.lastModifierId = lastModifierId;
	}
	
	public Timestamp getLastModifyTime() {
		return this.lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
    
}
