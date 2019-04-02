package com.jiuyescm.bms.calculate.api;

import com.jiuyescm.bms.calculate.vo.BmsFeesQtyVo;

public interface IBmsCalcuService {

	/**
	 * 仓储应收
	 */
	public final String StorageRec = "StorageRec";
	
	/**
	 * 配送应收
	 */
	public final String DispatchRec = "DispatchRec";
	
	/**
	 * 查询商家费用单量
	 * @param customerId 商家ID
	 * @param subjectCode 科目编码
	 * @param creMonth   业务月份 201901
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForSto(String customerId,String subjectCode,Integer creMonth);
	
	
	
	BmsFeesQtyVo queryFeesQtyForDis(String customerId,String subjectCode,Integer creMonth);
	
	/**
	 * 根据商家ID 查询合同归属
	 * @param customerId
	 * @return BMS-合同归属BMS  CONTRACT-合同归属合同在线
	 */
	String queryContractAttr(String customerId);
	
	/**
	 * 入库
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForStoInstock(String customerId,String subjectCode,Integer creMonth);
	
	/**
	 * 出库
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForStoOutstock(String customerId,String subjectCode,Integer creMonth);

	/**
	 * 耗材
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForStoMaterial(String customerId,String subjectCode,Integer creMonth);
	

	/**
	 * 商品按件
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForStoProductItem(String customerId,String subjectCode,Integer creMonth);
	
	/**
	 * 增值
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForStoAdd(String customerId,String subjectCode,Integer creMonth);
	
	/**
	 * 托数
	 * @param customerId
	 * @param subjectCode
	 * @param creMonth
	 * @return
	 */
	BmsFeesQtyVo queryFeesQtyForStoPallet(String customerId,String subjectCode,Integer creMonth);

}
