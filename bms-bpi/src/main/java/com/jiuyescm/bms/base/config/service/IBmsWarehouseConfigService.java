package com.jiuyescm.bms.base.config.service;

import java.util.List;

import com.jiuyescm.bms.base.config.vo.BmsWarehouseConfigVo;

public interface IBmsWarehouseConfigService {

	List<BmsWarehouseConfigVo> queryAll() throws Exception;
	int saveEntity(BmsWarehouseConfigVo voEntity) throws Exception;
}
