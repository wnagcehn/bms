package com.jiuyescm.bms.base.config.repository;

import java.util.List;

import com.jiuyescm.bms.base.config.BmsWarehouseConfigEntity;

public interface IBmsWarehouseConfigRepository {
	
	List<BmsWarehouseConfigEntity> queryAll();
	int insertEntity(BmsWarehouseConfigEntity entity);
	int updateEntity(BmsWarehouseConfigEntity entity);
	boolean checkConfigExist(String warehouseCode);
}
