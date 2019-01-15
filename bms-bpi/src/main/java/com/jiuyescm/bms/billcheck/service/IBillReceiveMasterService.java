package com.jiuyescm.bms.billcheck.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveExpectVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiveMasterVo;
import com.jiuyescm.bms.billcheck.vo.ReportBillImportMasterVo;
import com.jiuyescm.exception.BizException;

/**
 * ..Service
 * @author wangchen
 * 
 */
public interface IBillReceiveMasterService {

    BillReceiveMasterVo findById(Long id);
	
    PageInfo<BillReceiveMasterVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);

	List<BillReceiveMasterVo> query(Map<String, Object> condition);
	
	BillReceiveMasterVo queryOne(Map<String, Object> condition);

    int save(BillReceiveMasterVo entity);

    void update(BillReceiveMasterVo entity);
    
	void delete(String billNo,String status) throws BizException;
	
	/**
	 * 保存预估金额
	 */
	int saveExpect(BillReceiveExpectVo vo);

	BillReceiveExpectVo queryExpect(Map<String, Object> condition);
	
	Double getAbnormalMoney(String billNo);
	
	/**
	 * 获取耗材托数、耗材存储费
	 * @return
	 */
	Map<String,BigDecimal> queryMaterial(String billNo);
	
	/**
	 * 获取商品托数、商品存储数
	 * @return
	 */
	Map<String,BigDecimal> queryProduct(String billNo);
	
	/**
	 * 获取仓租费
	 * @param map
	 * @return
	 */
	Double queryStorageRent(String billNo);
	
	/**
	 * 干线理赔费用
	 * @param billNo
	 * @return
	 */
	Double queryTransportAbnormalFee(String billNo);
	
	/**
	 * 航空理赔费用
	 * @param billNo
	 * @return
	 */
	Double queryAirAbnormalFee(String billNo);
	
	/**
	 * 仓储理赔费用
	 * @param billNo
	 * @return
	 */
	Double queryStorageAbnormalFee(String billNo);
	
	/**
	 * 配送理赔费用
	 * @param billNo
	 * @return
	 */
	Double queryDispatchAbnormalFee(String billNo);
	
	int insertReportMaster(ReportBillImportMasterVo vo);
	
}
