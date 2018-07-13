package com.jiuyescm.bms.quotation.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceExtraQuotationRepository;
import com.jiuyescm.bms.quotation.storage.service.IPriceExtraQuotationService;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;

@Service("priceExtraQuotationService")
public class PriceExtraQuotationServiceImpl implements
		IPriceExtraQuotationService {
	
	@Autowired
	private IPriceExtraQuotationRepository repository;
	@Autowired
	private IGenericTemplateRepository genericTemplateRepository;

	@Override
	public PageInfo<PriceExtraQuotationEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query(condition, pageNo, pageSize);
	}

	@Override
	public Integer save(PriceExtraQuotationEntity entity) {
		return repository.insert(entity);
	}

	@Override
	public Integer update(PriceExtraQuotationEntity entity) {
		return repository.update(entity);
	}

	@Override
	public Integer delete(PriceExtraQuotationEntity entity) {
		return null;
	}

	@Override
	public Integer insertBatchTmp(List<PriceExtraQuotationEntity> list) {
		return repository.insertBatchTmp(list);
	}

	@Override
	public void removeAll(Map<String, Object> map) {
		repository.removeAll(map);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void remove(Map<String, Object> map) {
		genericTemplateRepository.remove(map);
		repository.removeAll(map);
	}

	@Override
	public List<PriceExtraQuotationEntity> queryPrice(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return repository.queryPrice(param);
	}

	@Override
	public List<PriceExtraQuotationEntity> queryPriceByParam(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		return repository.queryPriceByParam(param);
	}

	

}
