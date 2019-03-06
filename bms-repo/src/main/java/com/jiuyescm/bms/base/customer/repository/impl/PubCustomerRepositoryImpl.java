package com.jiuyescm.bms.base.customer.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerRepository;

/**
 * ..RepositoryImpl
 * @author liuzhicheng
 * 
 */
@Repository("pubCustomerRepository")
public class PubCustomerRepositoryImpl extends MyBatisDao<PubCustomerEntity> implements IPubCustomerRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public PubCustomerEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.base.customer.PubCustomerMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<PubCustomerEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubCustomerEntity> list = selectList("com.jiuyescm.bms.base.customer.PubCustomerMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubCustomerEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubCustomerEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.base.customer.PubCustomerMapper.query", condition);
	}

	@Override
	public PageInfo<PubCustomerEntity> queryPage(Map<String, Object> condition,
			int pageNo, int pageSize) {
        List<PubCustomerEntity> list = selectList("com.jiuyescm.bms.base.customer.PubCustomerMapper.queryPage", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubCustomerEntity>(list);
	}

}
