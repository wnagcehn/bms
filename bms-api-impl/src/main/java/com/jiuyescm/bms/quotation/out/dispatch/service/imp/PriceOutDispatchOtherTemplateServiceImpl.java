package com.jiuyescm.bms.quotation.out.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherTemplateEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchOtherTemplateDao;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherTemplateService;

/**
 * 报价模板操作实现
 * @author wubangjun
 */
@Service("outDeliverOtherTemplateService")
public class PriceOutDispatchOtherTemplateServiceImpl implements IPriceOutDispatchOtherTemplateService{
		
	@Autowired
	private IPriceOutDispatchOtherTemplateDao priceOutDispatchTemplateDao;
	
	@Override
	public PageInfo<PriceOutDispatchOtherTemplateEntity> query(Map<String, Object> condition,
			int pageNo, int pageSize) {
		return priceOutDispatchTemplateDao.query(condition, pageNo, pageSize);
	}
	
	@Override
	public PriceOutDispatchOtherTemplateEntity query(Map<String, Object> condition) {
		return priceOutDispatchTemplateDao.query(condition);
	}

	@Override
	public PriceOutDispatchOtherTemplateEntity findById(Long id) {
		return priceOutDispatchTemplateDao.findById(id);
	}

	@Override
	public PriceOutDispatchOtherTemplateEntity save(PriceOutDispatchOtherTemplateEntity entity) {
		return priceOutDispatchTemplateDao.save(entity);
	}

	@Override
	public PriceOutDispatchOtherTemplateEntity update(PriceOutDispatchOtherTemplateEntity entity) {
		return priceOutDispatchTemplateDao.update(entity);
	}

	@Override
	public int delete(PriceOutDispatchOtherTemplateEntity entity) {
		return priceOutDispatchTemplateDao.delete(entity);
	}

	@Override
	public List<PriceOutDispatchOtherTemplateEntity> queryDeliverTemplate(Map<String, Object> condition) {
		return priceOutDispatchTemplateDao.queryDeliverTemplate(condition);
	}

}
