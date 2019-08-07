package com.jiuyescm.bms.asyn.repo;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.asyn.entity.BmsAsynCalcuTaskEntity;

/**
 * 
 * @author cjw
 * 
 */
public interface IBmsAsynCalcuTaskRepository {

	public PageInfo<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition,int pageNo, int pageSize);

	public List<BmsAsynCalcuTaskEntity> query(Map<String, Object> condition);
	
    public BmsAsynCalcuTaskEntity save(BmsAsynCalcuTaskEntity entity);

    public BmsAsynCalcuTaskEntity update(BmsAsynCalcuTaskEntity entity);
    
    public int updateBatch(List<BmsAsynCalcuTaskEntity> list);

    BmsAsynCalcuTaskEntity queryOne(String taskId);
    
    List<BmsAsynCalcuTaskEntity> queryUnfinish(Map<String, Object> condition);
    
    List<BmsAsynCalcuTaskEntity> queryByMap(Map<String, Object> condition);
    
    /**
     * 汇总配送费用要发送的MQ
     * @param condition
     * @return
     */
	List<BmsAsynCalcuTaskEntity> queryDisByMap(Map<String, Object> condition);

	BmsAsynCalcuTaskEntity saveLog(BmsAsynCalcuTaskEntity entity);

	
	/**
	 * 查询界面（主）
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BmsAsynCalcuTaskEntity> queryMain(Map<String, Object> condition, int pageNo, int pageSize);
	
	/**
	 * 根据商家和月份查出状态和科目数量（主）
	 * @param map
	 * @return
	 */
	List<BmsAsynCalcuTaskEntity> queryInfoByCustomerId(Map<String, Object> map);
	
	/**
	 * 明细查询（子）
	 * @param map
	 * @return
	 */
	List<BmsAsynCalcuTaskEntity> queryDetail(Map<String, Object> map);

	BmsAsynCalcuTaskEntity updateByTaskId(BmsAsynCalcuTaskEntity entity);

	/**
	 * 
	 * <功能描述>查找符合条件根据customerId汇总的数据
	 * 
	 * @author zhangyuanzheng
	 * @date 2019年8月5日 上午11:41:07
	 *
	 * @param condition
	 * @return
	 */
	List<BmsAsynCalcuTaskEntity> queryGroupCustomer(Map<String, Object> condition, int pageNo, int pageSize);

	/**
	 * 
	 * <功能描述>查询界面（主）
	 * 
	 * @author zhangyuanzheng
	 * @date 2019年8月6日 上午9:56:10
	 *
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<BmsAsynCalcuTaskEntity> queryMainSe(Map<String, Object> condition, int pageNo, int pageSize);

	/**
	 * 
	 * <功能描述>查询界面（主） 条数
	 * 
	 * @author zhangyuanzheng
	 * @date 2019年8月6日 上午10:16:29
	 *
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	long queryMainSeCount(Map<String, Object> condition, int pageNo,
			int pageSize);

	List<BmsAsynCalcuTaskEntity> queryInfoByCustomerIdSe(Map<String, Object> map);

	
}
