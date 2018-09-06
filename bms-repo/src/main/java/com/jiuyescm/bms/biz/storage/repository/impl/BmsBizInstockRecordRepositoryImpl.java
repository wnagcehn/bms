package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockRecordEntity;
import com.jiuyescm.bms.biz.storage.repository.IBmsBizInstockRecordRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsBizInstockRecordRepository")
public class BmsBizInstockRecordRepositoryImpl extends MyBatisDao implements IBmsBizInstockRecordRepository {

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsBizInstockRecordEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBizInstockRecordEntity> list = selectList("com.jiuyescm.bms.biz.storage.BmsBizInstockRecordMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsBizInstockRecordEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsBizInstockRecordEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.storage.BmsBizInstockRecordMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity save(BmsBizInstockInfoEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BmsBizInstockRecordMapper.save", entity);
        return entity;
    }
    
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int saveBatch(List<BmsBizInstockInfoEntity> list) {
    	return insertBatch("com.jiuyescm.bms.biz.storage.BmsBizInstockRecordMapper.save", list);
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockRecordEntity update(BmsBizInstockRecordEntity entity) {
        update("com.jiuyescm.bms.biz.storage.BmsBizInstockRecordMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BmsBizInstockRecordMapper.delete", id);
    }
	
}
