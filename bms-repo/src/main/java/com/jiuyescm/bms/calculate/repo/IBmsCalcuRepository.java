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
	
	/**
	 * 入库
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	List<BmsFeesQtyEntity> queryFeesQtyForStoInstock(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 出库
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	List<BmsFeesQtyEntity> queryFeesQtyForStoOutstock(String customerId,String subjectCode, String startTime,String endTime);

	/**
	 * 耗材
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	List<BmsFeesQtyEntity> queryFeesQtyForStoMaterial(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 商品按件
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	List<BmsFeesQtyEntity> queryFeesQtyForStoProductItem(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 增值
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	List<BmsFeesQtyEntity> queryFeesQtyForStoAdd(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
	 * 托数
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	List<BmsFeesQtyEntity> queryFeesQtyForStoPallet(String customerId,String subjectCode, String startTime,String endTime);
	
	/**
     * 标准耗材使用费
     * @param customerId
     * @param subjectCode
     * @param creMonth
     * @return
     */
    List<BmsFeesQtyEntity> queryFeesQtyForStoStandMaterial(String customerId,String subjectCode, String startTime,String endTime);

    /**
     * 出库总费用
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:47:23
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoOutstock(String customerId, String subjectCode, String startTime,
            String endTime);

    /**
     * 入库总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:48:27
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoInstock(String customerId, String subjectCode, String startTime,
            String endTime);

    /**
     * 托数总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:48:51
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoPallet(String customerId, String subjectCode, String startTime,
            String endTime);

    /**
     * 商品按件总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:49:17
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoProductItem(String customerId, String subjectCode, String startTime,
            String endTime);

    /**
     * 标准包装方案总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:49:44
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoStandMaterial(String customerId, String subjectCode, String startTime,
            String endTime);

    /**
     * 增值总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:50:45
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoAdd(String customerId, String subjectCode, String startTime, String endTime);

    /**
     * 耗材总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:51:04
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoMaterial(String customerId, String subjectCode, String startTime,
            String endTime);

    /**
     * 配送总费用
     * <功能描述>
     * 
     * @author wangchen870
     * @date 2019年6月15日 下午2:51:24
     *
     * @param customerId
     * @param subjectCode
     * @param startTime
     * @param endTime
     * @return
     */
    BmsFeesQtyEntity queryTotalAmountForStoDis(String customerId, String subjectCode, String startTime, String endTime);
	
}
