package com.jiuyescm.bms.quotation.contract.service.imp;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDiscountRepository;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;

@Service("priceContractDiscountService")
public class PriceContractDiscountServiceImp implements IPriceContractDiscountService{
	private Logger logger = LoggerFactory.getLogger(PriceContractDiscountServiceImp.class);

	@Resource
	private IPriceContractDiscountRepository priceContractDiscountRepository;
	
	@Override
	public int updateDiscountItem(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceContractDiscountRepository.updateDiscountItem(condition);
	}

	@Override
	public int insertDiscountItem(List<PriceContractDiscountItemEntity> list) {
		// TODO Auto-generated method stub
		try {
			priceContractDiscountRepository.insertDiscountItem(list);
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("签约折扣服务失败",e);
			return 0;
		}
	}

	@Override
	public List<BmsQuoteDiscountDetailEntity> queryDiscountPrice(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceContractDiscountRepository.queryDiscountPrice(condition);
	}

	@Override
	public PriceContractDiscountItemEntity query(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		
		return priceContractDiscountRepository.query(condition);
	}
	
	@Override
	public List<PriceContractDiscountItemEntity> queryByCustomerId(BmsDiscountAsynTaskEntity entity) {	
		return priceContractDiscountRepository.queryByCustomerId(entity);
	}
	
	@Override
	public List<PriceContractDiscountItemEntity> queryByCustomerIdAndBizType(Map<String, String> param) {	
		return priceContractDiscountRepository.queryByCustomerIdAndBizType(param);
	}
	
	@Override
	public List<PriceContractDiscountItemEntity> queryAll(Timestamp time) {	
		return priceContractDiscountRepository.queryAll(time);
	}
}
