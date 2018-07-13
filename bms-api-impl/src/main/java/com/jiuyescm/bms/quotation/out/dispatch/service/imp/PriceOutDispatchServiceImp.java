package com.jiuyescm.bms.quotation.out.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PubWarehouseEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchDao;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchService;

@Service("outPriceDispatchService")
public class PriceOutDispatchServiceImp implements IPriceOutDispatchService{
    
	@Resource
	private IPriceOutDispatchDao priceOutDispatchDao;
	
	
	@Override
	public PageInfo<PriceOutMainDispatchEntity> queryAll(
			Map<String, Object> parameter, int aPageSize, int aPageNo) {
		return priceOutDispatchDao.queryAll(parameter, aPageSize, aPageNo);
	}


	/*@Override
	public PubAddressEntity getAddressById(String addressId) {
		return priceDispatchDao.getAddressById(addressId);
	}
*/

	@Override
	public void createPriceDistribution(
			PriceOutMainDispatchEntity aCondition) {
		priceOutDispatchDao.createPriceDistribution(aCondition);
		
	}


	@Override
	public void updatePriceDistribution(
			PriceOutMainDispatchEntity aCondition) {
		priceOutDispatchDao.updatePriceDistribution(aCondition);
	}


	@Override
	public List<PubWarehouseEntity> getAllPubWareHouse() {
		return priceOutDispatchDao.getAllPubWareHouse();
	}


	@Override
	public void removePriceDistribution(PriceOutMainDispatchEntity p) {
		priceOutDispatchDao.removePriceDistribution(p);
	}


	@Override
	public int insertBatchTmp(List<PriceOutMainDispatchEntity> list) {
		return priceOutDispatchDao.insertBatchTmp(list);
	}


	@Override
	public List<PriceOutMainDispatchEntity> getDispatchById(String id) {
		return priceOutDispatchDao.getDispatchById(id);
	}


	@Override
	public Integer getId(String tempId) {
		return priceOutDispatchDao.getId(tempId);
	}


	@Override
	public PubWarehouseEntity getWarehouseByName(String wareHouseName) {
		return priceOutDispatchDao.getWarehouseByName(wareHouseName);
	}
	
	@Override
	public PriceOutMainDispatchEntity queryOne(Map<String, Object> condition) {
		return priceOutDispatchDao.queryOne(condition);
	}


	@Override
	public int removeDispatchByMap(Map<String, Object> condition) {
		return priceOutDispatchDao.removeDispatchByMap(condition);
	}


	@Override
	public List<PriceOutMainDispatchEntity> queryAllById(
			Map<String, Object> parameter) {
		return priceOutDispatchDao.queryAllById(parameter);
	}
}
