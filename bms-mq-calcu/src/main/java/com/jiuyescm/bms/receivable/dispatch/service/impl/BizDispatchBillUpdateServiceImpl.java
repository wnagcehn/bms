package com.jiuyescm.bms.receivable.dispatch.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jiuyescm.bms.biz.dispatch.entity.BizDispatchBillUpdateEntity;
import com.jiuyescm.bms.receivable.dispatch.service.IBizDispatchBillUpdateService;
import com.jiuyescm.cfm.persistence.mybatis.MyBatisDao;

@Service("bizDispatchBillUpdateService")
public class BizDispatchBillUpdateServiceImpl  extends MyBatisDao<BizDispatchBillUpdateEntity> implements IBizDispatchBillUpdateService{

	@Override
	public List<BizDispatchBillUpdateEntity> queryData(Map<String, Object> map) {
		return this.selectList("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillUpdateMapper.query", map);
	}

	@Override
	public int updateToIsCalculated(BizDispatchBillUpdateEntity entity) {

		int ret = this.update("com.jiuyescm.bms.receivable.dispatch.BizDispatchBillUpdateMapper.updateToIsCalculated", entity);
		return ret;
	}

}
