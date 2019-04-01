package com.jiuyescm.bms.calcu.base;

import java.util.List;
import java.util.Map;

import com.jiuyescm.contract.quote.vo.ContractQuoteQueryInfoVo;


public interface ICalcuService<T,F> {

	/**
	 * 查询bms报价模板
	 */
	public void getQuoTemplete();
	
	/**
	 * 费用计算
	 */
	public void calcu(Map<String, Object> map);
	
	
	/**
	 * 缓存参数
	 */
	public void initConf();
	
	/**
	 * 初始化费用
	 */
	public F initFee(T entity);
	
	public boolean isNoExe(T entity,F fee);
	
	public void calcuForBms(T entity,F fee);
	
	public void calcuForContract(T entity,F fee);
	
	public ContractQuoteQueryInfoVo getCtConditon(T entity);
	
	public void updateBatch(List<T> bizList,List<F> feeList);
}
