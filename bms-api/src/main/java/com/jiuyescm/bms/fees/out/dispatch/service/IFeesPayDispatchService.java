package com.jiuyescm.bms.fees.out.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.fees.out.dispatch.entity.FeesPayDispatchEntity;


/**
 * 应收费用-配送费 接口
 * 
 * @author yangshuaishuai
 *
 */
public interface IFeesPayDispatchService {
	
	PageInfo<FeesPayDispatchEntity> query(Map<String, Object> condition, int pageNo, int pageSize);
	
	List<FeesPayDispatchEntity> queryList(Map<String, Object> condition);
	
	List<FeesPayDispatchEntity> queryDispatchByBillNo(String billno);
	
	/**
	 * 根据条件查询单个费用
	 */
	FeesPayDispatchEntity queryOne(Map<String, Object> condition);
	
	/**
	 * 判断单个写入配送费用表
	 * @param entity
	 */
	public boolean Insert(FeesPayDispatchEntity entity);
	
	int insertBatchTmp(List<FeesPayDispatchEntity> list);
	
	/**
	 * 根据条件删除费用
	 * @param condition
	 * @return
	 */
	int deleteFeesByMap(Map<String, Object> condition);
	
	//删除账单-理赔
	int deleteFeesBillAbnormal(Map<String, Object> parameter);
		
	/**
	 * 单条写入配送费用表
	 * @param entity
	 */
	public boolean InsertOne(FeesPayDispatchEntity entity);
}
