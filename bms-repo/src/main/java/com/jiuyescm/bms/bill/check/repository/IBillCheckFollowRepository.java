package com.jiuyescm.bms.bill.check.repository;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.check.BillCheckFollowEntity;
import com.jiuyescm.bms.bill.check.BillCheckInfoFollowEntity;

public interface IBillCheckFollowRepository {

	BillCheckFollowEntity addBillCheckFollowEntity(BillCheckFollowEntity entity);

	PageInfo<BillCheckInfoFollowEntity> queryList(
			Map<String, Object> condition, int pageNo, int pageSize);

	int updateFollowStatus(BillCheckFollowEntity entity);

	int finishFollow(BillCheckFollowEntity entity);

	boolean checkFollowManExist(String followManId);
}
