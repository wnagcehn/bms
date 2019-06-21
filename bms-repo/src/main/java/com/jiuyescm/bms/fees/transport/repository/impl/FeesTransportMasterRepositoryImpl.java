package com.jiuyescm.bms.fees.transport.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.fees.transport.entity.FeesTransportMasterEntity;
import com.jiuyescm.bms.fees.transport.repository.IFeesTransportMasterRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("feesTransportMasterRepository")
public class FeesTransportMasterRepositoryImpl extends MyBatisDao<FeesTransportMasterEntity> implements IFeesTransportMasterRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public FeesTransportMasterEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.fees.transport.FeesTransportMasterMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<FeesTransportMasterEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<FeesTransportMasterEntity> list = selectList("com.jiuyescm.bms.fees.transport.FeesTransportMasterMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<FeesTransportMasterEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<FeesTransportMasterEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.fees.transport.FeesTransportMasterMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public FeesTransportMasterEntity save(FeesTransportMasterEntity entity) {
        insert("com.jiuyescm.bms.fees.transport.FeesTransportMasterMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public FeesTransportMasterEntity update(FeesTransportMasterEntity entity) {
        update("com.jiuyescm.bms.fees.transport.FeesTransportMasterMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.fees.transport.FeesTransportMasterMapper.delete", id);
    }
	
}
