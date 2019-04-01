package com.jiuyescm.bms.general.service.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.general.entity.BizDispatchCarrierChangeDetailEntity;
import com.jiuyescm.bms.general.service.IBizDispatchCarrierChangeDetailService;
/**
 * ..ServiceImpl
 * @author wangchen
 * 
 */
@Service("bizDispatchCarrierChangeDetailService")
public class BizDispatchCarrierChangeDetailServiceImpl extends MyBatisDao<BizDispatchCarrierChangeDetailEntity> implements IBizDispatchCarrierChangeDetailService {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BizDispatchCarrierChangeDetailEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.findById", id);
    }
    
    /**
     * 查询状态为0的全部数据
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public List<BizDispatchCarrierChangeDetailEntity> queryAll() {
    	return selectList("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.queryAll", null);
    }
    
    /**
     * 根据运单号查询
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public BizDispatchCarrierChangeDetailEntity queryByWayBillNo(String waybillNo) {
    	return this.selectOne("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.queryByWayBillNo", waybillNo);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BizDispatchCarrierChangeDetailEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BizDispatchCarrierChangeDetailEntity> list = selectList("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BizDispatchCarrierChangeDetailEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BizDispatchCarrierChangeDetailEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchCarrierChangeDetailEntity save(BizDispatchCarrierChangeDetailEntity entity) {
        insert("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BizDispatchCarrierChangeDetailEntity update(BizDispatchCarrierChangeDetailEntity entity) {
        update("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.general.mapper.BizDispatchCarrierChangeDetailMapper.delete", id);
    }
	
}
