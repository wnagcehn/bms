package com.jiuyescm.bms.correct.vo;

import com.jiuyescm.cfm.domain.IEntity;

public class BmsOrderProductVo implements IEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1957983093441334259L;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductMark() {
		return productMark;
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
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	
	
}
