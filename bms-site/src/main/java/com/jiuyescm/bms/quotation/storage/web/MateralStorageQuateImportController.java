package com.jiuyescm.bms.quotation.storage.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
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
import com.jiuyescm.bms.quotation.storage.entity.PriceMaterialQuotationEntity;
import com.jiuyescm.bms.quotation.storage.service.IPriceMaterialQuotationService;
import com.jiuyescm.common.utils.upload.BaseDataType;
import com.jiuyescm.common.utils.upload.MaterialTemplateDataType;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.SystemCodeVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

/**
 *导入导出文件
 * @author Wuliangfeng
 *
 */
@Controller("materalStorageQuateImportController")
public class MateralStorageQuateImportController  extends HttpCommanImportCompare<PriceMaterialQuotationEntity,PriceMaterialQuotationEntity>{

	@Resource
	private ISystemCodeService systemCodeService; //业务类型
	@Resource
	private IPriceMaterialQuotationService  service;
	@Resource
	private IWarehouseService  warehouseService;
	@Resource
	private IBmsErrorLogInfoService bmsErrorLogInfoService;
	
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
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
			voEntity.setBaseType(new MaterialTemplateDataType());
			voEntity.setDataList(getDataList(parameter));
			return commanExport.exportFile(voEntity);
		}catch(Exception e){
			//写入日志
			bmsErrorLogInfoService.insertLog(this.getClass().getSimpleName(),Thread.currentThread().getStackTrace()[1].getMethodName(), "", e.toString());
			throw e;
		}
		
	}
	private void setwareHouseName(PriceMaterialQuotationEntity entity,List<WarehouseVo>  wareHouselist){
		if(entity.getWarehouseId()!=null){
			for(WarehouseVo vo:wareHouselist){
				if(entity.getWarehouseId().equals(vo.getWarehouseid())){
					entity.setWarehouseId(vo.getWarehousename());
					break;
				}
			}
		}
	}
	private List<Map<String,Object>> getDataList(Map<String,Object> parameter){
	
		List<PriceMaterialQuotationEntity> list=service.queryById(parameter);
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		for(PriceMaterialQuotationEntity entity:list){
			setwareHouseName(entity,wareHouselist);
		}
		Map<String,String> materialMap=getMaterailType();
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=null;
		for(PriceMaterialQuotationEntity entity:list){
			map=new HashMap<String,Object>();
			map.put("materialCode", entity.getMaterialCode());
			map.put("materialType", materialMap.get(entity.getMaterialType()));
			map.put("specName", entity.getSpecName());
			map.put("outsideDiameter", entity.getOutsideDiameter());
			map.put("innerDiameter", entity.getInnerDiameter());
			map.put("wallThickness", entity.getWallThickness());
			map.put("warehouseId", entity.getWarehouseId());
			map.put("unitPrice", entity.getUnitPrice());
			map.put("remark", entity.getRemark());
			map.put("creator", entity.getCreator());
			map.put("createTime", entity.getCreateTime());
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
	protected List<PriceMaterialQuotationEntity> getOrgList(
			Map<String, Object> parameter) {
		List<PriceMaterialQuotationEntity> list=service.queryById(parameter);
		//仓库信息
		List<WarehouseVo>  wareHouselist  = warehouseService.queryAllWarehouse();
		for(PriceMaterialQuotationEntity entity:list){
			setwareHouseName(entity,wareHouselist);
		}
		return list;
	}
	@Override
	protected BaseDataType getBaseDataType() {
		return new MaterialTemplateDataType();
	}
	@Override
	protected List<String> getKeyDataProperty() {
		List<String> list=new ArrayList<String>();
		list.add("warehouseId");
		list.add("materialCode");
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
	@Override
	protected List<String> getNoCompareProperty() {
		List<String> list=new ArrayList<String>();
		list.add("creator");
		list.add("createTime");
		list.add("lastModifier");
		list.add("lastModifyTime");
		list.add("id");
		return list;
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
	
	@DataProvider
	public Map<String, String> getMaterailType(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeVo> codeList = pubMaterialInfoService.findEnumList("");
		for (SystemCodeVo SystemCodeVo : codeList) {
			
			mapValue.put(SystemCodeVo.getCode(), SystemCodeVo.getCodeName());
		}		
		return mapValue;
	}
}
