package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsWeightAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsWeightAccountRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsProductsWeightAccountRepository")
public class BmsProductsWeightAccountRepositoryImpl extends MyBatisDao<BmsProductsWeightAccountEntity> implements IBmsProductsWeightAccountRepository {

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsProductsWeightAccountEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsProductsWeightAccountEntity> list = selectList("com.jiuyescm.bms.correct.BmsProductsWeightAccountMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsProductsWeightAccountEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsProductsWeightAccountEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.correct.BmsProductsWeightAccountMapper.query", condition);
	}

}
