package com.jiuyescm.bms.biz.storage.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialCancelEntity;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialCancelRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizOutstockPackmaterialCancelRepository")
public class BizOutstockPackmaterialCancelRepositoryImpl extends MyBatisDao<BizOutstockPackmaterialCancelEntity> implements IBizOutstockPackmaterialCancelRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BizOutstockPackmaterialCancelEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizOutstockPackmaterialCancelEntity> list = selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizOutstockPackmaterialCancelEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizOutstockPackmaterialCancelEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizOutstockPackmaterialCancelEntity save(BizOutstockPackmaterialCancelEntity entity) {
        insert("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizOutstockPackmaterialCancelEntity update(BizOutstockPackmaterialCancelEntity entity) {
        update("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.delete", id);
    }
	
    /**
     * 库里有就更新，没有就新增
     */
    @Override
    public int saveOrUpdate(List<BizOutstockPackmaterialCancelEntity> cancelList) {
        SqlSession session = this.getSqlSessionTemplate();
        return session.insert("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.saveOrUpdate", cancelList);
    }
    
    /**
     * 查询需要作废的
     */
    @Override
    public List<BizOutstockPackmaterialCancelEntity> queryNeedCancel(){
        return selectList("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.queryNeedCancel", null);
    }

    /**
     * 更新状态
     * @param entity
     * @return
     */
    @Override
    public int updateBatchStatus(List<BizOutstockPackmaterialCancelEntity> list) {
        return updateBatch("com.jiuyescm.bms.biz.storage.BizOutstockPackmaterialCancelMapper.updateBatch", list);
    }
    
}
