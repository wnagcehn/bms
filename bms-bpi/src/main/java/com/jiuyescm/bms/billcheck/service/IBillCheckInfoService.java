package com.jiuyescm.bms.billcheck.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.billcheck.vo.BillCheckAdjustInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckInfoVo;
import com.jiuyescm.bms.billcheck.vo.BillCheckLogVo;
import com.jiuyescm.bms.billcheck.vo.BillReceiptFollowVo;
import com.jiuyescm.exception.BizException;

public interface IBillCheckInfoService {
	/**
	 * 对账主表的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInfoVo> query(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	
	/**
	 * 分页查询预警账单
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInfoVo> queryWarn(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 分页查询预警账单List
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInfoVo> queryWarnList(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 对账主表的回款反馈信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillReceiptFollowVo> queryReceiptFollow(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	List<BillCheckInfoVo> queryList(Map<String, Object> condition);

	/**
	 * 对账主表调整的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List<BillCheckAdjustInfoVo> queryAdjust(Map<String, Object> condition);
	
	
	/**
	 * 对账主表调整的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public BillCheckAdjustInfoVo queryOneAdjust(Map<String, Object> condition);
	
	/**
	 * 更新账单
	 * @param billCheckInfoVo
	 * @return
	 */
	public int update(BillCheckInfoVo billCheckInfoVo);

	/**
	 * 根据条件查询
	 */
	BillCheckInfoVo queryOne(Map<String, Object> condition);
	
	/**
	 * 根据条件查询
	 */
	BillCheckInfoVo queryBillCheck(Map<String, Object> condition);
	
	/**
	 * 保存数据
	 * @param list
	 * @return
	 */
	public int saveList(List<BillCheckInfoVo> list);
	
	/**
	 * 保存回款反馈
	 * @param entity
	 * @return
	 */
	public int saveReceiptFollow(BillReceiptFollowVo entity);
	
	/**
	 * 批量保存回款反馈
	 * @param entity
	 * @return
	 */
	public int saveReceiptFollowList(List<BillReceiptFollowVo> list);
	
	
	/**
	 * 批量保存收款调整信息
	 * @param list
	 * @return
	 */
	public int saveAjustList(List<BillCheckAdjustInfoVo> list);
	
	/**
	 * 批量更新收款调整信息
	 * @param list
	 * @return
	 */
	public int updateAjustList(List<BillCheckAdjustInfoVo> list);

	String getBillCheckStatus(int id);

	int confirmBillCheckInfo(BillCheckInfoVo billCheckInfoVo, BillCheckLogVo vo) throws Exception;

	List<BillCheckInfoVo> queryAllByBillName(List<String> invoiceNameList);

	List<BillCheckInfoVo> queryAllByBillCheckId(List<Integer> checkIdList);
	
	public BillReceiptFollowVo queryReceiptFollow(Map<String, Object> condition);
	
	/**
	 * 对账收款明细的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInfoVo> queryReceiptDetail(Map<String, Object> condition, int pageNo,
            int pageSize);
	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	int save(BillCheckInfoVo vo); 
	
	/**
	 * 账单冲抵页面查询账单的信息
	 * @param condition
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageInfo<BillCheckInfoVo> queryForOut(Map<String, Object> condition, int pageNo,
            int pageSize);
	
	/**
	 * 获取最近账单的信息
	 */
	BillCheckInfoVo getLatestBill(Map<String, Object> condition);
	
	/**
	 * 导入校验
	 * @param condition
	 * @return
	 */
	public void importCheck(String billNo) throws BizException;
	
	/**
	 * 调整金额
	 * @param condition
	 */
	public void adjustMoney(String billNo,Double adjustMoney) throws BizException;
	}