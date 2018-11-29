package com.jiuyescm.bms.billimport.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.jiuyescm.bms.billimport.entity.BillFeesReceiveStorageTempEntity;
import com.jiuyescm.bms.billimport.repository.IBillFeesReceiveStorageTempRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billFeesReceiveStorageTempRepository")
public class BillFeesReceiveStorageTempRepositoryImpl extends MyBatisDao<BillFeesReceiveStorageTempEntity> implements IBillFeesReceiveStorageTempRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillFeesReceiveStorageTempEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillFeesReceiveStorageTempEntity> list = selectList("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillFeesReceiveStorageTempEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillFeesReceiveStorageTempEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveStorageTempEntity save(BillFeesReceiveStorageTempEntity entity) {
        insert("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillFeesReceiveStorageTempEntity update(BillFeesReceiveStorageTempEntity entity) {
        update("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.delete", id);
    }
    
	@Override
	public int insertBatch(List<BillFeesReceiveStorageTempEntity> list)throws Exception {
		SqlSession session = getSqlSessionTemplate();
		int result = session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.saveBatch", list);
		return result;
	}
	
	@Override
	public int deleteBatch(String billNo){
		int k = delete("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.deleteBatchTemp", billNo);
		return k;
	}

	@Override
	public int saveDataFromTemp(String billNo) {
		// TODO Auto-generated method stub
		 SqlSession session = getSqlSessionTemplate();
		 Map<String,String> map=Maps.newHashMap();
		 map.put("billNo", billNo);
		return session.insert("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.saveDataFromTemp", map);
	
	}

	@Override
	public Double getImportTotalAmount(String billNo) {
		// TODO Auto-generated method stub
		Object object=selectOne("com.jiuyescm.bms.billimport.BillFeesReceiveStorageTempMapper.getImportTotalAmount", billNo);
		Double money=0d;
		if(object!=null){
			money=Double.valueOf(object.toString());
		}
		return money;
	}
	
}
