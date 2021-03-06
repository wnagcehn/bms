package com.jiuyescm.bms.quotation.contract.repository.imp.imp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDiscountRepository;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * 
 * @author cjw by 2017-11-16
 * 
 */
@Repository("priceContractDiscountRepository")
public class PriceContractDiscountRepositoryImpl extends MyBatisDao implements IPriceContractDiscountRepository {

	private static final Logger logger = Logger.getLogger(PriceContractDiscountRepositoryImpl.class.getName());

	public PriceContractDiscountRepositoryImpl() {
		super();
	}

	@Override
	public int updateDiscountItem(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.updateDiscountItem", condition);
	}

	@Override
	public int insertDiscountItem(List<PriceContractDiscountItemEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.insertDiscountItem", list);
	}

	@Override
	public List<BmsQuoteDiscountDetailEntity> queryDiscountPrice(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.queryDiscountPrice", condition);
	}

	@Override
	public PriceContractDiscountItemEntity query(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (PriceContractDiscountItemEntity) selectOne("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.queryItem", condition);
	}
	
	@Override
	public List<PriceContractDiscountItemEntity> queryByCustomerId(BmsDiscountAsynTaskEntity entity) {
		return this.getSqlSessionTemplate().selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.queryByCustomerId", entity);
	}
	
	@Override
	public List<PriceContractDiscountItemEntity> queryByCustomerIdAndBizType(Map<String, String> param) {
		return this.getSqlSessionTemplate().selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.queryByCustomerIdAndBizType", param);
	}

	@Override
	public List<PriceContractDiscountItemEntity> queryAll(Timestamp time) {
		return this.getSqlSessionTemplate().selectList("com.jiuyescm.bms.quotation.contract.mapper.PriceContractDiscountItemMapper.queryAll", time);
	}
	
}
