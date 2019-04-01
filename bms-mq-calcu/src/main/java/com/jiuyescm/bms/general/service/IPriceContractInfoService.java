package com.jiuyescm.bms.general.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;

/**
 * 合同主信息接口
 * @author cjw 2017-06-06
 * 
 */
public interface IPriceContractInfoService {
    
    List<PriceContractInfoEntity> queryContract(Map<String, Object> condition);

    PriceContractInfoEntity queryContractByCustomer(Map<String, Object> condition);
    
    /**
	 * 查询应收具体配送报价信息
	 * @param aCondition
	 */
	public PriceMainDispatchEntity queryOne(Map<String,Object> parameter);
	
	/**
	 * 查询应付具体配送报价信息
	 * @param parameter
	 * @return
	 */
	public PriceMainDispatchEntity queryPayOne(Map<String, Object> parameter);

	String queryTemplateId(Map<String, Object> map);
	
	String queryStandardTemplateId(Map<String, Object> map);
	
	String queryOneTemplate(Map<String,Object> map);
	
	public List<String> queryJiuYeArea(Map<String, Object> map);
		
    /**
	 * 查询应收具体配送报价信息
	 * @param aCondition
	 */
	public BmsQuoteDispatchDetailVo queryNewOne(Map<String,Object> parameter);
	
	public List<BmsQuoteDispatchDetailVo> queryAllByTemplateId(Map<String, Object> map);
	
	public List<BmsQuoteDispatchDetailVo> queryShunfengDispatch(Map<String, Object> map);

	String queryPriceType(Map<String,Object> map);
	
	String queryShunfengPriceType(Map<String,Object> map);
}
