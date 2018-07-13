package com.jiuyescm.bms.base.config.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.base.config.BmsWarehouseConfigEntity;
import com.jiuyescm.bms.base.config.repository.IBmsWarehouseConfigRepository;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bmsWarehouseConfigRepository")
public class BmsWarehouseConfigRepositoryImpl extends MyBatisDao<BmsWarehouseConfigEntity> implements IBmsWarehouseConfigRepository {

	@Override
	public List<BmsWarehouseConfigEntity> queryAll() {
		
		return this.selectList("com.jiuyescm.bms.base.config.mapper.BmsWarehouseConfigMapper.queryAll", null);
	}

	@Override
	public int insertEntity(BmsWarehouseConfigEntity entity) {
		return this.insert("com.jiuyescm.bms.base.config.mapper.BmsWarehouseConfigMapper.insertEntity", entity);
	}

	@Override
	public int updateEntity(BmsWarehouseConfigEntity entity) {
		return this.update("com.jiuyescm.bms.base.config.mapper.BmsWarehouseConfigMapper.updateEntity", entity);
	}

	@Override
	public boolean checkConfigExist(String warehouseCode) {
		Object obj=this.selectOneForObject("com.jiuyescm.bms.base.config.mapper.BmsWarehouseConfigMapper.checkConfigExist", warehouseCode);
		if(obj!=null){
			int k=Integer.valueOf(obj.toString());
			if(k>0){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

}
