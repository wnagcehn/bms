package com.jiuyescm.bms.bill.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillPeriodInfoEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillPeriodInfoRepository;
import com.jiuyescm.bms.bill.customer.service.IBillPeriodInfoService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billPeriodInfoService")
public class BillPeriodInfoServiceImpl implements IBillPeriodInfoService {

	@Autowired
    private IBillPeriodInfoRepository billPeriodInfoRepository;

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillPeriodInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billPeriodInfoRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillPeriodInfoEntity> query(Map<String, Object> condition){
		return billPeriodInfoRepository.query(condition);
	}
	
	@Override
	public List<BillPeriodInfoEntity> queryByCustomer(Map<String, Object> condition){
	    return billPeriodInfoRepository.queryByCustomer(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillPeriodInfoEntity save(BillPeriodInfoEntity entity) {
        return billPeriodInfoRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillPeriodInfoEntity update(BillPeriodInfoEntity entity) {
        return billPeriodInfoRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(BillPeriodInfoEntity entity) {
        billPeriodInfoRepository.update(entity);
    }
	
}
