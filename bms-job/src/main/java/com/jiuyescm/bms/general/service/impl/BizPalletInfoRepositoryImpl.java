package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.general.service.IBizPalletInfoRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizPalletInfoService")
public class BizPalletInfoRepositoryImpl extends MyBatisDao implements IBizPalletInfoRepository {
	
	private static final Logger logger = Logger.getLogger(BizPalletInfoRepositoryImpl.class.getName());
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizPalletInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizPalletInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizPalletInfoEntity>(list);
    }
	
	/**
	 * 业务数据查询
	 */
	@Override
	public List<BizPalletInfoEntity> querybizPallet(Map<String, Object> condition) {
		List<BizPalletInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.querybizPallet", condition);
		return list;
	}
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizPalletInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity save(BizPalletInfoEntity entity) {
        insert("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.save", entity);
        return entity;
    }
    

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoEntity update(BizPalletInfoEntity entity) {
        update("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public int delete(List<BizPalletInfoEntity> lists) {
    	return updateBatch("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.update", lists);
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
			updateBatch("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.retryForCalcu", list);
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
       return updateBatch("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.updateBatch", list);
    }
	
    /**
	 * 分组统计
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizPalletInfoEntity> groupCount(Map<String, Object> condition, int pageNo, int pageSize){
		List<BizPalletInfoEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.groupCount", condition, new RowBounds(
                pageNo, pageSize));
		return new PageInfo<BizPalletInfoEntity>(list);
	}
	
	@Override
	public void updatebizPallet(List<BizPalletInfoEntity> list) {
		try{
			this.updateBatch("com.jiuyescm.bms.general.mapper.BizPalletInfoMapper.updatebizPallet", list);
		}
		catch(Exception ex){
			logger.error("【处置费任务】批量更新主表异常"+ex.getMessage());
		}
	}
    
}
