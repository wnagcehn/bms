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
public class BmsBizInstockInfoRepositoryImpl extends MyBatisDao<BmsBizInstockInfoEntity> implements IBmsBizInstockInfoRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BmsBizInstockInfoEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.biz.storage.BmsBizInstockInfoMapper.findById", id);
    }
	
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
	
}
