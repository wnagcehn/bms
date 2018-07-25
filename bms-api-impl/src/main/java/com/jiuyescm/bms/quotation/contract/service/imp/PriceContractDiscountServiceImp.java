package com.jiuyescm.bms.quotation.contract.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.quotation.contract.entity.PriceContractDiscountItemEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDiscountRepository;
import com.jiuyescm.bms.quotation.contract.service.IPriceContractDiscountService;

@Service("priceContractDiscountService")
public class PriceContractDiscountServiceImp implements IPriceContractDiscountService{

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
		return priceContractDiscountRepository.insertDiscountItem(list);
	}
	
}
