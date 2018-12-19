package com.jiuyescm.bms.base.dictionary.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.ewarehouse.entity.PubElecWarehouseEntity;
import com.jiuyescm.bms.base.ewarehouse.service.IEwareHouseService;
import com.jiuyescm.bms.common.enumtype.BadBillTypeEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckInvoiceStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckProcessStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckReceiptStateEnum;
import com.jiuyescm.bms.common.enumtype.BillCheckStateEnum;
import com.jiuyescm.bms.common.enumtype.BillReceiptFollowStateEnum;
import com.jiuyescm.bms.common.enumtype.BizState;
import com.jiuyescm.bms.common.enumtype.BothTypeEnum;
import com.jiuyescm.bms.common.enumtype.CalculateState;
import com.jiuyescm.bms.common.enumtype.CheckBillStatusEnum;
import com.jiuyescm.bms.common.enumtype.ContractAbnormalTypeEnum;
import com.jiuyescm.bms.common.enumtype.ImpState;
import com.jiuyescm.bms.common.enumtype.IsState;
import com.jiuyescm.bms.common.enumtype.OrderStatus;
import com.jiuyescm.bms.common.enumtype.TemplateTypeEnum;
import com.jiuyescm.constants.BmsEnums;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.SystemCodeVo;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;

@Component("bmsDictionaryEnum")
public class BmsDictionaryEnum {

	@Resource private ISystemCodeService systemCodeService;
	
	@Resource private ICarrierService carrierService;
	
	@Resource private IDeliverService deliverService;
	@Resource 
	private IEwareHouseService elecWareHouseService;
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	
	/**
	 * 通用数据字典枚举（类型有参数指定）
	 * @param typeCode 数据类型
	 * @return Map<String, String>
	 */
	@DataProvider
	public Map<String, String> getDictionaryEnum(String typeCode) {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList(typeCode);
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	@DataProvider  
	public Map<String, String> getCalculateStateEnum() {  
		//return CalculateState.
		return CalculateState.getMap();
	}
	
	@DataProvider
	public Map<Integer, String> getBothTypeEnum() {  
		//return CalculateState.
		return BothTypeEnum.getMap();
	}
	
	@DataProvider
	public Map<String, String> getDataStateEnum() {  
		//return CalculateState.
		return BizState.getMap();
	}
	
	@DataProvider
	public Map<String, String> getOrderStatusEnum() {  
		//return CalculateState.
		return OrderStatus.getMap();
	}
	
	@DataProvider
	public Map<String, String> getFileAsynType() {  
		//return CalculateState.
		return BmsEnums.taskStatus.getMap();
	}
	
	/**
	 * 电商平台枚举
	 * @return Map<String, String>
	 */
	@DataProvider
	public Map<String, String> getPlatFormEnum() {  
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("PLATFORM_ID");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 获取配送物流商
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCarrierIdList(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_COMPANY");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				if (null != systemCodeList.get(i).getExtattr1()) {
					map.put(systemCodeList.get(i).getExtattr1(), systemCodeList.get(i).getCodeName());
				}
			}
		}
		return map;
	}
	
	/**
	 * 获取oms维护的物流商
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<String, String> getOmsCarrierList(String all){
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		List<CarrierVo> list = carrierService.queryAllCarrier();
		if (null != list && list.size()>0) {
			for (CarrierVo carrierVo : list) {
				map.put(carrierVo.getCarrierid(), carrierVo.getName());
			}
		}
		
		return map;
	}
	
	/**
	 * 获取oms维护的宅配商
	 * @param all
	 * @return
	 */
	@DataProvider
	public Map<String, String> getOmsDeliveryList(String all){
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		List<DeliverVo> deliverList = deliverService.queryAllDeliver();
		if (null != deliverList && deliverList.size()>0) {
			for (DeliverVo deliverVo : deliverList) {
				map.put(deliverVo.getDeliverid(), deliverVo.getDelivername());
			}
		}
		
		return map;
	}
	
	/**
	 * 获取电商仓库
	 * @return
	 */
	@DataProvider
	public Map<String, String> getEWareHouseList(){
		Map<String, String> map =new LinkedHashMap<String,String>();
		//电商仓库
		PageInfo<PubElecWarehouseEntity> ewareHousePageInfo = elecWareHouseService.queryAll(null, 0, Integer.MAX_VALUE);
		if (null != ewareHousePageInfo && ewareHousePageInfo.getList().size() > 0) {
			for (PubElecWarehouseEntity elecWareHouse : ewareHousePageInfo.getList()) {
				if (null != elecWareHouse) {
					map.put(elecWareHouse.getWarehouseName(), elecWareHouse.getWarehouseName());
				}
			}
		}
		return map;
	}
	
	/**
	 * 温度类型
	 * @return
	 */
	@DataProvider
	public Map<String,String> getTemperature(String all){
		Map<String,String> map = new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				if (null != systemCodeList.get(i).getExtattr1()) {
					map.put(systemCodeList.get(i).getExtattr1(), systemCodeList.get(i).getCodeName());
				}
			}
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getImpStateEnum(){
		return ImpState.getMap();
	}
	
	@DataProvider
	public Map<String, String> getIsStateEnum(){
		return IsState.getMap();
	}
	
	@DataProvider
	public Map<String, String> getTemplateTypeMap(){
		return TemplateTypeEnum.getMap();
	}
	
	/**
	 * new账单状态
	 */
	@DataProvider
	public Map<String, String> getBillStatusEnum(String all) {  
		Map<String, String> map = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("BILL_STATUS");
		for (SystemCodeEntity code : codeList){
			map.put(code.getCode(), code.getCodeName());
		}
		return map;
	}
	
	/**
	 * new账单审批方式
	 */
	@DataProvider
	public Map<String, String> getApprovalWay() {  
		Map<String, String> map = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("APPROVALWAY");
		for (SystemCodeEntity code : codeList){
			map.put(code.getCode(), code.getCodeName());
		}
		return map;
	}
	
	/**
	 * 获取合同异常类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getContractAbnormalTypeMap(){
		return ContractAbnormalTypeEnum.getMap();
	}
	
	/**
	 * 对账状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBillCheckStateMap(){
		return BillCheckStateEnum.getMap();
	}
	
	/**
	 * 开票状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBillCheckInvoiceStateMap(){
		return BillCheckInvoiceStateEnum.getMap();
	}
	
	/**
	 * 处理状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBillCheckProcessMap(){
		return BillCheckProcessStateEnum.getMap();
	}
	
	/**
	 * 账单状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCheckBillStatusMap(){
		return CheckBillStatusEnum.getMap();
	}
	
	/**
	 * 坏账状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBadBillTypeMap(){
		return BadBillTypeEnum.getMap();
	}
	
	
	/**
	 * 回款状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBillReceiptFollowStateEnum(){
		return BillReceiptFollowStateEnum.getMap();
	}
	
	/**
	 * 收款状态
	 * @return
	 */
	@DataProvider
	public Map<String, String> getBillCheckReceiptStateEnum(){
		return BillCheckReceiptStateEnum.getMap();
	}
	
	/**
	 * 耗材类型 mdm
	 */
	@DataProvider
	public Map<String, String> getMaterialTypeEnum(String all) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		List<SystemCodeVo> codeList = pubMaterialInfoService.findEnumList("PACKMAGERIAL_TYPE");
		for (SystemCodeVo code : codeList){
			map.put(code.getCode(), code.getCodeName());
		}
		return map;
	}
	
	
	@DataProvider
	public Map<String,String> getValue(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("0", "是");
		mapValue.put("1", "否");
		return mapValue;
	}
}
