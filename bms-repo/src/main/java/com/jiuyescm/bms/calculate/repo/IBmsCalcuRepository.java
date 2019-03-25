package com.jiuyescm.bms.calculate.repo;

import java.util.List;

import com.jiuyescm.bms.calculate.BmsFeesQtyEntity;


public interface IBmsCalcuRepository {

	/**
	 * 
	 * @param customerId 商家ID
	 * @param subjectCode 科目编码
	 * @param startTime  业务数据开始时间
	 * @param endTime	   业务数据结束时间
	 * @return
	 */
	BmsFeesQtyEntity queryTotalFeesQtyForSto(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 查看商家 各个计算状态的费用单量
	 * @param customerId 商家ID
	 * @param subjectCode 科目编码
	 * @param startTime  业务数据开始时间
	 * @param endTime	   业务数据结束时间
	 * @return
	 */
	List<BmsFeesQtyEntity> queryStatusFeesQtyForSto(String customerId,String subjectCode, String startTime,String endTime);
	
	//List<BmsFeesQtyEntity> queryStatusFeesQtyForSto(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 查询商家配送费用单量
	 * @param customerId 商家ID
	 * @param subjectCode 科目编码
	 * @param startTime  业务数据开始时间
	 * @param endTime	   业务数据结束时间
	 * @return
	 */
	BmsFeesQtyEntity queryTotalFeesQtyForDis(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 查看商家 各个计算状态的费用单量
	 * @param customerId 商家ID
	 * @param subjectCode 科目编码
	 * @param startTime  业务数据开始时间
	 * @param endTime	   业务数据结束时间
	 * @return
	 */
	List<BmsFeesQtyEntity> queryStatusFeesQtyForDis(String customerId,String subjectCode, String startTime,String endTime);
	
	
}
