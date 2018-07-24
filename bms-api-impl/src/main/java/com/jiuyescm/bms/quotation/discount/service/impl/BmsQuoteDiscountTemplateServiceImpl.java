package com.jiuyescm.bms.quotation.discount.service.impl;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.repository.IBmsQuoteDiscountDetailRepository;
import com.jiuyescm.bms.quotation.discount.repository.IBmsQuoteDiscountTemplateRepository;
import com.jiuyescm.bms.quotation.discount.service.IBmsQuoteDiscountTemplateService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsQuoteDiscountTemplateService")
public class BmsQuoteDiscountTemplateServiceImpl implements IBmsQuoteDiscountTemplateService {

	@Autowired
    private IBmsQuoteDiscountTemplateRepository bmsQuoteDiscountTemplateRepository;
	
	@Autowired
	private IBmsQuoteDiscountDetailRepository bmsQuoteDiscountDetailRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BmsQuoteDiscountTemplateEntity findById(Long id) {
        return bmsQuoteDiscountTemplateRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BmsQuoteDiscountTemplateEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsQuoteDiscountTemplateRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsQuoteDiscountTemplateEntity> query(Map<String, Object> condition){
		return bmsQuoteDiscountTemplateRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountTemplateEntity save(BmsQuoteDiscountTemplateEntity entity) {
        return bmsQuoteDiscountTemplateRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountTemplateEntity update(BmsQuoteDiscountTemplateEntity entity) {
        return bmsQuoteDiscountTemplateRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(BmsQuoteDiscountTemplateEntity entity) {
        bmsQuoteDiscountTemplateRepository.delete(entity);
    }
	
}
