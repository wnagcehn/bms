package com.jiuyescm.bms.quotation.out.dispatch.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PubWarehouseEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherDetailEntity;
import com.jiuyescm.bms.quotation.out.dispatch.repository.IPriceOutDispatchOtherDao;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchOtherService;

@Service("outPriceDispatchOtherService")
public class PriceOutDispatchOtherServiceImp implements IPriceOutDispatchOtherService{
    
	@Resource
	private IPriceOutDispatchOtherDao priceOutDispatchDao;
	
	
	@Override
	public PageInfo<PriceOutDispatchOtherDetailEntity> queryAll(
			Map<String, Object> parameter, int aPageSize, int aPageNo) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.queryAll(parameter, aPageSize, aPageNo);
	}


	/*@Override
	public PubAddressEntity getAddressById(String addressId) {
		// TODO Auto-generated method stub
		return priceDispatchDao.getAddressById(addressId);
	}
*/

	@Override
	public void createPriceDistribution(
			PriceOutDispatchOtherDetailEntity aCondition) {
		priceOutDispatchDao.createPriceDistribution(aCondition);
		
	}


	@Override
	public void updatePriceDistribution(
			PriceOutDispatchOtherDetailEntity aCondition) {
		// TODO Auto-generated method stub
		priceOutDispatchDao.updatePriceDistribution(aCondition);
	}


	@Override
	public List<PubWarehouseEntity> getAllPubWareHouse() {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.getAllPubWareHouse();
	}


	@Override
	public void removePriceDistribution(PriceOutDispatchOtherDetailEntity p) {
		// TODO Auto-generated method stub
		priceOutDispatchDao.removePriceDistribution(p);
	}


	@Override
	public int insertBatchTmp(List<PriceOutDispatchOtherDetailEntity> list) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.insertBatchTmp(list);
	}


	@Override
	public List<PriceOutDispatchOtherDetailEntity> getDispatchById(String id) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.getDispatchById(id);
	}


	@Override
	public Integer getId(String tempId) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.getId(tempId);
	}


	@Override
	public PubWarehouseEntity getWarehouseByName(String wareHouseName) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.getWarehouseByName(wareHouseName);
	}
	
	@Override
	public PriceOutDispatchOtherDetailEntity queryOne(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.queryOne(condition);
	}


	@Override
	public int removeDispatchByMap(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return priceOutDispatchDao.removeDispatchByMap(condition);
	}
}
