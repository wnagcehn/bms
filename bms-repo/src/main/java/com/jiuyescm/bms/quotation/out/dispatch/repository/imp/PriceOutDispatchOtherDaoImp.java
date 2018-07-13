package com.jiuyescm.bms.quotation.out.dispatch.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PubWarehouseEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherDetailEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchOtherDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("outPriceDispatchOtherDao")
@SuppressWarnings("rawtypes")
public class PriceOutDispatchOtherDaoImp extends MyBatisDao implements IPriceOutDispatchOtherDao{

	
	/**
	 * 查询所有的配送报价信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<PriceOutDispatchOtherDetailEntity> queryAll(
			Map<String, Object> parameter,int aPageNo, int aPageSize) {
		// TODO Auto-generated method stub
		List<PriceOutDispatchOtherDetailEntity> list=selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.queryAll", parameter,new RowBounds(aPageNo,aPageSize));
		PageInfo<PriceOutDispatchOtherDetailEntity> pList=new PageInfo<>(list);
		
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
			PriceOutDispatchOtherDetailEntity aCondition) {
		// TODO Auto-generated method stub
		return insert("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.createPriceDistribution", aCondition);
	}

	/**
	 * 修改配送报价模板
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int updatePriceDistribution(
			PriceOutDispatchOtherDetailEntity aCondition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.updatePriceDistribution", aCondition);

	}
	
	/**
	 * 获取所有的电商仓库信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PubWarehouseEntity> getAllPubWareHouse() {
		// TODO Auto-generated method stub
		List<PubWarehouseEntity> list=selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.getAllPubWareHouse", "");
		return list;
	}

	/**
	 * 删除配送报价模板
	 */
	@Override
	public int removePriceDistribution(PriceOutDispatchOtherDetailEntity p) {
		// TODO Auto-generated method stub
		return delete("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.deletePriceDistribution", p);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int insertBatchTmp(List<PriceOutDispatchOtherDetailEntity> list) {
		// TODO Auto-generated method stub
		return insertBatch("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.insertBatchTemplate", list);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PriceOutDispatchOtherDetailEntity> getDispatchById(String temid) {
		// TODO Auto-generated method stub
		List<PriceOutDispatchOtherDetailEntity> l=selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.getDispatchById", temid);
		return l;
	}

	@Override
	public Integer getId(String temid) {
		// TODO Auto-generated method stub
		int id=(Integer)selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.getId", temid);
		
		System.out.println(id);
		return id;
	}

	/**
	 * 根据仓库名获取仓库
	 */
	@Override
	public PubWarehouseEntity getWarehouseByName(String wareHouseName) {
		// TODO Auto-generated method stub
		PubWarehouseEntity p=(PubWarehouseEntity)selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.getWareHouse", wareHouseName);
		return p;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public PriceOutDispatchOtherDetailEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		PriceOutDispatchOtherDetailEntity price=(PriceOutDispatchOtherDetailEntity)selectOne("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.queryOne", condition);
		return price;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int removeDispatchByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return update("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.removeDispatchByMap", condition);
	}

	@Override
	public List<PriceOutDispatchOtherDetailEntity> queryAllOutDispatch(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return selectList("com.jiuyescm.bms.quotation.out.dispatch.mapper.PriceOutDispatchOtherMapper.queryAllOutDispatch", map);
	}
	
}
