package com.jiuyescm.bms.bill.receive.repository.impl;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;
import com.jiuyescm.bms.bill.receive.entity.BillReceiveMasterEntity;
import com.jiuyescm.bms.bill.receive.repository.IBillReceiveMasterRepository;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billReceiveMasterRepository")
public class BillReceiveMasterRepositoryImpl extends MyBatisDao<BillReceiveMasterEntity> implements IBillReceiveMasterRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillReceiveMasterEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillReceiveMasterEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillReceiveMasterEntity> list = selectList("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillReceiveMasterEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillReceiveMasterEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public int save(BillReceiveMasterEntity entity) {
        int k = insert("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.save", entity);
        return k;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public int update(BillReceiveMasterEntity entity) {
        int k = update("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.update", entity);
        return k;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(String billNo) {
        delete("com.jiuyescm.bms.bill.receive.BillReceiveMasterMapper.delete", billNo);
    }
	
}
