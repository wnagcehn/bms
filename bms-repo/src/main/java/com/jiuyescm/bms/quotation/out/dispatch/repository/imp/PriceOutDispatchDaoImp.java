package com.jiuyescm.bms.quotation.out.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PubWarehouseEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("outPriceDispatchDao")
@SuppressWarnings("rawtypes")
public class PriceOutDispatchDaoImp extends MyBatisDao implements IPriceOutDispatchDao{

	
	/**
	 * 查询所有的配送报价信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<PriceOutMainDispatchEntity> queryAll(
			Map<String, Object> parameter,int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<PriceOutMainDispatchEntity> list=selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.queryAll", parameter,new RowBounds(aPageNo,aPageSize));
		PageInfo<PriceOutMainDispatchEntity> pList=new PageInfo<>(list);
		
		return pList;
	}

	/*@Override
	public PubAddressEntity getAddressById(String addressId) {
		// TODO Auto-generated method stub
		PubAddressEntity p=(PubAddressEntity) selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryAddress", addressId);
		return p;
	}*/
	
	/**
	 * 创建新的配送报价模板
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int createPriceDistribution(
			PriceOutMainDispatchEntity aCondition) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.createPriceDistribution", aCondition);
	}

	/**
	 * 修改配送报价模板
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int updatePriceDistribution(
			PriceOutMainDispatchEntity aCondition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.updatePriceDistribution", aCondition);

	}
	
	/**
	 * 获取所有的电商仓库信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PubWarehouseEntity> getAllPubWareHouse() {
		// TODO Auto-generated method stub
		List<PubWarehouseEntity> list=selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.OutPriceDispatchMapper.getAllPubWareHouse", "");
		return list;
	}

	/**
	 * 删除配送报价模板
	 */
	@Override
	public int removePriceDistribution(PriceOutMainDispatchEntity p) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.deletePriceDistribution", p);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int insertBatchTmp(List<PriceOutMainDispatchEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.insertBatchTemplate", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PriceOutMainDispatchEntity> getDispatchById(String temid) {
		// TODO Auto-generated method stub
		List<PriceOutMainDispatchEntity> l=selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.getDispatchById", temid);
		return l;
	}

	@Override
	public Integer getId(String temid) {
		// TODO Auto-generated method stub
		int id=(Integer)selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.getId", temid);
		
		System.out.println(id);
		return id;
	}

	/**
	 * 根据仓库名获取仓库
	 */
	@Override
	public PubWarehouseEntity getWarehouseByName(String wareHouseName) {
		// TODO Auto-generated method stub
		PubWarehouseEntity p=(PubWarehouseEntity)selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.getWareHouse", wareHouseName);
		return p;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public PriceOutMainDispatchEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		PriceOutMainDispatchEntity price=(PriceOutMainDispatchEntity)selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.queryOne", condition);
		return price;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int removeDispatchByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.removeDispatchByMap", condition);
	}

	@Override
	public List<PriceOutMainDispatchEntity> queryAllById(
			Map<String, Object> parameter) {
		return this.selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.queryAllById", parameter);
	}

	@Override
	public List<PriceOutMainDispatchEntity> queryAllOutDispatch(
			Map<String, Object> map) {
		return this.selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchMapper.queryAllOutDispatch", map);
	}
	
}
