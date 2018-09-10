package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockInfoRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsBizInstockInfoRepository")
public class BmsBizInstockInfoRepositoryImpl extends MyBatisDao implements IBmsBizInstockInfoRepository {

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsBizInstockInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBizInstockInfoEntity> list = selectList("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsBizInstockInfoEntity>(list);
    }
	
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsBizInstockInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.query", condition);
	}
	
    /**
	 * 分组统计
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsBizInstockInfoEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize){
		List<BmsBizInstockInfoEntity> list = selectList("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.groupCount", condition, new RowBounds(
                pageNo, pageSize));
		return new PageInfo<BmsBizInstockInfoEntity>(list);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity update(BmsBizInstockInfoEntity entity) {
        update("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity) {
    	update("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.update", entity);
    	return entity;
    }
    
	/**
	 * 批量更新
	 * @param entity
	 * @return
	 */
    @Override
    public int updateBatch(List<Map<String, Object>> list) {
       return updateBatch("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.updateBatch", list);
    }
    
    /**
     * 重算
     * @param param
     * @return
     */
	@Override
	public int reCalculate(List<BmsBizInstockInfoEntity> list) {
		try{
			updateBatch("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.retryForCalcu", list);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}
	
}
