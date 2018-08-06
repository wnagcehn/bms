package com.jiuyescm.bms.asyn.repo.impl;


import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.asyn.entity.AsynCalcuEntity;
import com.jiuyescm.bms.asyn.repo.IAsynCalcuRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("asynCalcuRepository")
public class AsynCalcuRepositoryImpl extends MyBatisDao<AsynCalcuEntity> implements IAsynCalcuRepository {
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<AsynCalcuEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<AsynCalcuEntity> list = selectList("com.jiuyescm.bms.asyn.AsynCalcuMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<AsynCalcuEntity>(list);
    }

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int save(AsynCalcuEntity entity) {
    	return this.insert("com.jiuyescm.bms.asyn.AsynCalcuMapper.save", entity);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public int update(AsynCalcuEntity entity) {
    	return this.update("com.jiuyescm.bms.asyn.AsynCalcuMapper.update", entity);
    }

	/**
	 * 删除
	 * @param entity
	 */
	@Override
	public int delete(AsynCalcuEntity entity) {
		return this.update("com.jiuyescm.bms.asyn.AsynCalcuMapper.update.delete", entity);
	}


	
}
