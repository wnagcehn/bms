package com.jiuyescm.bms.quotation.out.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispacthTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchTemplateDao;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchTemplateService;

/**
 * 报价模板操作实现
 * @author wubangjun
 */
@Service("outDeliverTemplateService")
public class PriceOutDispatchTemplateServiceImpl implements IPriceOutDispatchTemplateService{
		
	@Autowired
	private IPriceOutDispatchTemplateDao priceOutDispatchTemplateDao;
	
	@Override
	public PageInfo<PriceOutDispacthTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		return priceOutDispatchTemplateDao.query(condition, pageNo, pageSize);
	}
	
	@Override
	public PriceOutDispacthTemplateEntity query(Map<String, Object> condition) {
		return priceOutDispatchTemplateDao.query(condition);
	}

	@Override
	public PriceOutDispacthTemplateEntity findById(Long id) {
		return priceOutDispatchTemplateDao.findById(id);
	}

	@Override
	public PriceOutDispacthTemplateEntity save(PriceOutDispacthTemplateEntity entity) {
		return priceOutDispatchTemplateDao.save(entity);
	}

	@Override
	public PriceOutDispacthTemplateEntity update(PriceOutDispacthTemplateEntity entity) {
		return priceOutDispatchTemplateDao.update(entity);
	}

	@Override
	public int delete(PriceOutDispacthTemplateEntity entity) {
		return priceOutDispatchTemplateDao.delete(entity);
	}

	@Override
	public List<PriceOutDispacthTemplateEntity> queryDeliverTemplate(Map<String, Object> condition) {
		return priceOutDispatchTemplateDao.queryDeliverTemplate(condition);
	}

}
