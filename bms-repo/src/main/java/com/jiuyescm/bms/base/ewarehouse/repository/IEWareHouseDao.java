package com.jiuyescm.bms.base.ewarehouse.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.ewarehouse.entity.NewPlayForm;
import com.jiuyescm.bms.base.ewarehouse.entity.PubAddressEntity1;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;

public interface IEWareHouseDao {

	/**
	 * 查询所有的电商平台信息
	 * @return
	 */
	public List<NewPlayForm> queryAllPlayform();
	
	/**
	 * 查询所有的仓库信息
	 * @param acondition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageInfo<PubElecWarehouseEntity> queryAll(Map<String,Object> acondition,int pageNo,int pageSize);
	
	public List<PubElecWarehouseEntity> queryList(Map<String,Object> acondition);
	
	/**
	 * 查找所有省份
	 * @param pubAddressEntity
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<PubAddressEntity1> queryAllProvince(Map<String,Object> aCondition);
	
	/**
	 * 通过省份code查看所有的市
	 * @param aCondition
	 * @return
	 */
	public List<PubAddressEntity1> getCitiesByProvincecode(Map<String,Object> aCondition);


	/**
	 * 通过市code查看所有的县
	 * @param aCondition
	 * @return
	 */
	public List<PubAddressEntity1> getCountysByCitycode(Map<String,Object> aCondition);

	/**
	 * 删除电商仓库信息
	 * @param pubElecWarehouseEntity
	 * @return
	 */
	public int removeEWareHouse(String warehouseCode);
	
	/**
	 * 创建新的电商仓库
	 * @param aCondition
	 */
	public int createEWareHouse(PubElecWarehouseEntity aCondition);
	
	/**
	 * 修改电商仓库信息
	 * @param aCondition
	 * @return
	 */
	public int updateEWareHouse(PubElecWarehouseEntity aCondition);
}
