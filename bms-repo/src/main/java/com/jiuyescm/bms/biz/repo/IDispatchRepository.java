package com.jiuyescm.bms.biz.repo;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.biz.vo.BmsDispatchVo;

public interface IDispatchRepository {

	/**
	 * 查询配送运单业务数据
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsDispatchVo> querybizData(Map<String, Object> condition, int pageNo,int pageSize);
	
	/**
	 * 查询配送运单相关的所有数据，包括业务数据和费用数据
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsDispatchVo> queryAll(Map<String, Object> condition, int pageNo,int pageSize);
	
	/**
	 * 查询原始数据
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsDispatchVo> queryBizOrigin(Map<String, Object> condition, int pageNo,int pageSize);
	
	/**
	 * 查询业务数据修改记录
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsDispatchVo> queryBizModifyRecord(Map<String, Object> condition, int pageNo,int pageSize);
	
	/**
	 * 更新配送运单业务数据
	 * @param vo
	 * @return
	 */
	int updateBizData(List<BmsDispatchVo> vos);
	
	/**
	 * 配送费用重算
	 * @param condition
	 * @return
	 */
	int reCalcu(Map<String, Object> condition);
	
	/**
	 * 根据条件查询单个业务数据
	 * @param condition
	 * @return
	 */
	BmsDispatchVo queryOne(Map<String, Object> condition);
	
}
