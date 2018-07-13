package com.jiuyescm.bms.receivable.storage.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.jiuyescm.bms.biz.storage.entity.BizOutstockPackmaterialEntity;
import com.jiuyescm.bms.receivable.storage.service.IBizWmsOutstockPackmaterialService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Repository("bizWmsOutstockPackmaterialService")
public class BizWmsOutstockPackmaterialServiceImpl extends MyBatisDao<BizOutstockPackmaterialEntity> implements IBizWmsOutstockPackmaterialService{

	@Override
	public List<BizOutstockPackmaterialEntity> query(Map<String, Object> map) {
		return this.selectList("com.jiuyescm.bms.receivable.storage.BizWmsOutstockPackmaterialMapper.query", map);
	}

	@Override
	public int updateBatch(List<BizOutstockPackmaterialEntity> billList) {
		return this.updateBatch("com.jiuyescm.bms.receivable.storage.BizWmsOutstockPackmaterialMapper.update", billList);
	}

}
