package com.jiuyescm.bms.quotation.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.bms.quotation.storage.service.IPriceStepQuotationService;

@Service("priceStepQuotationService")
public class PriceStepQuotationServiceImpl implements
		IPriceStepQuotationService {
	
	@Autowired
	private IPriceStepQuotationRepository  repository;
	@Autowired
    private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;

	@Override
	public PageInfo<PriceStepQuotationEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query(condition, pageNo, pageSize);
	}

	@Override
	public Integer save(PriceStepQuotationEntity entity) {
		return repository.insert(entity);
	}

	@Override
	public Integer update(PriceStepQuotationEntity entity) {
		return repository.update(entity);
	}

	@Override
	public Integer delete(PriceStepQuotationEntity entity) {
		return null;
	}

	@Override
	public int insertBatchTmp(List<PriceStepQuotationEntity> list) {
		return repository.insertBatchTmp(list);
	}

	@Override
	public List<PriceStepQuotationEntity> queryPriceStep(Map<String, Object> param) {
		return repository.queryPriceStep(param);
	}

	@Override
	public void removeAll(Map<String, Object> map) {
		repository.removeAll(map);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void remove(Map<String, Object> map) {
		priceGeneralQuotationRepository.removeAll(map);
		repository.removeAll(map);
	}

	@Override
	public List<PriceStepQuotationEntity> queryPriceStandardStep(Map<String, Object> param) {
		return repository.queryPriceStandardStep(param);
	}

}
