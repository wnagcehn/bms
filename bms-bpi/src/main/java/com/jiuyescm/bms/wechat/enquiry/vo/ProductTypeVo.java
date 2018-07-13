package com.jiuyescm.bms.wechat.enquiry.vo;

import java.io.Serializable;

/**
 * 产品类型返回前端实体类
 * 
 * @author yangss
 * 
 */
public class ProductTypeVo implements Serializable {

	private static final long serialVersionUID = -5523191799295276299L;

	// 产品类型编码
	private String productTypeCode;
	// 产品类型名称
	private String productTypeName;
	// 简称
	private String shortName;

	public ProductTypeVo() {
		super();
	}

	public ProductTypeVo(String productTypeCode, String productTypeName,
			String shortName) {
		super();
		this.productTypeCode = productTypeCode;
		this.productTypeName = productTypeName;
		this.shortName = shortName;
	}

	public String getProductTypeCode() {
		return productTypeCode;
	}

	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}