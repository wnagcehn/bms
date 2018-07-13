package com.jiuyescm.bms.base.ewarehouse.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;

public interface IEwareHouseService {
	
	/**
	 * 查询所有的电仓仓库信息
	 * @param parameter
	 * @param aPageSize
	 * @param aPageNo
	 * @return
	 */
	public PageInfo<PubElecWarehouseEntity> queryAll(Map<String,Object> parameter,int aPageSize,int aPageNo);
	
	/**
	 * 查询所有的电仓仓库信息 去重
	 */
	public List<PubElecWarehouseEntity> queryList(Map<String,Object> parameter);

	
	/**
	 * 创建新的电商仓库
	 * @param aCondition
	 */
	public void createEWarehouse(PubElecWarehouseEntity aCondition);
	
	/**
	 * 修改电商仓库信息
	 * @param aCondtion
	 */
	public void updateEWarehouse(PubElecWarehouseEntity aCondtion);
	/**
	 * 删除电商仓库信息
	 * @param pubElecWarehouseEntity
	 * @return
	 */
	public int removeEWarehouse(String warehouseCode);
	
	
	
}
