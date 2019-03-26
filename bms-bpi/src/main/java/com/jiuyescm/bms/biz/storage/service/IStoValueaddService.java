package com.jiuyescm.bms.biz.storage.service;

import java.sql.Timestamp;
import java.util.List;

import com.jiuyescm.bms.biz.storage.vo.StoValueaddVo;
import com.jiuyescm.bms.biz.storage.vo.StoFeeValueaddVo;

/**
 * 增值服务
 * @author caojianwei
 *
 */
public interface IStoValueaddService {

	/**
	 * 查询未计算的入库数据 最多返回1000行
	 * @param customerId  商家ID
	 * @param subjectCode 科目编码
	 * @param startTime	      开始时间
	 * @param endTime     结束时间
	 * @return
	 */
	List<StoValueaddVo> queryUnExeBiz(String customerId,String subjectCode,Timestamp startTime,Timestamp endTime);
	
	/**
	 * 更新费用
	 * @param vos 费用集合
	 */
	void updateFee(List<StoFeeValueaddVo> vos);
}
