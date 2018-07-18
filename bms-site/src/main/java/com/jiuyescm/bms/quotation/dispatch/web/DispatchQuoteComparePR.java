package com.jiuyescm.bms.quotation.dispatch.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;
import com.jiuyescm.bms.common.vo.ExportDataVoEntity;
import com.jiuyescm.bms.common.web.HttpCommanExport;
import com.jiuyescm.bms.common.web.HttpCommanImportCompare;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.BmsQuoteDispatchDetailVo;
import com.jiuyescm.bms.quotation.dispatch.entity.vo.PriceMainDispatchImportEntity;
import com.jiuyescm.bms.quotation.dispatch.service.IBmsQuoteDispatchDetailService;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DispatchQuoteCompareDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IAddressService;
import com.jiuyescm.mdm.deliver.api.IDeliverService;
import com.jiuyescm.mdm.deliver.vo.DeliverVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
/**
 * 导入比较文件
 * @author Wuliangfeng
 *
 */
@Controller("dispatchQuoteComparePR")
public class DispatchQuoteComparePR extends HttpCommanImportCompare<BmsQuoteDispatchDetailVo,BmsQuoteDispatchDetailVo> {

	@Resource
	private IBmsQuoteDispatchDetailService bmsQuoteDispatchDetailService;
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private IAddressService addressService;
	
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource 
	private IDeliverService deliverService;
	
	/**
	 * 获取系统报价
	 */
	@Override
	protected List<BmsQuoteDispatchDetailVo> getOrgList(
			Map<String, Object> parameter) {
		List<BmsQuoteDispatchDetailVo> list = null;
		try {
			list = bmsQuoteDispatchDetailService.queryAllById(parameter);
			List<WarehouseVo> warehouseVoList=warehouseService.queryAllWarehouse();
			List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
			List<SystemCodeEntity> areaTypeCodeList = systemCodeService.findEnumList("DISPATCH_AREA_TYPE");
			List<SystemCodeEntity> timelinessCodeList = systemCodeService.findEnumList("DISPATCH_TIME");
			for(BmsQuoteDispatchDetailVo entity:list){
				setWareHouseName(entity,warehouseVoList);//调用MDM 设置仓配名称
				setAddress(entity);//设置省市区名称
				setTemperatureTypeCode(entity,temperatureList);//设置温度类别名称
				setAreaTypeCode(entity,areaTypeCodeList);//设置地域类型名称
				setTimeliness(entity,timelinessCodeList);//设置时效
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 设置时效
	 * @param list
	 */
	private void setTimeliness(BmsQuoteDispatchDetailVo entity,List<SystemCodeEntity> timelinessCodeList){
		if(entity.getTimeliness()!=null){
			for(SystemCodeEntity sysCode:timelinessCodeList){
				if(entity.getTimeliness().equals(sysCode.getCode())){
					entity.setTimeliness(sysCode.getCodeName());
					break;
				}
			}
		}	
	}
	/**
	 * 设置地域类型名称
	 * @param list
	 */
	private void setAreaTypeCode(BmsQuoteDispatchDetailVo entity,List<SystemCodeEntity> areaTypeCodeList){
		if(entity.getAreaTypeCode()!=null){
			for(SystemCodeEntity sysCode:areaTypeCodeList){
				if(entity.getAreaTypeCode().equals(sysCode.getCode())){
					entity.setAreaTypeCode(sysCode.getCodeName());
					break;
				}
			}
		}	
	}
	/**
	 * 设置温度类别名称
	 * @param list
	 */
	private void setTemperatureTypeCode(BmsQuoteDispatchDetailVo entity,List<SystemCodeEntity> temperatureList){
		if(entity.getTemperatureTypeCode()!=null){
			for(SystemCodeEntity sysCode:temperatureList){
				if(entity.getTemperatureTypeCode().equals(sysCode.getCode())){
					entity.setTemperatureTypeCode(sysCode.getCodeName());
					break;
				}
			}
		}	
	}
	
	/**
	 * 设置省市区名称
	 * @param list
	 */
	private void setAddress(BmsQuoteDispatchDetailVo entity){
		entity.setProvinceName(entity.getProvinceId());
		entity.setCityName(entity.getCityId());
		entity.setAreaName(entity.getAreaId());
	}
	
	/**
	 * 调用MDM 设置仓配名称
	 * @param list
	 */
    private void setWareHouseName(BmsQuoteDispatchDetailVo entity,List<WarehouseVo> warehouseVoList){
    	if(entity.getStartWarehouseId()!=null){
			for(WarehouseVo vo:warehouseVoList){
				if(vo.getWarehouseid().equals(entity.getStartWarehouseId())){
					entity.setStartWarehouseName(vo.getWarehousename());
					break;
				}
			}
    	}	
    }
    
    /**
     * 定义导入模板类型
     */
	@Override
	protected BaseDataType getBaseDataType() {	
		return new DispatchQuoteCompareDataType();
	}
    /**
     * 定义匹配唯一列名
     */
	@Override
	protected List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("startWarehouseName");//仓库名称
		list.add("provinceName");//省份
		list.add("cityName");//市
		list.add("areaName");//区
		list.add("weightDown");
		list.add("weightUp");
		list.add("unitPrice");
		list.add("firstWeight");
		list.add("firstWeightPrice");
		list.add("continuedWeight");
		list.add("continuedPrice");
		list.add("timeliness");
		list.add("temperatureTypeCode");
		list.add("areaTypeCode");
		list.add("productCase");
		list.add("deliverid");
		return list;
	}
	/**
	 * 对比文件
	 * @param file
	 * @param parameter
	 * @return
	 */
	@FileResolver
	public Map<String,Object> importCompareFile(UploadFile file,Map<String,Object> parameter){
		return super.importFile(file, parameter);
	}
	
	/**
	 * 获取进度
	 * @return
	 */
	@Expose
	public int getProgress() {
		return this.getProgressStatus();
	}
	
	@Expose
	public void setProgress(){
		 this.setProgressStatus();
	}
	
	/**
	 * 导出报价
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@FileProvider
	public DownloadFile downLoadData(Map<String,Object> parameter) throws Exception{
		try{
			String path=getPath();
			String templateName=parameter.get("templateName").toString();
			HttpCommanExport commanExport=new HttpCommanExport(path);
			ExportDataVoEntity voEntity=new ExportDataVoEntity();
			voEntity.setTitleName(templateName);
			voEntity.setBaseType(new DispatchQuoteCompareDataType());
			voEntity.setDataList(getDataList(parameter));
			return commanExport.exportXFile(voEntity);
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			throw e;
		}
		
	}
	
	private List<Map<String,Object>> getDataList(Map<String,Object> parameter) throws Exception{
		List<BmsQuoteDispatchDetailVo> list = bmsQuoteDispatchDetailService.queryAllById(parameter);
		List<WarehouseVo> warehouseVoList=warehouseService.queryAllWarehouse();
		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		List<SystemCodeEntity> areaTypeCodeList = systemCodeService.findEnumList("DISPATCH_AREA_TYPE");
		List<SystemCodeEntity> timelinessCodeList = systemCodeService.findEnumList("DISPATCH_TIME");
		Map<String, String> deliverMap = getDeliverMap();
		for(BmsQuoteDispatchDetailVo entity:list){
			setWareHouseName(entity,warehouseVoList);//调用MDM 设置仓配名称
			setAddress(entity);//设置省市区名称
			setTemperatureTypeCode(entity,temperatureList);//设置温度类别名称
			setAreaTypeCode(entity,areaTypeCodeList);//设置地域类型名称
			setTimeliness(entity,timelinessCodeList);//设置时效
		}
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		for(BmsQuoteDispatchDetailVo entity:list){
			map=new HashMap<String,Object>();
			map.put("startWarehouseName", entity.getStartWarehouseName());
			map.put("provinceName", entity.getProvinceName());
			map.put("cityName", entity.getCityName());
			map.put("areaName", entity.getAreaName());
			map.put("weightDown", entity.getWeightDown());
			map.put("weightUp", entity.getWeightUp());
			map.put("unitPrice", entity.getUnitPrice());
			map.put("firstWeight", entity.getFirstWeight());
			map.put("firstWeightPrice", entity.getFirstWeightPrice());
			map.put("continuedWeight", entity.getContinuedWeight());
			map.put("continuedPrice", entity.getContinuedPrice());
			map.put("timeliness", entity.getTimeliness());
			map.put("temperatureTypeCode", entity.getTemperatureTypeCode());
			map.put("areaTypeCode", entity.getAreaTypeCode());
			map.put("productCase", entity.getProductCase());
			map.put("deliverid", deliverMap.get(entity.getDeliverid()));
			
			mapList.add(map);
		}
		return mapList;
	}
	
	private String getPath(){
		SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode("GLOABL_PARAM", "EXPORT_PAY_BILL");
		if(systemCodeEntity == null){
			throw new BizException("请在系统参数中配置文件上传路径,参数GLOABL_PARAM,EXPORT_PAY_BILL");
		}
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 获取宅配商信息
	 * @return
	 */
	private Map<String, String> getDeliverMap(){
		Map<String, String> map = new HashMap<String, String>();
		List<DeliverVo> list = deliverService.queryAllDeliver();
		if (null != list && list.size() > 0) {
			for (DeliverVo deliverVo : list) {
				map.put(deliverVo.getDeliverid(), deliverVo.getDelivername());
			}
		}
		
		return map;
	}

	/**
	 * 设置不用对比的列
	 */
	@Override
	protected List<String> getNoCompareProperty() {
		
		return null;
	}
}
