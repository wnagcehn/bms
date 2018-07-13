/**
 * Copyright (c) 2016, Jiuye SCM and/or its affiliates. All rights reserved.
 *
 */
package com.jiuyescm.bms.bill.receive.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.web.DoradoContext;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.entity.BmsBillSubjectInfoEntity;
import com.jiuyescm.bms.bill.receive.repository.IBmsBillInfoRepository;
import com.jiuyescm.bms.bill.receive.service.IBmsBillSubjectInfoService;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.service.IBillLogRecordService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.repository.IFeesAbnormalRepository;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.dispatch.repository.IFeesReceiveDispatchRepository;
import com.jiuyescm.bms.fees.feesreceivedeliver.repository.IFeesReceiveDeliverDao;
import com.jiuyescm.bms.fees.storage.repository.IFeesReceiveStorageRepository;
import com.jiuyescm.cfm.common.JAppContext;

/**
 * 
 * @author stevenl
 * 
 */
@Controller("bmsBillSubjectInfoController")
public class BmsBillSubjectInfoController {

	private static final Logger logger = Logger.getLogger(BmsBillSubjectInfoController.class.getName());

	@Resource
	private IBmsBillSubjectInfoService bmsBillSubjectInfoService;
	
	@Autowired
	private IBillLogRecordService billLogRecordService;
	
	@Autowired
    private IBmsBillInfoRepository bmsBillInfoRepository;
	
	@Autowired
    private IFeesReceiveStorageRepository feesReceiveStorageRepository;
	
	@Autowired
	private IFeesReceiveDispatchRepository feeInDistributionRepository;
	
	@Autowired
	private IFeesReceiveDeliverDao feesReceiveDeliverDao;
	
	@Autowired 
	private IFeesAbnormalService feesAbnormalService;

	@DataProvider
	public BmsBillSubjectInfoEntity findById(Long id) throws Exception {
		BmsBillSubjectInfoEntity entity = null;
		entity = bmsBillSubjectInfoService.findById(id);
		return entity;
	}

	@DataProvider
	public Map<String,String> getBmsBillSubjectStatus(){
		return BillFeesSubjectStatusEnum.getMap();
	}
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<BmsBillSubjectInfoEntity> page, Map<String, Object> param) {
		PageInfo<BmsBillSubjectInfoEntity> pageInfo = bmsBillSubjectInfoService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}

	@DataResolver
	public void save(BmsBillSubjectInfoEntity entity) {
		if (entity.getId() == null) {
			bmsBillSubjectInfoService.save(entity);
		} else {
			bmsBillSubjectInfoService.update(entity);
		}
	}

	@DataResolver
	public void delete(BmsBillSubjectInfoEntity entity) {
		bmsBillSubjectInfoService.delete(entity.getId());
	}


	
	@DataProvider
	public List<BmsBillSubjectInfoEntity> queryAllByBillNo(Map<String,Object> parameter) throws Exception{

		List<BmsBillSubjectInfoEntity> billList= bmsBillSubjectInfoService.queryAllByBillNoAndwarehouse(parameter);
		/*
		List<BmsBillSubjectInfoEntity> updateList=new ArrayList<BmsBillSubjectInfoEntity>();
		
		for(int i=0;i<billList.size();i++){
			BmsBillSubjectInfoEntity bill=billList.get(i);	
			//仓储
			if("STORAGE".equals(bill.getFeesType())){	
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("billNo", bill.getBillNo());
				param.put("warehouseCode", bill.getWarehouseCode());
				if("Abnormal_Storage".equals(bill.getSubjectCode())){//仓储理赔
					param.put("subjectCode", "wh_abnormal_pay");
				}else{
					param.put("subjectCode", bill.getSubjectCode());
				}
				BmsBillSubjectInfoEntity subjectInfoEntity=feesReceiveStorageRepository.sumSubjectMoney(param);		
				if(subjectInfoEntity!=null){
					Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
					Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
					bill.setTotalAmount(totalMoney);
					bill.setDerateAmount(derateAmount);
					bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
					bill.setNum(subjectInfoEntity.getNum());
				}
			}
			//运输
			if("TRANSPORT".equals(bill.getFeesType())){
				Map<String, Object> param=new HashMap<String, Object>();
				param.put("billNo", bill.getBillNo());
				if("Abnormal_Transport".equals(bill.getSubjectCode())){
					param.put("subjectCode", "ts_abnormal_pay");
				}else{
					param.put("subjectCode", bill.getSubjectCode());
				}
				BmsBillSubjectInfoEntity subjectInfoEntity=feesReceiveDeliverDao.sumSubjectMoney(param);		
				if(subjectInfoEntity!=null){				
					Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
					Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
					bill.setTotalAmount(totalMoney);
					bill.setDerateAmount(derateAmount);
					bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
					bill.setNum(subjectInfoEntity.getNum());
				}
			}
			//配送
			if("DISPATCH".equals(bill.getFeesType())){
				if("Abnormal_Dispatch".equals(bill.getSubjectCode())){//配送理赔
					FeesAbnormalEntity abnormalEntity=feesAbnormalService.sumDispatchAmount(bill.getBillNo());
					if(abnormalEntity!=null){
						bill.setTotalAmount(-abnormalEntity.getPayMoney());
						bill.setDerateAmount(0.0);
						bill.setReceiptAmount(abnormalEntity.getPayMoney()-abnormalEntity.getDerateAmount());		
						bill.setNum(abnormalEntity.getNum());
					}
				}else if("Abnormal_DisChange".equals(bill.getSubjectCode())){
					FeesAbnormalEntity abnormalEntity=feesAbnormalService.sumDispatchChangeAmount(bill.getBillNo());
					if(abnormalEntity!=null){
						bill.setTotalAmount(-abnormalEntity.getPayMoney());
						bill.setDerateAmount(0.0);
						bill.setReceiptAmount(abnormalEntity.getPayMoney()-abnormalEntity.getDerateAmount());		
						bill.setNum(abnormalEntity.getNum());
					}
				}else{
					Map<String, Object> param=new HashMap<String, Object>();
					param.put("billNo", bill.getBillNo());
					param.put("warehouseCode", bill.getWarehouseCode());
					param.put("subjectCode", bill.getSubjectCode());
					BmsBillSubjectInfoEntity subjectInfoEntity=feeInDistributionRepository.sumSubjectMoney(param);		
					if(subjectInfoEntity!=null){
						Double totalMoney=subjectInfoEntity.getTotalAmount()==null?0:subjectInfoEntity.getTotalAmount();
						Double derateAmount=subjectInfoEntity.getDerateAmount()==null?0:subjectInfoEntity.getDerateAmount();					
						bill.setTotalAmount(totalMoney);
						bill.setDerateAmount(derateAmount);
						bill.setReceiptAmount(totalMoney-derateAmount-bill.getDiscountAmount());		
						bill.setNum(subjectInfoEntity.getNum());
					}
				}
			}
			updateList.add(bill);			
		}
		
		bmsBillSubjectInfoService.updateSubjectList(updateList);
		*/
		return billList;
	}
	/**
	 * 折扣账单
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
    public void discountBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bmsBillSubjectInfoService.discountBill(bill);
		
		//添加日志
		BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
		logEntity.setBillNo(bill.getBillNo());
		logEntity.setBillName(bill.getBillName());
		logEntity.setOperate("折扣");
		logEntity.setRemark(bill.getDiscountAmount()+"");
		logEntity.setCreator(JAppContext.currentUserName());
		logEntity.setCreateTime(JAppContext.currentTimestamp());
		billLogRecordService.log(logEntity);
	}
	
	/**
	 * 折扣运输账单
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void discountTransportBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bmsBillSubjectInfoService.discountBill(bill);
	}
	
	/**
	 * 折扣配送账单
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void discountDispatchBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bmsBillSubjectInfoService.discountBill(bill);
	}
	/**
	 * 清空仓储科目账单  账单费用置0,重新计算总账单  剔除 费用账单编号
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void deleteStorageBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setStatus(BillFeesSubjectStatusEnum.DELETE.getCode());
		bill.setTotalAmount(0.00);
		bill.setDerateAmount(0.00);
		bill.setDiscountAmount(0.00);
		bill.setReceiptAmount(0.00);
		bill.setDiscountRate(0.00);
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bill.setNum(0.00);
		bill.setRemark("清空账单");
		bmsBillSubjectInfoService.deleteStorageBill(bill);
		//剔除费用
		//仓储
		feesReceiveStorageRepository.deleteStorageBill(bill.getBillNo(), bill.getWarehouseCode(), bill.getSubjectCode());
	}
	
	/**
	 * 清空运输科目账单  账单费用置0,重新计算总账单  剔除 费用账单编号
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void deleteTransportBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setStatus(BillFeesSubjectStatusEnum.DELETE.getCode());
		bill.setTotalAmount(0.00);
		bill.setDerateAmount(0.00);
		bill.setDiscountAmount(0.00);
		bill.setReceiptAmount(0.00);
		bill.setDiscountRate(0.00);
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bill.setNum(0.00);
		bill.setRemark("清空账单");
		bmsBillSubjectInfoService.deleteTransportBill(bill);
		if(StringUtils.isNotBlank(bill.getSubjectCode())&&
				bill.getSubjectCode().equals("Abnormal_Transport")){
			feesReceiveDeliverDao.deleteAbnormalTransportBill(bill.getBillNo());
		}else{
			feesReceiveDeliverDao.deleteTransportBill(bill.getBillNo());

		}
	}
	/**
	 * 清空配送账单  账单费用置0,重新计算总账单  剔除 费用账单编号
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void deleteDispatchBill(BmsBillSubjectInfoEntity bill)throws Exception{
		bill.setStatus(BillFeesSubjectStatusEnum.DELETE.getCode());
		bill.setTotalAmount(0.00);
		bill.setDerateAmount(0.00);
		bill.setDiscountAmount(0.00);
		bill.setReceiptAmount(0.00);
		bill.setDiscountRate(0.00);
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bill.setNum(0.00);
		bill.setRemark("清空账单");
		bmsBillSubjectInfoService.deleteDispatchBill(bill);
		//删除费用
		//配送
		feeInDistributionRepository.deleteDispatchBill(bill.getBillNo(), bill.getWarehouseCode(), bill.getSubjectCode());
	}
	
	/**
	 * 清空理赔账单  账单费用置0,重新计算总账单  剔除 费用账单编号
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void deleteAbnormalBill(BmsBillSubjectInfoEntity bill)throws Exception{
		bill.setStatus(BillFeesSubjectStatusEnum.DELETE.getCode());
		bill.setTotalAmount(0.00);
		bill.setDerateAmount(0.00);
		bill.setDiscountAmount(0.00);
		bill.setReceiptAmount(0.00);
		bill.setDiscountRate(0.00);
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		bill.setNum(0.00);
		bill.setRemark("清空账单");
		bmsBillSubjectInfoService.deleteAbnormalBill(bill);	
		//删除费用
		feesAbnormalService.deleteAbnormalBill(bill.getBillNo(), bill.getWarehouseCode(), bill.getSubjectCode());
	}

	
	/**
	 * 重新生成仓储账单
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void reCountStorageBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		//删除状态下重新生成仓储账单
		if(bill.getStatus().equals(BillFeesSubjectStatusEnum.DELETE.getCode())){
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountStorageDeleteBill(bill);
		}else{//更新状态下重新生成账单
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountStorageUpdateBill(bill);
		}
	}
	
	/**
	 * 重新生成运输账单
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void reCountTransportBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		//删除状态下重新生成仓储账单
		if(bill.getStatus().equals(BillFeesSubjectStatusEnum.DELETE.getCode())){
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountTransportDeleteBill(bill);
		}else{//更新状态下重新生成账单
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountTransportUpdateBill(bill);
		}
	}
	
	/**
	 * 重新生成配送账单
	 * @param bill
	 * @throws Exception 
	 */
	@DataResolver
	public void reCountDispatchBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		//删除状态下重新生成仓储账单
		if(bill.getStatus().equals(BillFeesSubjectStatusEnum.DELETE.getCode())){
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountDispatchDeleteBill(bill);
		}else{//更新状态下重新生成账单
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountDispatchUpdateBill(bill);
		}
	}
	
	
	/**
	 * 重新生成仓储配送理赔费用账单
	 * @param bill
	 * @throws Exception
	 */
	@DataResolver
	public void reCountAbnormalBill(BmsBillSubjectInfoEntity bill) throws Exception{
		bill.setLastModifier(JAppContext.currentUserName());
		bill.setLastModifyTime(JAppContext.currentTimestamp());
		//删除状态
		if(bill.getStatus().equals(BillFeesSubjectStatusEnum.DELETE.getCode())){
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountAbnormalDeleteBill(bill);
		}else{//更新状态
			bill.setStatus(BillFeesSubjectStatusEnum.GENERATED.getCode());
			bmsBillSubjectInfoService.reCountAbnormalUpdateBill(bill);
		}
	}
	
	/**
	 * 宅配对账模板下载
	 * @param param
	 * @return
	 * @throws IOException
	 */
	@FileProvider
	public DownloadFile downloadTemplate(Map<String, Object> param) throws IOException {
		InputStream is = DoradoContext.getCurrent().getServletContext().getResourceAsStream("/WEB-INF/templates/bill/billdispatchdistinct_template.xlsx");
		return new DownloadFile("九曳宅配对账单导入模板.xlsx", is);
	}

	
	/*public double getAbnormalMoney(Map<String, Object> param,String subjectCode){
		Double totalMoney=0d;
		List<FeesAbnormalEntity> feeList=feesAbnormalRepository.queryFeeBySubject(param);	
		eg1：如果账单总额 100 元，某单赔偿金额为10元，运费5元（免运费），且为非商家原因；
		最终账单金额= 100 -（10 +5）
		eg2：如果账单总额为100元，某单赔偿金额为10元，运费5元（免运费 ），且为商家原因；
                    最终账单金额 = 100+10（无论是否免运费）
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
	}*/
}
