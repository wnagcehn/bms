package com.jiuyescm.bms.base.unitInfo.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.unitInfo.entity.BmsChargeUnitInfoEntity;
import com.jiuyescm.bms.base.unitInfo.repository.IBmsChargeUnitInfoRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("bmsChargeUnitInfoRepository")
public class BmsChargeUnitInfoRepositoryImpl extends MyBatisDao<BmsChargeUnitInfoEntity> implements IBmsChargeUnitInfoRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BmsChargeUnitInfoEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.base.unitInfo.BmsChargeUnitInfoMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BmsChargeUnitInfoEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BmsChargeUnitInfoEntity> list = selectList("com.jiuyescm.bms.base.unitInfo.BmsChargeUnitInfoMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BmsChargeUnitInfoEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BmsChargeUnitInfoEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.base.unitInfo.BmsChargeUnitInfoMapper.queryById", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BmsChargeUnitInfoEntity save(BmsChargeUnitInfoEntity entity) {
        insert("com.jiuyescm.bms.base.unitInfo.BmsChargeUnitInfoMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BmsChargeUnitInfoEntity update(BmsChargeUnitInfoEntity entity) {
        update("com.jiuyescm.bms.base.unitInfo.BmsChargeUnitInfoMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public BmsChargeUnitInfoEntity delete(BmsChargeUnitInfoEntity entity) {
    	update("com.jiuyescm.bms.base.unitInfo.BmsChargeUnitInfoMapper.update", entity);
    	return entity;
    }

}
