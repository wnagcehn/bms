package com.jiuyescm.bms.quotation.out.dispatch.web;

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
import com.jiuyescm.bms.quotation.out.dispatch.entity.vo.PriceOutMainDispatchEntity;
import com.jiuyescm.bms.quotation.out.dispatch.service.IPriceOutDispatchService;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.DeliverTemplateDataType;
import com.jiuyescm.common.utils.upload.DispatchImportCompareDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 * 配送报价
 * @author Wuliangfeng
 *
 */
@Controller("outDispatchImportComparePR")
public class OutDispatchImportComparePR  extends HttpCommanImportCompare<PriceOutMainDispatchEntity,PriceOutMainDispatchEntity>{
	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	@Autowired
	private IWarehouseService warehouseService;
	
	@Resource
	private IPriceOutDispatchService priceOutDispatchService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
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
			voEntity.setBaseType(new DispatchImportCompareDataType());
			voEntity.setDataList(getDataList(parameter));
			return commanExport.exportFile(voEntity);
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			
			throw e;
		}
		
	}
	/**
	 * 设置仓库名称
	 * @param entity
	 * @param warehouseVoList
	 */
	private void setWareHouseName(PriceOutMainDispatchEntity entity,List<WarehouseVo> warehouseVoList){
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
	 * 设置省市区名称
	 * @param list
	 */
	private void setAddress(PriceOutMainDispatchEntity entity){
		entity.setProvinceName(entity.getProvinceId());
		entity.setCityName(entity.getCityId());
		entity.setAreaName(entity.getAreaId());
	}
	/**
	 * 设置时效
	 * @param list
	 */
	private void setTimeliness(PriceOutMainDispatchEntity entity,List<SystemCodeEntity> timelinessCodeList){
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
	private void setAreaTypeCode(PriceOutMainDispatchEntity entity,List<SystemCodeEntity> areaTypeCodeList){
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
	private void setTemperatureTypeCode(PriceOutMainDispatchEntity entity,List<SystemCodeEntity> temperatureList){
		if(entity.getTemperatureTypeCode()!=null){
			for(SystemCodeEntity sysCode:temperatureList){
				if(entity.getTemperatureTypeCode().equals(sysCode.getCode())){
					entity.setTemperatureTypeCode(sysCode.getCodeName());
					break;
				}
			}
		}	
	}
	private List<Map<String,Object>> getDataList(Map<String,Object> parameter){
		List<PriceOutMainDispatchEntity> list=priceOutDispatchService.queryAllById(parameter);
		List<WarehouseVo> warehouseVoList=warehouseService.queryAllWarehouse();
		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		List<SystemCodeEntity> areaTypeCodeList = systemCodeService.findEnumList("DISPATCH_AREA_TYPE");
		List<SystemCodeEntity> timelinessCodeList = systemCodeService.findEnumList("DISPATCH_TIME");
		for(PriceOutMainDispatchEntity entity:list){
			setWareHouseName(entity,warehouseVoList);//调用MDM 设置仓配名称
			setAddress(entity);//设置省市区名称
			setTemperatureTypeCode(entity,temperatureList);//设置温度类别名称
			setAreaTypeCode(entity,areaTypeCodeList);//设置地域类型名称
			setTimeliness(entity,timelinessCodeList);//设置时效
		}
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		for(PriceOutMainDispatchEntity entity:list){
			map=new HashMap<String,Object>();
			map.put("startWarehouseName", entity.getStartWarehouseName());
			map.put("areaTypeCode", entity.getAreaTypeCode());
			map.put("provinceName", entity.getProvinceName());
			map.put("cityName", entity.getCityName());
			map.put("areaName", entity.getAreaName());
			map.put("timeliness", entity.getTimeliness());
			map.put("weightLimit", entity.getWeightLimit());
			map.put("unitPrice", entity.getUnitPrice());
			map.put("firstWeight", entity.getFirstWeight());
			map.put("firstWeightPrice", entity.getFirstWeightPrice());
			map.put("continuedWeight", entity.getContinuedWeight());
			map.put("continuedPrice", entity.getContinuedPrice());
			map.put("temperatureTypeCode", entity.getTemperatureTypeCode());
			map.put("extra1", entity.getExtra1());
			map.put("extra2", entity.getExtra2());
			map.put("extra3", entity.getExtra3());
			map.put("extra4", entity.getExtra3());
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
	@Override
	protected List<PriceOutMainDispatchEntity> getOrgList(
			Map<String, Object> parameter) {
		List<PriceOutMainDispatchEntity> list=priceOutDispatchService.queryAllById(parameter);
		List<WarehouseVo> warehouseVoList=warehouseService.queryAllWarehouse();
		List<SystemCodeEntity> temperatureList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		List<SystemCodeEntity> areaTypeCodeList = systemCodeService.findEnumList("DISPATCH_AREA_TYPE");
		List<SystemCodeEntity> timelinessCodeList = systemCodeService.findEnumList("DISPATCH_TIME");
		for(PriceOutMainDispatchEntity entity:list){
			setWareHouseName(entity,warehouseVoList);//调用MDM 设置仓配名称
			setAddress(entity);//设置省市区名称
			setTemperatureTypeCode(entity,temperatureList);//设置温度类别名称
			setAreaTypeCode(entity,areaTypeCodeList);//设置地域类型名称
			setTimeliness(entity,timelinessCodeList);//设置时效
		}
		return list;
	}
	@Override
	protected BaseDataType getBaseDataType() {
		return new DeliverTemplateDataType();
	}
	@Override
	protected List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("startWarehouseName");//仓库名称
		list.add("provinceName");//省份
		list.add("cityName");//市
		list.add("areaName");//区
		list.add("temperatureTypeCode");//温度类型
		list.add("areaTypeCode");//地域类型
		list.add("timeliness");//时效
		return list;
	}
	/**
	 * 不用比较的属性
	 */
	@Override
	protected List<String> getNoCompareProperty() {
		List<String> list=new ArrayList<String>();
		list.add("creator");//创建人
		list.add("createTime");//创建时间
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
}
