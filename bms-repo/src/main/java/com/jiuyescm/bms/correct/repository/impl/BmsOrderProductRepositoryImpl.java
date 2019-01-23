package com.jiuyescm.bms.correct.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.correct.BmsOrderProductEntity;
import com.jiuyescm.bms.correct.repository.IBmsOrderProductRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsOrderProductRepository")
public class BmsOrderProductRepositoryImpl extends MyBatisDao<BmsOrderProductEntity> implements IBmsOrderProductRepository {

	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsOrderProductEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsOrderProductEntity> list = selectList("com.jiuyescm.bms.correct.BmsOrderProductMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsOrderProductEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsOrderProductEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.correct.BmsOrderProductMapper.query", condition);
	}

}
