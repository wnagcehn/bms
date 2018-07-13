package com.jiuyescm.bms.quotation.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceExtraQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceExtraQuotationRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("priceExtraQuotationRepository")
public class IPriceExtraQuotationRepositoryImpl extends MyBatisDao<PriceExtraQuotationEntity> implements
		IPriceExtraQuotationRepository {

	@Override
	public PageInfo<PriceExtraQuotationEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		   List<PriceExtraQuotationEntity> list = selectList("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.query", condition, new RowBounds(
	                pageNo, pageSize));
	        PageInfo<PriceExtraQuotationEntity> pageInfo = new PageInfo<PriceExtraQuotationEntity>(list);
	        return pageInfo;
	}

	@Override
	public int insert(PriceExtraQuotationEntity record) {
		return insert("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.insert",record);
	}

	@Override
	public int update(PriceExtraQuotationEntity record) {
		return update("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.update",record);
	}

	@Override
	public int insertBatchTmp(List<PriceExtraQuotationEntity> list) {
		
		int i =0;
		i = insertBatch("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.insert",list);
		return i;
	}

	@Override
	public void removeAll(Map<String, Object> map) {
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.removeAll",map);
	}


	@Override
	public List<PriceExtraQuotationEntity> queryPrice(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return super.selectList("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.queryPrice",param);
	}

	@Override
	public List<PriceExtraQuotationEntity> queryPriceByParam(
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		return super.selectList("com.jiuyescm.bms.quotation.storage.PriceExtraQuotationMapper.queryPriceByParam", param);
	}

}
