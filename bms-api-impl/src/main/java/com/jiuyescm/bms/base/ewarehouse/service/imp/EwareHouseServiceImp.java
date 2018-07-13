package com.jiuyescm.bms.base.ewarehouse.service.imp;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.repository.IEWareHouseDao;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;

@Service("ewareHouseService")
public class EwareHouseServiceImp implements IEwareHouseService{

	
	@Resource
	private IEWareHouseDao iEWareHouseDao;
	
	/**
	 * 查询所有的电仓仓库信息
	 */
	@Override
	public PageInfo<PubElecWarehouseEntity> queryAll(Map<String, Object> parameter, int aPageSize, int aPageNo) {
		return iEWareHouseDao.queryAll(parameter, aPageNo, aPageSize);
	}
	
	/**
	 * 查询所有的电仓仓库信息
	 */
	@Override
	public List<PubElecWarehouseEntity> queryList(Map<String, Object> parameter) {
		return iEWareHouseDao.queryList(parameter);
	}
	
	

	/**
	 * 删除电商仓库信息
	 */
	@Override
	public int removeEWarehouse(String warehouseCode) {
		// TODO Auto-generated method stub
		return iEWareHouseDao.removeEWareHouse(warehouseCode);
	}

	/**
	 * 创建新的电商仓库
	 */
	@Override
	public void createEWarehouse(PubElecWarehouseEntity aCondition) {
		// TODO Auto-generated method stub
		iEWareHouseDao.createEWareHouse(aCondition);
	}

	@Override
	public void updateEWarehouse(PubElecWarehouseEntity aCondtion) {
		// TODO Auto-generated method stub
		iEWareHouseDao.updateEWareHouse(aCondtion);
	}
	
}
