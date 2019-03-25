package com.jiuyescm.bms.biz.storage.service;

import java.security.Timestamp;
import java.util.List;

import com.jiuyescm.bms.biz.storage.vo.StoBizInstockVo;
import com.jiuyescm.bms.biz.storage.vo.StoFeeInstockVo;

/**
 * 仓储出库服务
 * @author caojianwei
 *
 */
public interface StoInstockService {

	/**
	 * 入库单业务数据查询
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 *//*
	PageInfo<StoBizInstockVo> queryBiz(Map<String, Object> condition,int pageNo, int pageSize) throws Exception;*/
	
	/**
	 * 查询未计算的入库数据 最多返回1000行
	 * @param customerId  商家ID
	 * @param subjectCode 科目编码
	 * @param startTime	      开始时间
	 * @param endTime     结束时间
	 * @return
	 */
	List<StoBizInstockVo> queryUnExeBiz(String customerId,String subjectCode,Timestamp startTime,Timestamp endTime);
	
	/**
	 * 更新费用
	 * @param vos 费用集合
	 */
	void updateFee(List<StoFeeInstockVo> vos);
	
}
