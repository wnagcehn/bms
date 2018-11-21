/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.repository.IFileExportTaskRepository;
//import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInfoRepository;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInvoceInfoRepository;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.bill.receive.service.IBmsBillInfoService;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCountEntityVo;
import com.jiuyescm.bms.bill.receive.vo.BmsBillCustomerCountEntityVo;
import com.jiuyescm.bms.common.constants.BillConstant;
import com.jiuyescm.bms.common.constants.MessageConstant;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectAbormalEnum;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectEnum;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.common.enumtype.BillStatusEnum;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.service.IBillLogRecordService;
import com.jiuyescm.bms.common.sequence.repository.SequenceDao;
import com.jiuyescm.bms.common.system.ResponseVo;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalRepository;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.exception.BizException;

/**
 * 
 * @author stevenl
 * 
 */
@Service("bmsBillInfoService")
public class BmsBillInfoServiceImpl implements IBmsBillInfoService {

	private static final Logger logger = Logger.getLogger(BmsBillInfoServiceImpl.class.getName());
	
	@Autowired
    private IBmsBillInfoRepository bmsBillInfoRepository;
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	@Autowired
	private IFeesReceiveDispatchRepository feeInDistributionRepository;
	@Autowired
	private IFeesReceiveDeliverDao feesReceiveDeliverDao;
	@Autowired
    private IFeesAbnormalRepository feesAbnormalRepository;
	@Autowired
	private SequenceDao sequenceDao;
	@Autowired
	private IBmsBillSubjectInfoRepository billSubjectInfoRepository;
	@Autowired
    private IBmsBillInvoceInfoRepository bmsBillInvoceInfoRepository;
	@Autowired
	private ISystemCodeService systemCodeService;
	@Autowired
	private IBillLogRecordService billLogRecordService;
	@Autowired
	private IFileExportTaskRepository fileExportTaskRepository;
	
	/*@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;*/

    @Override
    public PageInfo<BmsBillInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsBillInfoRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BmsBillInfoEntity findById(Long id) {
        return bmsBillInfoRepository.findById(id);
    }

    @Override
    public int save(BmsBillInfoEntity entity) {
        return bmsBillInfoRepository.save(entity);
    }

    @Override
    public int update(BmsBillInfoEntity entity) {
        return bmsBillInfoRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bmsBillInfoRepository.delete(id);
    }

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo) {
		return bmsBillInfoRepository.querywarehouseAmount(billNo);
	}

    /**
     * 生成账单
     * 1、新增账单主表、更新账单编号到费用明细表
     * 2、新增账单科目费用表
     */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public ResponseVo generReceiveBill(BmsBillInfoEntity entity) throws Exception {
    	ResponseVo responseVo = null;
    	/***************************1、新增账单主表******************************/
    	// 是否存在费用信息（仓储、运输、配送、异常）
		int stroageNum = feesReceiveStorageRepository.queryCountByBillInfo(entity);
		int transportNum = feesReceiveDeliverDao.queryCountByBillInfo(entity);
		int dispatchNum = feeInDistributionRepository.queryCountByBillInfo(entity);
		int abnormalNum = feesAbnormalRepository.queryCountByBillInfo(entity);
		// 仓储配送异常无费用时
		if (0 == dispatchNum && 0 == stroageNum && 0==transportNum && 0 == abnormalNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.FEES_NOTFOUND_MSG);
			return new ResponseVo(ResponseVo.FAIL, BillConstant.FEES_NOTFOUND_MSG);
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 200);
		
		// 生成账单编号
		String billNo = sequenceDao.getBillNoOne(BmsBillInfoEntity.class.getName(), "Z", "0000000000");
		if (StringUtils.isBlank(billNo)) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.GENER_BILLNO_FAIL_MSG);
			return new ResponseVo(ResponseVo.FAIL, BillConstant.GENER_BILLNO_FAIL_MSG);
		}
		entity.setBillNo(billNo);
		
		// 更新各费用账单编号
		updateFees(entity, stroageNum,transportNum, dispatchNum, abnormalNum);
		
		//作以下修改  1.先生成费用科目 2.再根据费用科目来统计费用
		/***************************2、新增账单科目费用表******************************/
		List<BmsBillSubjectInfoEntity> saveList = handBillSubjectInfo(entity);
		if (null != saveList && saveList.size() > 0) {
			int saveNum = billSubjectInfoRepository.saveBillSubjectList(saveList);
			if (saveNum != saveList.size()) {
				throw new BizException(BillConstant.BMS_BILL_SUBJECT_SAVE_FAIL_MSG);
			}
		}
		
		/***************************3、统计账单科目费用表的费用和******************************/
		//统计金额（仓储、运输、配送、异常）
		double sumAmount = 0d;
		for(BmsBillSubjectInfoEntity billSubjectInfoEntity:saveList){
			sumAmount += billSubjectInfoEntity.getTotalAmount() != null?billSubjectInfoEntity.getTotalAmount():0;
		}
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 800);
		
		/***************************4、生成账单******************************/
		//新增账单
		saveBillSubjectInfo(entity, sumAmount);
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 900);
			
		return responseVo;
	}

	
    /**
     * 新增账单
     * @param entity
     * @param sumAmount
     * @return
     */
    private void saveBillSubjectInfo(BmsBillInfoEntity entity, double sumAmount) throws Exception{
		String operator = JAppContext.currentUserName();
		Timestamp operaterTime = JAppContext.currentTimestamp();
		
		//生成账单，更新费用数据
		entity.setStatus(BillStatusEnum.UNCONFIRMED.getCode());
		entity.setTotalAmount(sumAmount);//总金额
		entity.setDiscountAmount(0d);//账单折扣金额
		entity.setSubjectDiscountAmount(0d);//账单费用科目折扣金额
		entity.setDerateAmount(0d);//减免金额
		entity.setReceiveAmount(sumAmount);//应收金额
		entity.setReceiptAmount(0d);//实收金额
		entity.setCreator(operator);
		entity.setCreateTime(operaterTime);
		entity.setDelFlag(ConstantInterface.DelFlag.NO);//0 正常
				
		//账单状态为0
		int k=bmsBillInfoRepository.save(entity);
		if(k <= 0){
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.BMS_BILL_SAVE_FAIL_MSG);
			throw new BizException(BillConstant.BMS_BILL_SAVE_FAIL_MSG);
		}
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("生成账单");
		logEntity.setCreator(operator);
		logEntity.setCreateTime(operaterTime);
		billLogRecordService.log(logEntity);
    }
    
    /**
     * 跟新费用表
     * @param entity
     * @param stroageNum
     * @param transportNum
     * @param dispatchNum
     * @param abnormalNum
     * @throws Exception 
     */
    private void updateFees(BmsBillInfoEntity entity, int stroageNum, int transportNum, 
    		int dispatchNum, int abnormalNum) throws Exception{
    	entity.setStatus(ConstantInterface.FeeStatus.STATUS_1);
		entity.setLastModifier(JAppContext.currentUserName());
		entity.setLastModifyTime(JAppContext.currentTimestamp());
		
		//更新仓储费用
		int updStorageNum = feesReceiveStorageRepository.updateByBillInfo(entity);
		if (stroageNum != updStorageNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.FEE_STRO_UPDATE_FAIL_MSG);
			throw new BizException(BillConstant.FEE_STRO_UPDATE_FAIL_MSG);
		}
		//更新运输费用
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 300);
		int updTransportNum = feesReceiveDeliverDao.updateByBillInfo(entity);
		if (transportNum != updTransportNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.FEE_TRAN_UPDATE_FAIL_MSG);
			throw new BizException(BillConstant.FEE_TRAN_UPDATE_FAIL_MSG);
		}
		//更新配送费用
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 400);
		int updDispatchNum = feeInDistributionRepository.updateByBillInfo(entity);
		if (dispatchNum != updDispatchNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.FEE_DISP_UPDATE_FAIL_MSG);
			throw new BizException(BillConstant.FEE_DISP_UPDATE_FAIL_MSG);
		}
		//更新异常费用
		DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 500);
		int updAbnormalNum = feesAbnormalRepository.updateByBillInfo(entity);
		if (abnormalNum != updAbnormalNum) {
			DoradoContext.getAttachedRequest().getSession().setAttribute("progressFlag", 0);
			logger.info(BillConstant.FEE_ABNO_UPDATE_FAIL_MSG);
			throw new BizException(BillConstant.FEE_ABNO_UPDATE_FAIL_MSG);
		}
    }
    
    /**
     * 处理费用科目账单 费用类型、状态、删除标志
     */
    private List<BmsBillSubjectInfoEntity> handBillSubjectInfo(BmsBillInfoEntity entity) throws Exception{
    	//获取账单的结束时间
    	String endTime=entity.getEndTime().toString().substring(0, 7);
    	String operatorName = JAppContext.currentUserName();
    	Timestamp operatorTime = JAppContext.currentTimestamp();
    	
    	List<BmsBillSubjectInfoEntity> saveList = new ArrayList<BmsBillSubjectInfoEntity>();
    	
    	// 仓储
    	List<BmsBillSubjectInfoEntity> storSubjectFeeList = feesReceiveStorageRepository.queryFeesBillSubjectInfo(entity);
		if (null != storSubjectFeeList && storSubjectFeeList.size() > 0) {
			//仓储费用科目map
			Map<String, String> subjectMap = getStorageEnumList();
			for (BmsBillSubjectInfoEntity subjectEntity : storSubjectFeeList) {
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setBillMonth(endTime);
				//处理费用科目名称
				if (StringUtils.isNotBlank(subjectEntity.getSubjectCode())&&subjectMap.containsKey(subjectEntity.getSubjectCode())) {
					subjectEntity.setSubjectName(subjectMap.get(subjectEntity.getSubjectCode()));
				}
				subjectEntity.setFeesType(BillFeesSubjectEnum.STORAGE.getCode());
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
			}
			saveList.addAll(storSubjectFeeList);
		}
		
		// 运输-不包含理赔
		List<BmsBillSubjectInfoEntity> tranSubjectFeeList = feesReceiveDeliverDao.queryFeesBillSubjectInfo(entity);
		if (null != tranSubjectFeeList && tranSubjectFeeList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : tranSubjectFeeList) {
				subjectEntity.setBillMonth(endTime);
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setFeesType(BillFeesSubjectEnum.TRANSPORT.getCode());
				subjectEntity.setSubjectCode("ts_trans_amount");
				subjectEntity.setSubjectName("运输");
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
			}
			saveList.addAll(tranSubjectFeeList);
		}
		// 运输-包含理赔
		List<BmsBillSubjectInfoEntity> tranSubjectFeeAbnormalList = feesReceiveDeliverDao.queryFeesBillSubjectInfoAbnormal(entity);
		if (null != tranSubjectFeeAbnormalList && tranSubjectFeeAbnormalList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : tranSubjectFeeAbnormalList) {
				subjectEntity.setBillMonth(endTime);
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setFeesType(BillFeesSubjectEnum.TRANSPORT.getCode());
				subjectEntity.setSubjectCode(BillFeesSubjectAbormalEnum.Abnormal_Transport.getCode());
				subjectEntity.setSubjectName(BillFeesSubjectAbormalEnum.Abnormal_Transport.getDesc());
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
			}
			saveList.addAll(tranSubjectFeeAbnormalList);
		}
		
		// 配送
		List<BmsBillSubjectInfoEntity> dispSubjectFeeList = feeInDistributionRepository.queryFeesBillSubjectInfo(entity);
		if (null != dispSubjectFeeList && dispSubjectFeeList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : dispSubjectFeeList) {
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setBillMonth(endTime);
				subjectEntity.setFeesType(BillFeesSubjectEnum.DISPATCH.getCode());
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
			}
			saveList.addAll(dispSubjectFeeList);
		}
		
		/*
		 * 理赔--按照大原因
		 * 1	顾客原因(不考虑，过滤掉)，2	承运商原因，3	仓库原因，4	商家原因，56	公司原因
		 * 应收-仓储--仓租--理赔:来源是客诉–公司原因和仓库原因
		 * 应收-干线–理赔:来源是导入费用
		 * 应收-宅配--理赔:来源是客诉–承运商原因
		 * 应收-宅配-改地址和退件费:来源是客诉-商家原因
		 * 注意：fees_abnormal表中理赔金额都为正（+），
		 * 		理赔都为负数,改地址和退件费为正
		 */
		// 仓储-理赔
    	List<BmsBillSubjectInfoEntity> abnoSubjectFeeList = feesAbnormalRepository.queryFeesBillSubjectInfo(entity);
		if (null != abnoSubjectFeeList && abnoSubjectFeeList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : abnoSubjectFeeList) {
				subjectEntity.setSubjectCode(BillFeesSubjectAbormalEnum.Abnormal_Stroage.getCode());
				//对仓储理赔费用进行判断处理，统计出总费用
				Double totalMoney = getAbnormalMoney(subjectEntity);
				
				subjectEntity.setTotalAmount(totalMoney);			
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setBillMonth(endTime);
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());	
				subjectEntity.setSubjectName(BillFeesSubjectAbormalEnum.Abnormal_Stroage.getDesc());
				subjectEntity.setFeesType(BillFeesSubjectEnum.STORAGE.getCode());
			}
			saveList.addAll(abnoSubjectFeeList);
		}
		
		// 理赔-配送-承运商原因
		List<BmsBillSubjectInfoEntity> abnoDisSubjectFeeList = feesAbnormalRepository.queryFeesBillSubjectInfoDis(entity);
		if (null != abnoDisSubjectFeeList && abnoDisSubjectFeeList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : abnoDisSubjectFeeList) {
				subjectEntity.setSubjectCode(BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getCode());
				//对配送理赔费用进行判断处理，统计出总费用
				Double totalMoney = getAbnormalMoney(subjectEntity);
				
				subjectEntity.setTotalAmount(totalMoney);				
				subjectEntity.setBillMonth(endTime);
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
				subjectEntity.setSubjectName(BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getDesc());
				subjectEntity.setFeesType(BillFeesSubjectEnum.DISPATCH.getCode());
			}
			saveList.addAll(abnoDisSubjectFeeList);
		}
		
		// 理赔-配送-改地址和退件费(商家原因)
		List<BmsBillSubjectInfoEntity> abnoDisChangeSubjectFeeList = feesAbnormalRepository.queryFeesBillSubjectInfoDisChange(entity);
		if (null != abnoDisChangeSubjectFeeList && abnoDisChangeSubjectFeeList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : abnoDisChangeSubjectFeeList) {
				subjectEntity.setSubjectCode(BillFeesSubjectAbormalEnum.Abnormal_DisChange.getCode());
				//对配送理赔费用进行判断处理，统计出总费用
				Double totalMoney = getAbnormalMoney(subjectEntity);
				
				subjectEntity.setTotalAmount(totalMoney);				
				subjectEntity.setBillMonth(endTime);
				subjectEntity.setCreator(operatorName);
				subjectEntity.setCreateTime(operatorTime);
				subjectEntity.setDelFlag(ConstantInterface.DelFlag.NO);
				subjectEntity.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
				subjectEntity.setSubjectName(BillFeesSubjectAbormalEnum.Abnormal_DisChange.getDesc());
				subjectEntity.setFeesType(BillFeesSubjectEnum.DISPATCH.getCode());
			}
			saveList.addAll(abnoDisChangeSubjectFeeList);
		}
		
    	return saveList;
    }

    /**
     * 获取仓储应收一级费用科目
     * @return
     * @throws Exception
     */
    private Map<String, String> getStorageEnumList() throws Exception {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		/*List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}*/
		//mapValue=bmsGroupSubjectService.getSubject("receive_wh_contract_subject");
		mapValue = null;
		return mapValue;
	}
    
    /**
	 * 1、处理理赔费用金额
	 * 2、更新异常费用到对应的仓储、配送费用表中
	 * @param bmsBillSubjectInfoEntity
	 * @return
	 */
	private double getAbnormalMoney(BmsBillSubjectInfoEntity bmsBillSubjectInfoEntity) throws Exception {
		Double totalMoney=0d;
		List<FeesReceiveStorageEntity> feesReceiveStorageList = new ArrayList<FeesReceiveStorageEntity>();
		List<FeesReceiveDispatchEntity> feesReceiveDispatchList = new ArrayList<FeesReceiveDispatchEntity>();
		
		String subjectCode=bmsBillSubjectInfoEntity.getSubjectCode();
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode) {
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
			break;
		case "Abnormal_Storage":
			reasonIds.add("3");
			reasonIds.add("56");
			break;
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("warehouseCode", bmsBillSubjectInfoEntity.getWarehouseCode());
		map.put("reasonIds", reasonIds);
		map.put("billNo", bmsBillSubjectInfoEntity.getBillNo());		
		List<FeesAbnormalEntity> feeList = feesAbnormalRepository.queryFeeBySubject(map);
		
		/*
		 * eg1：某单赔偿金额为10元，运费5元（免运费），且为非商家原因；
		 * 理赔金额= - 10 - 5
		 * eg2：某单赔偿金额为10元，运费5元（免运费 ），且为商家原因；
		 * 理赔金额 = 10 - 5
		 */
		String operatorName = JAppContext.currentUserName();
    	Timestamp operatorTime = JAppContext.currentTimestamp();
		for(FeesAbnormalEntity fee : feeList){
			Double feeAmount = 0d;// 每条理赔费用金额
			
			if("Abnormal_Dispatch".equals(subjectCode) || "Abnormal_DisChange".equals(subjectCode)){ //配送理赔
				//商家原因
				if(4 == fee.getReasonId()){
					totalMoney += fee.getPayMoney();
					feeAmount += fee.getPayMoney();
				}else {
					totalMoney -= fee.getPayMoney();
					feeAmount -= fee.getPayMoney();
					//非商家原因
					if("0".equals(fee.getIsDeliveryFree())){
						//免运费,根据运单号查询费用
						Map<String, Object> condition=new HashMap<>();
						condition.put("waybillNo", fee.getExpressnum());
						FeesReceiveDispatchEntity feesReceiveDispatchEntity = feeInDistributionRepository.queryOne(condition);
						//免运费
						if(null != feesReceiveDispatchEntity) {
							double deliveryCost = (null == feesReceiveDispatchEntity.getAmount())? 0 : feesReceiveDispatchEntity.getAmount();
							totalMoney -= deliveryCost;
							feeAmount -= deliveryCost;
						}
					}
				}
				
				FeesReceiveDispatchEntity dispatchEntity = new FeesReceiveDispatchEntity();
				dispatchEntity.setBillNo(fee.getBillNo());
				dispatchEntity.setFeesNo(fee.getFeeNo());
				dispatchEntity.setWaybillNo(fee.getExpressnum());// 运单号
				dispatchEntity.setExternalNo(fee.getReference());// 外部单号
				dispatchEntity.setCustomerid(fee.getCustomerId());
				dispatchEntity.setCustomerName(fee.getCustomerName());
				dispatchEntity.setWarehouseCode(fee.getWarehouseId());
				dispatchEntity.setWarehouseName(fee.getWarehouseName());
				dispatchEntity.setCarrierid(fee.getCarrierId());
				dispatchEntity.setCarrierName(fee.getCarrierName());
				dispatchEntity.setDeliveryid(fee.getDeliverId());
				dispatchEntity.setDeliverName(fee.getDeliverName());
				dispatchEntity.setAmount(feeAmount);// 金额
				dispatchEntity.setDerateAmount(0.0);// 减免金额
				if ("Abnormal_Dispatch".equals(subjectCode)) {
					dispatchEntity.setSubjectCode("de_abnormal_pay");
					dispatchEntity.setOtherSubjectCode("de_abnormal_pay");
				}else {
					dispatchEntity.setSubjectCode("de_change");
					dispatchEntity.setOtherSubjectCode("de_change");
				}
				dispatchEntity.setStatus("1");
				dispatchEntity.setIsCalculated("1");
				dispatchEntity.setCreator(fee.getCreatePersonName());
				dispatchEntity.setCreateTime(fee.getCreateTime());
				dispatchEntity.setLastModifier(operatorName);
				dispatchEntity.setLastModifyTime(operatorTime);
				dispatchEntity.setDelFlag(ConstantInterface.DelFlag.NO);
				
				feesReceiveDispatchList.add(dispatchEntity);
			}else if("Abnormal_Storage".equals(subjectCode)) { //仓储理赔
				totalMoney -= fee.getPayMoney();
				feeAmount -= fee.getPayMoney();
				
				if("0".equals(fee.getIsDeliveryFree())){
					//免运费,根据运单号查询费用
					Map<String, Object> condition=new HashMap<>();
					condition.put("waybillNo", fee.getExpressnum());
					FeesReceiveDispatchEntity feesReceiveDispatchEntity = feeInDistributionRepository.queryOne(condition);
					if(null != feesReceiveDispatchEntity) {
						double deliveryCost = (null == feesReceiveDispatchEntity.getAmount())? 0 : feesReceiveDispatchEntity.getAmount();
						//免运费
						totalMoney -= deliveryCost;
						feeAmount -= deliveryCost;
					}
				}
				
				FeesReceiveStorageEntity storageEntity = new FeesReceiveStorageEntity();
				storageEntity.setBillNo(fee.getBillNo());
				storageEntity.setFeesNo(fee.getFeeNo());
				storageEntity.setWaybillNo(fee.getExpressnum());
				storageEntity.setOrderNo(fee.getOrderNo());
				storageEntity.setCustomerId(fee.getCustomerId());
				storageEntity.setCustomerName(fee.getCustomerName());
				storageEntity.setWarehouseCode(fee.getWarehouseId());
				storageEntity.setWarehouseName(fee.getWarehouseName());
				storageEntity.setCostType("FEE_TYPE_ABNORMAL");
				storageEntity.setSubjectCode("wh_abnormal_pay");
				storageEntity.setOtherSubjectCode("wh_abnormal_pay");
				storageEntity.setCost(new BigDecimal(feeAmount));// 金额
				storageEntity.setDerateAmount(0.0);// 减免金额
				storageEntity.setStatus("1");
				storageEntity.setIsCalculated("1");
				storageEntity.setCreator(fee.getCreatePersonName());
				storageEntity.setCreateTime(fee.getCreateTime());
				storageEntity.setOperateTime(fee.getCreateTime());
				storageEntity.setCalculateTime(fee.getCreateTime());
				storageEntity.setLastModifier(operatorName);
				storageEntity.setLastModifyTime(operatorTime);
				storageEntity.setDelFlag(ConstantInterface.DelFlag.NO);
				
				feesReceiveStorageList.add(storageEntity);
			}
		}
		
		if (null != feesReceiveStorageList && feesReceiveStorageList.size() > 0) {
			feesReceiveStorageRepository.insertBatch(feesReceiveStorageList);
		}
		if (null != feesReceiveDispatchList && feesReceiveDispatchList.size() > 0) {
			int updDispatchNum = feeInDistributionRepository.insertBatchTmp(feesReceiveDispatchList);
			if (updDispatchNum != feesReceiveDispatchList.size()) {
				throw new BizException("理赔费用添加到宅配费用表失败!");
			}
		}
		
		return totalMoney;
	}

    /**
     * 查询商家最后生成账单的时间
     */
	@Override
	public BmsBillInfoEntity queryLastBillTime(Map<String, Object> condition) {
		return bmsBillInfoRepository.queryLastBillTime(condition);
	}

	/**
	 * 删除账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResponseVo deleteReceiveBill(BmsBillInfoEntity entity) {
		// 从费用表删除理赔费用
		feesReceiveStorageRepository.deleteFeesBillAbnormal(entity);
		feeInDistributionRepository.deleteFeesBillAbnormal(entity);
		//更新费用
		feesReceiveDeliverDao.deleteFeesBill(entity);
		feesReceiveStorageRepository.deleteFeesBill(entity);
		feeInDistributionRepository.deleteFeesBill(entity);
		feesAbnormalRepository.deleteFeesBill(entity);
		//删除账单科目
		billSubjectInfoRepository.deleteFeesBill(entity);
		//删除开票信息
		bmsBillInvoceInfoRepository.deleteFeesBill(entity);
		//删除账单
		int k= bmsBillInfoRepository.deleteFeesBill(entity);
		if(k <= 0){
			throw new RuntimeException();
		}
		
		// 删除已生成的账单文件(未生成文件的不做处理)
		Map<String, Object> delCondition = new HashMap<String, Object>();
		delCondition.put("billNo", entity.getBillNo());
		List<FileExportTaskEntity> fileTaskList = fileExportTaskRepository.query(delCondition);
		if (null != fileTaskList && fileTaskList.size() > 0) {
			FileExportTaskEntity delFileTaskEntity = fileTaskList.get(0);
			// 删除文件
			File file = new File(delFileTaskEntity.getFilePath());
			if (file.exists()) {
				file.delete();
			}
			
			// 删除下载任务
			FileExportTaskEntity delEntity = new FileExportTaskEntity();
			delEntity.setTaskId(delFileTaskEntity.getTaskId());
			delEntity.setDelFlag("1");
			delEntity.setLastModifier(JAppContext.currentUserName());
			delEntity.setLastModifyTime(JAppContext.currentTimestamp());
			fileExportTaskRepository.update(delEntity);
		}
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("删除账单");
		logEntity.setCreator(JAppContext.currentUserName());
		logEntity.setCreateTime(JAppContext.currentTimestamp());
		billLogRecordService.log(logEntity);
				
		return null;
	}

	/**
	 * 确认账单
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResponseVo confirmFeesBill(BmsBillInfoEntity entity) {
		ResponseVo responseVo = null;
		//查询是否存在已更新的账单科目
		if (StringUtils.isEmpty(entity.getBillNo())) {
			return new ResponseVo(ResponseVo.FAIL, MessageConstant.PAGE_PARAM_ERROR_MSG);
		}
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("billNo", entity.getBillNo());
		List<BmsBillSubjectInfoEntity> subjectList = billSubjectInfoRepository.queryBillSubjectStatus(parameter);
		if (null != subjectList && subjectList.size() > 0) {
			for (BmsBillSubjectInfoEntity subjectEntity : subjectList) {
				if (BillFeesSubjectStatusEnum.UPDATE.getCode().equals(subjectEntity.getStatus())){
					return new ResponseVo(ResponseVo.FAIL, BillConstant.BILL_SUBJECT_UPDATED_MSG);
				}
			}
		}
		
		//根据账单编号查询账单信息
		BmsBillInfoEntity billEntity = bmsBillInfoRepository.findById(entity.getId());
		if (null == billEntity) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_NOTFOUND_MSG);
		}
		
		String operator = JAppContext.currentUserName();
		Timestamp operaterTime = JAppContext.currentTimestamp();
		//更新账单状态
		BmsBillInfoEntity updateEntity = new BmsBillInfoEntity();
		updateEntity.setId(entity.getId());
		updateEntity.setDiscountAmount(entity.getDiscountAmount());//账单折扣金额
		updateEntity.setReceiveAmount(entity.getReceiveAmount());//应收金额
		//updateEntity.setReceiptAmount(ArithUtil.sub(billEntity.getReceiptAmount()==null?0d:billEntity.getReceiptAmount(), entity.getDiscountAmount()==null?0d:entity.getDiscountAmount()));//实收
		updateEntity.setApprover(entity.getApprover());
		updateEntity.setDiscountType(entity.getDiscountType());
		updateEntity.setDiscountRate(entity.getDiscountRate());
		updateEntity.setApprovalWay(entity.getApprovalWay());
		updateEntity.setApprovalNo(entity.getApprovalNo());
		updateEntity.setApprovalTime(entity.getApprovalTime());
		updateEntity.setVersion(entity.getVersion());
		updateEntity.setLastModifier(operator);
		updateEntity.setLastModifyTime(operaterTime);
		updateEntity.setStatus(BillStatusEnum.CONFIRMED.getCode());//已确认
		int updateNum = bmsBillInfoRepository.update(updateEntity);
		if (updateNum <= 0) {
			responseVo = new ResponseVo(ResponseVo.FAIL, MessageConstant.OPERATOR_FAIL_MSG);
		}
		//更新账单科目状态
		BmsBillSubjectInfoEntity subjectEntity = new BmsBillSubjectInfoEntity();
		subjectEntity.setBillNo(entity.getBillNo());
		subjectEntity.setStatus(BillFeesSubjectStatusEnum.CONFIRM.getCode());
		billSubjectInfoRepository.update(subjectEntity);
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("确认账单");
		logEntity.setApprover(entity.getApprover());
		logEntity.setApprovalWay(entity.getApprovalWay());
		logEntity.setApprovalTime(entity.getApprovalTime());
		logEntity.setCreator(operator);
		logEntity.setCreateTime(operaterTime);
		logEntity.setRemark(entity.getRemark());
		billLogRecordService.log(logEntity);
		
		return responseVo;
	}

	/**
	 * 结账
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public ResponseVo sellteBill(BmsBillInfoEntity entity) {
		BmsBillInfoEntity billEntity = bmsBillInfoRepository.queryEntityByBillNo(entity.getBillNo());
		if (null == billEntity) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_NOTFOUND_MSG);
		}
		if (!BillStatusEnum.RECEIPTED.getCode().equals(billEntity.getStatus())) {
			return new ResponseVo(ResponseVo.FAIL, BillConstant.BMS_BILL_SETTLE_FAIL_MSG);
		}
		
		String operator = JAppContext.currentUserName();
		Timestamp operaterTime = JAppContext.currentTimestamp();
		BmsBillInfoEntity updateEntity = new BmsBillInfoEntity();
		updateEntity.setId(entity.getId());
		updateEntity.setStatus(BillStatusEnum.SETTLED.getCode());
		updateEntity.setVersion(entity.getVersion());
		updateEntity.setLastModifier(operator);
		updateEntity.setLastModifyTime(operaterTime);
		int updateNum = bmsBillInfoRepository.update(updateEntity);
		if (updateNum <= 0) {
			throw new RuntimeException();
		}
		
		//add log
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(entity.getBillNo());
		logEntity.setBillName(entity.getBillName());
		logEntity.setOperate("结账");
		logEntity.setCreator(operator);
		logEntity.setCreateTime(operaterTime);
		billLogRecordService.log(logEntity);
				
		return null;
	}

	@Override
	public List<BmsBillInfoEntity> queryBmsBill(Map<String, Object> condition) {
		return bmsBillInfoRepository.queryBmsBill(condition);
	}

	@Override
	public PageInfo<BmsBillCountEntityVo> countBill(
			Map<String, Object> condition, int pageNo, int pageSize) {
		return bmsBillInfoRepository.countBill(condition, pageNo, pageSize);
	}

	@Override
	public BmsBillCustomerCountEntityVo queryCustomerVo(Map<String, Object> condition) {
		return bmsBillInfoRepository.queryCustomerVo(condition);
	}
	
	
}
