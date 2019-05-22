package com.jiuyescm.bms.base.dict.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dict.entity.PubPackageDictEntity;
import com.jiuyescm.bms.base.dict.repository.IPubPackageDictRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;


/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("pubPackageDictRepository")
public class PubPackageDictRepositoryImpl extends MyBatisDao<PubPackageDictEntity> implements IPubPackageDictRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public PubPackageDictEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.base.dict.PubPackageDictMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<PubPackageDictEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<PubPackageDictEntity> list = selectList("com.jiuyescm.bms.base.dict.PubPackageDictMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<PubPackageDictEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<PubPackageDictEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.base.dict.PubPackageDictMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public PubPackageDictEntity save(PubPackageDictEntity entity) {
        insert("com.jiuyescm.bms.base.dict.PubPackageDictMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public PubPackageDictEntity update(PubPackageDictEntity entity) {
        update("com.jiuyescm.bms.base.dict.PubPackageDictMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.base.dict.PubPackageDictMapper.delete", id);
    }
}
