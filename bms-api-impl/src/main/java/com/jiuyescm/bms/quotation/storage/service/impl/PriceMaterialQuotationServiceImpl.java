package com.jiuyescm.bms.quotation.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.bms.quotation.storage.service.IPriceMaterialQuotationService;
import com.jiuyescm.bms.quotation.transport.repository.IGenericTemplateRepository;

@Service("priceMaterialQuotationService")
public class PriceMaterialQuotationServiceImpl implements
		IPriceMaterialQuotationService {
	
	@Autowired
	private IPriceMaterialQuotationRepository repository;
	@Autowired
	private IGenericTemplateRepository genericTemplateRepository;
	
	@Override
	public PageInfo<PriceMaterialQuotationEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return repository.query(condition, pageNo, pageSize);
	}

	@Override
	public Integer save(PriceMaterialQuotationEntity entity) {
		return repository.insert(entity);
	}

	@Override
	public Integer delete(PriceMaterialQuotationEntity entity) {
		return repository.delete(entity.getId());
	}

	@Override
	public Integer update(PriceMaterialQuotationEntity entity) {
		return repository.update(entity);
	}

	@Override
	public Integer insertBatchTmp(List<PriceMaterialQuotationEntity> list) {
		return  repository.insertBatchTmp(list);
	}

	@Override
	public List<PriceMaterialQuotationEntity> queryStepMaterial(Map<String, Object> condition) {
		return repository.queryStepMaterial(condition);
	}

	@Override
	public PriceMaterialQuotationEntity queryOneMaterial(Map<String, Object> condition) {
		return repository.queryOneMaterial(condition);
	}

	@Override
	public void removeDetail(Map<String, Object> map) {
		repository.removeDetail(map);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
	public void removeAll(Map<String, Object> map) {
		genericTemplateRepository.remove(map);
		repository.removeDetail(map);
	}

	/**
	 * 查询标准报价
	 */
	@Override
	public List<PriceMaterialQuotationEntity> queryStandardMaterial(Map<String, Object> condition) {
		return repository.queryStandardMaterial(condition);
	}

	@Override
	public List<PriceMaterialQuotationEntity> queryById(
			Map<String, Object> parameter) {
		return repository.queryAllById(parameter);
	}

}
