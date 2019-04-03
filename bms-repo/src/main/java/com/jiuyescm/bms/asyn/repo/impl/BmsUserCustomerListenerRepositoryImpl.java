package com.jiuyescm.bms.asyn.repo.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.asyn.entity.BmsUserCustomerListenerEntity;
import com.jiuyescm.bms.asyn.repo.IBmsUserCustomerListenerRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsUserCustomerListenerRepository")
public class BmsUserCustomerListenerRepositoryImpl extends MyBatisDao<BmsUserCustomerListenerEntity> implements IBmsUserCustomerListenerRepository {

    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsUserCustomerListenerEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.asyn.BmsUserCustomerListenerMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int save(BmsUserCustomerListenerEntity entity) {
        return insert("com.jiuyescm.bms.asyn.BmsUserCustomerListenerMapper.save", entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public int update(BmsUserCustomerListenerEntity entity) {
    	return update("com.jiuyescm.bms.asyn.BmsUserCustomerListenerMapper.update", entity);
    }
	
}
