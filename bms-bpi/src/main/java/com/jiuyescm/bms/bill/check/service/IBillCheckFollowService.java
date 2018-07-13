package com.jiuyescm.bms.bill.check.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.check.vo.BillCheckFollowVo;
import com.jiuyescm.bms.bill.check.vo.BillCheckInfoFollowVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;

public interface IBillCheckFollowService {

	int addBillCheckFollow(BillCheckFollowVo voEntity) throws Exception;
	int addBillCheckFollow(BillCheckFollowVo voEntity,BillCheckLogVo logVoEntity) throws Exception;
	PageInfo<BillCheckInfoFollowVo> queryAllCheckInfo(Map<String, Object> condition, int pageNo,
            int pageSize) throws Exception;
	int continueDeal(BillCheckFollowVo voEntity, BillCheckLogVo logVo) throws Exception;
	int finishProcess(BillCheckFollowVo voEntity, BillCheckLogVo logVo) throws Exception;
	boolean checkFollowManExist(String followManId);
}
