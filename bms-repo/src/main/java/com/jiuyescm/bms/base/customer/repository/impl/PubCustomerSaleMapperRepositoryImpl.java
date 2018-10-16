package com.jiuyescm.bms.base.customer.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.customer.entity.PubCustomerSaleMapperEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerSaleMapperRepository;
import com.jiuyescm.bms.quotation.dispatch.entity.BmsQuoteDispatchDetailEntity;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("pubCustomerSaleMapperRepository")
public class PubCustomerSaleMapperRepositoryImpl extends MyBatisDao<PubCustomerSaleMapperEntity> implements IPubCustomerSaleMapperRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public PubCustomerSaleMapperEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<PubCustomerSaleMapperEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubCustomerSaleMapperEntity> list = selectList("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubCustomerSaleMapperEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubCustomerSaleMapperEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public PubCustomerSaleMapperEntity save(PubCustomerSaleMapperEntity entity) {
        insert("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public PubCustomerSaleMapperEntity update(PubCustomerSaleMapperEntity entity) {
        update("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.delete", id);
    }
    
    /**
     * 批量写入
     * @param list
     * @return
     */
	@Override
	public int insertBatchTmp(List<PubCustomerSaleMapperEntity> list) {
		return insertBatch("com.jiuyescm.bms.base.customer.PubCustomerSaleMapperMapper.save", list);
	}
	
}
