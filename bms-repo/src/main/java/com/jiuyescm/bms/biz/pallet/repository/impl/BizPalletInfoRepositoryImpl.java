package com.jiuyescm.bms.biz.pallet.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoRepository;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;
import com.jiuyescm.bms.biz.storage.entity.BmsBizInstockInfoEntity;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizPalletInfoRepository")
public class BizPalletInfoRepositoryImpl extends MyBatisDao implements IBizPalletInfoRepository {

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizPalletInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizPalletInfoEntity> list = selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizPalletInfoEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizPalletInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity save(BizPalletInfoEntity entity) {
        insert("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.save", entity);
        return entity;
    }
    

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity update(BizPalletInfoEntity entity) {
        update("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public int delete(List<BizPalletInfoEntity> lists) {
    	return updateBatch("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.update", lists);
    }
    
    /**
     * 重算
     * @param param
     * @return
     */
	@SuppressWarnings("unchecked")
	@Override
	public int reCalculate(List<BizPalletInfoEntity> list) {
		try{
			updateBatch("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.retryForCalcu", list);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}
	
	/**
	 * 批量更新
	 * @param entity
	 * @return
	 */
    @Override
    public int updateBatch(List<Map<String, Object>> list) {
       return updateBatch("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.updateBatch", list);
    }
    
	/**
	 * 点击删除时批量更新导入托数
	 * @param entity
	 * @return
	 */
    @Override
    public int updatePalletNumBatch(List<BizPalletInfoEntity> list) {
       return updateBatch("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.updatePalletNumBatch", list);
    }
	
    /**
	 * 分组统计
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizPalletInfoEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize){
		List<BizPalletInfoEntity> list = selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoMapper.groupCount", condition, new RowBounds(
                pageNo, pageSize));
		return new PageInfo<BizPalletInfoEntity>(list);
	}
	
    
}
