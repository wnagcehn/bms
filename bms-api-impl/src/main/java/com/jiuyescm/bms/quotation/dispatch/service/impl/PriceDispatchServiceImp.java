package com.jiuyescm.bms.quotation.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchEntity;
import com.jiuyescm.bms.quotation.dispatch.repository.IPriceDispatchDao;
import com.jiuyescm.bms.quotation.dispatch.service.IPriceDispatchService;

@Service("priceDispatchService")
public class PriceDispatchServiceImp implements IPriceDispatchService{
    
	@Resource
	private IPriceDispatchDao priceDispatchDao;
	
	
	@Override
	public PageInfo<PriceMainDispatchEntity> queryAll(
			Map<String, Object> parameter, int aPageSize, int aPageNo) {
		// TODO Auto-generated method stub
		return priceDispatchDao.queryAll(parameter, aPageSize, aPageNo);
	}


	/*@Override
	public PubAddressEntity getAddressById(String addressId) {
		// TODO Auto-generated method stub
		return priceDispatchDao.getAddressById(addressId);
	}
*/

	@Override
	public void createPriceDistribution(
			PriceMainDispatchEntity aCondition) {
		priceDispatchDao.createPriceDistribution(aCondition);
		
	}


	@Override
	public void updatePriceDistribution(
			PriceMainDispatchEntity aCondition) {
		// TODO Auto-generated method stub
		priceDispatchDao.updatePriceDistribution(aCondition);
	}


	/*@Override
	public List<PubWarehouseEntity> getAllPubWareHouse() {
		// TODO Auto-generated method stub
		return priceDispatchDao.getAllPubWareHouse();
	}
*/

	@Override
	public void removePriceDistribution(PriceMainDispatchEntity p) {
		// TODO Auto-generated method stub
		priceDispatchDao.removePriceDistribution(p);
	}


	@Override
	public int insertBatchTmp(List<PriceMainDispatchEntity> list) {
		// TODO Auto-generated method stub
		return priceDispatchDao.insertBatchTmp(list);
	}


	@Override
	public List<PriceMainDispatchEntity> getDispatchById(String id) {
		// TODO Auto-generated method stub
		return priceDispatchDao.getDispatchById(id);
	}


	@Override
	public Integer getId(String tempId) {
		// TODO Auto-generated method stub
		return priceDispatchDao.getId(tempId);
	}


	@Override
	public PriceMainDispatchEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceDispatchDao.queryOne(condition);
	}


	@Override
	public int removeDispatchByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceDispatchDao.removeDispatchByMap(condition);
	}


	@Override
	public List<PriceMainDispatchEntity> queryAllById(
			Map<String, Object> parameter) {
		return priceDispatchDao.queryAllById(parameter);
	}


	/*@Override
	public PubWarehouseEntity getWarehouseByName(String wareHouseName) {
		// TODO Auto-generated method stub
		return priceDispatchDao.getWarehouseByName(wareHouseName);
	}*/
}
