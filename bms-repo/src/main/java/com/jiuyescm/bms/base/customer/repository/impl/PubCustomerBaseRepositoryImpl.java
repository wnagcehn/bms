package com.jiuyescm.bms.base.customer.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.customer.entity.PubCustomerBaseEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerBaseRepository;

/**
 * ..RepositoryImpl
 * @author liuzhicheng
 * 
 */
@Repository("pubCustomerBaseRepository")
public class PubCustomerBaseRepositoryImpl extends MyBatisDao<PubCustomerBaseEntity> implements IPubCustomerBaseRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public PubCustomerBaseEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.base.customer.PubCustomerBaseMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<PubCustomerBaseEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubCustomerBaseEntity> list = selectList("com.jiuyescm.bms.base.customer.PubCustomerBaseMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubCustomerBaseEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubCustomerBaseEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.base.customer.PubCustomerBaseMapper.query", condition);
	}
	
	
	@Override
	public List<PubCustomerBaseEntity> queryByMkInvoiceName(Map<String, Object> condition){
	    return selectList("com.jiuyescm.bms.base.customer.PubCustomerBaseMapper.queryByMkInvoiceName", condition);
	}
}
