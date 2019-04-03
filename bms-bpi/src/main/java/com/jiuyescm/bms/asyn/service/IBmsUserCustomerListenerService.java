package com.jiuyescm.bms.asyn.service;


import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsUserCustomerListenerEntity;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBmsUserCustomerListenerService {

	List<BmsUserCustomerListenerEntity> query(Map<String, Object> condition);

    int save(BmsUserCustomerListenerEntity entity);

    int update(BmsUserCustomerListenerEntity entity);

}
