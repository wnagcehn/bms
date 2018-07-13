package com.jiuyescm.bms.quotation.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceStepQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceStepQuotationRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("priceStepQuotationRepository")
public class PriceStepQuotationRepositoryImpl extends MyBatisDao<PriceStepQuotationEntity> implements
		IPriceStepQuotationRepository {

	@Override
	public PageInfo<PriceStepQuotationEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		List<PriceStepQuotationEntity> list = selectList("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.query", 
				 condition, new RowBounds(pageNo, pageSize));
		 PageInfo<PriceStepQuotationEntity> pageInfo = new PageInfo<PriceStepQuotationEntity>(list);
	     return pageInfo;
	}

	@Override
	public int delete(Long id) {
		return 0;
	}

	@Override
	public int insert(PriceStepQuotationEntity record) {
		return  insert("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.save", record);
	}

	@Override
	public int update(PriceStepQuotationEntity record) {
		return update("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.update", record);
	}

	@Override
	public int insertBatchTmp(List<PriceStepQuotationEntity> list) {
		return insertBatch("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.save", list);
	}

	@Override
	public List<PriceStepQuotationEntity> queryPriceStep(Map<String, Object> param) {
		return super.selectList("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.queryPriceStep",param);
	}

	@Override
	public void removeAll(Map<String, Object> map) {
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.removeAll",map);
	}
	
	@Override
	public List<PriceStepQuotationEntity> queryPriceStandardStep(Map<String, Object> param) {
		return selectList("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.queryPriceStandardStep", param);
	}

	@Override
	public List<PriceStepQuotationEntity> queryPriceStepByQuatationId(Map<String,Object> map) {
		
		return this.selectList("com.jiuyescm.bms.quotation.storage.PriceStepQuotationMapper.queryPriceStepByQuatationId", map);
	}
}
