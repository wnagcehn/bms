package com.jiuyescm.bms.calcu.base;

import org.apache.poi.ss.formula.functions.T;

import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;


public interface ICalcuService1 {

	/**
	 * 查询bms报价模板
	 */
	public void getQuoTemplete();
	
	/**
	 * 费用计算
	 */
	public void calcu();
	
	/**
	 * 查询业务数据
	 */
	public void queryBillList();
	
	/**
	 * 缓存参数
	 */
	public void initConf();
	
	/**
	 * 初始化费用
	 */
	public void initFee();
	
	public void bmsCalcu();
	
	public void contractCalcu();
	
	public ContractQuoteQueryInfoVo getCtConditon(T t);
}
