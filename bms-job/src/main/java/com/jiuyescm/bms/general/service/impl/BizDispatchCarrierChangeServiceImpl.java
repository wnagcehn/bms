package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeEntity;
import com.jiuyescm.bms.general.service.IBizDispatchCarrierChangeService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizDispatchCarrierChangeService")
public class BizDispatchCarrierChangeServiceImpl extends MyBatisDao<BizDispatchCarrierChangeEntity> implements IBizDispatchCarrierChangeService {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BizDispatchCarrierChangeEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.findById", id);
    }
    
    /**
     * 根据运单号查询
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public BizDispatchCarrierChangeEntity findByWaybillNo(String waybillNo) {
    	List<BizDispatchCarrierChangeEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.findByWaybillNo", waybillNo);
    	return list.isEmpty() ? null : list.get(0);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizDispatchCarrierChangeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizDispatchCarrierChangeEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizDispatchCarrierChangeEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizDispatchCarrierChangeEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.query", condition);
	}

	/**
	 * 保存并更新状态
	 * @param entity
	 * @return
	 */
    @Override
	@Transactional(propagation = Propagation.REQUIRED)
    public void saveAndUpdateState(BizDispatchCarrierChangeEntity entity) {
        insert("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.save", entity);
    	this.getSqlSessionTemplate().update("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.updateIsCalculated", entity.getWaybillNo());
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchCarrierChangeEntity update(BizDispatchCarrierChangeEntity entity) {
        update("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.update", entity);
        return entity;
    }
    
	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeMapper.delete", id);
    }
	
}
