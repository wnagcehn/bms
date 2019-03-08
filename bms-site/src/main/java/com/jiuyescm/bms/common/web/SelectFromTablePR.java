package com.jiuyescm.bms.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.config.service.IBmsWarehouseConfigService;
import com.jiuyescm.bms.base.config.vo.BmsWarehouseConfigVo;
import com.jiuyescm.bms.base.customer.entity.PubCustomerEntity;
import com.jiuyescm.bms.base.customer.repository.IPubCustomerRepository;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.jywarehouse.web.BmsWarehouseVo;
import com.jiuyescm.bms.pub.transport.entity.PubTransportProductTypeEntity;
import com.jiuyescm.bms.pub.transport.service.IPubTransportProductTypeService;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.common.ConstantInterface;
import com.jiuyescm.mdm.carrier.api.ICarrierService;
import com.jiuyescm.mdm.carrier.vo.CarrierVo;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Component("selFromTablePR")
public class SelectFromTablePR {
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	

	@Autowired private IWarehouseService warehouseService;
	@Autowired private ICarrierService carrierService;
	@Autowired private IBmsWarehouseConfigService bmsWarehouseConfigService;

	@Autowired 
	private ICustomerService customerService;
	@Resource
	private IPubTransportProductTypeService productTypeService;
	@Autowired
	private IPubCustomerRepository pubCustomerRepository;
	
	@DataProvider
	public Map<String, String> getBusinessTypeList(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("BUSSINESS_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getBusinessPayTypeList(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("BUSSINESS_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				if(!"STORAGE".equals(systemCodeList.get(i).getCode())){
					map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
				}
			}
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getPlatformIdList(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("PLATFORM_ID");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	
	@DataProvider
	public Map<String, String> getPriceFeeTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_PRICE_SUBJECT");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	
	/**
	 * 地域类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getDispatchAreaTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_AREA_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	

	@DataProvider
	public Map<String, String> getDispatchTime(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_TIME");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 获取配送所有的配送公司类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getDispatchTypeList(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_COMPANY");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("ALL".equalsIgnoreCase(all)) {
			map.put("ALL", "全部");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 温度类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getTemperatureTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	/**
	 * 计费单位
	 * @return
	 */
	@DataProvider
	public Map<String, String> getChargeUnitList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("CHARGE_UNIT");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	/**
	 * 车型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getCarModelList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("MOTORCYCLE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	//====================================以下是运输相关的数据字段=====================================================================
	/**
	 * 产品类型PRODUCT_CATEGORY_TYPE(商品大类：肉类、海鲜、水果、奶制品、蔬菜、鲜花、其他；)
	 */
	@DataProvider
	public Map<String, String> getProductCategoryTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("PRODUCT_CATEGORY_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	/**
	 * 运输产品类型TRANSPORT_PRODUCT_TYPE： 城际专列、同城专列、电商专列、航线达 ； 
	 */
	@DataProvider
	public Map<String, String> getTransportProductTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				//不需要增值费
				if(!StringUtils.equalsIgnoreCase(systemCodeList.get(i).getCode(), "ts_value_add_subject")){
					map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
				}
			}
		}
		return map;
	}
	/**
	 * 运输类型(包含增值)
	 */
	@DataProvider
	public Map<String, String> getTransportTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_PRODUCT_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	/**
	 * 运输方式TRANSPORT_FORM_TYPE ： 整车、零担； 
	 */
	@DataProvider
	public Map<String, String> getTransportFormTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_FORM_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	

	/**
	 * 运输报价模板类型： 运输运费报价模板、运输增值费报价模板
	 */
	@DataProvider
	public Map<String, String> getTransportTemplateTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TRANSPORT_TEMPLATE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 运输增值费用科目ts_value_add_subject：提货费、送货费、装卸费、逆向物流费、延时等待费、缠绕膜费、理货费 ； 
	 */
	@DataProvider
	public Map<String, String> getTransportSubjectTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("ts_value_add_subject");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 运输服务类型	SERVICE_TYPE
	 */
	@DataProvider
	public Map<String, String> getTransportServiceTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("SERVICE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	//=========================================================================================================
	/**
	 * 仓储费用科目STORAGE_PRICE_SUBJECT 
	 */
	@DataProvider
	public Map<String, String> getStorageSubjectTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		Map<String, String> map =new LinkedHashMap<String,String>();
		map.put("全部", "全部");
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 仓储费用科目STORAGE_PRICE_SUBJECT 
	 */
	@DataProvider
	public Map<String, String> getStorageAddValue(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("STORAGE_ADD_VALUE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 仓储费用科目STORAGE_PRICE_SUBJECT 
	 */
	@DataProvider
	public Map<String, String> getStorageAllSubjectType(){
		List<SystemCodeEntity> systemCodeList1 = systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");

		List<SystemCodeEntity> systemCodeList2 = systemCodeService.findEnumList("wh_value_add_subject");
		Map<String, String> map =new LinkedHashMap<String,String>();
		map.put("全部", "全部");
		
		if(systemCodeList1!=null && systemCodeList1.size()>0){
			for(int i=0;i<systemCodeList1.size();i++){
				map.put(systemCodeList1.get(i).getCode(), systemCodeList1.get(i).getCodeName());
			}
		}
		if(systemCodeList2!=null && systemCodeList2.size()>0){
			for(int i=0;i<systemCodeList2.size();i++){
				map.put(systemCodeList2.get(i).getCode(), systemCodeList2.get(i).getCodeName());
			}
		}
		return map;
	}
	

	/**
	 * 获取所有的合同类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getContractTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("CONTRACT_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 获取所有的合同类型
	 * @return
	 */
	@DataProvider
	public Map<String, String> getFeesTypeList(){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("FEES_RECEIVE_TYPE");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getJyWarehouse() throws Exception {
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		List<BmsWarehouseConfigVo> configList=bmsWarehouseConfigService.queryAll();
		List<BmsWarehouseVo> reList=new ArrayList<BmsWarehouseVo>();
		for(WarehouseVo entity:warehouseVos){
			BmsWarehouseVo vo=new BmsWarehouseVo();
			vo.setIsDropDisplay(0);
			vo.setDisplayLevel(0);
			for(BmsWarehouseConfigVo configVo:configList){
				if(configVo.getWarehouseCode().equals(entity.getWarehouseid())){
					vo.setDisplayLevel(configVo.getDisplayLevel());
					vo.setIsDropDisplay(configVo.getIsDropDisplay());
					break;
				}
			}
			vo.setWarehouseid(entity.getWarehouseid());
			vo.setWarehousename(entity.getWarehousename());
			if(vo.getIsDropDisplay()!=1){
				reList.add(vo);
			}
		}
		//冒泡排序  倒序排序 最大值最前面
		for(int i=0;i<reList.size()-1;i++){
			for(int j=1;j<reList.size()-i;j++){
				if(reList.get(j-1).getDisplayLevel().intValue()<reList.get(j).getDisplayLevel().intValue()){
					BmsWarehouseVo voTemp=null;
					voTemp=reList.get(j-1);
					reList.set((j-1), reList.get(j));
					reList.set(j, voTemp);
				}
			}
		}
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (BmsWarehouseVo warehouseVo : reList) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getCarrierMap() {
		String userid=JAppContext.currentUserID();
		Map<String, Object> parameter=new HashMap<String,Object>();
		parameter.put("userid", userid);
		PageInfo<CarrierVo> tmpPageInfo = carrierService.queryCarrier(parameter, 0, Integer.MAX_VALUE);
		
		Map<String, String> map = new LinkedHashMap<String,String>();
		if(tmpPageInfo != null && tmpPageInfo.getList() != null && tmpPageInfo.getList().size()>0){
			for (CarrierVo carrierVo : tmpPageInfo.getList()) {
				map.put(carrierVo.getShortname(), carrierVo.getName());
			}
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getAllCarrierMap(){
		List<CarrierVo> list=carrierService.queryAllCarrier();
		Map<String, String> map=new HashMap<String,String>();
		for(CarrierVo vo:list){
			map.put(vo.getCarrierid(),vo.getName());
		}
		return map;
	}
	
	/**
	 *  配送模板类型
	 */
	@DataProvider
	public Map<String, String> getDispatchTemplateTypeList(String result){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList(result);
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	/**
	 *  判断费用科目
	 */
	@DataProvider
	public Map<String, String> getDispatchSubjectTypeList(){
		List<SystemCodeEntity> systemCodeList1 = systemCodeService.findEnumList("DISPATCH_OTHER_SUBJECT_TYPE");
		List<SystemCodeEntity> systemCodeList2 = systemCodeService.findEnumList("DISPATCH_COMPANY");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if(systemCodeList1!=null && systemCodeList1.size()>0){
			for(int i=0;i<systemCodeList1.size();i++){
				map.put(systemCodeList1.get(i).getCode(), systemCodeList1.get(i).getCodeName());
			}
		}		
		if(systemCodeList2!=null && systemCodeList2.size()>0){
			for(int i=0;i<systemCodeList2.size();i++){
				map.put(systemCodeList2.get(i).getCode(), systemCodeList2.get(i).getCodeName());
			}
		}
		return map;
	}
	
	
	/**
	 *  判断费用科目
	 */
	@DataProvider
	public List<SystemCodeEntity> getAllDeliver(){
		List<SystemCodeEntity> list=new ArrayList<SystemCodeEntity>();
		
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("DISPATCH_COMPANY");

		
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				list.add(systemCodeList.get(i));
			}
		}		
	
		return list;
	}
	
	/**
	 *  获取所有的配送产品类型
	 */
	@DataProvider
	public Map<String,Object> getTransportProductType(){
		Map<String, Object> map =new HashMap<String,Object>();
		List<PubTransportProductTypeEntity> list=productTypeService.query(map);
		
		Map<String,Object> mapList =new LinkedHashMap<String,Object>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				mapList.put(list.get(i).getProductTypeName(),list.get(i).getProductTypeName());
			}
		}		
		return mapList;
	}
	
	@DataProvider
	public Map<String, String> getBmsBillStatus(String all){
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("BILL_STATUS");
		Map<String, String> map =new LinkedHashMap<String,String>();
		if ("".equalsIgnoreCase(all)) {
			map.put("", "");
		}
		if(systemCodeList!=null && systemCodeList.size()>0){
			for(int i=0;i<systemCodeList.size();i++){
				map.put(systemCodeList.get(i).getCode(), systemCodeList.get(i).getCodeName());
			}
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getJyCustomer() {
		PageInfo<CustomerVo> customerList=customerService.query(null, 0, Integer.MAX_VALUE);
		List<CustomerVo> cList=customerList.getList();
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (CustomerVo vo : cList) {
			map.put(vo.getCustomerid(), vo.getCustomername());
		}
		return map;
	}
	
	@DataProvider
	public Map<String, String> getCustomer() {
		List<PubCustomerEntity> list = pubCustomerRepository.query(null);
		Map<String, String> map = new LinkedHashMap<String,String>();
		for (PubCustomerEntity vo : list) {
			map.put(vo.getCustomerId(), vo.getCustomerName());
		}
		return map;
	}
	
	/**
	 * 获取所有的运输费用科目
	 * @return
	 */
	@DataProvider
	public Map<String, String> getAllSubject(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> sList=systemCodeService.findEnumList("ts_base_subject");
		List<SystemCodeEntity> mList=systemCodeService.findEnumList("ts_value_add_subject");
		//List<BmsSubjectInfoEntity> subjectList=bmsSubjectInfoService.queryAll("");
		for(SystemCodeEntity en:sList){
			mapValue.put(en.getCode(),en.getCodeName());
		}
		for(SystemCodeEntity en:mList){
			mapValue.put(en.getCode(),en.getCodeName());
		}
		return mapValue;
	}
	
	/**
	 * 仓储科目
	 * @return
	 */
	@DataProvider
	public Map<String, Object> getStorageSubjectList(){
		Map<String, Object> mapValue = new LinkedHashMap<String, Object>();
		//基础费费用名称
		List<SystemCodeEntity> baseSubjectList= systemCodeService.findEnumList("STORAGE_PRICE_SUBJECT");
		//增值费费用名称
		List<SystemCodeEntity> otherSubjectList=systemCodeService.findEnumList("wh_value_add_subject");
		
		for(SystemCodeEntity en:baseSubjectList){
			mapValue.put(en.getCodeName(),en);
		}
		for(SystemCodeEntity en:otherSubjectList){
			mapValue.put(en.getCodeName(),en);
		}
		return mapValue;
	}
	
	/**
	 * 仓储增值科目
	 * @return
	 */
	@DataProvider
	public Map<String, Object> getStorageAddSubjectList(){
		Map<String, Object> mapValue = new LinkedHashMap<String, Object>();
		//增值费费用名称
		List<SystemCodeEntity> otherSubjectList=systemCodeService.findEnumList("wh_value_add_subject");
		
		for(SystemCodeEntity en:otherSubjectList){
			mapValue.put(en.getCode(),en.getCodeName());
		}
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
	
	@DataProvider
	public Map<String, String> getIsDifferent(String all) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		if ("ALL".equalsIgnoreCase(all)) {
			mapValue.put("ALL", "全部");
		}
		mapValue.put(ConstantInterface.Issplitboxflag.YES, "是");
		mapValue.put(ConstantInterface.Issplitboxflag.NO, "否");
		return mapValue;
	}
	
	/**
	 * 获取所有的费用类型
	 * @return
	 */
	@DataProvider
	public Map<String,String> getFeesTypeMap(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList("FEES_TYPE");
		for (SystemCodeEntity code : codeList){
			mapValue.put(code.getCode(), code.getCodeName());
		}
		return mapValue;
	}
}
