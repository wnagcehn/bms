package com.jiuyescm.bms.bill.customer.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.customer.BillCustomerDetailEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillCustomerDetailRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billCustomerDetailRepository")
public class BillCustomerDetailRepositoryImpl extends MyBatisDao<BillCustomerDetailEntity> implements IBillCustomerDetailRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillCustomerDetailEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.bill.customer.BillCustomerDetailMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillCustomerDetailEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillCustomerDetailEntity> list = selectList("com.jiuyescm.bms.bill.customer.BillCustomerDetailMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillCustomerDetailEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillCustomerDetailEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.bill.customer.BillCustomerDetailMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillCustomerDetailEntity save(BillCustomerDetailEntity entity) {
        insert("com.jiuyescm.bms.bill.customer.BillCustomerDetailMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillCustomerDetailEntity update(BillCustomerDetailEntity entity) {
        update("com.jiuyescm.bms.bill.customer.BillCustomerDetailMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.bill.customer.BillCustomerDetailMapper.delete", id);
    }
	
}
