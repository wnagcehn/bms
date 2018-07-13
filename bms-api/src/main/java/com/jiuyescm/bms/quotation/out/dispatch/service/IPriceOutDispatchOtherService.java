package com.jiuyescm.bms.quotation.out.dispatch.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PubWarehouseEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.PriceOutDispatchOtherDetailEntity;

public interface IPriceOutDispatchOtherService {
	
	/**
	 * 查询所有的配送报价信息
	 * @param aCondition
	 */
	public PageInfo<PriceOutDispatchOtherDetailEntity> queryAll(Map<String,Object> parameter,int aPageSize,int aPageNo);

	/**
	 * 根据地址id查找地址
	 * @param addressId
	 * @return
	 */
	/*public PubAddressEntity getAddressById(String addressId);*/
	
	/**
	 * 创建新的配送报价模板
	 * @param aCondition
	 */
	public void createPriceDistribution(PriceOutDispatchOtherDetailEntity aCondition);
	
	/**
	 * 修改新的配送报价模板
	 * @param aCondition
	 */
	public void updatePriceDistribution(PriceOutDispatchOtherDetailEntity aCondition);

	/**
	 * 获取所有的电商仓库
	 * @return
	 */
	public List<PubWarehouseEntity> getAllPubWareHouse();
	
	
	/**
	 * 根据仓库名称获取仓库
	 * @param wareHouseName
	 * @return
	 */
	public PubWarehouseEntity getWarehouseByName(String wareHouseName);
	
	/**
	 * 删除配送报价模板
	 * @param id
	 */
	public void removePriceDistribution(PriceOutDispatchOtherDetailEntity p);
	
	  /**
	   * 批量插入模板信息
	   * @param list
	   * @return
	   */
	 public int insertBatchTmp(List<PriceOutDispatchOtherDetailEntity> list);
	 
	 /**
	  * 通过id查找出所有的配送报价信息
	  * @param id
	  * @return
	  */
	 public List<PriceOutDispatchOtherDetailEntity> getDispatchById(String id);
	 
	 /**
	  * 通过temid查找对应的id
	  * @param TempId
	  * @return
	  */
	 public Integer getId(String TempId);
	 
	 /**
	  * 查找唯一的报价
	  */
	 public PriceOutDispatchOtherDetailEntity queryOne(Map<String,Object> condition);

	 /**
	  * 根据map条件删除报价
	  */
	 public int removeDispatchByMap(Map<String,Object> condition);

}
