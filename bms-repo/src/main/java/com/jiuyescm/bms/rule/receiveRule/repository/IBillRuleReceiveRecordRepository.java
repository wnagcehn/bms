package com.jiuyescm.bms.rule.receiveRule.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.chargerule.receiverule.entity.BillRuleReceiveRecordEntity;

/**
 * ..Repository
 * @author wangchen
 * 
 */
public interface IBillRuleReceiveRecordRepository {

    BillRuleReceiveRecordEntity findById(Long id);
	
	PageInfo<BillRuleReceiveRecordEntity> query(Map<String, Object> condition,
		int pageNo, int pageSize);

	List<BillRuleReceiveRecordEntity> query(Map<String, Object> condition);

    BillRuleReceiveRecordEntity save(BillRuleReceiveRecordEntity entity);

    BillRuleReceiveRecordEntity update(BillRuleReceiveRecordEntity entity);

    void delete(Long id);

}
