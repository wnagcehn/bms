/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInvoceInfoEntity;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInfoRepository;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInvoceInfoRepository;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInvoceInfoService;
import com.jiuyescm.bms.common.constants.BillConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.BillInvoceStatusEnum;
import com.jiuyescm.bms.common.enumtype.BillStatusEnum;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.service.IBillLogRecordService;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.common.utils.ArithUtil;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bmsBillInvoceInfoService")
public class BmsBillInvoceInfoServiceImpl implements IBmsBillInvoceInfoService {
	
	@Autowired
    private IBmsBillInvoceInfoRepository bmsBillInvoceInfoRepository;
	@Autowired
    private IBmsBillInfoRepository bmsBillInfoRepository;
	@Autowired
	private IBillLogRecordService billLogRecordService;

    @Override
    public PageInfo<BmsBillInvoceInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsBillInvoceInfoRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BmsBillInvoceInfoEntity findById(Long id) {
        return bmsBillInvoceInfoRepository.findById(id);
    }

    @Override
    public int save(BmsBillInvoceInfoEntity entity) {
        return bmsBillInvoceInfoRepository.save(entity);
    }

    @Override
    public int update(BmsBillInvoceInfoEntity entity) {
        return bmsBillInvoceInfoRepository.update(entity);
    }

	@Override
	public BmsBillInvoceInfoEntity queryCountInvoceInfo(Map<String, Object> param) {
		return bmsBillInvoceInfoRepository.queryCountInvoceInfo(param);
	}

	/**
	 * 开票
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResponseVo openInvoce(BmsBillInvoceInfoEntity entity) {
		ResponseVo responseVo = null;
		
		//开票金额是否为应收金额,更新账单状态
		BmsBillInfoEntity billEntity = bmsBillInfoRepository.queryEntityByBillNo(entity.getBillNo());
		if (null == billEntity) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_NOTFOUND_MSG);
		}
		BmsBillInfoEntity updateBillEntity = new BmsBillInfoEntity();
		if (entity.getInvoceAmount().equals(billEntity.getReceiveAmount())) {
			//开票金额等于账单应收金额[已开票]
			updateBillEntity.setStatus(BillStatusEnum.INVOICED.getCode());
		}else if (entity.getInvoceAmount() < billEntity.getReceiveAmount()) {
			//开票金额小于账单应收金额[部分开票]
			updateBillEntity.setStatus(BillStatusEnum.PARTINVOICED.getCode());
		}else {
			//开票金额大于账单实收金额[不允许操作]
			return new ResponseVo(ResponseVo.FAIL, BillConstant.INVOCE_GT_BILL_AMOUNT_MSG);
		}
				
		//统计发票信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("billNo", entity.getBillNo());
		param.put("status", BillInvoceStatusEnum.UNSETTLED.getCode());
		BmsBillInvoceInfoEntity invoceEntity = bmsBillInvoceInfoRepository.queryCountInvoceInfo(param);
		if (null != invoceEntity) {
			//未开票金额=账单总金额-已开票总金额
			double unInvoceAmount = ArithUtil.sub(billEntity.getTotalAmount(), invoceEntity.getInvoceAmount());
			if (entity.getInvoceAmount() > unInvoceAmount) {
				//开票金额>未开票总金额[不允许操作]
				return new ResponseVo(ResponseVo.FAIL, BillConstant.BILL_INVOCE_NOTENOUGH_MSG);
			}
		}
		//开票
		String operater = JAppContext.currentUserName();
		Timestamp operaterTime = JAppContext.currentTimestamp();
		entity.setReceiveAmount(entity.getInvoceAmount());
		entity.setReceiptAmount(0.0);
		entity.setReceiptStatus(BillInvoceStatusEnum.UNSETTLED.getCode());
		entity.setCreator(operater);
		entity.setCreateTime(operaterTime);
		entity.setDelFlag(ConstantInterface.DelFlag.NO);
		int saveNum = bmsBillInvoceInfoRepository.save(entity);
		if (saveNum <= 0) {
			throw new RuntimeException();
		}
		
		//跟新账单状态
		updateBillEntity.setBillNo(entity.getBillNo());
		updateBillEntity.setVersion(entity.getVersion());
		updateBillEntity.setLastModifier(operater);
		updateBillEntity.setLastModifyTime(operaterTime);
		int updateNum = bmsBillInfoRepository.updateByBillNo(updateBillEntity);
		if (updateNum <= 0) {
			throw new RuntimeException();
		}
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("开票");
		if ("UNNEED".equals(entity.getIsNeedInvoce())) {
			logEntity.setRemark("该部分无需发票");
		}
		logEntity.setInvoceNo(entity.getInvoceNo());
		logEntity.setInvoceAmount(entity.getInvoceAmount());
		logEntity.setCreator(operater);
		logEntity.setCreateTime(operaterTime);
		billLogRecordService.log(logEntity);
		
		return responseVo;
	}

	@Override
	public BmsBillInvoceInfoEntity queryCountReceiptInfo(Map<String, Object> param) {
		return bmsBillInvoceInfoRepository.queryCountReceiptInfo(param);
	}

	/**
	 * 收账
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResponseVo receipt(BmsBillInvoceInfoEntity entity) {
		//收款金额是否大于开票金额
		if (entity.getReceiptAmount() > entity.getInvoceAmount()) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.RECEIPT_GT_INVOCE_AMOUNT_MSG);
		}
		
		//账单信息
		String billNo = entity.getBillNo();
		BmsBillInfoEntity billEntity = bmsBillInfoRepository.queryEntityByBillNo(billNo);
		if (null == billEntity) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_NOTFOUND_MSG);
		}
		
		String operater = JAppContext.currentUserName();
		Timestamp operaterTime = JAppContext.currentTimestamp();
		BmsBillInfoEntity updateBillEntity = new BmsBillInfoEntity();
		updateBillEntity.setBillNo(billNo);
		//发票统计信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("billNo", billNo);
		param.put("status", BillInvoceStatusEnum.UNSETTLED.getCode());
		BmsBillInvoceInfoEntity invoceEntity = bmsBillInvoceInfoRepository.queryCountInvoceInfo(param);
		if (null == invoceEntity) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_INVOCE_NOTFOUND_MSG);
		}else {
			//收款金额是否等于账单应收金额
			if (entity.getReceiptAmount() > billEntity.getReceiveAmount()) {
				//收款金额>账单应收金额[不允许操作]
				return new ResponseVo(ResponseVo.FAIL, BillConstant.RECEIPT_GT_BILL_AMOUNT_MSG);
			}else if (entity.getReceiptAmount().equals(billEntity.getReceiveAmount())) {
				//收款金额=账单应收收金额[已收款]
				updateBillEntity.setStatus(BillStatusEnum.RECEIPTED.getCode());
				//更新账单状态
				updateBillEntity.setVersion(entity.getVersion());
				updateBillEntity.setLastModifier(operater);
				updateBillEntity.setLastModifyTime(operaterTime);
				int updateNum = bmsBillInfoRepository.updateByBillNo(updateBillEntity);
				if (updateNum <= 0) {
					throw new RuntimeException();
				}
			}
		}
		
		//更新发票收账状态
		BmsBillInvoceInfoEntity updateInvoceEntity = new BmsBillInvoceInfoEntity();
		updateInvoceEntity.setReceiptTime(entity.getReceiptTime());
		updateInvoceEntity.setReceiptAmount(entity.getReceiptAmount());
		updateInvoceEntity.setReceiptStatus(BillInvoceStatusEnum.SETTLED.getCode());
		updateInvoceEntity.setId(entity.getId());
		updateInvoceEntity.setLastModifier(operater);
		updateInvoceEntity.setLastModifyTime(operaterTime);
		int updateInvoceNum = bmsBillInvoceInfoRepository.update(updateInvoceEntity);
		if (updateInvoceNum <= 0) {
			throw new RuntimeException();
		}
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("收款");
		logEntity.setReceiptAmount(entity.getReceiptAmount());
		logEntity.setCreator(operater);
		logEntity.setCreateTime(operaterTime);
		billLogRecordService.log(logEntity);
		
		return null;
	}

	/**
	 * 删除发票
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResponseVo delInvoce(BmsBillInvoceInfoEntity entity) {
		if (StringUtils.isEmpty(entity.getBillNo())) {
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		//是否为最后一条发票信息，是则需要更新账单状态，否则直接更新del_flag
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("billNo", entity.getBillNo());
		List<BmsBillInvoceInfoEntity> billList = bmsBillInvoceInfoRepository.query(condition);
		if (null == billList || billList.size() <= 0) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_NOTFOUND_MSG);
		}
		
		String operater = JAppContext.currentUserName();
		Timestamp operaterTime = JAppContext.currentTimestamp();
		if (billList.size() == 1) {
			//就一条发票信息
			if (!billList.get(0).getBillNo().equals(entity.getBillNo())) {
				return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_INVOCE_NOTFOUND_MSG);
			}
			//一条发票为当前要删除的发票，更新账单状态
			BmsBillInfoEntity billEntity = new BmsBillInfoEntity();
			billEntity.setBillNo(entity.getBillNo());
			billEntity.setStatus(BillStatusEnum.CONFIRMED.getCode());//已开票|部分开票-》已确认
			billEntity.setVersion(0);
			billEntity.setLastModifier(operater);
			billEntity.setLastModifyTime(operaterTime);
			int updateBillNum = bmsBillInfoRepository.updateByBillNo(billEntity);
			if (updateBillNum <= 0) {
				throw new RuntimeException();
			}
			
			//删除发票
			BmsBillInvoceInfoEntity delEntity = new BmsBillInvoceInfoEntity();
			delEntity.setId(entity.getId());
			delEntity.setDelFlag(ConstantInterface.DelFlag.YES);
			delEntity.setLastModifier(operater);
			delEntity.setLastModifyTime(operaterTime);
			int updateInvoceNum = bmsBillInvoceInfoRepository.update(delEntity);
			if (updateInvoceNum <= 0) {
				throw new RuntimeException();
			}
		}else {
			boolean delFlag = false;
			for (BmsBillInvoceInfoEntity invoceEntity : billList) {
				if (invoceEntity.getId().equals(entity.getId())) {
					delFlag = true;
					break;
				}
			}
			if (!delFlag) {
				//多条发票，但是没有当前要删除的发票[异常]
				return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_INVOCE_NOTFOUND_MSG);
			}else {
				//直接删除
				BmsBillInvoceInfoEntity delEntity = new BmsBillInvoceInfoEntity();
				delEntity.setId(entity.getId());
				delEntity.setDelFlag(ConstantInterface.DelFlag.YES);
				delEntity.setLastModifier(operater);
				delEntity.setLastModifyTime(operaterTime);
				int updateInvoceNum = bmsBillInvoceInfoRepository.update(delEntity);
				if (updateInvoceNum <= 0) {
					throw new RuntimeException();
				}
			}
		}
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("删除发票");
		logEntity.setInvoceNo(entity.getInvoceNo());
		logEntity.setCreator(operater);
		logEntity.setCreateTime(operaterTime);
		billLogRecordService.log(logEntity);
		
		return null;
	}

	@Override
	public List<BmsBillInvoceInfoEntity> queryInvoce(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsBillInvoceInfoRepository.queryInvoce(condition);
	}
	
}
