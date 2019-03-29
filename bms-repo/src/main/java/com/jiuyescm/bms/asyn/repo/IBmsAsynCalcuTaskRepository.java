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

    BmsAsynCalcuTaskEntity queryOne(String taskId);
    
    List<BmsAsynCalcuTaskEntity> queryUnfinish(Map<String, Object> condition);
    
    List<BmsAsynCalcuTaskEntity> queryByMap(Map<String, Object> condition);
    
    /**
     * 汇总配送费用要发送的MQ
     * @param condition
     * @return
     */
	List<BmsAsynCalcuTaskEntity> queryDisByMap(Map<String, Object> condition);
	
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
	
}
