package com.jiuyescm.bms.quotation.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceGeneralQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceGeneralQuotationRepository;
import com.jiuyescm.bms.quotation.storage.service.IPriceGeneralQuotationService;

/**
 * 
 * @author cjw
 * 
 */
@Service("priceGeneralQuotationService")
public class PriceGeneralQuotationServiceImpl implements IPriceGeneralQuotationService {
	
	@Autowired
    private IPriceGeneralQuotationRepository priceGeneralQuotationRepository;

    @Override
    public PageInfo<PriceGeneralQuotationEntity> query(Map<String, Object> condition,int pageNo, int pageSize) {
        return priceGeneralQuotationRepository.query(condition, pageNo, pageSize);
    }
    
    @Override
    public PriceGeneralQuotationEntity query(Map<String, Object> condition) {
    	return priceGeneralQuotationRepository.query(condition);
    }

    @Override
    public PriceGeneralQuotationEntity findById(Long id) {
        return priceGeneralQuotationRepository.findById(id);
    }

    @Override
    public PriceGeneralQuotationEntity save(PriceGeneralQuotationEntity entity) {
        return priceGeneralQuotationRepository.save(entity);
    }

    @Override
    public PriceGeneralQuotationEntity update(PriceGeneralQuotationEntity entity) {
        return priceGeneralQuotationRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        priceGeneralQuotationRepository.delete(id);
    }

	@Override
	public Integer insert(PriceGeneralQuotationEntity entity) {
		return priceGeneralQuotationRepository.insert(entity);
	}

	@Override
	public PriceGeneralQuotationEntity findByNo(String quotationNo) {
		return priceGeneralQuotationRepository.findByNo(quotationNo);
	}

	@Override
	public List<PriceGeneralQuotationEntity> queryPriceGeneral(
			Map<String, Object> condition) {
		return priceGeneralQuotationRepository.queryPriceGeneral(condition);
	}

	@Override
	public void removeAll(Map<String, Object> map) {
		priceGeneralQuotationRepository.removeAll(map);
	}

	@Override
	public List<PriceGeneralQuotationEntity> queryPriceStandardGeneral(Map<String, Object> condition) {
		return priceGeneralQuotationRepository.queryPriceStandardGeneral(condition);
	}

	@Override
	public PriceGeneralQuotationEntity queryByQuotationNo(String quotationNo) {
		return priceGeneralQuotationRepository.queryByQuotationNo(quotationNo);
	}
	
}
