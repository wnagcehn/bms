package com.jiuyescm.bms.quotation.storage.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.repository.IPriceMaterialQuotationRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("priceMaterialQuotationRepository")
public class IPriceMaterialQuotationRepositoryImpl extends MyBatisDao<PriceMaterialQuotationEntity> implements
		IPriceMaterialQuotationRepository {

	@Override
	public PageInfo<PriceMaterialQuotationEntity> query(
			Map<String, Object> condition, int pageNo, int pageSize) {
		   List<PriceMaterialQuotationEntity> list = selectList("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.query", condition, new RowBounds(
	                pageNo, pageSize));
	        PageInfo<PriceMaterialQuotationEntity> pageInfo = new PageInfo<PriceMaterialQuotationEntity>(list);
	        return pageInfo;
	}

	@Override
	public int insert(PriceMaterialQuotationEntity record) {
		return insert("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.insert",record);
	}

	@Override
	public int update(PriceMaterialQuotationEntity record) {
		return update("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.update",record);
	}

	@Override
	public int insertBatchTmp(List<PriceMaterialQuotationEntity> list) {
		return insertBatch("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.insert", list);
	}

	@Override
	public List<PriceMaterialQuotationEntity> queryStepMaterial(Map<String, Object> condition) {
		return super.selectList("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.queryMaterialStep", condition);
	}

	@Override
	public PriceMaterialQuotationEntity queryOneMaterial(
			Map<String, Object> condition) {
		return (PriceMaterialQuotationEntity) super.selectOne("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.queryOneMaterial", condition);
	}

	@Override
	public int delete(Long id) {
		return delete("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.delete", id);
	}

	@Override
	public void removeDetail(Map<String, Object> map) {
		SqlSession session = getSqlSessionTemplate();
		session.update("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.removeDetail",map);
	}

	/**
	 * 查询标准报价
	 */
	@Override
	public List<PriceMaterialQuotationEntity> queryStandardMaterial(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.queryStandardMaterial", condition);
	}

	@Override
	public List<PriceMaterialQuotationEntity> queryAllById(
			Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.queryAllById", parameter);
	}
	
	@Override
	public List<PriceMaterialQuotationEntity> queryByTemplateId(
			Map<String, Object> parameter) {
		return selectList("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.queryByTemplateId", parameter);
	}

	@Override
	public List<PriceMaterialQuotationEntity> queryMaterialQuatationByContract(
			Map<String, Object> map) {
		
		return this.selectList("com.jiuyescm.bms.quotation.storage.PriceMaterialQuotationMapper.queryMaterialQuatationByContract", map);
	}

}
