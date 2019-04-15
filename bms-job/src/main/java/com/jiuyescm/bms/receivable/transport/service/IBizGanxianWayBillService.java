/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.receivable.transport.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.transport.entity.BizGanxianWayBillEntity;

/**
 * 
 * @author wubangjun
 * 
 */
public interface IBizGanxianWayBillService {

	List<BizGanxianWayBillEntity> getGanxianWayBillList(Map<String, Object> condition);
	
	void update(BizGanxianWayBillEntity entity);
	
	void updateBatch(List<BizGanxianWayBillEntity> list);

}
