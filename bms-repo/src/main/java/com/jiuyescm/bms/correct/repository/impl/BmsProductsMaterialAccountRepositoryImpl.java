package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.correct.BmsProductsMaterialAccountEntity;
import com.jiuyescm.bms.correct.repository.IBmsProductsMaterialAccountRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsProductsMaterialAccountRepository")
public class BmsProductsMaterialAccountRepositoryImpl extends MyBatisDao<BmsProductsMaterialAccountEntity> implements IBmsProductsMaterialAccountRepository {
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsProductsMaterialAccountEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsProductsMaterialAccountEntity> list = selectList("com.jiuyescm.bms.correct.BmsProductsMaterialAccountMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsProductsMaterialAccountEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsProductsMaterialAccountEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.correct.BmsProductsMaterialAccountMapper.query", condition);
	}
	
}
