package com.jiuyescm.bms.quotation.out.dispatch.repository;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.quotation.dispatch.entity.PubWarehouseEntity;
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;

public interface IPriceOutDispatchDao {
	
	/**
	 * 查询所有的配送报价信息
	 * @param aCondition
	 */
	public PageInfo<PriceOutMainDispatchEntity> queryAll(Map<String,Object> parameter,int aPageSize,int aPageNo);

	
	/**
	 * 根据地址id查找地址
	 * @param addressId
	 * @return
	 */
	/*public PubAddressEntity getAddressById(String addressId);*/
	
	/**
	 * 创建新的配送报价模板
	 * @param aCondition
	 * @return
	 */
	public int createPriceDistribution(PriceOutMainDispatchEntity aCondition);

	/**
	 * 修改新的配送报价模板
	 * @param aCondition
	 */
	public int updatePriceDistribution(PriceOutMainDispatchEntity aCondition);

	/**
	 * 获取所有的仓库信息
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
	 * @param p
	 * @return
	 */
	public int removePriceDistribution(PriceOutMainDispatchEntity p);
	
	/**
	 * 批量插入配送模板信息
	 * @param list
	 * @return
	 */
	public int insertBatchTmp(List<PriceOutMainDispatchEntity> list);
	
	/**
	 * 根据id查询出对应的配送报价信息
	 * @param id
	 * @return
	 */
	public List<PriceOutMainDispatchEntity> getDispatchById(String id);
	
	/**
	 * 通过temid查找对应的id
	 * @param temid
	 * @return
	 */
	public Integer getId(String temid);
	
	 /**
	  * 查找唯一的报价
	  */
	 public PriceOutMainDispatchEntity queryOne(Map<String,Object> condition);
	 
	 /**
	  * 根据map条件删除报价
	  */
	 public int removeDispatchByMap(Map<String,Object> condition);


	public List<PriceOutMainDispatchEntity> queryAllById(
			Map<String, Object> parameter);


	public List<PriceOutMainDispatchEntity> queryAllOutDispatch(
			Map<String, Object> map);
}
