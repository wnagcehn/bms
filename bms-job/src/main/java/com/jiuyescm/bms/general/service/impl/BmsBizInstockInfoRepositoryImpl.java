package com.jiuyescm.bms.general.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.xxl.job.core.log.XxlJobLogger;
import com.jiuyescm.bms.general.entity.BmsBizInstockInfoEntity;
import com.jiuyescm.bms.general.service.IBmsBizInstockInfoRepository;
import com.jiuyescm.bms.general.entity.FeesReceiveStorageEntity;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizInstockInfoService")
public class BmsBizInstockInfoRepositoryImpl extends MyBatisDao implements IBmsBizInstockInfoRepository {

	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsBizInstockInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsBizInstockInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsBizInstockInfoEntity>(list);
    }
	
	/**
	 * 账单查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<FeesReceiveStorageEntity> queryForBill(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FeesReceiveStorageEntity> list = selectList("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.queryForBill", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<FeesReceiveStorageEntity>(list);
    }
	
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsBizInstockInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.query", condition);
	}
	
    /**
	 * 分组统计
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsBizInstockInfoEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize){
		List<BmsBizInstockInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.groupCount", condition, new RowBounds(
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
        insert("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsBizInstockInfoEntity update(BmsBizInstockInfoEntity entity) {
        update("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BmsBizInstockInfoEntity delete(BmsBizInstockInfoEntity entity) {
    	update("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.update", entity);
    	return entity;
    }
    
	/**
	 * 批量更新
	 * @param entity
	 * @return
	 */
    @Override
    public int updateBatch(List<Map<String, Object>> list) {
       return updateBatch("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.updateBatch", list);
    }
    
    /**
     * 重算
     * @param param
     * @return
     */
	@SuppressWarnings("unchecked")
	@Override
	public int reCalculate(List<BmsBizInstockInfoEntity> list) {
		try{
			updateBatch("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.retryForCalcu", list);
			return 1;
		}
		catch(Exception ex){
			return 0;
		}
	}
	
	@Override
	public List<BmsBizInstockInfoEntity> getInStockInfoList(Map<String, Object> condition) {
		try{
			List<BmsBizInstockInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.querybizInstockInfo", condition);
			return list;
		}
		catch(Exception ex){
			XxlJobLogger.log("入库单主表查询异常"+ex.getMessage());
			return null;
		}
	}
	
	@Override
	public void updateInstockBatchByFees(List<FeesReceiveStorageEntity> entities) {
		this.updateBatch("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.updatebizInstockInfoByFees", entities);
	}
	
	@Override
	public void updatebizInstockInfoById(List<FeesReceiveStorageEntity> entities) {
	    Map<String,Object> map=new HashMap<String, Object>();
	    map.put("list", entities);    
		this.update("com.jiuyescm.bms.general.mapper.BmsBizInstockInfoMapper.updatebizInstockInfoById", map);
	}
	
}
