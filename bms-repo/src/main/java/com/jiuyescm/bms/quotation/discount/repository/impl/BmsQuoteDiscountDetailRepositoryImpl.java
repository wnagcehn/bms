package com.jiuyescm.bms.quotation.discount.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.quotation.discount.entity.BmsQuoteDiscountDetailEntity;
import com.jiuyescm.bms.quotation.discount.repository.IBmsQuoteDiscountDetailRepository;
import com.jiuyescm.bms.quotation.dispatch.entity.BmsQuoteDispatchDetailEntity;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsQuoteDiscountDetailRepository")
public class BmsQuoteDiscountDetailRepositoryImpl extends MyBatisDao<BmsQuoteDiscountDetailEntity> implements IBmsQuoteDiscountDetailRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BmsQuoteDiscountDetailEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.findById", id);
    }
    
    @Override
    public List<BmsQuoteDiscountDetailEntity> queryByTemplateCode(String code) {
    	return selectList("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.queryByTemplateCode", code);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsQuoteDiscountDetailEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsQuoteDiscountDetailEntity> list = selectList("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsQuoteDiscountDetailEntity>(list);
    }

    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsQuoteDiscountDetailEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountDetailEntity save(BmsQuoteDiscountDetailEntity entity) {
        insert("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsQuoteDiscountDetailEntity update(BmsQuoteDiscountDetailEntity entity) {
        update("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(BmsQuoteDiscountDetailEntity entity) {
    	update("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.delete", entity);
    }
    
    /**
     * 导入
     * @param list
     * @return
     */
	@Override
	public int insertBatch(List<BmsQuoteDiscountDetailEntity> list) {
		return insertBatch("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.save", list);
	}
	
	/**
	 * 查询物流产品类型
	 */
	@Override
    public String queryServiceTypeName(Map<String, Object> condition) {
		String string = (String) selectOne("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.queryServiceTypeName", condition);
        return string;
    }

	@Override
    public String queryServiceTypeCode(Map<String, Object> condition) {
		String string = (String) selectOne("com.jiuyescm.bms.quotation.discount.BmsQuoteDiscountDetailMapper.queryServiceTypeCode", condition);
        return string;
    }
}
