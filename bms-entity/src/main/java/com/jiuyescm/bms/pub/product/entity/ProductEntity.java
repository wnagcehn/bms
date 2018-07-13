package com.jiuyescm.bms.pub.product.entity;

import com.jiuyescm.cfm.domain.IEntity;
import com.jiuyescm.bms.pub.customer.entity.CustomerEntity;
import com.jiuyescm.bms.pub.info.entity.UomEntity;

/**
 * 产品
 */
public class ProductEntity implements IEntity {

	private static final long serialVersionUID = 8589447506943560396L;
	// 商品ID
	private String productid;
	// 商品助记码
	private String productcode;
	// 商品名称
	private String productname;
	// 规格型号
	private String spec;
	// 包装规格
	private String packagedesc;
	// 英文名称
	private String englishname;
	// 商品简称
	private String shortname;
	// SKU
	private String sku;
	// 外部商品编码
	private String extendid;
	// 商家ID
	private String customerid;
	// 商品类别ID
	private String categoryid;
	//商品类别名称
	private String categoryname;
	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	// 基本计量单位ID
	private String uomid;
	// 净重
	private double netweight;
	// 净重分子
	private int numerator;
	// 净重分母
	private int denominator;
	// 毛重
	private double grossweight;
	// 长CM
	private double length;
	// 宽CM
	private double width;
	// 高CM
	private double height;
	// 体积
	private double volume;
	// 颜色
	private String color;
	// 尺寸
	private String size;
	// 零售价
	private double retailprice;
	// 安全库存上限
	private double safeqtyupperlimit;
	// 安全库存下限
	private double safeqtylowerlimit;
	// 安全库存预警系数
	private double safeqty_alertrate;
	// 是否需要保质期管理
	private int shelflifeflag;
	// 保质期(天)
	private int shelflifehour;
	// 保质期禁收天数
	private int shelfrejectday;
	// 保质期禁售天数
	private int shelflockupday;
	// 保质期预警天数
	private int shelfalertday;
	// 是否批次管理
	private int batchflag;
	// 是否易碎品
	private int fragileflag;
	// 是否危险品
	private int dangerflag;
	// 存储温度要求
	private String storedemand;
	// 配送温度要求
	private String transportdemand;
	// 季节编码
	private String seasoncode;
	// 季节名称
	private String seasondemand;
	// 品牌代码
	private String brandcode;
	// 品牌名称
	private String brandname;
	// 供应商编码
	private String suppliercode;
	// 供应商名称
	private String suppliername;
	// 原产地
	private String origin;
	// 库存周转策略
	private String stockturnoverpolicy;
	// 备注字段1
	private String extattribute1;
	// 备注字段2
	private String extattribute2;
	// 备注字段3
	private String extattribute3;
	// 备注字段4
	private String extattribute4;
	// 备注字段5
	private String extattribute5;
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
	//温度类别
	private String tempraturetype;//LC:冷藏;LD:冷冻;CW:常温
	
	/******************* 关联对象 *******************/
	// 产品分类
	private CategoryEntity category;
	// 度量单位
	private UomEntity uom;
	// 商家
	private CustomerEntity customer;
	//产品PN信息
	private ProductPNEntity productPN;

	// PN码
	private String pncode;
	
	/**2016.3.21导入时增加的临时字段，不保存到表中**/
	private String tempid; //临时id
	// 是否需要保质期管理（是/否）
	private String shelflife;
	// 是否批次管理（是/否）
	private String batch;
	private Integer rowno; //行号
	/*********************************************/
	private String msg;
	
	// 商品所有的pn,以逗号分隔
	private String pns;
	// 辅助 区分 商家 外部商品编码唯一
	private String uqpropertis;
	
	public ProductEntity() {

	}

	public String getProductid() {
		return this.productid;
	}

	public void setProductid(String aProductid) {
		this.productid = aProductid;
	}

	public String getProductcode() {
		return this.productcode;
	}

	public void setProductcode(String aProductcode) {
		this.productcode = aProductcode;
	}

	public String getProductname() {
		return this.productname;
	}

	public void setProductname(String aProductname) {
		this.productname = aProductname;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String aSpec) {
		this.spec = aSpec;
	}

	public String getPackagedesc() {
		return this.packagedesc;
	}

	public void setPackagedesc(String aPackagedesc) {
		this.packagedesc = aPackagedesc;
	}

	public String getEnglishname() {
		return this.englishname;
	}

	public void setEnglishname(String aEnglishname) {
		this.englishname = aEnglishname;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String aShortname) {
		this.shortname = aShortname;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String aSku) {
		this.sku = aSku;
	}

	public String getExtendid() {
		return this.extendid;
	}

	public void setExtendid(String aExtendid) {
		this.extendid = aExtendid;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String aCustomerid) {
		this.customerid = aCustomerid;
	}

	public String getCategoryid() {
		return this.categoryid;
	}

	public void setCategoryid(String aCategoryid) {
		this.categoryid = aCategoryid;
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

	public double getGrossweight() {
		return this.grossweight;
	}

	public void setGrossweight(double aGrossweight) {
		this.grossweight = aGrossweight;
	}

	public double getLength() {
		return this.length;
	}

	public void setLength(double aLength) {
		this.length = aLength;
	}

	public double getWidth() {
		return this.width;
	}

	public void setWidth(double aWidth) {
		this.width = aWidth;
	}

	public double getHeight() {
		return this.height;
	}

	public void setHeight(double aHeight) {
		this.height = aHeight;
	}

	public double getVolume() {
		return this.volume;
	}

	public void setVolume(double aVolume) {
		this.volume = aVolume;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String aColor) {
		this.color = aColor;
	}

	public String getSize() {
		return this.size;
	}

	public void setSize(String aSize) {
		this.size = aSize;
	}

	public double getRetailprice() {
		return this.retailprice;
	}

	public void setRetailprice(double aRetailprice) {
		this.retailprice = aRetailprice;
	}

	public double getSafeqtyupperlimit() {
		return this.safeqtyupperlimit;
	}

	public void setSafeqtyupperlimit(double aSafeqtyupperlimit) {
		this.safeqtyupperlimit = aSafeqtyupperlimit;
	}

	public double getSafeqtylowerlimit() {
		return this.safeqtylowerlimit;
	}

	public void setSafeqtylowerlimit(double aSafeqtylowerlimit) {
		this.safeqtylowerlimit = aSafeqtylowerlimit;
	}

	public double getSafeqty_alertrate() {
		return this.safeqty_alertrate;
	}

	public void setSafeqty_alertrate(double aSafeqty_alertrate) {
		this.safeqty_alertrate = aSafeqty_alertrate;
	}

	public int getShelflifeflag() {
		return this.shelflifeflag;
	}

	public void setShelflifeflag(int aShelflifeflag) {
		this.shelflifeflag = aShelflifeflag;
	}

	public int getShelflifehour() {
		return this.shelflifehour;
	}

	public void setShelflifehour(int aShelflifehour) {
		this.shelflifehour = aShelflifehour;
	}

	public int getShelfrejectday() {
		return this.shelfrejectday;
	}

	public void setShelfrejectday(int aShelfrejectday) {
		this.shelfrejectday = aShelfrejectday;
	}

	public int getShelflockupday() {
		return this.shelflockupday;
	}

	public void setShelflockupday(int aShelflockupday) {
		this.shelflockupday = aShelflockupday;
	}

	public int getShelfalertday() {
		return this.shelfalertday;
	}

	public void setShelfalertday(int aShelfalertday) {
		this.shelfalertday = aShelfalertday;
	}

	public int getBatchflag() {
		return this.batchflag;
	}

	public void setBatchflag(int aBatchflag) {
		this.batchflag = aBatchflag;
	}

	public int getFragileflag() {
		return this.fragileflag;
	}

	public void setFragileflag(int aFragileflag) {
		this.fragileflag = aFragileflag;
	}

	public int getDangerflag() {
		return this.dangerflag;
	}

	public void setDangerflag(int aDangerflag) {
		this.dangerflag = aDangerflag;
	}

	public String getStoredemand() {
		return this.storedemand;
	}

	public void setStoredemand(String aStoredemand) {
		this.storedemand = aStoredemand;
	}

	public String getTransportdemand() {
		return this.transportdemand;
	}

	public void setTransportdemand(String aTransportdemand) {
		this.transportdemand = aTransportdemand;
	}

	public String getSeasoncode() {
		return this.seasoncode;
	}

	public void setSeasoncode(String aSeasoncode) {
		this.seasoncode = aSeasoncode;
	}

	public String getSeasondemand() {
		return this.seasondemand;
	}

	public void setSeasondemand(String aSeasondemand) {
		this.seasondemand = aSeasondemand;
	}

	public String getBrandcode() {
		return this.brandcode;
	}

	public void setBrandcode(String aBrandcode) {
		this.brandcode = aBrandcode;
	}

	public String getBrandname() {
		return this.brandname;
	}

	public void setBrandname(String aBrandname) {
		this.brandname = aBrandname;
	}

	public String getSuppliercode() {
		return this.suppliercode;
	}

	public void setSuppliercode(String aSuppliercode) {
		this.suppliercode = aSuppliercode;
	}

	public String getSuppliername() {
		return this.suppliername;
	}

	public void setSuppliername(String aSuppliername) {
		this.suppliername = aSuppliername;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String aOrigin) {
		this.origin = aOrigin;
	}

	public String getStockturnoverpolicy() {
		return this.stockturnoverpolicy;
	}

	public void setStockturnoverpolicy(String aStockturnoverpolicy) {
		this.stockturnoverpolicy = aStockturnoverpolicy;
	}

	public String getExtattribute1() {
		return this.extattribute1;
	}

	public void setExtattribute1(String aExtattribute1) {
		this.extattribute1 = aExtattribute1;
	}

	public String getExtattribute2() {
		return this.extattribute2;
	}

	public void setExtattribute2(String aExtattribute2) {
		this.extattribute2 = aExtattribute2;
	}

	public String getExtattribute3() {
		return this.extattribute3;
	}

	public void setExtattribute3(String aExtattribute3) {
		this.extattribute3 = aExtattribute3;
	}

	public String getExtattribute4() {
		return this.extattribute4;
	}

	public void setExtattribute4(String aExtattribute4) {
		this.extattribute4 = aExtattribute4;
	}

	public String getExtattribute5() {
		return this.extattribute5;
	}

	public void setExtattribute5(String aExtattribute5) {
		this.extattribute5 = aExtattribute5;
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

	public String getTempraturetype() {
		return tempraturetype;
	}

	public void setTempraturetype(String tempraturetype) {
		this.tempraturetype = tempraturetype;
	}

	public void setCategory(CategoryEntity theCategory) {
		this.category = theCategory;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setUom(UomEntity theUom) {
		this.uom = theUom;
	}

	public UomEntity getUom() {
		return uom;
	}

	public void setCustomer(CustomerEntity theCustomer) {
		this.customer = theCustomer;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}
	
	public ProductPNEntity getProductPN() {
		return productPN;
	}

	public void setProductPN(ProductPNEntity productPN) {
		this.productPN = productPN;
	}

	public String getPncode() {
		return this.pncode;
	}

	public void setPncode(String aPncode) {
		this.pncode = aPncode;
	}
	
	public String getTempid() {
		return tempid;
	}

	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
	
	public String getShelflife() {
		return shelflife;
	}

	public void setShelflife(String shelflife) {
		this.shelflife = shelflife;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	public Integer getRowno() {
		return rowno;
	}

	public void setRowno(Integer rowno) {
		this.rowno = rowno;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPns() {
		return pns;
	}

	public void setPns(String pns) {
		this.pns = pns;
	}

	public String getUqpropertis() {
		return uqpropertis;
	}

	public void setUqpropertis(String uqpropertis) {
		this.uqpropertis = uqpropertis;
	}

	public String toString() {
		StringBuffer returnString = new StringBuffer();
		returnString.append("com.jiuyescm.bms.pub.product.entity.ProductEntity[");
		returnString.append("productid = " + this.productid + ";\n");
		returnString.append("productcode = " + this.productcode + ";\n");
		returnString.append("productname = " + this.productname + ";\n");
		returnString.append("spec = " + this.spec + ";\n");
		returnString.append("packagedesc = " + this.packagedesc + ";\n");
		returnString.append("englishname = " + this.englishname + ";\n");
		returnString.append("shortname = " + this.shortname + ";\n");
		returnString.append("sku = " + this.sku + ";\n");
		returnString.append("extendid = " + this.extendid + ";\n");
		returnString.append("customerid = " + this.customerid + ";\n");
		returnString.append("categoryid = " + this.categoryid + ";\n");
		returnString.append("uomid = " + this.uomid + ";\n");
		returnString.append("netweight = " + this.netweight + ";\n");
		returnString.append("numerator = " + this.numerator + ";\n");
		returnString.append("denominator = " + this.denominator + ";\n");
		returnString.append("grossweight = " + this.grossweight + ";\n");
		returnString.append("length = " + this.length + ";\n");
		returnString.append("width = " + this.width + ";\n");
		returnString.append("height = " + this.height + ";\n");
		returnString.append("volume = " + this.volume + ";\n");
		returnString.append("color = " + this.color + ";\n");
		returnString.append("size = " + this.size + ";\n");
		returnString.append("retailprice = " + this.retailprice + ";\n");
		returnString.append("safeqtyupperlimit = " + this.safeqtyupperlimit + ";\n");
		returnString.append("safeqtylowerlimit = " + this.safeqtylowerlimit + ";\n");
		returnString.append("safeqty_alertrate = " + this.safeqty_alertrate + ";\n");
		returnString.append("shelflifeflag = " + this.shelflifeflag + ";\n");
		returnString.append("shelflifehour = " + this.shelflifehour + ";\n");
		returnString.append("shelfrejectday = " + this.shelfrejectday + ";\n");
		returnString.append("shelflockupday = " + this.shelflockupday + ";\n");
		returnString.append("shelfalertday = " + this.shelfalertday + ";\n");
		returnString.append("batchflag = " + this.batchflag + ";\n");
		returnString.append("fragileflag = " + this.fragileflag + ";\n");
		returnString.append("dangerflag = " + this.dangerflag + ";\n");
		returnString.append("storedemand = " + this.storedemand + ";\n");
		returnString.append("transportdemand = " + this.transportdemand + ";\n");
		returnString.append("seasoncode = " + this.seasoncode + ";\n");
		returnString.append("seasondemand = " + this.seasondemand + ";\n");
		returnString.append("brandcode = " + this.brandcode + ";\n");
		returnString.append("brandname = " + this.brandname + ";\n");
		returnString.append("suppliercode = " + this.suppliercode + ";\n");
		returnString.append("suppliername = " + this.suppliername + ";\n");
		returnString.append("origin = " + this.origin + ";\n");
		returnString.append("stockturnoverpolicy = " + this.stockturnoverpolicy + ";\n");
		returnString.append("extattribute1 = " + this.extattribute1 + ";\n");
		returnString.append("extattribute2 = " + this.extattribute2 + ";\n");
		returnString.append("extattribute3 = " + this.extattribute3 + ";\n");
		returnString.append("extattribute4 = " + this.extattribute4 + ";\n");
		returnString.append("extattribute5 = " + this.extattribute5 + ";\n");
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
		if (productid == null || "".equals(productid.trim())) {
			return null;
		}
		return productid;
	}
}