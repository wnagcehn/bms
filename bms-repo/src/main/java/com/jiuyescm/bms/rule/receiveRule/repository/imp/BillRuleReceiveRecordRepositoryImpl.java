package com.jiuyescm.bms.rule.receiveRule.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveRecordEntity;
import com.jiuyescm.bms.rule.receiveRule.repository.IBillRuleReceiveRecordRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

/**
 * ..RepositoryImpl
 * @author wangchen
 * 
 */
@Repository("billRuleReceiveRecordRepository")
public class BillRuleReceiveRecordRepositoryImpl extends MyBatisDao<BillRuleReceiveRecordEntity> implements IBillRuleReceiveRecordRepository {

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 * @throws Exception
	*/
    @Override
    public BillRuleReceiveRecordEntity findById(Long id) {
        return selectOne("com.jiuyescm.bms.rule.receiveRule.mapper.BillRuleReceiveRecordMapper.findById", id);
    }
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 */
	@Override
    public PageInfo<BillRuleReceiveRecordEntity> query(Map<String, Object> condition, int pageNo, int pageSize) {
        List<BillRuleReceiveRecordEntity> list = selectList("com.jiuyescm.bms.rule.receiveRule.mapper.BillRuleReceiveRecordMapper.query", condition, new RowBounds(
                pageNo, pageSize));
        return new PageInfo<BillRuleReceiveRecordEntity>(list);
    }
    
    /**
	 * 查询
	 * @param page
	 * @param param
	 */
	@Override
    public List<BillRuleReceiveRecordEntity> query(Map<String, Object> condition){
		return selectList("com.jiuyescm.bms.rule.receiveRule.mapper.BillRuleReceiveRecordMapper.query", condition);
	}

	/**
	 * 保存
	 * @param entity
	 * @return
	 */
    @Override
    public BillRuleReceiveRecordEntity save(BillRuleReceiveRecordEntity entity) {
        insert("com.jiuyescm.bms.rule.receiveRule.mapper.BillRuleReceiveRecordMapper.save", entity);
        return entity;
    }

	/**
	 * 更新
	 * @param entity
	 * @return
	 */
    @Override
    public BillRuleReceiveRecordEntity update(BillRuleReceiveRecordEntity entity) {
        update("com.jiuyescm.bms.rule.receiveRule.mapper.BillRuleReceiveRecordMapper.update", entity);
        return entity;
    }

	/**
	 * 删除
	 * @param entity
	 */
    @Override
    public void delete(Long id) {
        delete("com.jiuyescm.bms.rule.receiveRule.mapper.BillRuleReceiveRecordMapper.delete", id);
    }
	
}
