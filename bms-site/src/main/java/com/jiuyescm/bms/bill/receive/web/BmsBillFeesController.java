package com.jiuyescm.bms.bill.receive.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.bill.receive.service.IBmsBillSubjectInfoService;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectAbormalEnum;
import com.jiuyescm.bms.common.enumtype.BillFeesSubjectStatusEnum;
import com.jiuyescm.bms.common.log.entity.BmsBillLogRecordEntity;
import com.jiuyescm.bms.common.log.service.IBillLogRecordService;
import com.jiuyescm.bms.fees.IFeesReceiverDeliverService;
import com.jiuyescm.bms.fees.abnormal.entity.FeesAbnormalEntity;
import com.jiuyescm.bms.fees.abnormal.service.IFeesAbnormalService;
import com.jiuyescm.bms.fees.dispatch.entity.FeesReceiveDispatchEntity;
import com.jiuyescm.bms.fees.dispatch.service.IFeesReceiveDispatchService;
import com.jiuyescm.bms.fees.entity.FeesBillWareHouseEntity;
import com.jiuyescm.bms.fees.entity.FeesReceiveDeliverEntity;
import com.jiuyescm.bms.fees.storage.entity.FeesReceiveStorageEntity;
import com.jiuyescm.bms.fees.storage.service.IFeesReceiveStorageService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Controller("bmsBillFeesController")
public class BmsBillFeesController {

	@Resource
	private IFeesReceiveStorageService feesReceiveStorageService;

	@Autowired 
	private IWarehouseService warehouseService;
	
	@Resource
	private IBmsBillSubjectInfoService bmsBillSubjectInfoService;
	@Resource
	private IFeesReceiveDispatchService feesReceiveDispatchService;
	@Resource
	private IFeesAbnormalService feesAbnormalService;
	
	@Resource
	private  IFeesReceiverDeliverService feesReceiverDeliverService;
	
	@Autowired
	private IBillLogRecordService billLogRecordService;
	/**
	 * 仓储费明细分页
	 * 仓库、账单编号
	 */
	@DataProvider
	public void queryProductStoragePage(Page<FeesReceiveStorageEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if(parameter.containsKey("feesno")){
				String feesno=parameter.get("feesno").toString();
				if(!StringUtils.isBlank(feesno)){
					List<String> feesnoList=new ArrayList<String>();
					String[] arr = feesno.split("\n");
					for(String s:arr){
						feesnoList.add(s);
					}
					parameter.put("feesnoList", feesnoList);
				}
			}
			PageInfo<FeesReceiveStorageEntity> pageInfo =feesReceiveStorageService.queryProductStoragePage(parameter,page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
		
	}
	
	/**
	 * 仓储增值费明细分页
	 * 仓库、账单编号
	 */
	@DataProvider
	public void queryStorageAddFeePage(Page<FeesReceiveStorageEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if(parameter.containsKey("feesno")){
				String feesno=parameter.get("feesno").toString();
				if(!StringUtils.isBlank(feesno)){
					List<String> feesnoList=new ArrayList<String>();
					String[] arr = feesno.split("\n");
					for(String s:arr){
						feesnoList.add(s);
					}
					parameter.put("feesnoList", feesnoList);
				}
			}
			PageInfo<FeesReceiveStorageEntity> pageInfo =feesReceiveStorageService.queryStorageAddFeePage(parameter,page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
		
	}
	
	/**
	 *查询 订单操作明细
	 * 仓库、账单编号
	 */
	@DataProvider
	public void queryOutStoragePage(Page<FeesReceiveStorageEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if(parameter.containsKey("feesno")){
				String feesno=parameter.get("feesno").toString();
				if(!StringUtils.isBlank(feesno)){
					List<String> feesnoList=new ArrayList<String>();
					String[] arr = feesno.split("\n");
					for(String s:arr){
						feesnoList.add(s);
					}
					parameter.put("feesnoList", feesnoList);
				}
			}
			PageInfo<FeesReceiveStorageEntity> pageInfo =feesReceiveStorageService.queryProductStoragePage(parameter,page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
		
	}
	@DataProvider
	public void queryMaterialPage(Page<FeesReceiveStorageEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if(parameter.containsKey("feesno")){
				String feesno=parameter.get("feesno").toString();
				if(!StringUtils.isBlank(feesno)){
					List<String> feesnoList=new ArrayList<String>();
					String[] arr = feesno.split("\n");
					for(String s:arr){
						feesnoList.add(s);
					}
					parameter.put("feesnoList", feesnoList);
				}
			}
			PageInfo<FeesReceiveStorageEntity> pageInfo =feesReceiveStorageService.queryMaterialPage(parameter,page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	
	@DataProvider
	public void queryTransportPage(Page<FeesReceiveDeliverEntity> page,Map<String,Object> parameter){
		
		PageInfo<FeesReceiveDeliverEntity> pageInfo =feesReceiverDeliverService.queryTransportPage(parameter,page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	@DataProvider
	public void queryDispatchPage(Page<FeesReceiveDispatchEntity> page,Map<String,Object> parameter){
		if(parameter!=null){
			if(parameter.containsKey("feesno")){
				String feesno=parameter.get("feesno").toString();
				if(!StringUtils.isBlank(feesno)){
					List<String> feesnoList=new ArrayList<String>();
					String[] arr = feesno.split("\n");
					for(String s:arr){
						feesnoList.add(s);
					}
					parameter.put("feesnoList", feesnoList);
				}
			}
			PageInfo<FeesReceiveDispatchEntity> pageInfo =feesReceiveDispatchService.queryDispatchPage(parameter,page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				page.setEntities(pageInfo.getList());
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	/**
	 * 查询仓储 费用 
	 * 仓库 汇总信息
	 */
	
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseStorageAmount(String billNo){
		String feesType="STORAGE";
		List<FeesBillWareHouseEntity> list=bmsBillSubjectInfoService.querywarehouseAmount(billNo,feesType);
		List<WarehouseVo> warehouseList=warehouseService.queryAllWarehouse();
		for(FeesBillWareHouseEntity entity:list){
			for(WarehouseVo vo:warehouseList){
				if(vo.getWarehouseid().equals(entity.getWarehouseCode())){
					entity.setWarehouseName(vo.getWarehousename());
					break;
				}
			}
		}
		return list;
	}
	
	/**
	 * 查询仓储 费用 
	 * 仓库 汇总信息
	 */
	
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseDispatchAmount(String billNo){
		String feesType="DISPATCH";
		List<FeesBillWareHouseEntity> list=bmsBillSubjectInfoService.querywarehouseAmount(billNo,feesType);
		List<WarehouseVo> warehouseList=warehouseService.queryAllWarehouse();
		for(FeesBillWareHouseEntity entity:list){
			for(WarehouseVo vo:warehouseList){
				if(vo.getWarehouseid().equals(entity.getWarehouseCode())){
					entity.setWarehouseName(vo.getWarehousename());
					break;
				}
			}
		}
		return list;
	}
	
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseTransportAmount(String billNo){
		String feesType="TRANSPORT";
		List<FeesBillWareHouseEntity> list=bmsBillSubjectInfoService.querywarehouseAmount(billNo,feesType);
		List<WarehouseVo> warehouseList=warehouseService.queryAllWarehouse();
		for(FeesBillWareHouseEntity entity:list){
			for(WarehouseVo vo:warehouseList){
				if(vo.getWarehouseid().equals(entity.getWarehouseCode())){
					entity.setWarehouseName(vo.getWarehousename());
					break;
				}
			}
		}
		return list;
	}
	
	@DataProvider
	public List<FeesBillWareHouseEntity> querywarehouseAmount(String billNo){
		List<FeesBillWareHouseEntity> list=bmsBillSubjectInfoService.querywarehouseAmount(billNo);
		List<WarehouseVo> warehouseList=warehouseService.queryAllWarehouse();
		for(FeesBillWareHouseEntity entity:list){
			for(WarehouseVo vo:warehouseList){
				if(vo.getWarehouseid().equals(entity.getWarehouseCode())){
					entity.setWarehouseName(vo.getWarehousename());
					break;
				}
			}
		}
		return list;
	}
	/**
	 * 仓储理赔原因ID
	 * @return
	 */
	private List<String> getStorageReasonId(){
		List<String> list=new ArrayList<String>();
		list.add("56");//公司原因
		list.add("3");//仓库原因
		return list;
	}
	
	/**
	 * 配送理赔原因ID
	 * @return
	 */
	private List<String> getDispatchReasonId(){
		List<String> list=new ArrayList<String>();
		list.add("2");//承运商原因
		return list;
	}
	
	/**
	 * 配送理赔原因ID
	 * @return
	 */
	private List<String> getDispatchChangeReasonId(){
		List<String> list=new ArrayList<String>();
		list.add("4");//商家原因
		return list;
	}
	
	@DataProvider
	public void queryAbnormalDetailPage(Page<FeesAbnormalEntity> page,Map<String,Object> parameter){
		if(parameter != null){
			if(parameter.containsKey("feesno")){
				String feesno=parameter.get("feesno").toString();
				if(!StringUtils.isBlank(feesno)){
					List<String> feesnoList=new ArrayList<String>();
					String[] arr = feesno.split("\n");
					for(String s:arr){
						feesnoList.add(s);
					}
					parameter.put("feesnoList", feesnoList);
				}
			}
			String subjectCode="";
			if(parameter.containsKey("subjectCode")){
				subjectCode=parameter.get("subjectCode").toString();
			}
			if(parameter.containsKey("feesType")){
				String feesType=parameter.get("feesType").toString();
				
				if(!StringUtils.isBlank(feesType)){
					List<String> reasonIds=null;
					switch(feesType){
					case "DISPATCH":
						if("Abnormal_Dispatch".equals(subjectCode)){
							reasonIds=getDispatchReasonId();
						}else if("Abnormal_DisChange".equals(subjectCode)){
							reasonIds=getDispatchChangeReasonId();
						}else{
							return;
						}
						break;
					case "STORAGE":
						reasonIds=getStorageReasonId();
						break;
					}
					parameter.put("reasonIds", reasonIds);
				}
			}
			PageInfo<FeesAbnormalEntity> pageInfo = bmsBillSubjectInfoService.queryAbnormalDetailGroupPage(parameter, page.getPageNo(), page.getPageSize());
			if (pageInfo != null) {
				List<FeesAbnormalEntity> list=pageInfo.getList();
				for(FeesAbnormalEntity entity:list){
					if("0".equals(entity.getIsDeliveryFree())){
						entity.setReceiptAmount(entity.getPayMoney()-entity.getDerateAmount()-entity.getDeliveryCost());
					}else{
						entity.setReceiptAmount(entity.getPayMoney()-entity.getDerateAmount());
					}
				}
				page.setEntities(list);
				page.setEntityCount((int) pageInfo.getTotal());
			}
		}
	}
	
	/**
	 * 批量减免费用
	 * @param list
	 */
	@DataResolver
	public void derateBatchAmount(List<FeesReceiveStorageEntity> list){
		feesReceiveStorageService.derateBatchAmount(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate(list.get(0).getSubjectCode()+"批量减免费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
			
	}
	/**
	 * 批量剔除仓储费用
	 */
	@DataResolver
	public void deleteBatchFees(List<FeesReceiveStorageEntity> list){
		feesReceiveStorageService.deleteBatchFees(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate(list.get(0).getSubjectCode()+"批量剔除费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	/**
	 * 批量修改仓储费用
	 */
	@DataResolver
	public void addBatchFees(List<FeesReceiveStorageEntity> list){
		int num=feesReceiveStorageService.updateBatch(list); 
		if(num>=1){
			FeesReceiveStorageEntity fee=list.get(0);
			Map<String, Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("billNo", fee.getBillNo());
			conditionMap.put("warehouseCode", fee.getWarehouseCode());
			conditionMap.put("subjectCode", fee.getSubjectCode());
			conditionMap.put("status", BillFeesSubjectStatusEnum.UPDATE);
			
			bmsBillSubjectInfoService.updateBillSubject(conditionMap);
					
			//添加日志
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(fee.getBillNo());
			logEntity.setOperate(fee.getSubjectCode()+"批量新增费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);		
		}

	}
	
	/**
	 * 批量剔除运输费用
	 */
	@DataResolver
	public void deleteTransportBatchFees(List<FeesReceiveDeliverEntity> list){
		feesReceiverDeliverService.deleteBatchFees(list);
		
		FeesReceiveDeliverEntity fee=list.get(0);
		Map<String, Object> conditionMap=new HashMap<String, Object>();
		conditionMap.put("billNo", fee.getBillno());
		conditionMap.put("feesType", "TRANSPORT");
		//conditionMap.put("subjectCode", value)
		conditionMap.put("status", BillFeesSubjectStatusEnum.UPDATE);
		if("ts_abnormal_pay".equals(fee.getSubjectCode())){
			conditionMap.put("subjectCode", "Abnormal_Transport");
			bmsBillSubjectInfoService.updateAbnormalTransportBillSubject(conditionMap);
		}else{
			conditionMap.put("subjectCode", "Abnormal_Transport");
			bmsBillSubjectInfoService.updateTransportBillSubject(conditionMap);
		}
		
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillno());
			logEntity.setOperate("运输批量剔除费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	/**
	 * 批量修改运输费用
	 */
	@DataResolver
	public void addTransportBatchFees(List<FeesReceiveDeliverEntity> list){
		int num=feesReceiverDeliverService.updateBatchFees(list); 
		if(num>=1){
			FeesReceiveDeliverEntity fee=list.get(0);
			Map<String, Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("billNo", fee.getBillno());
			conditionMap.put("feesType", "TRANSPORT");
			conditionMap.put("status", BillFeesSubjectStatusEnum.UPDATE);
			if("ts_abnormal_pay".equals(fee.getSubjectCode())){
				conditionMap.put("subjectCode", "Abnormal_Transport");
				bmsBillSubjectInfoService.updateAbnormalTransportBillSubject(conditionMap);
			}else{
				conditionMap.put("subjectCode", "Abnormal_Transport");
				bmsBillSubjectInfoService.updateTransportBillSubject(conditionMap);
			}
					
			//添加日志
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(fee.getBillno());
			logEntity.setOperate("运输批量新增费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);		
		}

	}
	
	
	/**
	 * 批量减免配送费用
	 * @param list
	 */
	@DataResolver
	public void derateDispatchBatchAmount(List<FeesReceiveDispatchEntity> list){
		feesReceiveDispatchService.derateBatchAmount(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate(list.get(0).getCarrierName()+"批量减免费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	
	/**
	 * 批量剔除配送费用
	 */
	@DataResolver
	public void deleteDispatchBatchFees(List<FeesReceiveDispatchEntity> list){
		feesReceiveDispatchService.deleteBatchFees(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate(list.get(0).getCarrierName()+"批量剔除费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
		
	/**
	 * 批量新增配送费用
	 */
	@DataResolver
	public void addDispatchBatchFees(List<FeesReceiveDispatchEntity> list){
		int num=feesReceiveDispatchService.updateBatchTmp(list);
		//费用状态修改成功后更新费用科目
		if(num >= 1){
			FeesReceiveDispatchEntity fee=list.get(0);
			Map<String, Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("billNo", fee.getBillNo());
			conditionMap.put("warehouseCode", fee.getWarehouseCode());
			conditionMap.put("subjectCode", fee.getCarrierid());
			conditionMap.put("status", BillFeesSubjectStatusEnum.UPDATE);
			bmsBillSubjectInfoService.updateBillSubject(conditionMap);
			
			//添加日志
			if(list!=null && list.size()>0){
				BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
				logEntity.setBillNo(list.get(0).getBillNo());
				logEntity.setOperate(list.get(0).getCarrierName()+"批量新增费用");
				logEntity.setCreator(JAppContext.currentUserName());
				logEntity.setCreateTime(JAppContext.currentTimestamp());
				billLogRecordService.log(logEntity);	
			}
		}
		
	}
	
	/**
	 * 批量减免配送理赔费用
	 * @param list
	 */
	@DataResolver
	public void derateAbormalDispatchBatchAmount(List<FeesAbnormalEntity> list){
		feesAbnormalService.derateDispatchBatchAmount(list);	
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate("批量减免理赔费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	
	/**
	 * 批量剔除配送理赔费用
	 * @throws Exception 
	 */
	@DataResolver
	public void deleteAbormalDispatchBatchFees(List<FeesAbnormalEntity> list) throws Exception{
		feesAbnormalService.deleteDispatchBatchFees(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate("批量剔除理赔费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	
	/**
	 * 批量新增理赔费用-宅配
	 */
	@DataResolver
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void addAbormalDispatchBatchFees(List<FeesAbnormalEntity> list) throws Exception{
		int num = feesAbnormalService.updateBatchBillNo(list);
		if(num >= 1){
			FeesAbnormalEntity fee=list.get(0);
			String subjectCode = "";
			if(fee.getReasonId()==4){
				subjectCode=BillFeesSubjectAbormalEnum.Abnormal_DisChange.getCode();
			}else if(fee.getReasonId()==2){
				subjectCode=BillFeesSubjectAbormalEnum.Abnormal_Dispatch.getCode();
			}
			// 1、添加理赔费用到仓储费用表;
			addAbnormalToFees(subjectCode, list);
						
			// 2、费用状态修改成功后更新费用科目
			
			Map<String, Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("billNo", fee.getBillNo());
			conditionMap.put("warehouseCode", fee.getWarehouseId());
			conditionMap.put("subjectCode", subjectCode);
			conditionMap.put("status", BillFeesSubjectStatusEnum.UPDATE);
			bmsBillSubjectInfoService.updateBillSubject(conditionMap);
			//添加日志
			if(list!=null && list.size()>0){
				BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
				logEntity.setBillNo(list.get(0).getBillNo());
				logEntity.setOperate("批量新增理赔费用");
				logEntity.setCreator(JAppContext.currentUserName());
				logEntity.setCreateTime(JAppContext.currentTimestamp());
				billLogRecordService.log(logEntity);	
			}
		}
	}
	
	/**
	 * 批量减免仓储理赔费用
	 * @param list
	 */
	@DataResolver
	public void derateAbormalStorageBatchAmount(List<FeesAbnormalEntity> list){
		feesAbnormalService.derateStorageBatchAmount(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate("批量减免仓储理赔费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	
	/**
	 * 批量剔除仓储理赔费用
	 */
	@DataResolver
	public void deleteAbormalStorageBatchFees(List<FeesAbnormalEntity> list){
		feesAbnormalService.deleteStorageBatchFees(list);
		//添加日志
		if(list!=null && list.size()>0){
			BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
			logEntity.setBillNo(list.get(0).getBillNo());
			logEntity.setOperate("批量剔除仓储理赔费用");
			logEntity.setCreator(JAppContext.currentUserName());
			logEntity.setCreateTime(JAppContext.currentTimestamp());
			billLogRecordService.log(logEntity);	
		}
	}
	

	/**
	 * 批量新增理赔费用
	 */
	@DataResolver
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void addAbormalStorageBatchFees(List<FeesAbnormalEntity> list) throws Exception{
		int num = feesAbnormalService.updateBatchBillNo(list);
		if(num >= 1){
			// 添加理赔费用到仓储费用表
			String subjectCode = "Abnormal_Storage";
			addAbnormalToFees(subjectCode, list);
			
			// 费用状态修改成功后更新费用科目
			FeesAbnormalEntity fee=list.get(0);
			Map<String, Object> conditionMap=new HashMap<String, Object>();
			conditionMap.put("billNo", fee.getBillNo());
			conditionMap.put("warehouseCode", fee.getWarehouseId());
			conditionMap.put("subjectCode", subjectCode);
			conditionMap.put("status", BillFeesSubjectStatusEnum.UPDATE);
			bmsBillSubjectInfoService.updateBillSubject(conditionMap);
			
			// 添加日志
			if(list!=null && list.size()>0){
				BmsBillLogRecordEntity logEntity = new BmsBillLogRecordEntity();
				logEntity.setBillNo(list.get(0).getBillNo());
				logEntity.setOperate("批量新增理赔费用");
				logEntity.setCreator(JAppContext.currentUserName());
				logEntity.setCreateTime(JAppContext.currentTimestamp());
				billLogRecordService.log(logEntity);	
			}
		}
	}
	
	/**
	 * 1、处理理赔费用金额
	 * 2、更新异常费用到对应的仓储、配送费用表中
	 * @param bmsBillSubjectInfoEntity
	 * @return
	 */
	private void addAbnormalToFees(String subjectCode, List<FeesAbnormalEntity> list) throws Exception {
		List<FeesReceiveStorageEntity> feesReceiveStorageList = new ArrayList<FeesReceiveStorageEntity>();
		List<FeesReceiveDispatchEntity> feesReceiveDispatchList = new ArrayList<FeesReceiveDispatchEntity>();
		
		List<String> feeNoList = new ArrayList<String>();
		for (FeesAbnormalEntity abnormalEntity : list) {
			feeNoList.add(abnormalEntity.getFeeNo());
		}
		Map<String, Object> conditionMap=new HashMap<String, Object>();
		conditionMap.put("billNo", list.get(0).getBillNo());
		conditionMap.put("feeNos", feeNoList);
		List<FeesAbnormalEntity> feesAbnormalList = feesAbnormalService.queryByFeesNos(conditionMap);
		
		/*
		 * eg1：某单赔偿金额为10元，运费5元（免运费），且为非商家原因；
		 * 理赔金额= - 10 - 5
		 * eg2：某单赔偿金额为10元，运费5元（免运费 ），且为商家原因；
		 * 理赔金额 = 10 - 5
		 */
		String operatorName = JAppContext.currentUserName();
    	Timestamp operatorTime = JAppContext.currentTimestamp();
		for(FeesAbnormalEntity fee : feesAbnormalList){
			Double feeAmount = 0d;// 每条理赔费用金额
			
			if("Abnormal_Dispatch".equals(subjectCode) || "Abnormal_DisChange".equals(subjectCode)){ //配送理赔
				//商家原因
				if(4 == fee.getReasonId()){
					feeAmount += fee.getPayMoney();
				}else {
					feeAmount -= fee.getPayMoney();
					//非商家原因
					if("0".equals(fee.getIsDeliveryFree())){
						//免运费,根据运单号查询费用
						Map<String, Object> condition=new HashMap<>();
						condition.put("waybillNo", fee.getExpressnum());
						FeesReceiveDispatchEntity feesReceiveDispatchEntity = feesReceiveDispatchService.queryOne(condition);
						//免运费
						if(null != feesReceiveDispatchEntity) {
							double deliveryCost = (null == feesReceiveDispatchEntity.getAmount())? 0 : feesReceiveDispatchEntity.getAmount();
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
				feeAmount -= fee.getPayMoney();
				
				if("0".equals(fee.getIsDeliveryFree())){
					//免运费,根据运单号查询费用
					Map<String, Object> condition=new HashMap<>();
					condition.put("waybillNo", fee.getExpressnum());
					FeesReceiveDispatchEntity feesReceiveDispatchEntity = feesReceiveDispatchService.queryOne(condition);
					//免运费
					if (null != feesReceiveDispatchEntity) {
						double deliveryCost = (null == feesReceiveDispatchEntity.getAmount())? 0 : feesReceiveDispatchEntity.getAmount();
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
			feesReceiveStorageService.insertBatchTmp(feesReceiveStorageList);
		}
		if (null != feesReceiveDispatchList && feesReceiveDispatchList.size() > 0) {
			int updDispatchNum = feesReceiveDispatchService.insertBatchTmp(feesReceiveDispatchList);
			if (updDispatchNum != feesReceiveDispatchList.size()) {
				throw new BizException("理赔费用添加到宅配费用表失败!");
			}
		}
	}
	
	/**
	 * 运费分页
	 * 账单编号
	 */
	@DataProvider
	public void queryTransportFeePage(Page<FeesReceiveDeliverEntity> page,Map<String,Object> parameter){
		if(parameter==null){
			return;
		}
		String subjectCode="";
		if(parameter.containsKey("subjectCode")){
			subjectCode=parameter.get("subjectCode")==null?"":parameter.get("subjectCode").toString();
		}
		PageInfo<FeesReceiveDeliverEntity> pageInfo=null;
		if("Abnormal_Transport".equals(subjectCode)){//理赔
			pageInfo =feesReceiverDeliverService.queryAbnormalFeeByGroup(parameter,page.getPageNo(), page.getPageSize());
		}else{
			pageInfo =feesReceiverDeliverService.queryFeeByGroup(parameter,page.getPageNo(), page.getPageSize());
		}
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}	
	}
	
	/**
	 * 运费明细
	 * 账单编号
	 */
	@DataProvider
	public List<FeesReceiveDeliverEntity> queryTransportFeeDetail(Map<String,Object> parameter){
		if(parameter==null){
			return null;
		}
		String subjectCode="";
		if(parameter.containsKey("subjectCode")){
			subjectCode=parameter.get("subjectCode")==null?"":parameter.get("subjectCode").toString();
		}
		List<FeesReceiveDeliverEntity> list=null;
		if("ts_abnormal_pay".equals(subjectCode)){
			list =feesReceiverDeliverService.queryAbnormalFeeDetail(parameter);
		}else{
			list =feesReceiverDeliverService.queryFeeDetail(parameter);
		}
		return list;
	}
}
