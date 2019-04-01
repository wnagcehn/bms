package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.general.service.IPriceContractInfoService;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw 2017-06-06
 * 
 */
@Service("jobPriceContractInfoService")
public class PriceContractInfoServiceImpl extends MyBatisDao implements IPriceContractInfoService {

	private Logger logger = LoggerFactory.getLogger(PriceContractInfoServiceImpl.class);
    /**
     * 查询单个合同
     */
	@Override
	public List<PriceContractInfoEntity> queryContract(Map<String, Object> cond) {
		/*try{
			return this.getSqlSessionTemplate("BMS").selectList("com.jiuyescm.omstodms.OmsSendDmsMapper.queryContract", cond);
		}
		catch(Exception ex){
			XxlJobLogger.log("查询单个合同异常--"+ex.getMessage());
			return null;
		}*/
		return null;
	}

	@Override
	public PriceContractInfoEntity queryContractByCustomer(Map<String, Object> condition) {
		if(condition.get("customerid")==null){
			logger.warn("商家id为空");
			return null;
		}
		try{
			return (PriceContractInfoEntity)selectOne("com.jiuyescm.bms.general.PriceContractInfoMapper.queryContractByCustomer", condition);
		}
		catch(Exception ex){
			logger.error("查询单个合同异常--"+ex);
			return null;
		}
	}
	
	
	
	/**
	 * 应收配送报价查询具体的报价
	 */
	@Override
	public PriceMainDispatchEntity queryOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		try{
			return (PriceMainDispatchEntity)selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryOne", parameter);
		}
		catch(Exception ex){
			logger.error("查询单个报价异常--"+ex);
			return null;
		}
	}
	
	/**
	 * 应付配送报价查询具体的报价
	 */
	@Override
	public PriceMainDispatchEntity queryPayOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		try{
			return (PriceMainDispatchEntity)selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryPayOne", parameter);
		}
		catch(Exception ex){
			logger.error("查询单个报价异常--"+ex);
			return null;
		}
	}

	@Override
	public String queryTemplateId(Map<String, Object> map) {
		
		Object obj=this.selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryTemplateId", map);
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}

	@Override
	public String queryStandardTemplateId(Map<String, Object> map) {
		Object obj=this.selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryStandardTemplateId", map);
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}

	@Override
	public String queryOneTemplate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Object obj=this.selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryOneTemplate", map);
		if(obj==null){
			return "";
		}else{
			return obj.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BmsQuoteDispatchDetailVo> queryAllByTemplateId(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		try{
			return selectList("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryAllByTemplateId", map);
		}
		catch(Exception ex){
			logger.error("查询报价异常--"+ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BmsQuoteDispatchDetailVo> queryShunfengDispatch(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		try{
			return selectList("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryShunfengDispatch", map);
		}
		catch(Exception ex){
			logger.error("查询顺丰报价异常--"+ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public BmsQuoteDispatchDetailVo queryNewOne(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		try{
			return (BmsQuoteDispatchDetailVo)selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryNewOne", parameter);
		}
		catch(Exception ex){
			logger.error("查询单个报价异常--"+ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryJiuYeArea(Map<String, Object> map) {
		// TODO Auto-generated method stub
		try{
			return selectList("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryJiuYeArea", map);
		}
		catch(Exception ex){
			logger.error("查询九曳配送范围异常--"+ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String queryPriceType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (String) selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryPriceType", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String queryShunfengPriceType(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (String) selectOne("com.jiuyescm.bms.general.mapper.PriceDispatchMapper.queryShunfengPriceType", map);
	}
}
