package com.jiuyescm.bms.biz.storage.service;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.biz.storage.vo.StoInstockVo;

/**
 * 仓储出库服务
 * @author caojianwei
 *
 */
public interface IStoInstockService {

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
	 * 查询未计算的入库数据 最多返回1000行  用于费用计算
	 * @param customerId  商家ID
	 * @param subjectCode 科目编码
	 * @param startTime	      开始时间
	 * @param endTime     结束时间
	 * @return
	 */
	List<StoInstockVo> queryUnExeBiz(Map<String, Object> map);
	
	/**
	 * 更新费用
	 * @param vos 费用集合
	 */
	void updateFee(List<StoInstockVo> vos);
	
}
