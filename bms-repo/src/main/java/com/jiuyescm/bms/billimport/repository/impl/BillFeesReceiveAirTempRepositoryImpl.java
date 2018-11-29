package com.jiuyescm.bms.billimport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.zookeeper.server.SessionTracker.Session;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveAirTempEntity;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveTransportTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveAirTempRepository;

/**
 * ..RepositoryImpl
 * @author liuzhicheng
 * 
 */
@Repository("billFeesReceiveAirTempRepository")
public class BillFeesReceiveAirTempRepositoryImpl extends MyBatisDao<BillFeesReceiveAirTempEntity> implements IBillFeesReceiveAirTempRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillFeesReceiveAirTempEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillFeesReceiveAirTempEntity> list = selectList("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillFeesReceiveAirTempEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillFeesReceiveAirTempEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveAirTempEntity save(BillFeesReceiveAirTempEntity entity) {
        insert("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveAirTempEntity update(BillFeesReceiveAirTempEntity entity) {
        update("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.delete", id);
    }
    
    /**
     * 批量写入
     */
	@Override
	public int insertBatch(List<BillFeesReceiveAirTempEntity> list){
	SqlSession session = getSqlSessionTemplate();
	int result = session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.saveBatch", list);
	return result;
	}

	/**
	 * 批量 删除
	 */
	@Override
	public int deleteBatch(String billNo) {
		int d = delete("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.deleteBatch", billNo);
		return d;
	}

	@Override
	public int saveDataFromTemp(String billNo) {
		// TODO Auto-generated method stub
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,String> map=Maps.newHashMap();
		 map.put("billNo", billNo);
		return session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveAirTempMapper.saveDataFromTemp", map);
	}
	
}
