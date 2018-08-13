package com.jiuyescm.bms.base.servicetype.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.base.servicetype.entity.PubCarrierServicetypeEntity;
import com.jiuyescm.bms.base.servicetype.repository.IPubCarrierServicetypeRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("pubCarrierServicetypeRepository")
public class PubCarrierServicetypeRepositoryImpl extends MyBatisDao<PubCarrierServicetypeEntity> implements IPubCarrierServicetypeRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public PubCarrierServicetypeEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.base.servicetype.PubCarrierServicetypeMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<PubCarrierServicetypeEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubCarrierServicetypeEntity> list = selectList("com.jiuyescm.bms.base.servicetype.PubCarrierServicetypeMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubCarrierServicetypeEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubCarrierServicetypeEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.base.servicetype.PubCarrierServicetypeMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public PubCarrierServicetypeEntity save(PubCarrierServicetypeEntity entity) {
        insert("com.jiuyescm.bms.base.servicetype.PubCarrierServicetypeMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public PubCarrierServicetypeEntity update(PubCarrierServicetypeEntity entity) {
        update("com.jiuyescm.bms.base.servicetype.PubCarrierServicetypeMapper.update", entity);
        return entity;
    }
	
}
