package com.jiuyescm.bms.quotation.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("iPriceDispatchDao")
@SuppressWarnings("rawtypes")
public class PriceDispatchDaoImp extends MyBatisDao implements IPriceDispatchDao{

	
	/**
	 * 查询所有的配送报价信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<PriceMainDispatchEntity> queryAll(
			Map<String, Object> parameter,int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<PriceMainDispatchEntity> list=selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryAll", parameter,new RowBounds(aPageNo,aPageSize));
		PageInfo<PriceMainDispatchEntity> pList=new PageInfo<>(list);
		
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
			PriceMainDispatchEntity aCondition) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.createPriceDistribution", aCondition);
	}

	/**
	 * 修改配送报价模板
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int updatePriceDistribution(
			PriceMainDispatchEntity aCondition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.updatePriceDistribution", aCondition);

	}
	
	/**
	 * 获取所有的电商仓库信息
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public List<PubWarehouseEntity> getAllPubWareHouse() {
		// TODO Auto-generated method stub
		List<PubWarehouseEntity> list=selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.getAllPubWareHouse", "");
		return list;
	}*/

	/**
	 * 删除配送报价模板
	 */
	@Override
	public int removePriceDistribution(PriceMainDispatchEntity p) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.deletePriceDistribution", p);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int insertBatchTmp(List<PriceMainDispatchEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.insertBatchTemplate", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PriceMainDispatchEntity> getDispatchById(String temid) {
		// TODO Auto-generated method stub
		List<PriceMainDispatchEntity> l=selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.getDispatchById", temid);
		return l;
	}

	@Override
	public Integer getId(String temid) {
		// TODO Auto-generated method stub
		int id=(Integer)selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.getId", temid);
		
		System.out.println(id);
		return id;
	}

	@Override
	@SuppressWarnings("unchecked")
	public PriceMainDispatchEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		PriceMainDispatchEntity price=(PriceMainDispatchEntity)selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryOne", condition);
		return price;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int removeDispatchByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.removeDispatchByMap", condition);
	}

	@Override
	public List<PriceMainDispatchEntity> queryAllById(
			Map<String, Object> parameter) {
		
		return this.selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryAllById", parameter);
	}

	/**
	 * 根据商家ID查询商家报价
	 */
	@Override
	public List<PriceMainDispatchEntity> queryDispatchQuos(Map<String, Object> parameter) {
		return this.selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryDispatchQuos", parameter);
	}
	
	/**
	 * 查询九曳标准报价
	 */
	@Override
	public List<PriceMainDispatchEntity> queryStandardDispatchQuos(Map<String, Object> parameter) {
		return this.selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryDispatchQuos", parameter);
	}

	@Override
	public List<PriceMainDispatchEntity> queryAllByTemplateId(Map<String,Object> map) {
		return this.selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryAllByTemplateId", map);
	}

	@Override
	public List<PriceMainDispatchEntity> queryShunfengDispatch(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryShunfengDispatch", map);
	}

	@Override
	public List<String> queryJiuYeArea(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.selectList("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.queryJiuYeArea", map);
	}


	/**
	 * 根据仓库名获取仓库
	 */
/*	@Override
	public PubWarehouseEntity getWarehouseByName(String wareHouseName) {
		// TODO Auto-generated method stub
		PubWarehouseEntity p=(PubWarehouseEntity)selectOne("com.jiuyescm.bms.quotation.dispatch.mapper.PriceDispatchMapper.getWareHouse", wareHouseName);
		return p;
	}*/
	
	
}
