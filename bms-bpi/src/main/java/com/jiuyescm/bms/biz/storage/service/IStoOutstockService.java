package com.jiuyescm.bms.biz.storage.service;

import java.security.Timestamp;
import java.util.List;

import com.jiuyescm.bms.biz.storage.vo.StoOutstockVo;
import com.jiuyescm.bms.biz.storage.vo.StoFeeOutstockVo;

/**
 * 出库费用服务
 * @author caojianwei
 *
 */
public interface IStoOutstockService {

	/**
	 * 查询未计算的入库数据 最多返回1000行
	 * @param customerId  商家ID
	 * @param subjectCode 科目编码
	 * @param startTime	      开始时间
	 * @param endTime     结束时间
	 * @return
	 */
	List<StoOutstockVo> queryUnExeBiz(String customerId,String subjectCode,Timestamp startTime,Timestamp endTime);
	
	/**
	 * 更新费用
	 * @param vos 费用集合
	 */
	void updateFee(List<StoFeeOutstockVo> vos);
}
