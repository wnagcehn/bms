package com.jiuyescm.bms.biz.discount.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.discount.entity.BmsDiscountAsynTaskEntity;
import com.jiuyescm.bms.biz.discount.repository.IBmsDiscountAsynTaskRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsDiscountAsynTaskRepository")
public class BmsDiscountAsynTaskRepositoryImpl extends MyBatisDao<BmsDiscountAsynTaskEntity> implements IBmsDiscountAsynTaskRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BmsDiscountAsynTaskEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsDiscountAsynTaskEntity> list = selectList("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsDiscountAsynTaskEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsDiscountAsynTaskEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity save(BmsDiscountAsynTaskEntity entity) {
        insert("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsDiscountAsynTaskEntity update(BmsDiscountAsynTaskEntity entity) {
        update("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.delete", id);
    }

	@Override
	public BmsDiscountAsynTaskEntity queryTask(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return (BmsDiscountAsynTaskEntity) selectOne("com.jiuyescm.bms.biz.discount.BmsDiscountAsynTaskMapper.queryTask", condition);
	}
	
}
