package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsMarkingProductsEntity;
import com.jiuyescm.bms.correct.repository.IBmsMarkingProductsRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsMarkingProductsRepository")
public class BmsMarkingProductsRepositoryImpl extends MyBatisDao<BmsMarkingProductsEntity> implements IBmsMarkingProductsRepository {
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */	
	@Override
    public PageInfo<BmsMarkingProductsEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsMarkingProductsEntity> list = selectList("com.jiuyescm.bms.correct.BmsMarkingProductsMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsMarkingProductsEntity>(list);
    }
	
	/**
	 * 查询该组合下不同运单重量对应的运单明细
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
    public PageInfo<BmsMarkingProductsEntity> queryByWeight(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsMarkingProductsEntity> list = selectList("com.jiuyescm.bms.correct.BmsMarkingProductsMapper.queryByWeight", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsMarkingProductsEntity>(list);
    }
    
	/**
	 * 查询不同耗材组合对应的耗材明细
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Override
    public PageInfo<BmsMarkingProductsEntity> queryByMaterial(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsMarkingProductsEntity> list = selectList("com.jiuyescm.bms.correct.BmsMarkingProductsMapper.queryByMaterial", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsMarkingProductsEntity>(list);
    }
	

	
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsMarkingProductsEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.correct.BmsMarkingProductsMapper.query", condition);
	}
	
}
