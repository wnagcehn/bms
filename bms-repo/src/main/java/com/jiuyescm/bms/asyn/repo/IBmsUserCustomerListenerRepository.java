package com.jiuyescm.bms.asyn.repo;


import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.asyn.entity.BmsUserCustomerListenerEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBmsUserCustomerListenerRepository {

	List<BmsUserCustomerListenerEntity> query(Map<String, Object> condition);

    int save(BmsUserCustomerListenerEntity entity);

    int update(BmsUserCustomerListenerEntity entity);

}
