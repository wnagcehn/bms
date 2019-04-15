package com.jiuyescm.bms.pub.product.entity;

import java.util.ArrayList;
import java.util.List;

import com.jiuyescm.bms.pub.customer.entity.CustomerEntity;
import com.jiuyescm.cfm.domain.IEntity;

/**
 * 产品PN信息
 */
public class ProductPNEntity implements IEntity {

	private static final long serialVersionUID = 6876675774524442022L;
	// 标识
	private String id;
	// 商品ID
	private String productid;
	// 商家ID
	private String customerid;
	// PN码
	private String pncode;
	// 计量单位
	private String uomid;
	// 单位净重
	private double netweight;
	// 单位毛重
	private double grossweight;
	// 净重分子
	private int numerator;
	// 净重分母
	private int denominator;
	// 包装规格
	private String packagedesc;
	// 备注
	private String remark;
	// 删除标识
	private int delflag;
	// 创建者ID
	private String crepersonid;
	// 创建者
	private String creperson;
	// 创建时间
	private java.sql.Timestamp cretime;
	// 修改者ID
	private String modpersonid;
	// 修改者
	private String modperson;
	// 修改时间
	private java.sql.Timestamp modtime;
	
	/******************* 关联对象 *******************/
	//产品
	private ProductEntity product;
	// 商家
	private CustomerEntity customer;

	/**2016.3.22导入时增加的临时字段，不保存到表中**/
	private String tempid; //临时id
	private Integer rowno; //行号
	/*********************************************/
	
	public ProductPNEntity() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String aId) {
		this.id = aId;
	}

	public String getProductid() {
		return this.productid;
	}

	public void setProductid(String aProductid) {
		this.productid = aProductid;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String aCustomerid) {
		this.customerid = aCustomerid;
	}

	public String getPncode() {
		return this.pncode;
	}

	public void setPncode(String aPncode) {
		this.pncode = aPncode;
	}

	public String getUomid() {
		return this.uomid;
	}

	public void setUomid(String aUomid) {
		this.uomid = aUomid;
	}

	public double getNetweight() {
		return this.netweight;
	}

	public void setNetweight(double aNetweight) {
		this.netweight = aNetweight;
	}

	public double getGrossweight() {
		return this.grossweight;
	}

	public void setGrossweight(double aGrossweight) {
		this.grossweight = aGrossweight;
	}

	public int getNumerator() {
		return this.numerator;
	}

	public void setNumerator(int aNumerator) {
		this.numerator = aNumerator;
	}

	public int getDenominator() {
		return this.denominator;
	}

	public void setDenominator(int aDenominator) {
		this.denominator = aDenominator;
	}

	public String getPackagedesc() {
		return this.packagedesc;
	}

	public void setPackagedesc(String aPackagedesc) {
		this.packagedesc = aPackagedesc;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String aRemark) {
		this.remark = aRemark;
	}

	public int getDelflag() {
		return this.delflag;
	}

	public void setDelflag(int aDelflag) {
		this.delflag = aDelflag;
	}

	public String getCrepersonid() {
		return this.crepersonid;
	}

	public void setCrepersonid(String aCrepersonid) {
		this.crepersonid = aCrepersonid;
	}

	public String getCreperson() {
		return this.creperson;
	}

	public void setCreperson(String aCreperson) {
		this.creperson = aCreperson;
	}

	public java.sql.Timestamp getCretime() {
		return this.cretime;
	}

	public void setCretime(java.sql.Timestamp aCretime) {
		this.cretime = aCretime;
	}

	public String getModpersonid() {
		return this.modpersonid;
	}

	public void setModpersonid(String aModpersonid) {
		this.modpersonid = aModpersonid;
	}

	public String getModperson() {
		return this.modperson;
	}

	public void setModperson(String aModperson) {
		this.modperson = aModperson;
	}

	public java.sql.Timestamp getModtime() {
		return this.modtime;
	}

	public void setModtime(java.sql.Timestamp aModtime) {
		this.modtime = aModtime;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}
	
	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
	
	public Integer getRowno() {
		return rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	
	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.product.entity.ProductPNEntity[");
		returnString.append("id = " + this.id + ";\n");
		returnString.append("productid = " + this.productid + ";\n");
		returnString.append("customerid = " + this.customerid + ";\n");
		returnString.append("pncode = " + this.pncode + ";\n");
		returnString.append("uomid = " + this.uomid + ";\n");
		returnString.append("netweight = " + this.netweight + ";\n");
		returnString.append("grossweight = " + this.grossweight + ";\n");
		returnString.append("numerator = " + this.numerator + ";\n");
		returnString.append("denominator = " + this.denominator + ";\n");
		returnString.append("packagedesc = " + this.packagedesc + ";\n");
		returnString.append("remark = " + this.remark + ";\n");
		returnString.append("delflag = " + this.delflag + ";\n");
		returnString.append("crepersonid = " + this.crepersonid + ";\n");
		returnString.append("creperson = " + this.creperson + ";\n");
		returnString.append("cretime = " + this.cretime + ";\n");
		returnString.append("modpersonid = " + this.modpersonid + ";\n");
		returnString.append("modperson = " + this.modperson + ";\n");
		returnString.append("modtime = " + this.modtime + ";\n");
		returnString.append("]\n");
		return returnString.toString();
	}

	/******************* 辅助方法 *******************/
	/*
	 * 实体Key字符
	 */
	public String getKey() {
		if (id == null || "".equals(id.trim())) {
			return null;
		}
		return id;
	}
	
	public static void main(String[] orgs){
		List<String> a = new ArrayList<>();
		a.add("a1");
		a.add("a2");
		a.add("a3");
		List<String> b = new ArrayList<>();
		b.add("a1");
		b.add("b2");
		b.add("a3");
		a.removeAll(b);
		for(String d : a){
			//System.out.println(d);
		}
	} 
}