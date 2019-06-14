package com.jiuyescm.bms.bill.customer.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.bill.customer.BillPeriodInfoEntity;
import com.jiuyescm.bms.bill.customer.repository.IBillPeriodInfoRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billPeriodInfoRepository")
public class BillPeriodInfoRepositoryImpl extends MyBatisDao<BillPeriodInfoEntity> implements IBillPeriodInfoRepository {
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillPeriodInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillPeriodInfoEntity> list = selectList("com.jiuyescm.bms.bill.customer.BillPeriodInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillPeriodInfoEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillPeriodInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.bill.customer.BillPeriodInfoMapper.query", condition);
	}
	
    /**
     * 查询
     * @param page
     * @param param
     */
    @Override
    public List<BillPeriodInfoEntity> queryByCustomer(Map<String, Object> condition){
        return selectList("com.jiuyescm.bms.bill.customer.BillPeriodInfoMapper.queryByCustomer", condition);
    }

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillPeriodInfoEntity save(BillPeriodInfoEntity entity) {
        insert("com.jiuyescm.bms.bill.customer.BillPeriodInfoMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillPeriodInfoEntity update(BillPeriodInfoEntity entity) {
        update("com.jiuyescm.bms.bill.customer.BillPeriodInfoMapper.update", entity);
        return entity;
    }
	
}
