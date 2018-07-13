package com.jiuyescm.bms.paymanage.payimport.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.paymanage.payimport.FeesPayImportEntity;

public interface IFeesPayImportService {
	/**
	 * 查询所有的信息
	 * @param aCondition
	 */
	public PageInfo<FeesPayImportEntity> queryAll(Map<String,Object> parameter,int aPageSize,int aPageNo);
	
	/**
	 * 保存成本数据
	 */
	public int saveList(List<FeesPayImportEntity> list);
	
	/**
	 * 批量更新数据
	 */
	public int updateList(List<FeesPayImportEntity> list);
	
	/**
	 * 更新数据
	 */
	public int updateOne(FeesPayImportEntity fee);
	
	/**
	 * 根据条件查询list
	 */
	public List<FeesPayImportEntity> queryList(Map<String, Object> condition);
	
	/**
	 * 根据条件查询one
	 */
	public FeesPayImportEntity queryOne(Map<String, Object> condition);
	
	/**
	 * 插入或者更新
	 */
	public int insertOrUpdate(Map<String, Object> param);
	
	/**
	 * 批量删除报价
	 */
	public int remove(List<FeesPayImportEntity> list);
}
