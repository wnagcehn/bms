package com.jiuyescm.bms.bill.receive.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterRecordEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRecordRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billReceiveMasterRecordRepository")
public class BillReceiveMasterRecordRepositoryImpl extends MyBatisDao<BillReceiveMasterRecordEntity> implements IBillReceiveMasterRecordRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillReceiveMasterRecordEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.bill.receive.BillReceiveMasterRecordMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillReceiveMasterRecordEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillReceiveMasterRecordEntity> list = selectList("com.jiuyescm.bms.bill.receive.BillReceiveMasterRecordMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillReceiveMasterRecordEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillReceiveMasterRecordEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.bill.receive.BillReceiveMasterRecordMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int save(BillReceiveMasterRecordEntity entity) {
        int k = insert("com.jiuyescm.bms.bill.receive.BillReceiveMasterRecordMapper.save", entity);
        return k;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillReceiveMasterRecordEntity update(BillReceiveMasterRecordEntity entity) {
        update("com.jiuyescm.bms.bill.receive.BillReceiveMasterRecordMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.bill.receive.BillReceiveMasterRecordMapper.delete", id);
    }
	
}
