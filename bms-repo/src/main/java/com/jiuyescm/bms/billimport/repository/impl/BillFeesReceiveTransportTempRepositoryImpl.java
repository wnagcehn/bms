package com.jiuyescm.bms.billimport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveTransportTempRepository;

/**
 * ..RepositoryImpl
 * @author liuzhicheng
 * 
 */
@Repository("billFeesReceiveTransportTempRepository")
public class BillFeesReceiveTransportTempRepositoryImpl extends MyBatisDao<BillFeesReceiveTransportTempEntity> implements IBillFeesReceiveTransportTempRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillFeesReceiveTransportTempEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillFeesReceiveTransportTempEntity> list = selectList("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillFeesReceiveTransportTempEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillFeesReceiveTransportTempEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveTransportTempEntity save(BillFeesReceiveTransportTempEntity entity) {
        insert("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveTransportTempEntity update(BillFeesReceiveTransportTempEntity entity) {
        update("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempMapper.delete", id);
    }
    
    /**
     * 批量写入
     */
	@Override
	public int insertBatch(List<BillFeesReceiveTransportTempEntity> list){
	SqlSession session = getSqlSessionTemplate();
	int result = session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempEntityMapper.saveBatch", list);
	return result;
	}

	/**
	 * 批量 删除
	 */
	@Override
	public int deleteBatch(Map<String, Object> condition) {
		int d = delete("com.jiuyescm.bms.billimport.BillFeesReceiveTransportTempEntityMapper.deleteBatch", condition);
		return d;
	}
	
}
