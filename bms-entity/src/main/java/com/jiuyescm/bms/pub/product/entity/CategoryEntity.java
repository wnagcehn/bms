package com.jiuyescm.bms.pub.product.entity;

import java.util.List;

import javax.persistence.Transient;

import com.jiuyescm.cfm.domain.IEntity;

/**
 * 产品分类
 */
public class CategoryEntity implements IEntity {

	private static final long serialVersionUID = 6904655623793361632L;
	// 商品类别ID
	private String categoryid;
	// 父类别ID
	private String parentcategoryid;
	// 商品类别编码
	private String categorycode;
	// 商品类别名称
	private String categoryname;
	// 类别Path
	private String categorypath;
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

	@Transient
	private List<CategoryEntity> children;

	public CategoryEntity() {

	}

	public String getCategoryid() {
		return this.categoryid;
	}

	public void setCategoryid(String aCategoryid) {
		this.categoryid = aCategoryid;
	}

	public String getParentcategoryid() {
		return this.parentcategoryid;
	}

	public void setParentcategoryid(String aParentcategoryid) {
		this.parentcategoryid = aParentcategoryid;
	}

	public String getCategorycode() {
		return this.categorycode;
	}

	public void setCategorycode(String aCategorycode) {
		this.categorycode = aCategorycode;
	}

	public String getCategoryname() {
		return this.categoryname;
	}

	public void setCategoryname(String aCategoryname) {
		this.categoryname = aCategoryname;
	}

	public String getCategorypath() {
		return this.categorypath;
	}

	public void setCategorypath(String aCategorypath) {
		this.categorypath = aCategorypath;
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

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.product.entity.CategoryEntity[");
		returnString.append("categoryid = " + this.categoryid + ";\n");
		returnString.append("parentcategoryid = " + this.parentcategoryid + ";\n");
		returnString.append("categorycode = " + this.categorycode + ";\n");
		returnString.append("categoryname = " + this.categoryname + ";\n");
		returnString.append("categorypath = " + this.categorypath + ";\n");
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
		if (categoryid == null || "".equals(categoryid.trim())) {
			return null;
		}
		return categoryid;
	}

	public List<CategoryEntity> getChildren() {
		return children;
	}

	public void setChildren(List<CategoryEntity> children) {
		this.children = children;
	}

}