package com.jiuyescm.bms.bill.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillCustomerDetailRepository;
import com.jiuyescm.bms.bill.customer.service.IBillCustomerDetailService;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("billCustomerDetailService")
public class BillCustomerDetailServiceImpl implements IBillCustomerDetailService {

	@Autowired
    private IBillCustomerDetailRepository billCustomerDetailRepository;

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
	@Override
    public BillCustomerDetailEntity findById(Long id) {
        return billCustomerDetailRepository.findById(id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
    @Override
    public PageInfo<BillCustomerDetailEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return billCustomerDetailRepository.query(condition, pageNo, pageSize);
    }
    
     /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillCustomerDetailEntity> query(Map<String, Object> condition){
		return billCustomerDetailRepository.query(condition);
	}
	
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillCustomerDetailEntity save(BillCustomerDetailEntity entity) {
        return billCustomerDetailRepository.save(entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillCustomerDetailEntity update(BillCustomerDetailEntity entity) {
        return billCustomerDetailRepository.update(entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        billCustomerDetailRepository.delete(id);
    }
	
}
