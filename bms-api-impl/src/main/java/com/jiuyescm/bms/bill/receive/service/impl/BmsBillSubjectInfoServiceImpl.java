/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.repository.ISystemCodeRepository;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.file.entity.FileExportTaskEntity;
import com.jiuyescm.bms.base.file.repository.IFileExportTaskRepository;
//import com.jiuyescm.bms.base.group.service.IBmsGroupSubjectService;
import com.jiuyescm.bms.bill.receive.entity.BmsBillInfoEntity;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInfoRepository;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillSubjectInfoRepository;
import com.jiuyescm.bms.bill.receive.service.IBmsBillSubjectInfoService;
import com.jiuyescm.bms.biz.storage.repository.IBizOutstockPackmaterialRepository;
import com.jiuyescm.bms.common.constants.FileConstant;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectEnum;
import com.jiuyescm.bms.common.enumtype.BizState;
import com.jiuyescm.bms.common.enumtype.FileTaskStateEnum;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalRepository;
import com.jiuyescm.bms.fees.bill.repository.IFeesBillRepository;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.bms.fees.storage.vo.FeesReceiveMaterial;
import com.jiuyescm.bms.quotation.contract.entity.PriceContractInfoEntity;
import com.jiuyescm.bms.quotation.contract.repository.imp.IPriceContractDao;
import com.jiuyescm.bs.util.ExportUtil;
import com.jiuyescm.common.utils.excel.POISXSSUtil;
import com.jiuyescm.exception.BizException;
/*import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;*/

/**
 * 
 * @author stevenl
 * 
 */
@Service("bmsBillSubjectInfoService")
public class BmsBillSubjectInfoServiceImpl implements IBmsBillSubjectInfoService {

	private static final Logger logger = Logger.getLogger(BmsBillSubjectInfoServiceImpl.class.getName());
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	@Autowired
    private IBmsBillSubjectInfoRepository bmsBillSubjectInfoRepository;
	@Autowired 
	private IFeesAbnormalRepository feesAbnormalRepository;
	@Autowired
    private IBmsBillInfoRepository bmsBillInfoRepository;
	@Autowired
	private IFeesReceiveDispatchRepository feeInDistributionRepository;
	
	@Autowired
    private ISystemCodeRepository systemCodeRepository;
	
	/*@Autowired 
	private IWarehouseService warehouseService;*/
	
	@Resource
	private IPriceContractDao priceContractDao;
	
	@Resource
    private IFeesBillRepository feesBillRepository;
	
	@Autowired
	private IBizOutstockPackmaterialRepository repository;
	
	@Resource
	private IFeesReceiveDeliverDao feesReceiveDeliverDao;
	
	@Autowired
    private IFileExportTaskRepository fileExportTaskRepository;
	
	@Autowired
	private IBizOutstockPackmaterialRepository  bizOutPackRepository;
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;	
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
		
	/*@Resource
	private IBmsGroupSubjectService bmsGroupSubjectService;*/
	//费用类型 Map
	private Map<String,String> mapSystemcode;
	private Map<String,String> mapStatus;
	private Map<String,String> mapWarehouse;
	private Map<String,String> costSubjectMap;
	private Map<String,String> tempretureMap;
	private Map<String,String> transportProductTypeMap;
	private Map<String,String> carrierIdMap;
	private Map<String,String> chargeTypeMap;
	
    @Override
    public PageInfo<BmsBillSubjectInfoEntity> query(Map<String, Object> condition,
            int pageNo, int pageSize) {
        return bmsBillSubjectInfoRepository.query(condition, pageNo, pageSize);
    }

    @Override
    public BmsBillSubjectInfoEntity findById(Long id) {
        return bmsBillSubjectInfoRepository.findById(id);
    }

    @Override
    public BmsBillSubjectInfoEntity save(BmsBillSubjectInfoEntity entity) {
        return bmsBillSubjectInfoRepository.save(entity);
    }

    @Override
    public BmsBillSubjectInfoEntity update(BmsBillSubjectInfoEntity entity) {
        return bmsBillSubjectInfoRepository.update(entity);
    }

    @Override
    public void delete(Long id) {
        bmsBillSubjectInfoRepository.delete(id);
    }
    /**
     * 
     */

	@Override
	public void discountBill(BmsBillSubjectInfoEntity bill) throws Exception {
		discountStorageBill(bill);
	}
	
   @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void discountStorageBill(BmsBillSubjectInfoEntity bill){
		bmsBillSubjectInfoRepository.updateDiscountStorageBill(bill);
		bmsBillInfoRepository.refeshAmount(bill);
	}

	@Override
	public List<BmsBillSubjectInfoEntity> queryAllByBillNoAndwarehouse(
			Map<String, Object> parameter) {
		return bmsBillSubjectInfoRepository.queryAllByBillNoAndwarehouse(parameter);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo,
			String feesType) {
		
		return bmsBillSubjectInfoRepository.querywarehouseAmount(billNo,feesType);
	}

	@Override
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo) {
		return bmsBillSubjectInfoRepository.querywarehouseAmount(billNo);
	}

	@Override
	public PageInfo<FeesAbnormalEntity> queryAbnormalDetailGroupPage(
			Map<String, Object> parameter, int pageNo, int pageSize) {
		return feesAbnormalRepository.queryAbnormalDetailPage(parameter,pageNo,pageSize);
	}

	/**
	 * 清空账单 科目
	 * 1.清空 某仓库 某个仓储科目 账单金额
	 * 2.更新总账单 费用 
	 * 3.清空费用里面账单编号 条件 仓库ID+费用科目+账单编号
	 */
	@Override
	public void deleteStorageBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		billInfoEntity.setLastModifier(bill.getLastModifier());
		billInfoEntity.setLastModifyTime(bill.getLastModifyTime());
		if(billInfoEntity==null){
			throw new Exception(String.format("账单【{0}】不存在", bill.getBillNo()));
		}
		deleteStorageBill(bill,billInfoEntity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void deleteStorageBill(BmsBillSubjectInfoEntity bill,BmsBillInfoEntity billInfoEntity){
			bmsBillSubjectInfoRepository.deleteStorageBill(bill);
			bmsBillInfoRepository.refeshAmount(bill);
			feesReceiveStorageRepository.deleteStorageBill(bill.getBillNo(),bill.getWarehouseCode(),bill.getSubjectCode());
	}

	/**
	 * 重新生成已删除 科目明细账单
	 * 1.根据账单条件 查询要生成账单的数据
	 * 2.更新费用数据账单编号，已过账
	 * 3.更新账单明细
	 * 4.更新账单主表 费用以及版本号
	 * @throws Exception 
	 */
	@Override
	public void reCountStorageDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		if(billInfoEntity==null){
			throw new Exception("账单不存在");
		}
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", "");
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("subjectCode", bill.getSubjectCode());
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());
		//统计费用
		BmsBillSubjectInfoEntity subjectInfoEntity=feesReceiveStorageRepository.sumSubjectMoney(param);		
		if(subjectInfoEntity!=null){
			Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
			bill.setTotalAmount(totalMoney);
			bill.setDerateAmount(derateAmount);
			bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
			bill.setNum(subjectInfoEntity.getNum());
		}	
		reCountStorageDeleteBill(bill,billInfoEntity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void reCountStorageDeleteBill(BmsBillSubjectInfoEntity bill,BmsBillInfoEntity billInfoEntity){
		bmsBillSubjectInfoRepository.update(bill);
		bmsBillInfoRepository.refeshAmount(bill);
		
		Map<String, Object> param=new HashMap<String, Object>();		
		param.put("billNo", bill.getBillNo());
		param.put("status", "1");
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("subjectCode", bill.getSubjectCode());
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());
		
		feesReceiveStorageRepository.updateFeeByParam(param);
	}

	/**
	 * 重新生成已剔除 的账单 或者更新减免金额的账单
	 * 重新计算 总金额 和 减免金额
	 * 更新账单主表 总金额 和减免金额 
	 * @throws Exception 
	 */
	@Override
	public void reCountStorageUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", bill.getBillNo());
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("subjectCode", bill.getSubjectCode());
		//统计费用
		BmsBillSubjectInfoEntity subjectInfoEntity=feesReceiveStorageRepository.sumSubjectMoney(param);		
		if(subjectInfoEntity!=null){
			Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
			bill.setTotalAmount(totalMoney);
			bill.setDerateAmount(derateAmount);
			bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
			bill.setNum(subjectInfoEntity.getNum());
		}
		
		reCountUpdateBill(bill);
	}

	/**
	 * 清空账单 科目
	 * 1.清空 配送 账单金额
	 * 2.更新总账单 费用 
	 * 3.清空费用里面账单编号 条件 仓库ID+物流商ID+账单编号
	 * @throws Exception 
	 */
	@Override
	public void deleteTransportBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		billInfoEntity.setLastModifier(bill.getLastModifier());
		billInfoEntity.setLastModifyTime(bill.getLastModifyTime());
		if(billInfoEntity==null){
			throw new Exception(String.format("账单【{0}】不存在", bill.getBillNo()));
		}
		deleteTransportBill(bill,billInfoEntity);
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void deleteTransportBill(BmsBillSubjectInfoEntity bill,
			BmsBillInfoEntity billInfoEntity) {
		bmsBillSubjectInfoRepository.deleteStorageBill(bill);
		bmsBillInfoRepository.refeshAmount(bill);
		feesReceiveDeliverDao.deleteTransportBill(bill.getBillNo());
	}

	/**
	 * 重新生成已删除 科目明细账单
	 * 1.根据账单条件 查询要生成账单的数据
	 * 2.更新费用数据账单编号，已过账
	 * 3.更新账单明细
	 * 4.更新账单主表 费用以及版本号
	 * @throws Exception 
	 */
	@Override
	public void reCountTransportDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		if(billInfoEntity==null){
			throw new Exception("账单不存在");
		}
	
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", "");
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());

		BmsBillSubjectInfoEntity subjectInfoEntity=null;
		if("Abnormal_Transport".equals(bill.getSubjectCode())){
			subjectInfoEntity=feesReceiveDeliverDao.sumAbnormalSubjectMoney(param);	
		}else{
			subjectInfoEntity=feesReceiveDeliverDao.sumSubjectMoney(param);	
		}		
		if(subjectInfoEntity!=null){
			Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
			bill.setTotalAmount(totalMoney);
			bill.setDerateAmount(derateAmount);
			bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
			bill.setNum(subjectInfoEntity.getNum());
		}	
		
		reCountTransportDeleteBill(bill,billInfoEntity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void reCountTransportDeleteBill(BmsBillSubjectInfoEntity bill,BmsBillInfoEntity billInfoEntity){
		bmsBillSubjectInfoRepository.update(bill);
		bmsBillInfoRepository.refeshAmount(bill);
		Map<String, Object> param=new HashMap<String, Object>();		
		param.put("billNo", bill.getBillNo());
		param.put("status", "1");
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());
		feesReceiveDeliverDao.updateFeeByParam(param);
	}
	
	/**
	 * 重新生成已剔除 的账单 或者更新减免金额的账单
	 * 重新计算 总金额 和 减免金额
	 * 更新账单主表 总金额 和减免金额 
	 * @throws Exception 
	 */
	@Override
	public void reCountTransportUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception {
		
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		if(billInfoEntity==null){
			throw new Exception("账单不存在");
		}
		
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", bill.getBillNo());
		BmsBillSubjectInfoEntity subjectInfoEntity=null;
		if("Abnormal_Transport".equals(bill.getSubjectCode())){
			subjectInfoEntity=feesReceiveDeliverDao.sumAbnormalSubjectMoney(param);	
		}else{
			subjectInfoEntity=feesReceiveDeliverDao.sumSubjectMoney(param);	
		}
		//统计费用
		//BmsBillSubjectInfoEntity subjectInfoEntity=feesReceiveDeliverDao.sumSubjectMoney(param);		
		if(subjectInfoEntity!=null){
			Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
			bill.setTotalAmount(totalMoney);
			bill.setDerateAmount(derateAmount);
			bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
			bill.setNum(subjectInfoEntity.getNum());
		}
		
		reCountUpdateTransportBill(bill);
		
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void reCountUpdateTransportBill(BmsBillSubjectInfoEntity bill) {
		bmsBillSubjectInfoRepository.update(bill);
		bmsBillInfoRepository.refeshAmount(bill);
	}
	
	
	/**
	 * 清空账单 科目
	 * 1.清空 配送 账单金额
	 * 2.更新总账单 费用 
	 * 3.清空费用里面账单编号 条件 仓库ID+物流商ID+账单编号
	 * @throws Exception 
	 */
	@Override
	public void deleteDispatchBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		billInfoEntity.setLastModifier(bill.getLastModifier());
		billInfoEntity.setLastModifyTime(bill.getLastModifyTime());
		if(billInfoEntity==null){
			throw new Exception(String.format("账单【{0}】不存在", bill.getBillNo()));
		}
		deleteDispatchBill(bill,billInfoEntity);
		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void deleteDispatchBill(BmsBillSubjectInfoEntity bill,
			BmsBillInfoEntity billInfoEntity) {
		bmsBillSubjectInfoRepository.deleteStorageBill(bill);
		bmsBillInfoRepository.refeshAmount(bill);
		feeInDistributionRepository.deleteDispatchBill(bill.getBillNo(),bill.getWarehouseCode(),bill.getSubjectCode());
	}

	@Override
	public void reCountDispatchDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		if(billInfoEntity==null){
			throw new Exception("账单不存在");
		}
	
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", "");
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("subjectCode", bill.getSubjectCode());
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());

		//统计费用
		BmsBillSubjectInfoEntity subjectInfoEntity=feeInDistributionRepository.sumSubjectMoney(param);		
		if(subjectInfoEntity!=null){
			Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
			bill.setTotalAmount(totalMoney);
			bill.setDerateAmount(derateAmount);
			bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
			bill.setNum(subjectInfoEntity.getNum());
		}	
		
		reCountDispatchDeleteBill(bill,billInfoEntity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void reCountDispatchDeleteBill(BmsBillSubjectInfoEntity bill,BmsBillInfoEntity billInfoEntity){
		bmsBillSubjectInfoRepository.update(bill);
		bmsBillInfoRepository.refeshAmount(bill);
		Map<String, Object> param=new HashMap<String, Object>();		
		param.put("billNo", bill.getBillNo());
		param.put("status", "1");
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("subjectCode", bill.getSubjectCode());
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());
		feeInDistributionRepository.updateFeeByParam(param);
	}
	
	@Override
	public void reCountDispatchUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception {
		
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", bill.getBillNo());
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("subjectCode", bill.getSubjectCode());
		//统计费用
		BmsBillSubjectInfoEntity subjectInfoEntity=feeInDistributionRepository.sumSubjectMoney(param);		
		if(subjectInfoEntity!=null){
			Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
			bill.setTotalAmount(totalMoney);
			bill.setDerateAmount(derateAmount);
			bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
			bill.setNum(subjectInfoEntity.getNum());
		}
		
		reCountUpdateBill(bill);
		
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void reCountUpdateBill(BmsBillSubjectInfoEntity bill) {
		bmsBillSubjectInfoRepository.update(bill);
		bmsBillInfoRepository.refeshAmount(bill);
	}

	@Override
	public void deleteAbnormalBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		billInfoEntity.setLastModifier(bill.getLastModifier());
		billInfoEntity.setLastModifyTime(bill.getLastModifyTime());
		if(billInfoEntity==null){
			throw new Exception(String.format("账单【{0}】不存在", bill.getBillNo()));
		}
		deleteAbnormalBill(bill,billInfoEntity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void deleteAbnormalBill(BmsBillSubjectInfoEntity bill,
			BmsBillInfoEntity billInfoEntity) throws Exception {
		bmsBillSubjectInfoRepository.deleteSubjectBill(bill);
		bmsBillInfoRepository.refeshAmount(bill);
		feesAbnormalRepository.deleteAbnormalBill(bill.getBillNo(),bill.getWarehouseCode(),bill.getSubjectCode());
	}

	@Override
	public void reCountAbnormalUpdateBill(BmsBillSubjectInfoEntity bill) throws Exception {	
		String subjectCode=bill.getSubjectCode();
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode){
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_Storage":
			reasonIds.add("56");
			reasonIds.add("3");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
		}
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", bill.getBillNo());
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("reasonIds", reasonIds);
		//统计费用
		BmsBillSubjectInfoEntity subjectInfoEntity=feesAbnormalRepository.sumSubjectMoney(param);		
		//Double totalMoney=getAbnormalMoney(param, subjectCode);
		Double totalMoney=subjectInfoEntity.getTotalAmount();
		if(subjectInfoEntity!=null){
			//Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();
			
			switch(subjectCode){
			case "Abnormal_Dispatch":
				bill.setTotalAmount(-totalMoney);
				bill.setDerateAmount(derateAmount);
				bill.setReceiptAmount(-totalMoney+derateAmount+bill.getDiscountAmount());		
				break;
			case "Abnormal_Storage":
				bill.setTotalAmount(totalMoney);
				bill.setDerateAmount(derateAmount);
				bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
				break;
			case "Abnormal_DisChange":
				bill.setTotalAmount(totalMoney);
				bill.setDerateAmount(derateAmount);
				bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());
			}

			bill.setNum(subjectInfoEntity.getNum());
			
		}
		
		reCountUpdateBill(bill);
	}
	
	@Override
	public void reCountAbnormalDeleteBill(BmsBillSubjectInfoEntity bill) throws Exception {
		BmsBillInfoEntity billInfoEntity=bmsBillInfoRepository.queryEntityByBillNo(bill.getBillNo());
		if(billInfoEntity==null){
			throw new Exception("账单不存在");
		}
		String subjectCode=bill.getSubjectCode();
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode){
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_Storage":
			reasonIds.add("56");
			reasonIds.add("3");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
			break;
		}
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("billNo", "");
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("reasonIds", reasonIds);
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());
		//统计费用
		BmsBillSubjectInfoEntity subjectInfoEntity=feesAbnormalRepository.sumSubjectMoney(param);		
		//Double totalMoney=getAbnormalMoney(param, subjectCode);
		Double totalMoney=subjectInfoEntity.getTotalAmount();
		if(subjectInfoEntity!=null){
			//Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
			Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();
			switch(subjectCode){
			case "Abnormal_Dispatch":
				bill.setTotalAmount(-totalMoney);
				bill.setDerateAmount(derateAmount);
				bill.setReceiptAmount(-totalMoney+derateAmount+bill.getDiscountAmount());	
				break;
			case "Abnormal_Storage":
				bill.setTotalAmount(totalMoney);
				bill.setDerateAmount(derateAmount);
				bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());	
				break;
			case "Abnormal_DisChange":
				bill.setTotalAmount(totalMoney);
				bill.setDerateAmount(derateAmount);
				bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());	
				break;
			}
			
			
			bill.setNum(subjectInfoEntity.getNum());
		}
		reCountAbnormalDeleteBill(bill,billInfoEntity);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	private void reCountAbnormalDeleteBill(BmsBillSubjectInfoEntity bill,BmsBillInfoEntity billInfoEntity){
		bmsBillSubjectInfoRepository.update(bill);
		bmsBillInfoRepository.refeshAmount(bill);
			
		String subjectCode=bill.getSubjectCode();
		List<String> reasonIds=new ArrayList<String>();
		switch(subjectCode){
		case "Abnormal_Dispatch":
			reasonIds.add("2");
			break;
		case "Abnormal_Storage":
			reasonIds.add("56");
			reasonIds.add("3");
			break;
		case "Abnormal_DisChange":
			reasonIds.add("4");
			break;
		}
		Map<String, Object> param=new HashMap<String, Object>();		
		param.put("billNo", bill.getBillNo());
		param.put("status", "1");
		param.put("warehouseCode", bill.getWarehouseCode());
		param.put("reasonIds", reasonIds);
		param.put("customerId", billInfoEntity.getCustomerId());
		param.put("startTime",billInfoEntity.getStartTime());
		param.put("endTime", billInfoEntity.getEndTime());	
		feesAbnormalRepository.updateFeeByParam(param);
	}

	@Override
	public int updateBillSubject(
			Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return bmsBillSubjectInfoRepository.updateBillSubject(condition);
	}

	@Override
	public List<String> queryBillWarehouse(Map<String, Object> param) {
		return bizOutPackRepository.queryBillWarehouse(param);
	}
	
	@SuppressWarnings("deprecation")
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeRepository.getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_BILL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_RECEIVE_BILL");
		}
		return systemCodeEntity.getExtattr1();
	}

	@Override
	public void export(BmsBillInfoEntity paramer, String taskId)throws Exception {
     
		    String billno = paramer.getBillNo();
			
			updateExportTask(taskId, FileTaskStateEnum.INPROCESS.getCode(), 10);
			
			String billName=paramer.getBillName();
			String path = getPath();
			long beginTime = System.currentTimeMillis();
			logger.info("====应收账单导出："+billName + "["+billno+"]");
			
			//初始化参数
			init();
			
	    	File storeFolder=new File(path);
			//如果存放上传文件的目录不存在就新建
			if(!storeFolder.isDirectory()){
				storeFolder.mkdirs();
			}
			
			POISXSSUtil poiUtil = new POISXSSUtil();
	    	SXSSFWorkbook workbook = poiUtil.getXSSFWorkbook();
	    	String filePath = path+ FileConstant.SEPARATOR + 
	    			FileConstant.REC_BILL_PREFIX + billno + FileConstant.SUFFIX_XLSX;
	    	
	    	logger.info("====应收账单导出：" + "["+billno+"]" + "写入Excel begin.");
	    	
	    	Map<String, Object> parameter = new HashMap<String,Object>();
	    	
	    	parameter.put("billNo", billno);
	    	parameter.put("creator",paramer.getCreator()==null?"":paramer.getCreator());
	    	logger.info("====生成账单明细sheet");
	    	List<String>  warehouseList = queryBillWarehouse(parameter);
	    	//首页
	    	createFristPage(workbook,parameter,poiUtil,warehouseList);
	    	logger.info("====首页");
	    	//配送明细费用
	    	handDispatch(poiUtil, workbook, filePath, billno);
	    	logger.info("====配送明细费用");
	    	//仓储
	    	logger.info("====仓储");
	    	createStorage(workbook,parameter,poiUtil,warehouseList);
	    	//耗材
	    	createMaterial(workbook,parameter,poiUtil,warehouseList);
	    	logger.info("====耗材");
	    	//干线
	    	handTransport(poiUtil, workbook,path, billno);
	    	
	    	//理赔
	    	handAbnormal(poiUtil,workbook, path,billno);
	    	//改地址和退件费
	    	handAbnormalChange(poiUtil,workbook, path,billno);
	    	//增值
	    	handAdd(poiUtil, workbook,path,parameter);
	    	//最后写到文件
	    	poiUtil.write2FilePath(workbook, filePath);
	    	
	    	updateExportTask(taskId, FileTaskStateEnum.SUCCESS.getCode(), 100);
	    	logger.info("====应收账单导出：" + "["+billno+"]" + "写入Excel end.==总耗时：" + (System.currentTimeMillis() - beginTime));
	    	
	}
	
	private void handAbnormalChange(POISXSSUtil poiUtil,
			SXSSFWorkbook workbook, String path, String billno) {
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		int pageSize = 20000;
		
		List<FeesAbnormalEntity>  dataList = new ArrayList<FeesAbnormalEntity>();
		Map<String,Object>  param = new  HashMap<String,Object>();
		param.put("billno", billno);
		
		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = 
					feesAbnormalRepository.queryAbnormalChangeByBillNo(param, pageNo, pageSize);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				
				dataList.addAll(abnormalList.getList());
			}else {
				doLoop = false;
			}
		}
		
		if(dataList.size()==0){
			return;
		}
		
		 Sheet sheet = workbook.createSheet("改地址和退件费");
		 
		 Font font = workbook.createFont();
	     font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
			 
		 Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellStyle(style);
		 cell0.setCellValue("发货仓库");
		 Cell cell01 = row0.createCell(1);
		 cell01.setCellStyle(style);
		 cell01.setCellValue("运单日期");
	     Cell cell1 = row0.createCell(2);
	     cell1.setCellValue("运单号");
	     cell1.setCellStyle(style);
	     Cell cell2 = row0.createCell(3);
	     cell2.setCellValue("客户");
	     cell2.setCellStyle(style);
		 Cell cell3 = row0.createCell(4);
		 cell3.setCellValue("赔付类型");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(5);
		 cell4.setCellValue("赔款额");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(6);
		 cell5.setCellValue("运费");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(7);
		 cell6.setCellValue("是否免运费");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(8);
		 cell7.setCellValue("登记人");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(9);
		 cell8.setCellValue("备注");
		 cell8.setCellStyle(style);
		 
		 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 int rowIndex = 1;
		 for(int i=0;i<dataList.size();i++)
		 {
			 FeesAbnormalEntity entity = dataList.get(i);
			 
             Row row = sheet.createRow(rowIndex);
             rowIndex++;
			 
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getWarehouseName()==null?"":entity.getWarehouseName());
		    Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getOperateTime()==null?"":sdf.format(entity.getOperateTime()));
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getExpressnum()==null?"":entity.getExpressnum());
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getCustomerName()==null?"":entity.getCustomerName());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getReason()==null?"":entity.getReason());
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(entity.getPayMoney()==null?0:entity.getPayMoney());
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(entity.getDeliveryCost()==null?0:entity.getDeliveryCost());
			//0-不免运费 1-免运费
			if(null!=entity.getIsDeliveryFree()){
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(entity.getIsDeliveryFree());
			}
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getCreatePersonName()==null?"":entity.getCreatePersonName());
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getRemark()==null?"":entity.getRemark());
		 }
		
	}

	private Map<String,String> getwarehouse(){
//		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		Map<String, String> map = new LinkedHashMap<String,String>();
//		for (WarehouseVo warehouseVo : warehouseVos) {
//			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
//		}
		return map;
	}
	private Map<String,String> getStatus(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("0", "未过账");
		map.put("1", "已过账");
		return map;
	}
	private Map<String,String> getSystemcode(){
		List<SystemCodeEntity> codeList=systemCodeRepository.queryCodeList(new HashMap<String,Object>());
		Map<String, String> map =new LinkedHashMap<String,String>();
		for (SystemCodeEntity systemCodeEntity : codeList) {
			map.put(systemCodeEntity.getCode(),systemCodeEntity.getCodeName());
		}
		return map;
	}
	private Map<String, String> getCostSubjectMap(){
		Map<String, String> costSubjectMap = new HashMap<String, String>();
		/*List<SystemCodeEntity> systemCodeList1 = systemCodeRepository.findEnumList("STORAGE_PRICE_SUBJECT");
		List<SystemCodeEntity> systemCodeList2 = systemCodeRepository.findEnumList("wh_value_add_subject");
		if(systemCodeList1!=null && systemCodeList1.size()>0){
			for(int i=0;i<systemCodeList1.size();i++){
				costSubjectMap.put(systemCodeList1.get(i).getCode(), systemCodeList1.get(i).getCodeName());
			}
		}
		if(systemCodeList2!=null && systemCodeList2.size()>0){
			for(int i=0;i<systemCodeList2.size();i++){
				costSubjectMap.put(systemCodeList2.get(i).getCode(), systemCodeList2.get(i).getCodeName());
			}
		}*/
		
		//costSubjectMap=bmsGroupSubjectService.getExportSubject("receive_bill_storage_subject");
		return costSubjectMap;
	}
	//温度类型
	private Map<String, String> getTemperatureMap(){
		List<SystemCodeEntity> systemCodeList3 = systemCodeRepository.findEnumList("TEMPERATURE_TYPE");
		Map<String, String> tempretureMap =new HashMap<String,String>();
		if(systemCodeList3!=null && systemCodeList3.size()>0){
			for(int i=0;i<systemCodeList3.size();i++){
				tempretureMap.put(systemCodeList3.get(i).getCode(), systemCodeList3.get(i).getCodeName());
			}
		}
		return tempretureMap;
	}
	
	/**
	 * 运输产品类型TRANSPORT_PRODUCT_TYPE： 城际专列、同城专列、电商专列、航线达 ； 
	 */
	private Map<String, String> getTransportProductTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeRepository.findEnumList("TRANSPORT_PRODUCT_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	/**
	 * 获取配送物流商
	 */
	private Map<String, String> getCarrierIdList(){
		List<SystemCodeEntity> systemCodeList = systemCodeRepository.findEnumList("DISPATCH_COMPANY");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				if (null != systemCodeList.get(i).getExtattr1()) {
					map.put(systemCodeList.get(i).getExtattr1(), systemCodeList.get(i).getCodeName());
				}
			}
		}
		return map;
	}
	
	private void init(){
		// mapSystemcode=getSystemcode();
		// mapStatus=getStatus();
		mapWarehouse=getwarehouse();
		costSubjectMap=getCostSubjectMap();
		tempretureMap=getTemperatureMap();
		transportProductTypeMap=getTransportProductTypeList();
		carrierIdMap=getCarrierIdList();
		chargeTypeMap = getChargeTypeMap();
	}
	
  void createFristPage(SXSSFWorkbook workbook,Map<String, Object> parameter,POISXSSUtil poiUtil,List<String>  warehouseList){
		
		//账单信息
    	Sheet sheet = poiUtil.getXSSFSheet(workbook,"首页");
    	
    	sheet.setColumnWidth(1, 3400);
    	sheet.setColumnWidth(2, 3700);
    	sheet.setColumnWidth(3, 5200);
    	
    	sheet.setColumnWidth(4, 3700);
    	sheet.setColumnWidth(5, 3700);
    	sheet.setColumnWidth(6, 3700);
    	sheet.setColumnWidth(7, 3700);
    	sheet.setColumnWidth(8, 3700);
    	sheet.setColumnWidth(9, 3700);
		
		PageInfo<BmsBillInfoEntity> pageInfo1 = bmsBillInfoRepository.query(parameter, 1, Integer.MAX_VALUE);
		
	    BmsBillInfoEntity bill = pageInfo1.getList().get(0);
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
	    
	    String startTime = "";
	    String endTime = "";
	    
	    if(null!=bill.getStartTime()){
	    	 startTime = sdf.format(bill.getStartTime());
	    }
	    
	    if(null!=bill.getEndTime()){
	    	endTime = sdf.format(bill.getEndTime());
	    }
	    parameter.put("customerId",bill.getCustomerId());
	    
	    List<PriceContractInfoEntity> priceContractList = priceContractDao.queryContract(parameter);
	    String contractName="";
	    if(priceContractList.size()>0){
	    	contractName = priceContractList.get(0).getPaperContractNo();
	    }
	    
	    Font font = workbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
				 
		 CellStyle style0 = workbook.createCellStyle();
	     style0.setBorderTop(CellStyle.BORDER_DOTTED);
	     style0.setTopBorderColor(HSSFColor.GOLD.index);
	     
	     CellStyle style01 = workbook.createCellStyle();
	     style01.setBorderRight(CellStyle.BORDER_DOTTED);
	     style01.setBorderTop(CellStyle.BORDER_DOTTED);
	     style01.setRightBorderColor(HSSFColor.GOLD.index);
	     style01.setTopBorderColor(HSSFColor.GOLD.index);
	     
	     CellStyle style011 = workbook.createCellStyle();
	     style011.setBorderRight(CellStyle.BORDER_DOTTED);
	     style011.setRightBorderColor(HSSFColor.GOLD.index);
	     style011.setBorderLeft(CellStyle.BORDER_DOTTED);
	     style011.setLeftBorderColor(HSSFColor.GOLD.index);
	     
	     CellStyle style012 = workbook.createCellStyle();
	     style012.setBorderLeft(CellStyle.BORDER_DOTTED);
	     style012.setLeftBorderColor(HSSFColor.GOLD.index);
	     
	     CellStyle style0122 = workbook.createCellStyle();
	     style0122.setBorderLeft(CellStyle.BORDER_DOTTED);
	     style0122.setLeftBorderColor(HSSFColor.GOLD.index);
	     style0122.setAlignment(CellStyle.ALIGN_CENTER);
	     
	     CellStyle style01222 = workbook.createCellStyle();
	     style01222.setBorderLeft(CellStyle.BORDER_DOTTED);
	     style01222.setLeftBorderColor(HSSFColor.GOLD.index);
	     style01222.setAlignment(CellStyle.ALIGN_CENTER);
	     style01222.setFont(font);
	     
	     CellStyle style013 = workbook.createCellStyle();
	     style013.setBorderRight(CellStyle.BORDER_DOTTED);
	     style013.setRightBorderColor(HSSFColor.GOLD.index);
	     style013.setAlignment(CellStyle.ALIGN_CENTER);
	     
	     CellStyle style0133 = workbook.createCellStyle();
	     style0133.setBorderRight(CellStyle.BORDER_DOTTED);
	     style0133.setRightBorderColor(HSSFColor.GOLD.index);
	     style0133.setAlignment(CellStyle.ALIGN_CENTER);
	     style0133.setFont(font);
	     
	     CellStyle style014 = workbook.createCellStyle();
	     style014.setBorderLeft(CellStyle.BORDER_DOTTED);
	     style014.setBorderBottom(CellStyle.BORDER_DOTTED);
	     style014.setLeftBorderColor(HSSFColor.GOLD.index);
	     style014.setBottomBorderColor(HSSFColor.GOLD.index);
	     style014.setAlignment(CellStyle.ALIGN_CENTER);
	     style014.setFont(font);
	     
	     CellStyle style015 = workbook.createCellStyle();
	     style015.setBorderBottom(CellStyle.BORDER_DOTTED);
	     style015.setBottomBorderColor(HSSFColor.GOLD.index);
	     
	     CellStyle style016 = workbook.createCellStyle();
	     style016.setBorderRight(CellStyle.BORDER_DOTTED);
	     style016.setBorderBottom(CellStyle.BORDER_DOTTED);
	     style016.setRightBorderColor(HSSFColor.GOLD.index);
	     style016.setBottomBorderColor(HSSFColor.GOLD.index);
	    
	     style013.setRightBorderColor(HSSFColor.GOLD.index);
	     
		 CellStyle style1 = workbook.createCellStyle();
	     style1.setBorderTop(CellStyle.BORDER_DOTTED);
	     style1.setBorderLeft(CellStyle.BORDER_DOTTED);
	     style1.setBorderRight(CellStyle.BORDER_DOTTED);
	     
	     style1.setTopBorderColor(HSSFColor.GOLD.index);
	     style1.setLeftBorderColor(HSSFColor.GOLD.index);
	     style1.setRightBorderColor(HSSFColor.GOLD.index);
	     
		
		 Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 if(StringUtils.isNotBlank(contractName)){
			 cell0.setCellValue(bill.getCustomerName()+"（"+contractName+"）");
		 }else{
			 cell0.setCellValue(bill.getCustomerName());
		 }
		 cell0.setCellStyle(style1);
		 
		 row0.createCell(1).setCellStyle(style0);
		 row0.createCell(2).setCellStyle(style0);
		 row0.createCell(3).setCellStyle(style0);
		 row0.createCell(4).setCellStyle(style0);
		 row0.createCell(5).setCellStyle(style0);
		 row0.createCell(6).setCellStyle(style0);
		 row0.createCell(7).setCellStyle(style0);
		 row0.createCell(8).setCellStyle(style0);
		 row0.createCell(9).setCellStyle(style01);
		 
		 Row row01 = sheet.createRow(1);
		 row01.createCell(0).setCellStyle(style012);
		 row01.createCell(9).setCellStyle(style013);
		 
		 Row row1 = sheet.createRow(2);
		 Cell cell1 = row1.createCell(0);
		 cell1.setCellValue("费用结算周期："+startTime+"至"+endTime);
		 cell1.setCellStyle(style012);
		 row1.createCell(9).setCellStyle(style013);
		 
		 Row row2 = sheet.createRow(3);
		 Cell cell2 = row2.createCell(0);
		 cell2.setCellStyle(style012);
		 cell2.setCellValue("久耶核算员："+parameter.get("creator").toString());
		 row2.createCell(9).setCellStyle(style013);
		 
		 sheet.addMergedRegion(new CellRangeAddress(0,1,0,9));
		 sheet.addMergedRegion(new CellRangeAddress(2,2,0,9));
		 sheet.addMergedRegion(new CellRangeAddress(3,3,0,9));
		 sheet.addMergedRegion(new CellRangeAddress(4,4,2,3));
		 
		 
		 Row row4 = sheet.createRow(4);
		
		 Cell cell40 = row4.createCell(0);
		 cell40.setCellStyle(style01222);
		 Cell cell41 = row4.createCell(1);
		 Cell cell42 = row4.createCell(2);
		 
		 Cell cell44 = row4.createCell(4);
		 Cell cell45 = row4.createCell(5);
		 Cell cell46 = row4.createCell(6);
		 Cell cell47 = row4.createCell(7);
		 Cell cell48 = row4.createCell(8);
		 Cell cell49 = row4.createCell(9);
		 cell49.setCellStyle(style0133);
		 
		 cell40.setCellValue("序号");
		 cell41.setCellValue("仓库");
		 cell41.setCellStyle(style);
		 cell42.setCellValue("费用项目");
		 cell42.setCellStyle(style);
		 cell44.setCellValue("总金额");
		 cell44.setCellStyle(style);
		 cell45.setCellValue("减免");
		 cell45.setCellStyle(style);
		 cell46.setCellValue("折扣");
		 cell46.setCellStyle(style);
		 cell47.setCellValue("应收金额");
		 cell47.setCellStyle(style);
		 cell48.setCellValue("数量");
		 cell48.setCellStyle(style);
		 cell49.setCellValue("备注");
		 
		 //干线数据
		 List<BmsBillSubjectInfoEntity> transportList = new ArrayList<BmsBillSubjectInfoEntity>();
		 
		 parameter.put("feesType", BillFeesSubjectEnum.TRANSPORT.getCode());
		 
		 PageInfo<BmsBillSubjectInfoEntity> pageInfo = query(parameter, 1, Integer.MAX_VALUE);
		 transportList = pageInfo.getList();
		 
		 //st
		 Map<String, Object> param = new HashMap<String,Object>();
		 param.put("feesType", BillFeesSubjectEnum.TRANSPORT.getCode());
		 param.put("billNo", parameter.get("billNo"));
		 
		 BmsBillSubjectInfoEntity transportSum = bmsBillSubjectInfoRepository.queryTransportSum(param);
		 //end
		 
		 Map<String,Object> warehouseMap = new HashMap<String,Object>();
		 
		 for(String warehouseCode:warehouseList)
		 {
			 parameter.remove("feesType");
			 parameter.put("warehouseCode", warehouseCode);
			
			 PageInfo<BmsBillSubjectInfoEntity> page = query(parameter, 1, Integer.MAX_VALUE);
			 List<BmsBillSubjectInfoEntity> list = page.getList();
			 List<BmsBillSubjectInfoEntity> storageList = new ArrayList<BmsBillSubjectInfoEntity>();
			 List<BmsBillSubjectInfoEntity> dispatchList = new ArrayList<BmsBillSubjectInfoEntity>();
			 List<Map<String,Object>>  mapList = new ArrayList<Map<String,Object>>();
			 
			 Map<String,Object>  storageMap = new HashMap<String,Object>();
			 Map<String,Object>  dispatchMap = new HashMap<String,Object>();
			 
			 for(BmsBillSubjectInfoEntity entity:list)
			 {
				 if("STORAGE".equals(entity.getFeesType())){
					 storageList.add(entity);
				 }else if("DISPATCH".equals(entity.getFeesType())){
					 dispatchList.add(entity);
				 }else if("TRANSPORT".equals(entity.getFeesType())){
					 transportList.add(entity);
				 }
			 }
			 storageMap.put("storage", storageList);
			 dispatchMap.put("dispatch", dispatchList);
			 
			 mapList.add(storageMap);
			 mapList.add(dispatchMap);
			
			 warehouseMap.put(warehouseCode,mapList);
		 }
		 
		 Iterator<String> iterator = warehouseMap.keySet().iterator();
		 
		 List<String> keyList = new ArrayList<String>(warehouseMap.keySet());  
		 
		 int rowIndex = 5;
		 int xulieIndex = 1;
		 
		 for(int k=0;k<keyList.size();k++)
		 {
             List<Map<String,Object>> data = (List<Map<String,Object>>)warehouseMap.get(keyList.get(k));
			 
			 List<BmsBillSubjectInfoEntity> storageList =  (List<BmsBillSubjectInfoEntity>)data.get(0).get("storage");
			 
			 List<BmsBillSubjectInfoEntity> dispatchList = (List<BmsBillSubjectInfoEntity>)data.get(1).get("dispatch");
			 
			 for(int i=0;i<storageList.size();i++)
			 {
				 Row row5 = sheet.createRow(i+rowIndex);
				
				 Cell cell = row5.createCell(0);
				 cell.setCellStyle(style0122);
				 cell.setCellValue(xulieIndex);
				 xulieIndex++;
				 if(i==0)
				 {
					 sheet.addMergedRegion(new CellRangeAddress(rowIndex,storageList.size()+dispatchList.size()+rowIndex-1,1,1));
					 row5.createCell(1).setCellValue(mapWarehouse.get(iterator.next()));//设置仓库
					 
					 row5.createCell(2).setCellValue("仓储费用");//设置存储费用
					
					 sheet.addMergedRegion(new CellRangeAddress(rowIndex,storageList.size()+rowIndex-1,2,2));
				 }
				 row5.createCell(3).setCellValue(storageList.get(i).getSubjectName());
				 row5.createCell(4).setCellValue(storageList.get(i).getTotalAmount()==null?0:storageList.get(i).getTotalAmount());
				 row5.createCell(5).setCellValue(storageList.get(i).getDerateAmount()==null?0:storageList.get(i).getDerateAmount());
				 row5.createCell(6).setCellValue(storageList.get(i).getDiscountAmount()==null?0:storageList.get(i).getDiscountAmount());
				 double d1 = storageList.get(i).getDerateAmount()==null?0.0:storageList.get(i).getDerateAmount();
				 double d2 = storageList.get(i).getDiscountAmount()==null?0.0:storageList.get(i).getDiscountAmount();
				 double l = storageList.get(i).getTotalAmount()==null?0.0:storageList.get(i).getTotalAmount();
				 double d = l-(d1+d2);
				 row5.createCell(7).setCellValue(d);
				 row5.createCell(8).setCellValue(storageList.get(i).getNum()==null?0:storageList.get(i).getNum());
				 row5.createCell(9).setCellValue(storageList.get(i).getRemark());
				 row5.createCell(9).setCellStyle(style013);
			 }
			 
			 for(int i=0;i<dispatchList.size();i++)
			 {
				 Row row5 = sheet.createRow(storageList.size()+rowIndex+i);
				 Cell cell = row5.createCell(0);
				 cell.setCellStyle(style0122);
				 cell.setCellValue(xulieIndex);
				 xulieIndex++;
				 if(i==0)
				 {
					 row5.createCell(2).setCellValue("宅配费用");
					 sheet.addMergedRegion(new CellRangeAddress(rowIndex+storageList.size(),storageList.size()+dispatchList.size()+rowIndex-1,2,2));
				 }
				 row5.createCell(3).setCellValue(dispatchList.get(i).getSubjectName()==null?"":dispatchList.get(i).getSubjectName());
				 row5.createCell(4).setCellValue(dispatchList.get(i).getTotalAmount()==null?0:dispatchList.get(i).getTotalAmount());
				 row5.createCell(5).setCellValue(dispatchList.get(i).getDerateAmount()==null?0:dispatchList.get(i).getDerateAmount());
				 row5.createCell(6).setCellValue(dispatchList.get(i).getDiscountAmount()==null?0:dispatchList.get(i).getDiscountAmount());
				 double d1 = dispatchList.get(i).getDerateAmount()==null?0.0:dispatchList.get(i).getDerateAmount();
				 double d2 = dispatchList.get(i).getDiscountAmount()==null?0.0:dispatchList.get(i).getDiscountAmount();
				 double l = dispatchList.get(i).getTotalAmount()==null?0.0:dispatchList.get(i).getTotalAmount();
				 double d = l-(d1+d2);
				 
				 row5.createCell(7).setCellValue(d);
				 row5.createCell(8).setCellValue(dispatchList.get(i).getNum()==null?0:dispatchList.get(i).getNum());
				 row5.createCell(9).setCellValue(dispatchList.get(i).getRemark());
				 row5.createCell(9).setCellStyle(style013);
			 }
			 
			 rowIndex = storageList.size()+dispatchList.size()+rowIndex;
		 }
		 
		 Row row6 = sheet.createRow(rowIndex);
		 
		 Cell cell61 = row6.createCell(0);
		 cell61.setCellStyle(style0122);
		 cell61.setCellValue(xulieIndex);
		 xulieIndex++;
		 Cell cell62 = row6.createCell(1);
		 cell62.setCellValue("干线费用");
		 Cell cell63 = row6.createCell(2);
		 cell63.setCellValue("干线");
		 if(null!=transportSum){
			 Cell cell64 = row6.createCell(4);
			 cell64.setCellValue(transportSum.getTotalAmount()==null?0:transportSum.getTotalAmount()); 
			 row6.createCell(5).setCellValue(transportSum.getDerateAmount()==null?0:transportSum.getDerateAmount());
			 row6.createCell(6).setCellValue(transportSum.getDiscountAmount()==null?0:transportSum.getDiscountAmount());
			 double d1 = transportSum.getDerateAmount()==null?0.0:transportSum.getDerateAmount();
			 double d2 = transportSum.getDiscountAmount()==null?0.0:transportSum.getDiscountAmount();
			 double l = transportSum.getTotalAmount()==null?0.0:transportSum.getTotalAmount();
			 double d = l-(d1+d2);
			 row6.createCell(7).setCellValue(d);
			 row6.createCell(8).setCellValue(transportSum.getNum()==null?0:transportSum.getNum());
		 }
		 
		 row6.createCell(9).setCellStyle(style013);
		 
		 sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex+1,1,1));
		 
		 rowIndex++;
		 Row row7 = sheet.createRow(rowIndex);
		 
		 Cell cell71 = row7.createCell(0);
		 cell71.setCellStyle(style0122);
		 cell71.setCellValue(xulieIndex);
		 xulieIndex++;
		 Cell cell73 = row7.createCell(2);
		 cell73.setCellValue("理赔");
		 row7.createCell(9).setCellStyle(style013);
		 
		 rowIndex++;
         Row row8 = sheet.createRow(rowIndex);
		 
		 Cell cell81 = row8.createCell(0);
		 cell81.setCellValue(xulieIndex);
		 cell81.setCellStyle(style0122);
		 xulieIndex++;
		 Cell cell82 = row8.createCell(1);
		 cell82.setCellValue("航空费用");
		 Cell cell83 = row8.createCell(2);
		 cell83.setCellValue("航空");
		 row8.createCell(9).setCellStyle(style013);
		 
		 sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex+1,1,1));
		 
		 rowIndex++;
		 Row row9 = sheet.createRow(rowIndex);
		 
		 Cell cell91 = row9.createCell(0);
		 cell91.setCellStyle(style0122);
		 cell91.setCellValue(xulieIndex);
		 xulieIndex++;
		 
		 Cell cell93 = row9.createCell(2);
		 cell93.setCellValue("理赔");
		 row9.createCell(9).setCellStyle(style013);
		 
		 rowIndex++;
		 Row row10 = sheet.createRow(rowIndex);
		 Cell cell100 = row10.createCell(0);
		 cell100.setCellStyle(style014);
		 row10.createCell(1).setCellStyle(style015);
		 row10.createCell(2).setCellStyle(style015);
		 row10.createCell(3).setCellStyle(style015);
		 cell100.setCellValue("开票金额合计");
		 sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,3));
		 Cell cell104 = row10.createCell(4);
		 cell104.setCellStyle(style015);
		 cell104.setCellValue(bill.getTotalAmount()==null?0:bill.getTotalAmount());
		 Cell cell105 = row10.createCell(5);
		 cell105.setCellStyle(style015);
		 cell105.setCellValue(bill.getDerateAmount()==null?0:bill.getDerateAmount());
		 Cell cell106 = row10.createCell(6);
		 cell106.setCellStyle(style015);
		 cell106.setCellValue(bill.getDiscountAmount()==null?0:bill.getDiscountAmount());
		 
		 double dd = bill.getDerateAmount()==null?0.0:bill.getDerateAmount().doubleValue();
		 double dd1 = bill.getDiscountAmount()==null?0.0:bill.getDiscountAmount().doubleValue();
		 
		 double dd0 = bill.getTotalAmount().doubleValue()-(dd+dd1);
		 
		 Cell cell107 = row10.createCell(7);
		 cell107.setCellStyle(style015);
		 cell107.setCellValue(dd0);
		 
		 row10.createCell(8).setCellStyle(style015);
		 row10.createCell(9).setCellStyle(style016);
		 

	}

    void createStorage(SXSSFWorkbook workbook,Map<String, Object> parameter,POISXSSUtil poiUtil,List<String>  warehouseList){
	
	 for(String warehouseCode:warehouseList)
	 {
		 int conIndex = 0;
		 parameter.put("warehouseCode", warehouseCode);
		 
		 Set<Timestamp> set = new HashSet<Timestamp>();
		 
		 parameter.put("subjectId", "wh_product_storage");//按托存储费
		 parameter.put("unit", "PALLETS");
		 
	     List<FeesReceiveStorageEntity> palletList = feesReceiveStorageRepository.queryFeesData(parameter);
	     
	     for(FeesReceiveStorageEntity entity:palletList)
	     {
	    	 conIndex++;
	    	 if(!set.contains(entity.getCreateTime()))
	    	 {
	    		 set.add(entity.getCreateTime());
	    	 }
	     }
	     
	     parameter.put("subjectId", "wh_material_storage");//耗材存储
	     
	     List<FeesReceiveStorageEntity> packList = feesReceiveStorageRepository.queryFeesData(parameter);//耗材存储
	     
	     for(FeesReceiveStorageEntity entity:packList)
	     {
	    	 conIndex++;
	    	 if(!set.contains(entity.getCreateTime()))
	    	 {
	    		 set.add(entity.getCreateTime());
	    	 }
	     }
	     
	     
	     if(conIndex==0){
	    	 continue;
	     }
	     //st
         Sheet sheet = workbook.createSheet(mapWarehouse.get(warehouseCode));
         
         Font font = workbook.createFont();
 	     font.setBoldweight(Font.BOLDWEIGHT_BOLD);
 		 CellStyle style = workbook.createCellStyle();
 		 style.setAlignment(CellStyle.ALIGN_CENTER);
 		 style.setWrapText(true);
 		 style.setFont(font);
		 
		 Row row0 = sheet.createRow(0);
		 row0.setHeight((short) (20 * 20));
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellValue("序号");
		 cell0.setCellStyle(style);
		 Cell cell1 = row0.createCell(1);
		 cell1.setCellValue("日期");
		 cell1.setCellStyle(style);
		 Cell cell2 = row0.createCell(2);
		 cell2.setCellValue("库存板数");
		 cell2.setCellStyle(style);
		 Cell cell7 = row0.createCell(7);
		 cell7.setCellStyle(style);
		 cell7.setCellValue("库存件数");
		 Cell cell8 = row0.createCell(8);
		 cell8.setCellStyle(style);
		 cell8.setCellValue("入库板数");
		 Cell cell9 = row0.createCell(9);
		 cell9.setCellStyle(style);
		 cell9.setCellValue("入库件数");
		 Cell cell10 = row0.createCell(10);
		 cell10.setCellStyle(style);
		 cell10.setCellValue("入库重量");
		 Cell cell11 = row0.createCell(11);
		 cell11.setCellValue("出库板数");
		 cell11.setCellStyle(style);
		 Cell cell12 = row0.createCell(12);
		 cell12.setCellValue("2B出库板数");
		 cell12.setCellStyle(style);
		 Cell cell13 = row0.createCell(13);
		 cell13.setCellValue("2B出库重量");
		 cell13.setCellStyle(style);
		 Cell cell14 = row0.createCell(14);
		 cell14.setCellValue("出库件数");
		 cell14.setCellStyle(style);
		 Cell cell15 = row0.createCell(15);
		 cell15.setCellValue("出库订单数");
		 cell15.setCellStyle(style);
		 Cell cell16 = row0.createCell(16);
		 cell16.setCellValue("撤销订单数");
		 cell16.setCellStyle(style);
		 Cell cell17 = row0.createCell(17);
		 cell17.setCellValue("装卸费/吨/元");
		 cell17.setCellStyle(style);
		 Cell cell18 = row0.createCell(19);
		 cell18.setCellValue("仓储费/托/元");
		 cell18.setCellStyle(style);
		 Cell cell19 = row0.createCell(24);
		 cell19.setCellValue("操作费/单/元");
		 cell19.setCellStyle(style);
		 Cell cell20 = row0.createCell(26);
		 cell20.setCellValue("收入合计");
		 cell20.setCellStyle(style);
		 
		 sheet.addMergedRegion(new CellRangeAddress(0,2,0,0));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,1,1));
		 sheet.addMergedRegion(new CellRangeAddress(0,0,2,6));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,7,7));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,8,8));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,9,9));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,10,10));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,11,11));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,12,12));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,13,13));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,14,14));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,15,15));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,16,16));
		 sheet.addMergedRegion(new CellRangeAddress(0,0,17,18));
		 sheet.addMergedRegion(new CellRangeAddress(0,0,19,23));
		 sheet.addMergedRegion(new CellRangeAddress(0,0,24,25));
		 sheet.addMergedRegion(new CellRangeAddress(0,2,26,26));
		 
		 Row row1 = sheet.createRow(1);
		 
		 row1.setHeight((short) (25 * 20));
		 
		 Cell cell22 = row1.createCell(2);
		 cell22.setCellValue("冷冻");
		 cell22.setCellStyle(style);
		 
		 Cell cell23 = row1.createCell(3);
		 cell23.setCellValue("冷藏");
		 cell23.setCellStyle(style);
		 
		 Cell cell24 = row1.createCell(4);
		 cell24.setCellValue("恒温");
		 cell24.setCellStyle(style);
		 
		 Cell cell25 = row1.createCell(5);
		 cell25.setCellValue("常温");
		 cell25.setCellStyle(style);
		 
		 Cell cell26 = row1.createCell(6);
		 cell26.setCellValue("包材");
		 cell26.setCellStyle(style);
		 
		 Cell cellk24 = row1.createCell(17);
		 cellk24.setCellValue("入库装卸费");
		 cellk24.setCellStyle(style);
		 
		 Cell cellk25 = row1.createCell(18);
		 cellk25.setCellValue("2B入库装卸费");
		 cellk25.setCellStyle(style);
		 
		 Cell cellk26 = row1.createCell(19);
		 cellk26.setCellValue("冷冻费小计/元");
		 cellk26.setCellStyle(style);
		 
		 Cell cellk27 = row1.createCell(20);
		 cellk27.setCellValue("冷藏费小计/元");
		 cellk27.setCellStyle(style);
		 
		 Cell cellk28 = row1.createCell(21);
		 cellk28.setCellValue("恒温费小计/元");
		 cellk28.setCellStyle(style);
		 
		 Cell cellk29 = row1.createCell(22);
		 cellk29.setCellValue("常温费小计/元");
		 cellk29.setCellStyle(style);
		 
		 Cell cellk30 = row1.createCell(23);
		 cellk30.setCellValue("包材费小计/元");
		 cellk30.setCellStyle(style);
		 
		 Cell cellk31 = row1.createCell(24);
		 cellk31.setCellValue("操作费/元");
		 cellk31.setCellStyle(style);
		 
		 Cell cellk32 = row1.createCell(25);
		 cellk32.setCellValue("撤销订单费/元");
		 cellk32.setCellStyle(style);
		 
		 sheet.addMergedRegion(new CellRangeAddress(1,2,2,2));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,3,3));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,4,4));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,5,5));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,6,6));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,17,17));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,18,18));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,19,19));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,20,20));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,21,21));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,22,22));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,23,23));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,24,24));
		 sheet.addMergedRegion(new CellRangeAddress(1,2,25,25));
	     //end
		 
	     SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	     
	     List<Timestamp> rqList = new ArrayList<Timestamp>(set);
	     
	     int rowIndex = 3;
	     double totalcost = 0.0;
	     double packcost = 0.0;
	     double ldcost = 0.0;
	     double lccost = 0.0;
	     double hwcost = 0.0;
	     double cwcost = 0.0;
	     double zxfcost = 0.0;
	     double xhfcost = 0.0;
	     
	     for(int i=0;i<rqList.size();i++){
	    	 double cost = 0.0;
	    	 Timestamp timestamp = rqList.get(i);
	    	 String rq = sdf.format(rqList.get(i));
	    	 Row row = sheet.createRow(rowIndex);
	    	 rowIndex++;
	    	 
	    	 Cell cell40 = row.createCell(0);
	    	 cell40.setCellValue(i+1);
	    	 
	    	 Cell cell41 = row.createCell(1);
	    	 cell41.setCellValue(rq);
	    	 
	    	 FeesReceiveStorageEntity tempPallet = null;
	    	 for(int j=0;j<palletList.size();j++){
	    		 FeesReceiveStorageEntity entity =  palletList.get(j);
	    		 if(entity.getCreateTime().equals(timestamp)){
	    			 tempPallet = entity;
	    			 if(tempPallet!=null){
			    		 if("LD".equals(tempPallet.getTempretureType())){
				    		 Cell cell42 = row.createCell(2);
					    	 cell42.setCellValue(tempPallet.getQuantity());
				    	 }else if("LC".equals(tempPallet.getTempretureType())){
				    		 Cell cell42 = row.createCell(3);
					    	 cell42.setCellValue(tempPallet.getQuantity());
				    	 }else if("HW".equals(tempPallet.getTempretureType())){
				    		 Cell cell42 = row.createCell(4);
					    	 cell42.setCellValue(tempPallet.getQuantity());
				    	 }else if("CW".equals(tempPallet.getTempretureType())){
				    		 Cell cell42 = row.createCell(5);
					    	 cell42.setCellValue(tempPallet.getQuantity());
				    	 }
			    		 
			    		 if("LD".equals(tempPallet.getTempretureType())){
				    		 Cell cell49 = row.createCell(19);
					    	 cell49.setCellValue(tempPallet.getCost().doubleValue());
					    	 ldcost = ldcost+tempPallet.getCost().doubleValue();
				    	 }else if("LC".equals(tempPallet.getTempretureType())){
				    		 Cell cell49 = row.createCell(20);
					    	 cell49.setCellValue(tempPallet.getCost().doubleValue());
					    	 lccost = lccost+tempPallet.getCost().doubleValue();
				    	 }else if("HW".equals(tempPallet.getTempretureType())){
				    		 Cell cell49 = row.createCell(21);
					    	 cell49.setCellValue(tempPallet.getCost().doubleValue());
					    	 hwcost = hwcost+tempPallet.getCost().doubleValue();
				    	 }else if("CW".equals(tempPallet.getTempretureType())){
				    		 Cell cell49 = row.createCell(22);
					    	 cell49.setCellValue(tempPallet.getCost().doubleValue());
					    	 cwcost = cwcost+tempPallet.getCost().doubleValue();
				    	 }
			    		 cost = cost + tempPallet.getCost().doubleValue();
			    	 }
	    		 }
	    	 }
	    	
	    	 
	    	 FeesReceiveStorageEntity tempPack = null;
	    	 for(int j=0;j<packList.size();j++){
	    		 FeesReceiveStorageEntity entity =  packList.get(j);
	    		 if(entity.getCreateTime().equals(timestamp)){
	    			 tempPack = entity;
	    			 if(tempPack!=null){
			    		 Cell cell46 = row.createCell(6);
				    	 cell46.setCellValue(tempPack.getQuantity());
				    	 
				    	 Cell cell49 = row.createCell(23);
				    	 cell49.setCellValue(tempPack.getCost().doubleValue());
				    	 
				    	 cost = cost + tempPack.getCost().doubleValue();
				    	 packcost = packcost + tempPack.getCost().doubleValue();
			    	 }
	    		 }
	    	 }
	    	 
	    	 totalcost = cost+totalcost;
	    	 
    		 Cell cell49 = row.createCell(26);
    		 cell49.setCellValue(cost);
	    	 
	     }
	     
	     Row row = sheet.createRow(rowIndex);
	     Cell cellLast0 = row.createCell(19);
	     cellLast0.setCellValue(ldcost);
	     
         Cell cellLast1 = row.createCell(20);
	     cellLast1.setCellValue(lccost);
	     
	     Cell cellLast6 = row.createCell(21);
	     cellLast6.setCellValue(hwcost);
	     
         Cell cellLast2 = row.createCell(22);
	     cellLast2.setCellValue(cwcost);
	     
	     Cell cellLast3 = row.createCell(23);
	     cellLast3.setCellValue(packcost);
	     
	     Cell cellLast4 = row.createCell(18);
	     cellLast4.setCellValue(zxfcost);
	     
	     Cell cellLast5 = row.createCell(17);
	     cellLast5.setCellValue(xhfcost);
	     
	     Cell cellLast = row.createCell(26);
	     cellLast.setCellValue(totalcost);
	 }
	
   }
    
    void createMaterial(SXSSFWorkbook workbook,Map<String, Object> parameter,POISXSSUtil poiUtil,List<String>  warehouseList)
    {
    	for(String warehouseCode:warehouseList)
    	{
    		int pageNo = 1;
    		int packMaterialLineNo = 1;
            int size = 20000;
    		boolean doLoop = true;
    		String billno = (String)parameter.get("billNo");
    		List<FeesReceiveMaterial> dataList = new  ArrayList<FeesReceiveMaterial>();
    		Map<String,Object> param = new HashMap<String,Object>();
    		param.put("billNo", billno);
    		param.put("warehouseCode", warehouseCode);
    		List<Map<String,String>> ListHead = repository.getMaterialCode(param);//此账单下的所有耗材编码
    		Map<String,Object> headParam = getRealHead(ListHead);
    		List<String> realList = (List<String>)headParam.get("headList");
    		Integer extendNum = (Integer)headParam.get("extendNum");//存在多少中耗材编码是硬编码枚举中没有的
    		//动态获取列名及扩展列名
    		List<Map<String, Object>> headPackMaterialMapList = getNewPackMaterialFeeHead(realList,extendNum);
    		while (doLoop) {
    			PageInfo<FeesReceiveMaterial> packMaterialList = feesReceiveStorageRepository.queryNewMaterialByBillNo(param, pageNo, size);
    			if (packMaterialList.getList().size() < size) {
    				doLoop = false;
    			}else {
    				pageNo += 1;
    			}
    			
    			dataList.addAll(packMaterialList.getList());
    		}
    		
    		if(dataList.size()==0){
    			continue;
    		}
    		
    		boolean isAddColumn = false;//是否存在扩展列
    		if(extendNum>0){
    			isAddColumn = true;
    		}
    		
    		List<Map<String, Object>> dataPackMaterialList = getMaterialExportItem(dataList,realList,isAddColumn);
    		// 处理耗材为空的数量，金额设置为0
    		for (Map<String, Object> itemMap : dataPackMaterialList) {
    			for (Map<String, Object> headItemMap : headPackMaterialMapList) {
					// 以_cost结尾(BD_cost=2.08) || 拓展耗材quantity0=2, unitPrice0=4.6, extendcost0=9.2
	    			String headKey = headItemMap.get("dataKey").toString();
					if (headKey.endsWith("_cost") || headKey.endsWith("_sl") ||
						(Pattern.matches("^quantity\\d+$", headKey) ||
						Pattern.matches("^unitPrice\\d+$", headKey) || 
						Pattern.matches("^extendcost\\d+$", headKey))) {
						if (!itemMap.containsKey(headKey)) {
							itemMap.put(headKey, 0);
						}
					}
				}
			}
    		
    		String warehouseName = mapWarehouse.get(warehouseCode);
    		try {
    			poiUtil.exportExcel2FilePath(poiUtil, workbook, warehouseName+"耗材使用费",packMaterialLineNo, headPackMaterialMapList, dataPackMaterialList);
    		} catch (IOException e) {
    			logger.error(e.getMessage(), e);
    			//写入日志
    			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());

    		}
    	}
    	
    }
    
	public Map<String,Object> getRealHead(List<Map<String,String>> listHead)
	{
		Map<String,Object>  map = new HashMap<String,Object>();
		Integer extendNum = 0;
		Map<String,String>  materialMap = ExportUtil.getMaterialMap();
		List<String> headMapList=new ArrayList<String>();
        Set<String> set = materialMap.keySet();          
        for(int j=0;j<listHead.size();j++)
        {
        	boolean fu = false;
        	Map<String,String> temp = listHead.get(j);
        	String code = temp.get("code").trim();
        	for (String str : set) 
        	{  
        	      if(code.indexOf(str)>=0)
        	      {
        	    	  fu = true;
        	    	  if(!headMapList.contains(str)){
        	    		  headMapList.add(str);
        	    	  }
        	      }
        	} 
        	
        	if(!fu)//如果耗材编码在硬编码枚举中不存在；
        	{
        		extendNum = extendNum +1;
        	}
        }
        
        map.put("extendNum", extendNum);
        map.put("headList", headMapList);
        return map;
	}
	
	public List<Map<String, Object>> getNewPackMaterialFeeHead(List<String> realList,Integer extendNum){
		
		Map<String,String>  materialMap = ExportUtil.getMaterialMap();
		
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
        
		itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商家名称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headMapList.add(itemMap);
                               
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outstockNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "商品总数");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalqty");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "systemWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "商品和数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "包材名及数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "materialAndNum");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "商家ID");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerId");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "外部物流号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "箱号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "xiangHao");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "物流商简称");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "接单时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "收件人");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "收件人省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveProvinceId");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "收件人市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveCityId");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "收件人地址");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "receiveDetailAddress");
        headMapList.add(itemMap);        
        
        for(int i=0;i<realList.size();i++)
        {
        	String str = realList.get(i);
        	
          	  String name = str+"_"+"name";
          	  String sl = str + "_"+ "sl";
          	  String dj = str + "_"+"dj";
          	  String costStr = str+"_"+"cost";
              
              itemMap = new HashMap<String, Object>();
              itemMap.put("title", materialMap.get(str));
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", name);
              headMapList.add(itemMap);
              
              itemMap = new HashMap<String, Object>();
              if("GB".equals(str)){
            	  itemMap.put("title", "重量");
              }else{
            	  itemMap.put("title", "数量");
              }
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", sl);
              headMapList.add(itemMap);
              
              itemMap = new HashMap<String, Object>();
          	  itemMap.put("title", "金额");
              itemMap.put("columnWidth", 25);
              itemMap.put("dataKey", costStr);
              headMapList.add(itemMap);
        	
        }
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "合计");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalCost");
        headMapList.add(itemMap);
        
 
        for(int j=0;j<extendNum;j++){
        	 itemMap = new HashMap<String, Object>();
             itemMap.put("title", "商品编号");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "productNo"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "商品名称");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "productName"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "外部商品编号");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "externalProductNo"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "数量");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "quantity"+j);
             headMapList.add(itemMap);
     		             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "单价");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "unitPrice"+j);
             headMapList.add(itemMap);
             
             itemMap = new HashMap<String, Object>();
             itemMap.put("title", "金额");
             itemMap.put("columnWidth", 25);
             itemMap.put("dataKey", "extendcost"+j);
             headMapList.add(itemMap);
        }
        return headMapList;
	}
	
	/**
	 * 获取耗材导出的行数据
	 * cjw 2018-02-07
	 * @param list
	 * @param realList
	 * @param isAddColumn
	 * @return
	 */
	public List<Map<String, Object>> getMaterialExportItem(List<FeesReceiveMaterial> list,List<String> realList,boolean isAddColumn){
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		List<FeesReceiveMaterial> oneBillList = new  ArrayList<FeesReceiveMaterial>();//单个运单耗材明细集合
		try{
			String waybill_no = list.get(0).getWaybillNo();//将第0个运单号设为默认比对单号
			if(StringUtils.isBlank(waybill_no)){
				return dataList;
			}
			for(int i = 0;i<list.size();i++){
				if(waybill_no.equals(list.get(i).getWaybillNo())){
					oneBillList.add(list.get(i));				
					//如果是最后一个
					if(i==(list.size()-1)){
						handleOneBillList(dataList,oneBillList,realList,isAddColumn);
						oneBillList.clear();
					}			
				}
				else{
					waybill_no = list.get(i).getWaybillNo();
					i--;//继续遍历下一个运单 此处用递归实现更合适
					handleOneBillList(dataList,oneBillList,realList,isAddColumn);
					oneBillList.clear();
				}
			}
			return dataList;
		}
		catch(Exception ex){
			logger.error("获取耗材导出的行数据",ex);
			return dataList;
		}
		
	}
	
	private List<Map<String, Object>> handleOneBillList(List<Map<String, Object>> dataList,List<FeesReceiveMaterial> oneBillList,List<String> realList,boolean isAddColumn){
		
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");
		
		Map<String, Object> dataItem=new HashMap<String, Object>();
		dataItem.put("customerName", oneBillList.get(0).getCustomerName());
		dataItem.put("warehouseName", mapWarehouse.get(oneBillList.get(0).getWarehouseCode()));
		dataItem.put("outstockNo", oneBillList.get(0).getOutstockNo());
		dataItem.put("totalqty", oneBillList.get(0).getTotalqty());
		dataItem.put("createTime", oneBillList.get(0).getCreateTime());
		dataItem.put("waybillNo", oneBillList.get(0).getWaybillNo());
		dataItem.put("systemWeight", oneBillList.get(0).getSystemWeight());
		dataItem.put("productDetail", oneBillList.get(0).getProductDetail());
		dataItem.put("materialAndNum", "");
		dataItem.put("customerId", oneBillList.get(0).getCustomerId());
		dataItem.put("externalNo", oneBillList.get(0).getExternalNo());
		dataItem.put("xiangHao", "");
		dataItem.put("carrierName", oneBillList.get(0).getCarrierName());
		dataItem.put("createTime", oneBillList.get(0).getCreateTime()==null?"":sdf.format(oneBillList.get(0).getCreateTime()));
		dataItem.put("receiveName", oneBillList.get(0).getReceiveName());
		dataItem.put("receiveProvinceId", oneBillList.get(0).getReceiveProvinceId());
		dataItem.put("receiveCityId", oneBillList.get(0).getReceiveCityId());
		dataItem.put("receiveDetailAddress", oneBillList.get(0).getReceiveDetailAddress());
		
		Double cost = 0d;
		int idx = 0;
		// realList: [BD, PMX]
		for(FeesReceiveMaterial entity: oneBillList) {
			boolean isextend = false; //是否扩展列 默认不是
			for(String str:realList)
			{
				if(entity.getProductNo().indexOf(str)>=0)
				{
	              	String name = str+"_"+"name"; 		// 耗编码
	              	//如果存在已有耗材,","分隔累加
	              	if (dataItem.containsKey(name)) {
	              		String sl = str + "_"+ "sl";		//耗材数量
		              	String costStr = str+"_"+"cost";	//耗材费用
		              	  
						dataItem.put(name, dataItem.get(name) + "," + entity.getProductName());
						//如果是干冰，取重量，否则取数量
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, dataItem.get(sl) + "," + entity.getWeight()); 
						}else{
							dataItem.put(sl, dataItem.get(sl) + "," + entity.getQuantity());
						}
						dataItem.put(costStr, dataItem.get(costStr) + "," + entity.getCost());
						cost += entity.getCost();
						isextend = false;//非扩展列
						break;
					}
	              	String sl = str + "_"+ "sl";		//耗材数量
	              	String costStr = str+"_"+"cost";	//耗材费用
	              	  
					dataItem.put(name, entity.getProductName());
					//如果是干冰，取重量，否则取数量
					if(entity.getProductNo().indexOf("GB")>=0){
						dataItem.put(sl, entity.getWeight()); 
					}else{
						dataItem.put(sl, entity.getQuantity());
					}
					dataItem.put(costStr, entity.getCost());
					cost += entity.getCost();
					isextend = false;//非扩展列
					break;
				}
				else{
					isextend = true;
				}
			}
			//如果是扩展列
			if(isextend)
			{
				dataItem.put("productNo"+idx, entity.getProductNo());
				dataItem.put("productName"+idx, entity.getProductName());
				dataItem.put("externalProductNo"+idx, entity.getExternalProductNo());//外部商品编码 ？
				dataItem.put("quantity"+idx, entity.getQuantity());
				dataItem.put("unitPrice"+idx, entity.getUnitPrice());
				dataItem.put("extendcost"+idx, entity.getCost());//金额
				cost += entity.getCost();
				idx++;
			}
		}
		dataItem.put("totalCost", cost);//金额
		dataList.add(dataItem);
		return dataList;
	}
	
	public List<Map<String, Object>> getNewPackMaterialHeadItem( List<FeesReceiveMaterial> list,List<String> realList,boolean isAddColumn){
		
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 String tempWillBillNo = "";
		 Double cost = null;
		 int i=0;
		 int j = 0;
		 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd");
		 List<FeesReceiveMaterial> exlist = new  ArrayList<FeesReceiveMaterial>();
		 for(FeesReceiveMaterial entity:list)
		 {
			 if(StringUtils.isEmpty(entity.getWaybillNo())){
					continue;//空的运单号不做处理，也没有办法搞定
			 }else{
				 exlist.add(entity);
			 }
		 }
		 
		 for(FeesReceiveMaterial entity:exlist)
		 {
			 j = j+1;
			 if(StringUtils.isEmpty(tempWillBillNo)) //遍历第一条耗材数据
			 {
				tempWillBillNo = entity.getWaybillNo();//临时运单号，用于合并运单
				dataItem=new HashMap<String, Object>();
				cost = 0.0;//单个运单所有耗材的总金额
				dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
				dataItem.put("outstockNo", entity.getOutstockNo());
				dataItem.put("totalqty", entity.getTotalqty());
				dataItem.put("createTime", entity.getCreateTime());
				dataItem.put("waybillNo", tempWillBillNo);
				dataItem.put("systemWeight", entity.getSystemWeight());
				dataItem.put("productDetail", entity.getProductDetail());
				dataItem.put("materialAndNum", "");
				dataItem.put("customerId", entity.getCustomerId());
				dataItem.put("externalNo", entity.getExternalNo());
				dataItem.put("xiangHao", "");
				dataItem.put("carrierName", entity.getCarrierName());
				dataItem.put("createTime", entity.getCreateTime()==null?"":sdf.format(entity.getCreateTime()));
				dataItem.put("receiveName", entity.getReceiveName());
				dataItem.put("receiveProvinceId", entity.getReceiveProvinceId());
				dataItem.put("receiveCityId", entity.getReceiveCityId());
				dataItem.put("receiveDetailAddress", entity.getReceiveDetailAddress());
				// realList: [BD, PMX]
				for(String str:realList)
				{
					if(entity.getProductNo().indexOf(str)>=0)
					{
	                  	 String name = str+"_"+"name"; 		// 耗编码
	                  	 String sl = str + "_"+ "sl";		//耗材数量
	                  	 String costStr = str+"_"+"cost";	//耗材费用
	                  	  
						dataItem.put(name, entity.getProductName());
						//如果是干冰，取重量，否则取数量
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, entity.getWeight()); 
						}else{
							dataItem.put(sl, entity.getQuantity());
						}
						dataItem.put(costStr, entity.getCost());
						cost = cost+entity.getCost();
						dataItem.put("totalCost", cost);//金额
						continue;
					}
				}
				
				if(isAddColumn)
				{
					dataItem.put("productNo"+i, entity.getProductNo());
					dataItem.put("productName"+i, entity.getProductName());
					dataItem.put("externalProductNo"+i, entity.getExternalProductNo());//外部商品编码 ？
					dataItem.put("quantity"+i, entity.getQuantity());
					dataItem.put("unitPrice"+i, entity.getUnitPrice());
					cost = cost+entity.getCost();
					dataItem.put("totalCost", cost);//金额
					
				}
				
				 //添加最后的数据
				 if(exlist.size()==j)
				 {
					 dataList.add(dataItem);
				 }
								
				continue;//不能再往下走
				
			 }
		
			 if(!entity.getWaybillNo().equals(tempWillBillNo))
			 {
				i = 0;//复位
				dataList.add(dataItem);//添加一个运单的数据
				 
				tempWillBillNo = entity.getWaybillNo();
				//dataItem=new HashMap<String, Object>();//清零
				cost = 0.0;//清零
				
				/*dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
				dataItem.put("createTime", entity.getCreateTime());
				dataItem.put("waybillNo", tempWillBillNo);*/
				
				dataItem.put("customerName", entity.getCustomerName());
				dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
				dataItem.put("outstockNo", entity.getOutstockNo());
				dataItem.put("totalqty", entity.getTotalqty());
				dataItem.put("createTime", entity.getCreateTime());
				dataItem.put("waybillNo", tempWillBillNo);
				dataItem.put("systemWeight", entity.getSystemWeight());
				dataItem.put("productDetail", entity.getProductDetail());
				dataItem.put("materialAndNum", "");
				dataItem.put("customerId", entity.getCustomerId());
				dataItem.put("externalNo", entity.getExternalNo());
				dataItem.put("xiangHao", "");
				dataItem.put("carrierName", entity.getCarrierName());
				dataItem.put("createTime", entity.getCreateTime()==null?"":sdf.format(entity.getCreateTime()));
				dataItem.put("receiveName", entity.getReceiveName());
				dataItem.put("receiveProvinceId", entity.getReceiveProvinceId());
				dataItem.put("receiveCityId", entity.getReceiveCityId());
				dataItem.put("receiveDetailAddress", entity.getReceiveDetailAddress());
				
				
				
				for(String str:realList)
				{
					if(entity.getProductNo().indexOf(str)>=0)
					{
						  String bm = str+"_"+"bm";//商品编号
	                  	  String name = str+"_"+"name";
	                  	  String wbbh = str+"_"+"bh";//商品外部编号
	                  	  String sl = str + "_"+ "sl";//耗材数量
	                  	  String dj = str + "_"+"dj";//单价
	                  	  
	                  	dataItem.put(bm, entity.getProductNo());
						dataItem.put(name, entity.getProductName());
						dataItem.put(wbbh, entity.getExternalProductNo());
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, entity.getWeight());
						}else{
							dataItem.put(sl, entity.getQuantity());
						}
						dataItem.put(dj, entity.getUnitPrice());
						
						cost = cost+entity.getCost();
						dataItem.put("totalCost", cost);//金额
												
					}
				}
				
				if(isAddColumn)
				{
					dataItem.put("productNo"+i, entity.getProductNo());
					dataItem.put("productName"+i, entity.getProductName());
					dataItem.put("externalProductNo"+i, entity.getExternalProductNo());
					dataItem.put("quantity"+i, entity.getQuantity());
					dataItem.put("unitPrice"+i, entity.getUnitPrice());
					
					cost = cost+entity.getCost();
					dataItem.put("totalCost", cost);//金额
				}
				
			 }
			 else{
				i = i+1;
				for(String str:realList)
				{
					if(entity.getProductNo().indexOf(str)>=0)
					{
						  String bm = str+"_"+"bm";
	                  	  String name = str+"_"+"name";
	                  	  String wbbh = str+"_"+"bh";
	                  	  String sl = str + "_"+ "sl";
	                  	  String dj = str + "_"+"dj";
	                  	  
	                  	dataItem.put(bm, entity.getProductNo());
						dataItem.put(name, entity.getProductName());
						dataItem.put(wbbh, entity.getExternalProductNo());
						if(entity.getProductNo().indexOf("GB")>=0){
							dataItem.put(sl, entity.getWeight());
						}
						else{
							dataItem.put(sl, entity.getQuantity());
						}
						dataItem.put(dj, entity.getUnitPrice());
						
						cost = cost+entity.getCost();
						dataItem.put("totalCost", cost);//金额
						
					}
				}
				
				if(isAddColumn)
				{
					dataItem.put("productNo"+i, entity.getProductNo());
					dataItem.put("productName"+i, entity.getProductName());
					dataItem.put("externalProductNo"+i, entity.getExternalProductNo());
					dataItem.put("quantity"+i, entity.getQuantity());
					dataItem.put("unitPrice"+i, entity.getUnitPrice());
					
					cost = cost+entity.getCost();
					dataItem.put("totalCost", cost);//金额
					
					
				}
			 }
			 //添加最后的数据
			 if(exlist.size()==j)
			 {
				 dataList.add(dataItem);
			 }
			
		 }
		 return dataList;
	}
	
	/**
	 * 配送
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handDispatch(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String path, String billno)throws Exception{
		int pageNo = 1;
		int dispatchLineNo = 1;
		boolean doLoop = true;
		int pageSize = 20000;
		logger.info("账单导出查询配送运单信息...");
        List<FeesReceiveDispatchEntity> dataList = new  ArrayList<FeesReceiveDispatchEntity>();
		
		while (doLoop) {
			PageInfo<FeesReceiveDispatchEntity> distributionList = 
					feeInDistributionRepository.querydistributionDetailByBillNo(billno, pageNo, pageSize);
			if (null != distributionList && distributionList.getList().size() > 0) {
				if (distributionList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				dataList.addAll(distributionList.getList());
			}else {
				doLoop = false;
			}
			
		}
		if(dataList.size()==0){
			return;
		}
		logger.info("账单导出生成sheet。。。");
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"宅配");
		
		sheet.setColumnWidth(1, 3300);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 3300);
		
		Font font = workbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
		 
		Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellValue("仓库");
		 cell0.setCellStyle(style);
		 Cell cell1 = row0.createCell(1);
		 cell1.setCellValue("商家名称");
		 cell1.setCellStyle(style);
		 Cell cell2 = row0.createCell(2);
		 cell2.setCellStyle(style);
		 cell2.setCellValue("九曳订单号");
		 Cell cell3 = row0.createCell(3);
		 cell3.setCellValue("商家订单号");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(4);
		 cell4.setCellValue("运单号");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(5);
		 cell5.setCellValue("运单生成时间");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(6);
		 cell6.setCellValue("转寄后运单号");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(7);
		 cell7.setCellValue("运单重量");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(8);
		 cell8.setCellValue("商品数量");
		 cell8.setCellStyle(style);
		 Cell cell9 = row0.createCell(9);
		 cell9.setCellValue("商品明细");
		 cell9.setCellStyle(style);
		 Cell cell10 = row0.createCell(10);
		 cell10.setCellValue("物流商");
		 cell10.setCellStyle(style);
		 Cell cell11 = row0.createCell(11);
		 cell11.setCellValue("收件人");
		 cell11.setCellStyle(style);
		 Cell cell12 = row0.createCell(12);
		 cell12.setCellValue("收件人省");
		 cell12.setCellStyle(style);
		 Cell cell13 = row0.createCell(13);
		 cell13.setCellValue("收件人市");
		 cell13.setCellStyle(style);
		 Cell cell14 = row0.createCell(14);
		 cell14.setCellValue("收件人区县");
		 cell14.setCellStyle(style);
		 Cell cell15 = row0.createCell(15);
		 cell15.setCellValue("计费重量");
		 cell15.setCellStyle(style);
		 Cell cell16 = row0.createCell(16);
		 cell16.setCellValue("首重金额");
		 cell16.setCellStyle(style);
		 Cell cell17 = row0.createCell(17);
		 cell17.setCellValue("续重金额");
		 cell17.setCellStyle(style);
		 Cell cell18 = row0.createCell(18);
		 cell18.setCellValue("运费");
		 cell18.setCellStyle(style);
		 Cell cell19 = row0.createCell(19);
		 cell19.setCellValue("折扣后运费");
		 cell19.setCellStyle(style);
		 Cell cell20 = row0.createCell(20);
		 cell20.setCellValue("操作费");
		 cell20.setCellStyle(style);
		 Cell cell21 = row0.createCell(21);
		 cell21.setCellValue("责任方");
		 cell21.setCellStyle(style);
		 Cell cell22 = row0.createCell(22);
		 cell22.setCellValue("原因详情");
		 cell22.setCellStyle(style);
		 
		logger.info("账单导出给sheet赋值。。。");
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		double yunfei = 0.0;
		double zhekouhou = 0.0;
		double caozao = 0.0;
		int RowIndex = 1;
		for(int i=0;i<dataList.size();i++)
		{	
			FeesReceiveDispatchEntity entity = dataList.get(i);
			Row row = sheet.createRow(RowIndex);
			RowIndex++;
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getWarehouseName());
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getCustomerName());
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getOutstockNo());
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getExternalNo());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getWaybillNo());
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(entity.getCreateTime()==null?"":sdf.format(entity.getCreateTime()));
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(entity.getZexpressnum());
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(entity.getTotalWeight()==null?0:entity.getTotalWeight());
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getTotalQuantity() == null?0:entity.getTotalQuantity());
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getProductDetail());
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(entity.getCarrierName());
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(entity.getReceiveName());
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(entity.getToProvinceName());
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(entity.getToCityName());
			Cell cel14 = row.createCell(14);
			cel14.setCellValue(entity.getToDistrictName());
			Cell cel15 = row.createCell(15);
//			logger.info(entity);
//			logger.info(entity.getChargedWeight());
			cel15.setCellValue(entity.getChargedWeight() == null?0:entity.getChargedWeight());
			Cell cel16 = row.createCell(16);
			cel16.setCellValue(entity.getHeadPrice()==null?0:entity.getHeadPrice());
			Cell cel17 = row.createCell(17);
			cel17.setCellValue(entity.getContinuedPrice()==null?0:entity.getContinuedPrice());
			Cell cel18 = row.createCell(18);
			cel18.setCellValue(entity.getAmount()==null?0:entity.getAmount());
			Cell cel19 = row.createCell(19);
			cel19.setCellValue(entity.getDiscountAmount()==null?0:entity.getDiscountAmount());
			Cell cel20 = row.createCell(20);
			cel20.setCellValue(entity.getOrderOperatorAmount()==null?"0":entity.getOrderOperatorAmount());
	
			Cell cel21= row.createCell(21);
			cel21.setCellValue(entity.getDutyType());
			Cell cel22= row.createCell(22);
			cel22.setCellValue(entity.getUpdateReasonDetail());
			yunfei = yunfei + (entity.getAmount()==null?0:entity.getAmount());
			zhekouhou = zhekouhou + (entity.getDiscountAmount()==null?0:entity.getDiscountAmount());
			caozao = caozao + Double.valueOf(entity.getOrderOperatorAmount()==null?"0":entity.getOrderOperatorAmount());
			
		}
		
		Row lastRow = sheet.createRow(RowIndex);
		Cell cellast = lastRow.createCell(17);
		cellast.setCellValue("合计金额：");
		Cell cellast0 = lastRow.createCell(18);
		cellast0.setCellValue(yunfei);
		Cell cellast1 = lastRow.createCell(19);
		cellast1.setCellValue(zhekouhou);	
		Cell cellast2 = lastRow.createCell(20);
		cellast2.setCellValue(caozao);
	}
	
	/**
	 * 配送费用明细
	 */
	public List<Map<String, Object>> getDispatchFeeHead(){
		List<Map<String, Object>> headMapList=new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		
    	itemMap.put("title", "商家");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "customerName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "仓库");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "warehouseName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "物流商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "carrierName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "宅配商");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "delivery");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "出库单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "outstockNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "外部单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "externalNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "运单号");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "waybillNo");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的省");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toProvinceName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的市");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toCityName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "目的区县");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "toDistrictName");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品数量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productNums");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "商品明细");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "productDetail");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "总重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "totalWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "计费重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "chargedWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "重量界限");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "weightLimit");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "单价");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "unitPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重重量");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedWeight");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "首重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "headPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "续重价格");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "continuedPrice");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "配送金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "amount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "温度类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "temperatureType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
    	itemMap.put("title", "订单操作费金额");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "orderAmount");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "业务类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "billTypeName");
        headMapList.add(itemMap);
       
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "大状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bigstatus");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "小状态");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "smallstatus");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "数据类型");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "bizType");
        headMapList.add(itemMap);
        
        itemMap = new HashMap<String, Object>();
        itemMap.put("title", "创建时间");
        itemMap.put("columnWidth", 25);
        itemMap.put("dataKey", "createTime");
        headMapList.add(itemMap);
        
        return headMapList;
	}
	
	private List<Map<String, Object>> getDispatchFeeHeadItem(List<FeesReceiveDispatchEntity> list){
		 List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		 Map<String, Object> dataItem = null;
		 for(FeesReceiveDispatchEntity entity:list){
			dataItem=new HashMap<String, Object>();
			dataItem.put("customerName", entity.getCustomerName());
			dataItem.put("warehouseName", mapWarehouse.get(entity.getWarehouseCode()));
			dataItem.put("carrierName", entity.getCarrierName());
			dataItem.put("delivery", entity.getDeliverName());
			dataItem.put("outstockNo", entity.getOutstockNo());
			dataItem.put("externalNo", entity.getExternalNo());
			dataItem.put("waybillNo", entity.getWaybillNo());
			dataItem.put("toProvinceName", entity.getToProvinceName());
			dataItem.put("toCityName", entity.getToCityName());
			dataItem.put("toDistrictName", entity.getToDistrictName());
			dataItem.put("productNums", entity.getTotalQuantity());
			dataItem.put("productDetail", entity.getProductDetail());
			dataItem.put("totalWeight", entity.getTotalWeight());
			dataItem.put("chargedWeight", entity.getChargedWeight());
			dataItem.put("weightLimit", entity.getWeightLimit());
			dataItem.put("unitPrice", entity.getUnitPrice());
			dataItem.put("headWeight", entity.getHeadWeight());
			dataItem.put("continuedWeight", entity.getContinuedWeight());
			dataItem.put("headPrice", entity.getHeadPrice());
			dataItem.put("continuedPrice", entity.getContinuedPrice());
			dataItem.put("amount", entity.getAmount());
       	    dataItem.put("temperatureType", tempretureMap.get(entity.getTemperatureType()));
			dataItem.put("orderAmount", entity.getOrderOperatorAmount());
			dataItem.put("billTypeName", entity.getBillTypeName());
			dataItem.put("bigstatus", entity.getBigstatus());
        	dataItem.put("smallstatus", entity.getSmallstatus());
        	dataItem.put("bizType", BizState.getDesc(entity.getBizType()));
			dataItem.put("createTime", entity.getCreateTime());
			dataList.add(dataItem);
		 }
		 return dataList;
	}
	
	private void handTransport(POISXSSUtil poiUtil, SXSSFWorkbook workbook,String path, String billno)throws Exception{
		int pageNo = 1;
		int transportLineNo = 1;
		boolean doLoop = true;
		int pageSize = 20000;

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		List<FeesReceiveDeliverEntity> dataList = new ArrayList<FeesReceiveDeliverEntity>();
		
		while (doLoop)
		{
			PageInfo<FeesReceiveDeliverEntity> deliverList = 
					feesReceiveDeliverDao.querydeliverDetailByBillNoNew(billno, pageNo, pageSize);
			if (null != deliverList && deliverList.getList().size() > 0) {
				if (deliverList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				
				dataList.addAll(deliverList.getList());
				
			}else {
				doLoop = false;
			}
		}
		
		if(dataList.size()==0){
			return;
		}
		
		//获取当前账单中所有的费用科目
		Map<String, Object> param=new HashMap<>();
		param.put("billNo", billno);
		List<String> subjectList=feesReceiveDeliverDao.getAllSubject(param);
		
		Sheet sheet = poiUtil.getXSSFSheet(workbook,"干线");
		
		Font font = workbook.createFont();
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		style.setFont(font);
		 
		 
		Row row0 = sheet.createRow(0);
		
		Cell cell0 = row0.createCell(0);
		 cell0.setCellValue("创建时间");
		 cell0.setCellStyle(style);
		Cell cell1 = row0.createCell(1);
		 cell1.setCellValue("外部订单号");
		 cell1.setCellStyle(style);
		Cell cell2 = row0.createCell(2);
		 cell2.setCellValue("订单号");
		 cell2.setCellStyle(style);
		Cell cell3 = row0.createCell(3);
		 cell3.setCellValue("路单号");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(4);
		 cell4.setCellValue("运单号");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(5);
		 cell5.setCellValue("客户名称");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(6);
		 cell6.setCellValue("发货地");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(7);
		 cell7.setCellValue("到达地");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(8);
		 cell8.setCellValue("产品");
		 cell8.setCellStyle(style);
		 Cell cell9 = row0.createCell(9);
		 cell9.setCellValue("订单箱数");
		 cell9.setCellStyle(style);
		 Cell cell10 = row0.createCell(10);
		 cell10.setCellValue("订单件数");
		 cell10.setCellStyle(style);
		 Cell cell11 = row0.createCell(11);
		 cell11.setCellValue("重量（KG）");
		 cell11.setCellStyle(style);
		 Cell cell12 = row0.createCell(12);
		 cell12.setCellValue("单价");
		 cell12.setCellStyle(style);
		 Cell cell13 = row0.createCell(13);
		 cell13.setCellValue("体积(m³)");
		 cell13.setCellStyle(style);
		 Cell cell14 = row0.createCell(14);
		 cell14.setCellValue("配送类型");
		 cell14.setCellStyle(style);
		 Cell cell15 = row0.createCell(15);
		 cell15.setCellValue("货物类型");
		 cell15.setCellStyle(style);
		 Cell cell16 = row0.createCell(16);
		 cell16.setCellValue("发货日期");
		 cell16.setCellStyle(style);
		 Cell cell17 = row0.createCell(17);
		 cell17.setCellValue("签收日期");
		 cell17.setCellStyle(style);
		 Cell cell18 = row0.createCell(18);
		 cell18.setCellValue("退货");
		 cell18.setCellStyle(style);
		 Cell cell19 = row0.createCell(19);
		 cell19.setCellValue("实收件数");
		 cell19.setCellStyle(style);
		 Cell cell20 = row0.createCell(20);
		 cell20.setCellValue("有无回单");
		 cell20.setCellStyle(style);
		 
		 Map<String, Object> cellKeyListMap=new HashMap<>();
		 
		 Cell[] cellList=new Cell[subjectList.size()+21];
		 for(int i=0;i<subjectList.size();i++){
			 cellList[17+i] = row0.createCell(17+i);
			 cellList[17+i].setCellValue(getAllSubject().get(subjectList.get(i)));
			 cellList[17+i].setCellStyle(style);
			 cellKeyListMap.put(subjectList.get(i), 17+i);
		 }
		 
		 /*Cell cell17 = row0.createCell(17);		
		 cell17.setCellValue("延时等待费");
		 cell17.setCellStyle(style);
		 Cell cell18 = row0.createCell(18);
		 cell18.setCellValue("运费");
		 cell18.setCellStyle(style);
		 Cell cell19 = row0.createCell(19);
		 cell19.setCellValue("送货费");
		 cell19.setCellStyle(style);
		 Cell cell20 = row0.createCell(20);
		 cell20.setCellValue("卸货费");
		 cell20.setCellStyle(style);
		 Cell cell21 = row0.createCell(21);
		 cell21.setCellValue("缠绕膜费");
		 cell21.setCellStyle(style);
		 Cell cell22 = row0.createCell(22);
		 cell22.setCellValue("逆向物流费");
		 cell22.setCellStyle(style);*/
		 
		 
		/* Cell celllast1 = row0.createCell(subjectList.size()+17);
		 celllast1.setCellValue("合计");
		 celllast1.setCellStyle(style);
		 Cell celllast2 = row0.createCell(subjectList.size()+18);
		 celllast2.setCellValue("备注");
		 celllast2.setCellStyle(style);*/
		 
		 String orderNo = "";
		 Double hj = 0.0;//合计
		 Map<String,List<FeesReceiveDeliverEntity>> dataL = new HashMap<String,List<FeesReceiveDeliverEntity>>();
		 List<FeesReceiveDeliverEntity> tempList = null;
		 for(int i=0;i<dataList.size();i++)
		 {
			FeesReceiveDeliverEntity entity = dataList.get(i);
			
			if(StringUtils.isEmpty(entity.getOrderno()))
			    continue;
			
			if(StringUtils.isEmpty(orderNo))
			{
				tempList = new  ArrayList<FeesReceiveDeliverEntity>();
				orderNo = entity.getOrderno(); 
				tempList.add(entity);
				
				dataL.put(orderNo, tempList);
				
				continue;
			}
			
			if(!orderNo.equals(entity.getOrderno())){
				tempList = new  ArrayList<FeesReceiveDeliverEntity>();
				orderNo = entity.getOrderno();
				tempList.add(entity);
				
			}else{
				tempList.add(entity);
			}
			
			dataL.put(orderNo, tempList);
						
		 }
		 
		 List<String> dataMap = new ArrayList<String>(dataL.keySet());
		 
		 int rowIndex = 1;
		 for(int i=0;i<dataMap.size();i++)
		 {
			Row row = sheet.createRow(rowIndex);
			rowIndex++;
			List<FeesReceiveDeliverEntity>  realList = dataL.get(dataMap.get(i));
			
			FeesReceiveDeliverEntity entity = getNosub(realList);
			
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(sdf.format(entity.getCretime()));
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getExternalNo()==null?"":entity.getExternalNo());
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getOrderno()==null?"":entity.getOrderno());
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getRoadbillNo()==null?"":entity.getRoadbillNo());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getWaybillNo()==null?"":entity.getWaybillNo());
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(entity.getCustomerName()==null?"":entity.getCustomerName());
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(entity.getWarehouseName()==null?"":entity.getWarehouseName());
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(entity.getTargetwarehouse()==null?"":entity.getTargetwarehouse());
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getProductType()==null?"":transportProductTypeMap.get(entity.getProductType()));
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getTotalBox()==null?0.0:entity.getTotalBox());
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(entity.getTotalPackage()==null?0.0:entity.getTotalPackage());
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(entity.getWeight()==null?0.0:entity.getWeight());
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(entity.getUnitprice()==null?0.0:entity.getUnitprice());
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(entity.getVolume()==null?0.0:entity.getVolume());
			Cell cel14 = row.createCell(14);
			cel14.setCellValue(entity.getSubjectCode()==null?"":transportProductTypeMap.get(entity.getSubjectCode()));
			Cell cel15 = row.createCell(15);
			cel15.setCellValue(entity.getCarmodel()==null?"":entity.getCarmodel());
			Cell cel16 = row.createCell(16);
			cel16.setCellValue(entity.getSendTime()==null?"":sdf.format(entity.getSendTime()));
			Cell cel17 = row.createCell(17);
			cel17.setCellValue(entity.getSigntime()==null?"":sdf.format(entity.getSigntime()));
			Cell cel18 = row.createCell(18);
			cel18.setCellValue(entity.getReturnGoods()==null?"":entity.getReturnGoods());
			Cell cel19 = row.createCell(19);
			cel19.setCellValue(entity.getTotalPackage()==null?0.0:entity.getTotalPackage());
			Cell cel20 = row.createCell(20);
			cel19.setCellValue(entity.getIsReceiveOrder()==null?"":entity.getIsReceiveOrder());
			
			
			for(int j=0;j<realList.size();j++)
			{
				FeesReceiveDeliverEntity temp  = realList.get(j);
				
				if(!StringUtils.isEmpty(temp.getSubjectCode()))
				{
					if(cellKeyListMap.containsKey(temp.getSubjectCode())){
						Cell cell=row.createCell((int)cellKeyListMap.get(temp.getSubjectCode()));
						cell.setCellValue(temp.getTotleprice());
						hj = hj + temp.getTotleprice();
					}
					
				/*	if("ts_delay_waiting".equals(temp.getOtherSubjectCode())){//延时等待费ts_delay_waiting
						Cell cel17 = row.createCell(17);
						cel17.setCellValue(temp.getTotleprice());
						hj = hj + temp.getTotleprice();
					}else if("ts_send".equals(temp.getOtherSubjectCode())){//送货费
						Cell cel19 = row.createCell(19);
						cel19.setCellValue(temp.getTotleprice());
						hj = hj + temp.getTotleprice();
					}else if("ts_unloading".equals(temp.getOtherSubjectCode())){//卸货费
						Cell cel20 = row.createCell(20);
						cel20.setCellValue(temp.getTotleprice());
						hj = hj + temp.getTotleprice();
					}else if("ts_wrapping_film".equals(temp.getOtherSubjectCode())){//缠绕膜费
						Cell cel21 = row.createCell(21);
						cel21.setCellValue(temp.getTotleprice());
						hj = hj + temp.getTotleprice();
					}else if("ts_reverse_logistic".equals(temp.getOtherSubjectCode())){//逆向物流费
						Cell cel22 = row.createCell(22);
						cel22.setCellValue(temp.getTotleprice());
						hj = hj + temp.getTotleprice();
					}*/
				}/*else{
					Cell cel18 = row.createCell(18);
					cel18.setCellValue(temp.getTotleprice()==null?0.0:temp.getTotleprice());
					hj = hj + temp.getTotleprice();
				}*/
				
			}
			
	
	/*		 Cell celllast3 = row.createCell(subjectList.size()+17);
			 celllast3.setCellValue(hj);
			 celllast3.setCellStyle(style);
			 Cell celllast4 = row.createCell(subjectList.size()+18);
			 celllast4.setCellValue("");
			 celllast4.setCellStyle(style);	*/
		 }

	}
	
	/**
	 * 理赔
	 * @param poiUtil
	 * @param workbook
	 * @param path
	 * @param billno
	 * @throws Exception
	 */
	private void handAbnormal(POISXSSUtil poiUtil, SXSSFWorkbook workbook, 
			String path, String billno)throws Exception{
		int pageNo = 1;
		int abnormalLineNo = 1;
		boolean doLoop = true;
		int pageSize = 20000;
		
		List<FeesAbnormalEntity>  dataList = new ArrayList<FeesAbnormalEntity>();
		Map<String,Object>  param = new  HashMap<String,Object>();
		param.put("billno", billno);
		
		while (doLoop) {
			PageInfo<FeesAbnormalEntity> abnormalList = 
					feesAbnormalRepository.queryAbnormalByBillNo(param, pageNo, pageSize);
			if (null != abnormalList && abnormalList.getList().size() > 0) {
				if (abnormalList.getList().size() < pageSize) {
					doLoop = false;
				}else {
					pageNo += 1; 
				}
				
				dataList.addAll(abnormalList.getList());
			}else {
				doLoop = false;
			}
		}
		
		if(dataList.size()==0){
			return;
		}
		
		 Sheet sheet = workbook.createSheet("理赔");
		 
		 Font font = workbook.createFont();
	     font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
			 
		 Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellStyle(style);
		 cell0.setCellValue("发货仓库");
		 Cell cell01 = row0.createCell(1);
		 cell01.setCellStyle(style);
		 cell01.setCellValue("运单日期");
	     Cell cell1 = row0.createCell(2);
	     cell1.setCellValue("运单号");
	     cell1.setCellStyle(style);
	     Cell cell2 = row0.createCell(3);
	     cell2.setCellValue("客户");
	     cell2.setCellStyle(style);
		 Cell cell3 = row0.createCell(4);
		 cell3.setCellValue("赔付类型");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(5);
		 cell4.setCellValue("赔款额");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(6);
		 cell5.setCellValue("运费");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(7);
		 cell6.setCellValue("是否免运费");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(8);
		 cell7.setCellValue("登记人");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(9);
		 cell8.setCellValue("备注");
		 cell8.setCellStyle(style);
		 
		 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 int rowIndex = 1;
		 for(int i=0;i<dataList.size();i++)
		 {
			 FeesAbnormalEntity entity = dataList.get(i);
			 
             Row row = sheet.createRow(rowIndex);
             rowIndex++;
			 
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getWarehouseName()==null?"":entity.getWarehouseName());
		    Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getOperateTime()==null?"":sdf.format(entity.getOperateTime()));
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getExpressnum()==null?"":entity.getExpressnum());
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getCustomerName()==null?"":entity.getCustomerName());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getReason()==null?"":entity.getReason());
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(entity.getPayMoney()==null?0:entity.getPayMoney());
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(entity.getDeliveryCost()==null?0:entity.getDeliveryCost());
			//0-不免运费 1-免运费
			if(null!=entity.getIsDeliveryFree()){
				Cell cel7 = row.createCell(7);
				cel7.setCellValue(entity.getIsDeliveryFree());
			}
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getCreatePersonName()==null?"":entity.getCreatePersonName());
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getRemark()==null?"":entity.getRemark());
		 }
		
	}
	
	void handAdd(POISXSSUtil poiUtil, SXSSFWorkbook workbook, String path, Map<String, Object> parameter){
		
		//List<FeesReceiveStorageEntity> dataList = feesReceiveStorageRepository.queryStorageAdd(parameter);
		Map<String, Object> condition=new HashMap<>();
		if(parameter!=null && parameter.containsKey("billNo")){
			condition.put("billno", parameter.get("billNo"));
		}
		
		PageInfo<FeesReceiveStorageEntity> pageInfo = feesReceiveStorageRepository.queryStorageAddFeePage(condition, 0, Integer.MAX_VALUE);
		List<FeesReceiveStorageEntity> dataList=new ArrayList<FeesReceiveStorageEntity>();
		if(pageInfo!=null && pageInfo.getList().size()>0){
			dataList=pageInfo.getList();
		}
		if(dataList.size()==0){
			return;
		}
		
		 Sheet sheet = workbook.createSheet("增值");
		 
		 Font font = workbook.createFont();
	     font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 CellStyle style = workbook.createCellStyle();
		 style.setAlignment(CellStyle.ALIGN_CENTER);
		 style.setWrapText(true);
		 style.setFont(font);
		 
		 Row row0 = sheet.createRow(0);
		 Cell cell0 = row0.createCell(0);
		 cell0.setCellStyle(style);
		 cell0.setCellValue("创建时间");
	     Cell cell1 = row0.createCell(1);
	     cell1.setCellStyle(style);
	     cell1.setCellValue("仓库名称");
	     Cell cell2 = row0.createCell(2);
	     cell2.setCellValue("商家");
	     cell2.setCellStyle(style);
		 Cell cell3 = row0.createCell(3);
		 cell3.setCellValue("费用编号");
		 cell3.setCellStyle(style);
		 Cell cell4 = row0.createCell(4);
		 cell4.setCellValue("费用科目");
		 cell4.setCellStyle(style);
		 Cell cell5 = row0.createCell(5);
		 cell5.setCellValue("服务内容");
		 cell5.setCellStyle(style);
		 Cell cell6 = row0.createCell(6);
		 cell6.setCellValue("金额");
		 cell6.setCellStyle(style);
		 Cell cell7 = row0.createCell(7);
		 cell7.setCellValue("减免");
		 cell7.setCellStyle(style);
		 Cell cell8 = row0.createCell(8);
		 cell8.setCellValue("实收金额");
		 cell8.setCellStyle(style);
		 Cell cell9 = row0.createCell(9);
		 cell9.setCellValue("数量");
		 cell9.setCellStyle(style);
		 Cell cell10 = row0.createCell(10);
		 cell10.setCellValue("单位");
		 cell10.setCellStyle(style);
		 Cell cell11 = row0.createCell(11);
		 cell11.setCellValue("单价");
		 cell11.setCellStyle(style);
		 Cell cell12 = row0.createCell(12);
		 cell12.setCellValue("关联单号");
		 cell12.setCellStyle(style);
		 Cell cell13 = row0.createCell(13);
		 cell13.setCellValue("备注");
		 cell13.setCellStyle(style);
		//仓储增值类型
		 Map<String, String> temperatureMap = new HashMap<String, String>();
		// temperatureMap=bmsGroupSubjectService.getExportSubject("receive_wh_valueadd_subject");
		/*Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeCode", "wh_value_add_subject");
		List<SystemCodeEntity> temperatureList = systemCodeRepository.queryCodeList(param);
		if(temperatureList != null && temperatureList.size()>0){
		    for(SystemCodeEntity scEntity : temperatureList){
		    	temperatureMap.put(scEntity.getCode(), scEntity.getCodeName().trim());
		    }
		}*/
		 
		 int rowIndex = 1;
		 java.text.SimpleDateFormat  sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 for(int i=0;i<dataList.size();i++)
		 {
			 FeesReceiveStorageEntity entity = dataList.get(i);
			 
			 Row row = sheet.createRow(rowIndex);
			 rowIndex++;
			 
			 	
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(entity.getCreateTime()==null?"":sdf.format(entity.getCreateTime()));
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(entity.getWarehouseName()==null?"":entity.getWarehouseName());
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(entity.getCustomerName()==null?"":entity.getCustomerName());	
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(entity.getFeesNo()==null?"":entity.getFeesNo());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(entity.getOtherSubjectCode()==null?"":temperatureMap.get(entity.getOtherSubjectCode()));
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(entity.getServiceContent()==null?"":entity.getServiceContent());
			Cell cel6 = row.createCell(6);
			double money=0d;
			if(entity.getCost()!=null){
				money=entity.getCost().doubleValue();
			}
			cel6.setCellValue(money);
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(entity.getDerateAmount()==null?0:entity.getDerateAmount());
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(entity.getReceiptAmount()==null?0:entity.getReceiptAmount());		
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(entity.getQuantity()==null?0:entity.getQuantity());
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(entity.getUnit()==null?"":entity.getUnit());
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(entity.getUnitPrice()==null?0:entity.getUnitPrice());
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(entity.getExternalNo()==null?"":entity.getExternalNo());
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(entity.getRemarkContent()==null?"":entity.getRemarkContent());
		 }
		
	}
	
	private void updateExportTask(String taskId, String taskState, double process){
		FileExportTaskEntity entity = new FileExportTaskEntity();
		if (StringUtils.isNotEmpty(taskState)) {
			entity.setTaskState(taskState);
		}
		entity.setTaskId(taskId);
		entity.setProgress(process);
		fileExportTaskRepository.update(entity);
	}
	
	FeesReceiveDeliverEntity  getNosub(List<FeesReceiveDeliverEntity>  realList){
		if(realList.size()==1)
			return realList.get(0);
		for(FeesReceiveDeliverEntity entity:realList){
			if(StringUtils.isEmpty(entity.getOtherSubjectCode())){
				return entity;
			}
		}
		return null;
	}

	@Override
	public int updateSubjectList(List<BmsBillSubjectInfoEntity> condition) {
		// TODO Auto-generated method stub
		return bmsBillSubjectInfoRepository.updateSubjectList(condition);
	}	
    
	/**
	 * 获取所有的运输费用科目
	 * @return
	 */
	@DataProvider
	public Map<String, String> getAllSubject(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		/*List<SystemCodeEntity> sList=systemCodeService.findEnumList("ts_base_subject");
		List<SystemCodeEntity> mList=systemCodeService.findEnumList("ts_value_add_subject");
		//List<BmsSubjectInfoEntity> subjectList=bmsSubjectInfoService.queryAll("");
		for(SystemCodeEntity en:sList){
			mapValue.put(en.getCode(),en.getCodeName());
		}
		for(SystemCodeEntity en:mList){
			mapValue.put(en.getCode(),en.getCodeName());
		}*/
		//mapValue=bmsGroupSubjectService.getExportSubject("receive_bill_transport_subject");
		return mapValue;
	}
	
	
	@DataProvider
	public Map<String, String> getChargeTypeMap() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("STORAGE_CHARGE_TYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	public double getAbnormalMoney(Map<String, Object> param,String subjectCode){
		Double totalMoney=0d;
		List<FeesAbnormalEntity> feeList=feesAbnormalRepository.queryFeeBySubject(param);	
		/*eg1：如果账单总额 100 元，某单赔偿金额为10元，运费5元（免运费），且为非商家原因；
		最终账单金额= 100 -（10 +5）
		eg2：如果账单总额为100元，某单赔偿金额为10元，运费5元（免运费 ），且为商家原因；
                    最终账单金额 = 100+10（无论是否免运费）*/
		//仓储理赔
		if("Abnormal_Storage".equals(subjectCode)){
			for(FeesAbnormalEntity fee:feeList){
				//商家原因
				if(4==fee.getReasonId()){
					totalMoney+=fee.getPayMoney();
				}else{
					//非商家原因
					if("0".equals(fee.getIsDeliveryFree())){
						//免运费
						//根据运单号查询费用
						Map<String, Object> condition=new HashMap<>();
						condition.put("waybillNo", fee.getExpressnum());
						FeesReceiveDispatchEntity feesReceiveDispatchEntity=feeInDistributionRepository.queryOne(condition);
						//免运费
						if(feesReceiveDispatchEntity!=null){
							totalMoney-=feesReceiveDispatchEntity.getAmount();
						}
						totalMoney-=fee.getPayMoney();
					}else{
						//不免运费
						totalMoney-=fee.getPayMoney();
					}
				}
			}
		}else if("Abnormal_Dispatch".equals(subjectCode)){ //配送理赔
			for(FeesAbnormalEntity fee:feeList){
				//非商家原因
				if("0".equals(fee.getIsDeliveryFree())){
					//免运费
					//根据运单号查询费用
					Map<String, Object> condition=new HashMap<>();
					condition.put("waybillNo", fee.getExpressnum());
					FeesReceiveDispatchEntity feesReceiveDispatchEntity=feeInDistributionRepository.queryOne(condition);
					//免运费
					totalMoney-=(fee.getPayMoney()+feesReceiveDispatchEntity.getAmount());
				}else{
					//不免运费
					totalMoney-=fee.getPayMoney();
				}				
			}	
		}	
		return totalMoney;
	}

	@Override
	public void updateAbnormalTransportBillSubject(
			Map<String, Object> conditionMap) {
		 bmsBillSubjectInfoRepository.updateAbnormalTransportBillSubject(conditionMap);		
	}

	@Override
	public void updateTransportBillSubject(Map<String, Object> conditionMap) {
		 bmsBillSubjectInfoRepository.updateTransportBillSubject(conditionMap);
	}
}
