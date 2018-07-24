package com.jiuyescm.bms.quotation.discount.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountTemplateEntity;
import com.jiuyescm.bms.quotation.discount.repository.IBmsQuoteDiscountTemplateRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsQuoteDiscountTemplateRepository")
public class BmsQuoteDiscountTemplateRepositoryImpl extends MyBatisDao<BmsQuoteDiscountTemplateEntity> implements IBmsQuoteDiscountTemplateRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BmsQuoteDiscountTemplateEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountTemplateMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsQuoteDiscountTemplateEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsQuoteDiscountTemplateEntity> list = selectList("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountTemplateMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsQuoteDiscountTemplateEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsQuoteDiscountTemplateEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountTemplateMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountTemplateEntity save(BmsQuoteDiscountTemplateEntity entity) {
        insert("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountTemplateMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountTemplateEntity update(BmsQuoteDiscountTemplateEntity entity) {
        update("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountTemplateMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(BmsQuoteDiscountTemplateEntity entity) {
        update("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountTemplateMapper.delete", entity);
    }
	
}
