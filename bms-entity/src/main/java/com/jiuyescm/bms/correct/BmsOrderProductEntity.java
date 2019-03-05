package com.jiuyescm.bms.correct;

import com.jiuyescm.cfm.domain.IEntity;

 /**
 * ..Entity
 * @author wangchen
 * 
 */
public class BmsOrderProductEntity implements IEntity {

    
	/**
	 * 
	 */
	private static final long serialVersionUID = 356280595848716589L;
	// 自增主键
	private Long id;
	// 商品标
	private String productMark;
	// ProductDetail
	private String productDetail;
	// 代表运单
	private String waybillNo;
	// 商家id
	private String customerId;
	// 业务数据年月 201801
	private Integer createTime;

	public BmsOrderProductEntity() {
		super();
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getProductMark() {
		return this.productMark;
	}

	public void setProductMark(String productMark) {
		this.productMark = productMark;
	}
	
	
	
	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	public String getWaybillNo() {
		return this.waybillNo;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public Integer getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
    
}
