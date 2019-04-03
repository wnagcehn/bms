package com.jiuyescm.bms.asyn.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.asyn.entity.BmsUserCustomerListenerEntity;
import com.jiuyescm.bms.asyn.repo.IBmsUserCustomerListenerRepository;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bmsUserCustomerListenerService")
public class BmsUserCustomerListenerServiceImpl implements IBmsUserCustomerListenerService {

	@Autowired
    private IBmsUserCustomerListenerRepository bmsUserCustomerListenerRepository;

    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsUserCustomerListenerEntity> query(Map<String, Object> condition){
		return bmsUserCustomerListenerRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int save(BmsUserCustomerListenerEntity entity) {
        return bmsUserCustomerListenerRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public int update(BmsUserCustomerListenerEntity entity) {
        return bmsUserCustomerListenerRepository.update(entity);
    }

}
