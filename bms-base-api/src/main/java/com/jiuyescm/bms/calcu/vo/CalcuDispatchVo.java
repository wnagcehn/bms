package com.jiuyescm.bms.calcu.vo;

import java.math.BigDecimal;

/**
 * 标准配送计费vo
 * 
 * @author caojianwei
 *
 */
public class CalcuDispatchVo {

	// 运单号
	private String waybillNo;
	// 计费重量
	private BigDecimal chargeWeight;
	// 计费数量
	private BigDecimal chargeQty;
	// 数量
	private BigDecimal chargeSku;
	// 箱数
	private BigDecimal chargeBox;
	// 体积
	private BigDecimal chargeVolume;

}
