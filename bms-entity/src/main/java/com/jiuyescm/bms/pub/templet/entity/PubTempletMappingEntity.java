package com.jiuyescm.bms.pub.templet.entity;

import java.sql.Timestamp;

import com.jiuyescm.cfm.domain.IEntity;

public class PubTempletMappingEntity implements IEntity{

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
		private String col01;
		// 始发仓ID
		private String col02;
		// 商家ID
		private String col03;
		// 外部物流单号
		private String col04;
		// 项目ID
		private String col05;
		// 平台编码
		private String col06;
		// 平台交易单号
		private String col07;
		// 收件人联系人
		private String col08;
		// 收件人手机
		private String col09;
		// 收件人电话
		private String col10;
		// 省
		private String col11;
		// 市
		private String col12;
		// 区
		private String col13;
		// 收件人地址
		private String col14;
		// 收件人邮编
		private String col15;
		// 要求送达日期
		private String col16;
		// 投递时延要求
		private String col17;
		// 付款方式
		private String col18;
		// 付款金额
		private String decnum01;
		// 订单金额
		private String decnum02;
		// 买家备注
		private String col21;
		// 卖家备注
		private String col22;
		// 行号
		private String col23;
		// OMS商品ID
		private String col24;
		// 商品PN
		private String col25;
		// 数量
		private String decnum03;
		// 库存类型编码
		private String col26;
		// 批次编码
		private String col27;
		// 明细备注
		private String col28;
		// B2B形态
		private String col29;
		// 指定物流商ID
		private String col30;
		// 指定宅配商ID
		private String col31;
		// 指定站点
		private String col32;
		// 指定发件人
		private String col33;
		// 指定发件人手机
		private String col34;
		// 指定发件人电话
		private String col35;
		// 省(发件人)
		private String col36;
		// 市(发件人)
		private String col37;
		// 区(发件人)
		private String col38;
		// 发件人地址
		private String col39;
		// 指定运单号
		private String col40;
		// 国际运单号
		private String col41;

		public PubTempletMappingEntity() {

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
	
		/**
		 * 单据类型
		 */
		public String getCol01() {
			return this.col01;
		}

		/**
		 * 单据类型
		 * 
		 * @param col01
		 */
		public void setCol01(String col01) {
			this.col01 = col01;
		}

		/**
		 * 始发仓ID
		 */
		public String getCol02() {
			return this.col02;
		}

		/**
		 * 始发仓ID
		 * 
		 * @param col02
		 */
		public void setCol02(String col02) {
			this.col02 = col02;
		}

		/**
		 * 商家ID
		 */
		public String getCol03() {
			return this.col03;
		}

		/**
		 * 商家ID
		 * 
		 * @param col03
		 */
		public void setCol03(String col03) {
			this.col03 = col03;
		}

		/**
		 * 外部物流单号
		 */
		public String getCol04() {
			return this.col04;
		}

		/**
		 * 外部物流单号
		 * 
		 * @param col04
		 */
		public void setCol04(String col04) {
			this.col04 = col04;
		}

		/**
		 * 项目ID
		 */
		public String getCol05() {
			return this.col05;
		}

		/**
		 * 项目ID
		 * 
		 * @param col05
		 */
		public void setCol05(String col05) {
			this.col05 = col05;
		}

		/**
		 * 平台编码
		 */
		public String getCol06() {
			return this.col06;
		}

		/**
		 * 平台编码
		 * 
		 * @param col06
		 */
		public void setCol06(String col06) {
			this.col06 = col06;
		}

		/**
		 * 平台交易单号
		 */
		public String getCol07() {
			return this.col07;
		}

		/**
		 * 平台交易单号
		 * 
		 * @param col07
		 */
		public void setCol07(String col07) {
			this.col07 = col07;
		}

		/**
		 * 收件人联系人
		 */
		public String getCol08() {
			return this.col08;
		}

		/**
		 * 收件人联系人
		 * 
		 * @param col08
		 */
		public void setCol08(String col08) {
			this.col08 = col08;
		}

		/**
		 * 收件人手机
		 */
		public String getCol09() {
			return this.col09;
		}

		/**
		 * 收件人手机
		 * 
		 * @param col09
		 */
		public void setCol09(String col09) {
			this.col09 = col09;
		}

		/**
		 * 收件人电话
		 */
		public String getCol10() {
			return this.col10;
		}

		/**
		 * 收件人电话
		 * 
		 * @param col10
		 */
		public void setCol10(String col10) {
			this.col10 = col10;
		}

		/**
		 * 省
		 */
		public String getCol11() {
			return this.col11;
		}

		/**
		 * 省
		 * 
		 * @param col11
		 */
		public void setCol11(String col11) {
			this.col11 = col11;
		}

		/**
		 * 市
		 */
		public String getCol12() {
			return this.col12;
		}

		/**
		 * 市
		 * 
		 * @param col12
		 */
		public void setCol12(String col12) {
			this.col12 = col12;
		}

		/**
		 * 区
		 */
		public String getCol13() {
			return this.col13;
		}

		/**
		 * 区
		 * 
		 * @param col13
		 */
		public void setCol13(String col13) {
			this.col13 = col13;
		}

		/**
		 * 收件人地址
		 */
		public String getCol14() {
			return this.col14;
		}

		/**
		 * 收件人地址
		 * 
		 * @param col14
		 */
		public void setCol14(String col14) {
			this.col14 = col14;
		}

		/**
		 * 收件人邮编
		 */
		public String getCol15() {
			return this.col15;
		}

		/**
		 * 收件人邮编
		 * 
		 * @param col15
		 */
		public void setCol15(String col15) {
			this.col15 = col15;
		}

		/**
		 * 要求送达日期
		 */
		public String getCol16() {
			return this.col16;
		}

		/**
		 * 要求送达日期
		 * 
		 * @param col16
		 */
		public void setCol16(String col16) {
			this.col16 = col16;
		}

		/**
		 * 投递时延要求
		 */
		public String getCol17() {
			return this.col17;
		}

		/**
		 * 投递时延要求
		 * 
		 * @param col17
		 */
		public void setCol17(String col17) {
			this.col17 = col17;
		}

		/**
		 * 付款方式
		 */
		public String getCol18() {
			return this.col18;
		}

		/**
		 * 付款方式
		 * 
		 * @param col18
		 */
		public void setCol18(String col18) {
			this.col18 = col18;
		}

		/**
		 * 付款金额
		 */
		public String getDecnum01() {
			return this.decnum01;
		}

		/**
		 * 付款金额
		 * 
		 * @param decnum01
		 */
		public void setDecnum01(String decnum01) {
			this.decnum01 = decnum01;
		}

		/**
		 * 订单金额
		 */
		public String getDecnum02() {
			return this.decnum02;
		}

		/**
		 * 订单金额
		 * 
		 * @param decnum02
		 */
		public void setDecnum02(String decnum02) {
			this.decnum02 = decnum02;
		}

		/**
		 * 买家备注
		 */
		public String getCol21() {
			return this.col21;
		}

		/**
		 * 买家备注
		 * 
		 * @param col21
		 */
		public void setCol21(String col21) {
			this.col21 = col21;
		}

		/**
		 * 卖家备注
		 */
		public String getCol22() {
			return this.col22;
		}

		/**
		 * 卖家备注
		 * 
		 * @param col22
		 */
		public void setCol22(String col22) {
			this.col22 = col22;
		}

		/**
		 * 行号
		 */
		public String getCol23() {
			return this.col23;
		}

		/**
		 * 行号
		 * 
		 * @param col23
		 */
		public void setCol23(String col23) {
			this.col23 = col23;
		}

		/**
		 * OMS商品ID
		 */
		public String getCol24() {
			return this.col24;
		}

		/**
		 * OMS商品ID
		 * 
		 * @param col24
		 */
		public void setCol24(String col24) {
			this.col24 = col24;
		}

		/**
		 * 商品PN
		 */
		public String getCol25() {
			return this.col25;
		}

		/**
		 * 商品PN
		 * 
		 * @param col25
		 */
		public void setCol25(String col25) {
			this.col25 = col25;
		}

		/**
		 * 数量
		 */
		public String getDecnum03() {
			return this.decnum03;
		}

		/**
		 * 数量
		 * 
		 * @param decnum03
		 */
		public void setDecnum03(String decnum03) {
			this.decnum03 = decnum03;
		}

		/**
		 * 库存类型编码
		 */
		public String getCol26() {
			return this.col26;
		}

		/**
		 * 库存类型编码
		 * 
		 * @param col26
		 */
		public void setCol26(String col26) {
			this.col26 = col26;
		}

		/**
		 * 批次编码
		 */
		public String getCol27() {
			return this.col27;
		}

		/**
		 * 批次编码
		 * 
		 * @param col27
		 */
		public void setCol27(String col27) {
			this.col27 = col27;
		}

		/**
		 * 明细备注
		 */
		public String getCol28() {
			return this.col28;
		}

		/**
		 * 明细备注
		 * 
		 * @param col28
		 */
		public void setCol28(String col28) {
			this.col28 = col28;
		}

		/**
		 * B2B形态
		 */
		public String getCol29() {
			return this.col29;
		}

		/**
		 * B2B形态
		 * 
		 * @param col29
		 */
		public void setCol29(String col29) {
			this.col29 = col29;
		}

		/**
		 * 指定物流商ID
		 */
		public String getCol30() {
			return this.col30;
		}

		/**
		 * 指定物流商ID
		 * 
		 * @param col30
		 */
		public void setCol30(String col30) {
			this.col30 = col30;
		}

		/**
		 * 指定宅配商ID
		 */
		public String getCol31() {
			return this.col31;
		}

		/**
		 * 指定宅配商ID
		 * 
		 * @param col31
		 */
		public void setCol31(String col31) {
			this.col31 = col31;
		}

		/**
		 * 指定站点
		 */
		public String getCol32() {
			return this.col32;
		}

		/**
		 * 指定站点
		 * 
		 * @param col32
		 */
		public void setCol32(String col32) {
			this.col32 = col32;
		}

		/**
		 * 指定发件人
		 */
		public String getCol33() {
			return this.col33;
		}

		/**
		 * 指定发件人
		 * 
		 * @param col33
		 */
		public void setCol33(String col33) {
			this.col33 = col33;
		}

		/**
		 * 指定发件人手机
		 */
		public String getCol34() {
			return this.col34;
		}

		/**
		 * 指定发件人手机
		 * 
		 * @param col34
		 */
		public void setCol34(String col34) {
			this.col34 = col34;
		}

		/**
		 * 指定发件人电话
		 */
		public String getCol35() {
			return this.col35;
		}

		/**
		 * 指定发件人电话
		 * 
		 * @param col35
		 */
		public void setCol35(String col35) {
			this.col35 = col35;
		}

		/**
		 * 省(发件人)
		 */
		public String getCol36() {
			return this.col36;
		}

		/**
		 * 省(发件人)
		 * 
		 * @param col36
		 */
		public void setCol36(String col36) {
			this.col36 = col36;
		}

		/**
		 * 市(发件人)
		 */
		public String getCol37() {
			return this.col37;
		}

		/**
		 * 市(发件人)
		 * 
		 * @param col37
		 */
		public void setCol37(String col37) {
			this.col37 = col37;
		}

		/**
		 * 区(发件人)
		 */
		public String getCol38() {
			return this.col38;
		}

		/**
		 * 区(发件人)
		 * 
		 * @param col38
		 */
		public void setCol38(String col38) {
			this.col38 = col38;
		}

		/**
		 * 发件人地址
		 */
		public String getCol39() {
			return this.col39;
		}

		/**
		 * 发件人地址
		 * 
		 * @param col39
		 */
		public void setCol39(String col39) {
			this.col39 = col39;
		}

		/**
		 * 指定运单号
		 */
		public String getCol40() {
			return this.col40;
		}

		/**
		 * 指定运单号
		 * 
		 * @param col40
		 */
		public void setCol40(String col40) {
			this.col40 = col40;
		}

		/**
		 * 国际运单号
		 */
		public String getCol41() {
			return this.col41;
		}

		/**
		 * 国际运单号
		 * 
		 * @param col41
		 */
		public void setCol41(String col41) {
			this.col41 = col41;
		}
}
