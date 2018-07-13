package com.jiuyescm.bms.pub.templet.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class PubTempletEntity implements IEntity{

	private static final long serialVersionUID = 3088535634203721585L;

	private String templetShortname;
	private String templetCreperson;
		// 主键-自增长
		private Integer id;
		// 模板名称
		private String templetName;
		// 模板类型 0-标准模板 1-自定义模板
		private Long templetType;
		// 商家id
		private String templetCustomerid;
		// 创建人ID
		private String templetCrepersonid;
		// 创建时间
		private Timestamp templetCretime;
		// 单据类型
		private String ordertypeid;
		// 始发仓ID
		private String originwarehouseid;
		// 商家ID
		private String customerid;
		// 外部物流单号
		private String reference;
		// 项目ID
		private String projectid;
		// 平台编码
		private String platformid;
		// 平台交易单号
		private String tradeid;
		// 收件人联系人
		private String receiverName;
		// 收件人手机
		private String receiverMobile;
		// 收件人电话
		private String receiverTel;
		// 省
		private String receiverProvince;
		// 市
		private String receiverCity;
		// 区
		private String receiverDistribute;
		// 收件人地址
		private String receiverAddress;
		// 收件人邮编
		private String receiverZip;
		// 要求送达日期
		private String dateServed;
		// 投递时延要求
		private String delayRequirement;
		// 付款方式
		private String payType;
		// 付款金额
		private String payMoney;
		// 订单金额
		private String orderMoney;
		// 买家备注
		private String receiverRemark;
		// 卖家备注
		private String sellerRemark;
		// 行号
		private String lineno;
		// OMS商品ID
		private String productId;
		// 商品PN
		private String productPn;
		// 数量
		private String productCount;
		// 库存类型编码
		private String stockCode;
		// 批次编码
		private String batchCode;
		// 明细备注
		private String detailRemark;
		// B2B形态
		private String b2bPattern;
		// 指定物流商ID
		private String carrierId;
		// 指定宅配商ID
		private String deliverId;
		// 指定站点
		private String stationId;
		// 指定发件人
		private String seller;
		// 指定发件人手机
		private String sellerMobile;
		// 指定发件人电话
		private String sellerTel;
		// 省(发件人)
		private String sellerProvince;
		// 市(发件人)
		private String sellerCity;
		// 区(发件人)
		private String sellerDistribute;
		// 发件人地址
		private String sellerAddress;
		// 指定运单号
		private String tookWorkcode;
		// 国际运单号
		private String naWorkcode;

		public PubTempletEntity() {

		}
		
		/**
	     * 主键-自增长
	     */
		public Integer getId() {
			return this.id;
		}

	    /**
	     * 主键-自增长
	     *
	     * @param id
	     */
		public void setId(Integer id) {
			this.id = id;
		}
		
		/**
	     * 单据类型
	     */
		public String getOrdertypeid() {
			return this.ordertypeid;
		}

	    /**
	     * 单据类型
	     *
	     * @param ordertypeid
	     */
		public void setOrdertypeid(String ordertypeid) {
			this.ordertypeid = ordertypeid;
		}
		
		/**
	     * 始发仓ID
	     */
		public String getOriginwarehouseid() {
			return this.originwarehouseid;
		}

	    /**
	     * 始发仓ID
	     *
	     * @param originwarehouseid
	     */
		public void setOriginwarehouseid(String originwarehouseid) {
			this.originwarehouseid = originwarehouseid;
		}
		
		/**
	     * 商家ID
	     */
		public String getCustomerid() {
			return this.customerid;
		}

	    /**
	     * 商家ID
	     *
	     * @param customerid
	     */
		public void setCustomerid(String customerid) {
			this.customerid = customerid;
		}
		
		/**
	     * 外部物流单号
	     */
		public String getReference() {
			return this.reference;
		}

	    /**
	     * 外部物流单号
	     *
	     * @param reference
	     */
		public void setReference(String reference) {
			this.reference = reference;
		}
		
		/**
	     * 项目ID
	     */
		public String getProjectid() {
			return this.projectid;
		}

	    /**
	     * 项目ID
	     *
	     * @param projectid
	     */
		public void setProjectid(String projectid) {
			this.projectid = projectid;
		}
		
		/**
	     * 平台编码
	     */
		public String getPlatformid() {
			return this.platformid;
		}

	    /**
	     * 平台编码
	     *
	     * @param platformid
	     */
		public void setPlatformid(String platformid) {
			this.platformid = platformid;
		}
		
		/**
	     * 平台交易单号
	     */
		public String getTradeid() {
			return this.tradeid;
		}

	    /**
	     * 平台交易单号
	     *
	     * @param tradeid
	     */
		public void setTradeid(String tradeid) {
			this.tradeid = tradeid;
		}
		
		/**
	     * 收件人联系人
	     */
		public String getReceiverName() {
			return this.receiverName;
		}

	    /**
	     * 收件人联系人
	     *
	     * @param receiverName
	     */
		public void setReceiverName(String receiverName) {
			this.receiverName = receiverName;
		}
		
		/**
	     * 收件人手机
	     */
		public String getReceiverMobile() {
			return this.receiverMobile;
		}

	    /**
	     * 收件人手机
	     *
	     * @param receiverMobile
	     */
		public void setReceiverMobile(String receiverMobile) {
			this.receiverMobile = receiverMobile;
		}
		
		/**
	     * 收件人电话
	     */
		public String getReceiverTel() {
			return this.receiverTel;
		}

	    /**
	     * 收件人电话
	     *
	     * @param receiverTel
	     */
		public void setReceiverTel(String receiverTel) {
			this.receiverTel = receiverTel;
		}
		
		/**
	     * 省
	     */
		public String getReceiverProvince() {
			return this.receiverProvince;
		}

	    /**
	     * 省
	     *
	     * @param receiverProvince
	     */
		public void setReceiverProvince(String receiverProvince) {
			this.receiverProvince = receiverProvince;
		}
		
		/**
	     * 市
	     */
		public String getReceiverCity() {
			return this.receiverCity;
		}

	    /**
	     * 市
	     *
	     * @param receiverCity
	     */
		public void setReceiverCity(String receiverCity) {
			this.receiverCity = receiverCity;
		}
		
		/**
	     * 区
	     */
		public String getReceiverDistribute() {
			return this.receiverDistribute;
		}

	    /**
	     * 区
	     *
	     * @param receiverDistribute
	     */
		public void setReceiverDistribute(String receiverDistribute) {
			this.receiverDistribute = receiverDistribute;
		}
		
		/**
	     * 收件人地址
	     */
		public String getReceiverAddress() {
			return this.receiverAddress;
		}

	    /**
	     * 收件人地址
	     *
	     * @param receiverAddress
	     */
		public void setReceiverAddress(String receiverAddress) {
			this.receiverAddress = receiverAddress;
		}
		
		/**
	     * 收件人邮编
	     */
		public String getReceiverZip() {
			return this.receiverZip;
		}

	    /**
	     * 收件人邮编
	     *
	     * @param receiverZip
	     */
		public void setReceiverZip(String receiverZip) {
			this.receiverZip = receiverZip;
		}
		
		/**
	     * 要求送达日期
	     */
		public String getDateServed() {
			return this.dateServed;
		}

	    /**
	     * 要求送达日期
	     *
	     * @param dateServed
	     */
		public void setDateServed(String dateServed) {
			this.dateServed = dateServed;
		}
		
		/**
	     * 投递时延要求
	     */
		public String getDelayRequirement() {
			return this.delayRequirement;
		}

	    /**
	     * 投递时延要求
	     *
	     * @param delayRequirement
	     */
		public void setDelayRequirement(String delayRequirement) {
			this.delayRequirement = delayRequirement;
		}
		
		/**
	     * 付款方式
	     */
		public String getPayType() {
			return this.payType;
		}

	    /**
	     * 付款方式
	     *
	     * @param payType
	     */
		public void setPayType(String payType) {
			this.payType = payType;
		}
		
		/**
	     * 付款金额
	     */
		public String getPayMoney() {
			return this.payMoney;
		}

	    /**
	     * 付款金额
	     *
	     * @param payMoney
	     */
		public void setPayMoney(String payMoney) {
			this.payMoney = payMoney;
		}
		
		/**
	     * 订单金额
	     */
		public String getOrderMoney() {
			return this.orderMoney;
		}

	    /**
	     * 订单金额
	     *
	     * @param orderMoney
	     */
		public void setOrderMoney(String orderMoney) {
			this.orderMoney = orderMoney;
		}
		
		/**
	     * 买家备注
	     */
		public String getReceiverRemark() {
			return this.receiverRemark;
		}

	    /**
	     * 买家备注
	     *
	     * @param receiverRemark
	     */
		public void setReceiverRemark(String receiverRemark) {
			this.receiverRemark = receiverRemark;
		}
		
		/**
	     * 卖家备注
	     */
		public String getSellerRemark() {
			return this.sellerRemark;
		}

	    /**
	     * 卖家备注
	     *
	     * @param sellerRemark
	     */
		public void setSellerRemark(String sellerRemark) {
			this.sellerRemark = sellerRemark;
		}
		
		/**
	     * 行号
	     */
		public String getLineno() {
			return this.lineno;
		}

	    /**
	     * 行号
	     *
	     * @param lineno
	     */
		public void setLineno(String lineno) {
			this.lineno = lineno;
		}
		
		/**
	     * OMS商品ID
	     */
		public String getProductId() {
			return this.productId;
		}

	    /**
	     * OMS商品ID
	     *
	     * @param productId
	     */
		public void setProductId(String productId) {
			this.productId = productId;
		}
		
		/**
	     * 商品PN
	     */
		public String getProductPn() {
			return this.productPn;
		}

	    /**
	     * 商品PN
	     *
	     * @param productPn
	     */
		public void setProductPn(String productPn) {
			this.productPn = productPn;
		}
		
		/**
	     * 数量
	     */
		public String getProductCount() {
			return this.productCount;
		}

	    /**
	     * 数量
	     *
	     * @param productCount
	     */
		public void setProductCount(String productCount) {
			this.productCount = productCount;
		}
		
		/**
	     * 库存类型编码
	     */
		public String getStockCode() {
			return this.stockCode;
		}

	    /**
	     * 库存类型编码
	     *
	     * @param stockCode
	     */
		public void setStockCode(String stockCode) {
			this.stockCode = stockCode;
		}
		
		/**
	     * 批次编码
	     */
		public String getBatchCode() {
			return this.batchCode;
		}

	    /**
	     * 批次编码
	     *
	     * @param batchCode
	     */
		public void setBatchCode(String batchCode) {
			this.batchCode = batchCode;
		}
		
		/**
	     * 明细备注
	     */
		public String getDetailRemark() {
			return this.detailRemark;
		}

	    /**
	     * 明细备注
	     *
	     * @param detailRemark
	     */
		public void setDetailRemark(String detailRemark) {
			this.detailRemark = detailRemark;
		}
		
		/**
	     * B2B形态
	     */
		public String getB2bPattern() {
			return this.b2bPattern;
		}

	    /**
	     * B2B形态
	     *
	     * @param b2bPattern
	     */
		public void setB2bPattern(String b2bPattern) {
			this.b2bPattern = b2bPattern;
		}
		
		/**
	     * 指定物流商ID
	     */
		public String getCarrierId() {
			return this.carrierId;
		}

	    /**
	     * 指定物流商ID
	     *
	     * @param carrierId
	     */
		public void setCarrierId(String carrierId) {
			this.carrierId = carrierId;
		}
		
		/**
	     * 指定宅配商ID
	     */
		public String getDeliverId() {
			return this.deliverId;
		}

	    /**
	     * 指定宅配商ID
	     *
	     * @param deliverId
	     */
		public void setDeliverId(String deliverId) {
			this.deliverId = deliverId;
		}
		
		/**
	     * 指定站点
	     */
		public String getStationId() {
			return this.stationId;
		}

	    /**
	     * 指定站点
	     *
	     * @param stationId
	     */
		public void setStationId(String stationId) {
			this.stationId = stationId;
		}
		
		/**
	     * 指定发件人
	     */
		public String getSeller() {
			return this.seller;
		}

	    /**
	     * 指定发件人
	     *
	     * @param seller
	     */
		public void setSeller(String seller) {
			this.seller = seller;
		}
		
		/**
	     * 指定发件人手机
	     */
		public String getSellerMobile() {
			return this.sellerMobile;
		}

	    /**
	     * 指定发件人手机
	     *
	     * @param sellerMobile
	     */
		public void setSellerMobile(String sellerMobile) {
			this.sellerMobile = sellerMobile;
		}
		
		/**
	     * 指定发件人电话
	     */
		public String getSellerTel() {
			return this.sellerTel;
		}

	    /**
	     * 指定发件人电话
	     *
	     * @param sellerTel
	     */
		public void setSellerTel(String sellerTel) {
			this.sellerTel = sellerTel;
		}
		
		/**
	     * 省(发件人)
	     */
		public String getSellerProvince() {
			return this.sellerProvince;
		}

	    /**
	     * 省(发件人)
	     *
	     * @param sellerProvince
	     */
		public void setSellerProvince(String sellerProvince) {
			this.sellerProvince = sellerProvince;
		}
		
		/**
	     * 市(发件人)
	     */
		public String getSellerCity() {
			return this.sellerCity;
		}

	    /**
	     * 市(发件人)
	     *
	     * @param sellerCity
	     */
		public void setSellerCity(String sellerCity) {
			this.sellerCity = sellerCity;
		}
		
		/**
	     * 区(发件人)
	     */
		public String getSellerDistribute() {
			return this.sellerDistribute;
		}

	    /**
	     * 区(发件人)
	     *
	     * @param sellerDistribute
	     */
		public void setSellerDistribute(String sellerDistribute) {
			this.sellerDistribute = sellerDistribute;
		}
		
		/**
	     * 发件人地址
	     */
		public String getSellerAddress() {
			return this.sellerAddress;
		}

	    /**
	     * 发件人地址
	     *
	     * @param sellerAddress
	     */
		public void setSellerAddress(String sellerAddress) {
			this.sellerAddress = sellerAddress;
		}
		
		/**
	     * 指定运单号
	     */
		public String getTookWorkcode() {
			return this.tookWorkcode;
		}

	    /**
	     * 指定运单号
	     *
	     * @param tookWorkcode
	     */
		public void setTookWorkcode(String tookWorkcode) {
			this.tookWorkcode = tookWorkcode;
		}
		
		/**
	     * 国际运单号
	     */
		public String getNaWorkcode() {
			return this.naWorkcode;
		}

	    /**
	     * 国际运单号
	     *
	     * @param naWorkcode
	     */
		public void setNaWorkcode(String naWorkcode) {
			this.naWorkcode = naWorkcode;
		}

		public String getTempletName() {
			return templetName;
		}

		public void setTempletName(String templetName) {
			this.templetName = templetName;
		}

		public Long getTempletType() {
			return templetType;
		}

		public void setTempletType(Long templetType) {
			this.templetType = templetType;
		}

		public String getTempletCustomerid() {
			return templetCustomerid;
		}

		public void setTempletCustomerid(String templetCustomerid) {
			this.templetCustomerid = templetCustomerid;
		}

		public String getTempletCrepersonid() {
			return templetCrepersonid;
		}

		public void setTempletCrepersonid(String templetCrepersonid) {
			this.templetCrepersonid = templetCrepersonid;
		}

		public Timestamp getTempletCretime() {
			return templetCretime;
		}

		public void setTempletCretime(Timestamp templetCretime) {
			this.templetCretime = templetCretime;
		}

		public String getTempletShortname() {
			return templetShortname;
		}

		public void setTempletShortname(String templetShortname) {
			this.templetShortname = templetShortname;
		}

		public String getTempletCreperson() {
			return templetCreperson;
		}

		public void setTempletCreperson(String templetCreperson) {
			this.templetCreperson = templetCreperson;
		}
	
}
