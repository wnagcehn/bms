package com.jiuyescm.bms.biz.pallet.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoEntity;
import com.jiuyescm.bms.biz.pallet.entity.BizPalletInfoTempEntity;
import com.jiuyescm.bms.biz.pallet.repository.IBizPalletInfoTempRepository;
import com.jiuyescm.bms.biz.storage.entity.BizProductPalletStorageTempEntity;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bizPalletInfoTempRepository")
public class BizPalletInfoTempRepositoryImpl extends MyBatisDao<BizPalletInfoTempEntity> implements IBizPalletInfoTempRepository {

	private static final Logger logger = Logger.getLogger(BizPalletInfoTempRepositoryImpl.class.getName());
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BizPalletInfoTempEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.findById", id);
    }
    
    /**
     * 校验唯一性
     * @param taskId
     * @return
     */
	@Override
	public List<BizPalletInfoTempEntity> queryInBiz(String taskId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", taskId);
		return this.selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.queryInBiz", map);
		
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizPalletInfoTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizPalletInfoTempEntity> list = selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizPalletInfoTempEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizPalletInfoTempEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoTempEntity save(BizPalletInfoTempEntity entity) {
        insert("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.save", entity);
        return entity;
    }
    
    /**
     * 批量保存
     * @param list
     */
	@Override
	public void saveBatch(List<BizPalletInfoTempEntity> list) {
		SqlSession session = getSqlSessionTemplate();
		int ret = session.insert("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.save", list);
		logger.info("保存行数【"+ret+"】");
	}

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizPalletInfoTempEntity update(BizPalletInfoTempEntity entity) {
        update("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.delete", id);
    }
    
    /**
     * 从临时表保存到业务表
     * @param taskId
     * @return
     */
	@Override
	public int saveTempData(String taskId) {
		// TODO Auto-generated method stub
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,String> map=Maps.newHashMap();
		 map.put("taskId", taskId);
		 return session.insert("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.saveDataFromTemp", map);
	}
	
	/**
	 * 批量删除
	 * @param taskId
	 * @return
	 */
	@Override
	public int deleteBybatchNum(String taskId) {
		// TODO Auto-generated method stub
		Map<String,String> map=Maps.newHashMap();
		map.put("taskId", taskId);
		int k=this.delete("com.jiuyescm.bms.biz.pallet.BizPalletInfoTempMapper.deleteBybatchNum", map);
		logger.info("删除托数临时表 行数【"+k+"】,批次号【"+taskId+"】");
		return k;
	}
	
}
