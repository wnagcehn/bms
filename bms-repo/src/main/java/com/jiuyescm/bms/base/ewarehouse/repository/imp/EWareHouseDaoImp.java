package com.jiuyescm.bms.base.ewarehouse.repository.imp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.ewarehouse.entity.NewPlayForm;
import com.jiuyescm.bms.base.ewarehouse.entity.PubAddressEntity1;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.repository.IEWareHouseDao;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("eWareHouseDao")
@SuppressWarnings("rawtypes")
public class EWareHouseDaoImp extends MyBatisDao implements IEWareHouseDao{

	/**
	 * 查询所有的电商仓库信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public PageInfo<PubElecWarehouseEntity> queryAll(
			Map<String, Object> acondition, int pageNo, int pageSize) {
		List<PubElecWarehouseEntity> list=selectList("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.queryAll", acondition,new RowBounds(pageNo,pageSize));
		PageInfo<PubElecWarehouseEntity> pList=new PageInfo<PubElecWarehouseEntity>(list);
		
		return pList;
	}
	
	/**
	 * 查询所有的电商仓库信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PubElecWarehouseEntity> queryList(Map<String, Object> condition) {
		return selectList("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.queryAll", condition);
	}

	/**
	 * 查询所有的省份资料
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PubAddressEntity1> queryAllProvince(
			Map<String, Object> aCondition) {
		List<PubAddressEntity1> list = selectList("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.getAllProvinces", aCondition);
		return list;
	}

	/**
	 * 通过省份code查看所有的省份资料
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PubAddressEntity1> getCitiesByProvincecode(
			Map<String, Object> aCondition) {
		List<PubAddressEntity1> list = selectList("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.getCitiesByProvincecode", aCondition);

		return list;
	}

	/**
	 * 通过市code获取县
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PubAddressEntity1> getCountysByCitycode(
			Map<String, Object> aCondition) {
		List<PubAddressEntity1> list = selectList("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.getCountysByCitycode", aCondition);

		return list;
	}

	/**
	 * 删除电商仓库信息
	 */
	@Override
	public int removeEWareHouse(String warehouseCode) {
		return delete("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.removeEWareHouse", warehouseCode);
	}

	/**
	 * 创建新的电商仓库
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int createEWareHouse(PubElecWarehouseEntity aCondition) {
		return insert("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.createEWareHouse", aCondition);
	}

	/**
	 * 修改电商仓库信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int updateEWareHouse(PubElecWarehouseEntity aCondition) {
		return update("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.updateEWareHouse", aCondition);
	}

	/**
	 * 查询所有的电商平台信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<NewPlayForm> queryAllPlayform() {
		return selectList("com.jiuyescm.bms.base.ewarehouse.mapper.EWareHouseMapper.queryAllPlayform", "");
	}
	

}
