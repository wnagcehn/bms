/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

import com.jiuyescm.bms.general.entity.BizAddFeeEntity;
import com.jiuyescm.bms.general.service.IBizAddFeeService;

/**
 * 
 * @author stevenl
 * 
 */
@Repository("bizAddFeeServiceImpl")
public class BizAddFeeServiceImpl extends MyBatisDao<BizAddFeeEntity> implements IBizAddFeeService {

	private static final Logger logger = Logger.getLogger(BizAddFeeServiceImpl.class.getName());

	public BizAddFeeServiceImpl(){
		super();
	}
	
	@Override
    public PageInfo<BizAddFeeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizAddFeeEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        PageInfo<BizAddFeeEntity> pageInfo = new PageInfo<BizAddFeeEntity>(list);
        return pageInfo;
    }

    @Override
    public BizAddFeeEntity findById(Long id) {
        BizAddFeeEntity entity = selectOne("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.findById", id);
        return entity;
    }

    @Override
    public BizAddFeeEntity save(BizAddFeeEntity entity) {
        insert("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.save", entity);
        return entity;
    }

    @Override
    public BizAddFeeEntity update(BizAddFeeEntity entity) {
        update("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.update", entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.delete", id);
    }

	@Override
	public int saveList(List<BizAddFeeEntity> addList) {
		return insertBatch("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.save",addList);
	}

	@Override
	public int updateList(List<BizAddFeeEntity> updateList) {
		try {
			return this.updateBatch("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.update",updateList);
		} catch (Exception ex) {
			logger.error("批量更新主表异常"+ex.getMessage());
		}	
		return 0;
	}
	
	@Override
	public int updateByFees(List<BizAddFeeEntity> updateList) {
		try {
			return this.updateBatch("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.updateByFees",updateList);
		} catch (Exception ex) {
			logger.error("批量更新主表异常"+ex.getMessage());
		}	
		return 0;
	}

	@Override
	public BizAddFeeEntity selectOne(Map<String, Object> param) {
		return (BizAddFeeEntity)selectOne("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.selectOne",param);
	}

	@Override
	public List<BizAddFeeEntity> queryList(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.queryRecount",condition);
	}

	@Override
	public BizAddFeeEntity queryWms(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (BizAddFeeEntity) selectOne("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.queryWms", param);
	}
	
	@Override
	public List<BizAddFeeEntity> querybizAddFee(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.general.mapper.BizAddFeeEntityMapper.querybizAddFee",condition);
	}
	
}
