/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.payable.transport.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.transport.entity.BizGanxianRoadBillEntity;


/**
 * 干线路单
 * @author wubangjun
 */
public interface IBizGanxianRoadBillService {

	List<BizGanxianRoadBillEntity> getGanxianRoadBillList(Map<String, Object> condition);
	
	void update(BizGanxianRoadBillEntity entity);
	
	void updateBatch(List<BizGanxianRoadBillEntity> list);

}
