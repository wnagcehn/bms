package com.jiuyescm.bms.quotation.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PriceDispatchTemplateEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchTemplateDao;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchTemplateService;

/**
 * 报价模板操作实现
 * @author wubangjun
 */
@Service("deliverTemplateService")
public class PriceDispatchTemplateServiceImpl implements IPriceDispatchTemplateService{
		
	@Autowired
	private IPriceDispatchTemplateDao priceDispatchTemplateDao;
	
	@Override
	public PageInfo<PriceDispatchTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		return priceDispatchTemplateDao.query(condition, pageNo, pageSize);
	}
	
	@Override
	public PriceDispatchTemplateEntity query(Map<String, Object> condition) {
		return priceDispatchTemplateDao.query(condition);
	}

	@Override
	public PriceDispatchTemplateEntity findById(Long id) {
		return priceDispatchTemplateDao.findById(id);
	}

	@Override
	public PriceDispatchTemplateEntity save(PriceDispatchTemplateEntity entity) {
		return priceDispatchTemplateDao.save(entity);
	}

	@Override
	public PriceDispatchTemplateEntity update(PriceDispatchTemplateEntity entity) {
		return priceDispatchTemplateDao.update(entity);
	}

	@Override
	public int delete(PriceDispatchTemplateEntity entity) {
		return priceDispatchTemplateDao.delete(entity);
	}

	@Override
	public List<PriceDispatchTemplateEntity> queryDeliverTemplate(Map<String, Object> condition) {
		return priceDispatchTemplateDao.queryDeliverTemplate(condition);
	}

}
