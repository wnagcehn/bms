package com.jiuyescm.bms.base.dictionary.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.github.pagehelper.PageInfo;
import com.jiuyescm.bms.base.config.service.IBmsWarehouseConfigService;
import com.jiuyescm.bms.base.config.vo.BmsWarehouseConfigVo;
import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeTypeService;
import com.jiuyescm.bms.base.jywarehouse.web.BmsWarehouseVo;
import com.jiuyescm.cfm.common.JAppContext;
import com.jiuyescm.mdm.customer.api.IOmsCsrReasonService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.OmsCustComplaintsReasonVo;
import com.jiuyescm.mdm.customer.vo.SystemCodeVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;
/**
 * 
 * @author cjw
 * 
 */
@Controller("systemCodeController")
public class SystemCodeController {

	private static final Logger logger = Logger.getLogger(SystemCodeController.class.getName());

	@Resource
	private ISystemCodeService systemCodeService;
	
	@Resource
	private ISystemCodeTypeService systemCodeTypeService;
	
	@Resource
	private IWarehouseService  warehouseService;
		
	@Resource
	private IOmsCsrReasonService  reasonService;
	
	@Resource
	private IPubMaterialInfoService pubMaterialInfoService;
	@Autowired 
	private IBmsWarehouseConfigService bmsWarehouseConfigService;

	@DataProvider
	public SystemCodeEntity findById(Long id) throws Exception {
		SystemCodeEntity entity = null;
		entity = systemCodeService.findById(id);
		return entity;
	}

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void query(Page<SystemCodeEntity> page, Map<String, Object> param) {
		PageInfo<SystemCodeEntity> pageInfo = systemCodeService.query(param, page.getPageNo(), page.getPageSize());
		if (pageInfo != null) {
			page.setEntities(pageInfo.getList());
			page.setEntityCount((int) pageInfo.getTotal());
		}
	}
	
	/**
	 * 分页查询
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void queryTemType(Page<SystemCodeEntity> page, Map<String, Object> param) {

		if (param == null) {
			param = new HashMap<String, Object>();
		}
		param.put("typeCode", "TEMP_TYPE");

		List<SystemCodeEntity> codeList = systemCodeService.queryTempType(param);

		page.setEntities(codeList);
		page.setEntityCount(codeList.size());
		 
	}

	@DataResolver
	public void save(SystemCodeEntity entity) throws Exception{
		
		//entity.setCode(entity.getCode().toUpperCase());
		
		if (entity.getId() == null) { 
			entity.setCreateId(JAppContext.currentUserID());
			entity.setCreateDt(JAppContext.currentTimestamp());
			systemCodeService.save(entity);
		} else {
			entity.setUpdateId(JAppContext.currentUserID());
			entity.setUpdateDt(JAppContext.currentTimestamp());
			systemCodeService.update(entity);
		}
	}

	@DataResolver
	public void delete(SystemCodeEntity entity) {
		entity.setDeleteId(JAppContext.currentUserID());
		entity.setDeleteDt(JAppContext.currentTimestamp());
		systemCodeService.delete(entity.getId());
	}
	
	/**
	 * 枚举值
	 * @return
	 */
	@DataProvider
	public Map<String, String> getEnumList(String typeCode) {
		
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("ALL","全部");
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList(typeCode);
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		
		return mapValue;
	}
	
	
	@Expose
	public int getPartOutboundStatus(){
		return systemCodeService.getPartOutboundStatus();
	}
	
	@DataProvider
	public Map<String,String> getWarehouseList() throws Exception{
		
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
	public Map<String, String> getNewEnumList(String typeCode) {
				
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
	
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList(typeCode);
		
		mapValue.put("ALL","全部");
		
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		
		if(containStr(typeCode))
		{
			mapValue.remove("wh_value_add_subject");
			mapValue.remove("wh_material_use");
		}
		
		return mapValue;
	}
	
	@DataProvider
	public Map<String,String> getComplaintReasonList(){
		
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		
		Map<String, Object> condition = new HashMap<String,Object>();
		
		condition.put("parentid", "0");
		
		List<OmsCustComplaintsReasonVo> list = reasonService.getAllCsrReason(condition);
		
		mapValue.put("ALL","全部");
		for(OmsCustComplaintsReasonVo entity:list)
			mapValue.put(entity.getId().toString(), entity.getReason());
		
		return mapValue;
		
	}
	
	@DataProvider
	public Map<String, String> getNewEnumList2(String typeCode) {
				
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
	
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList(typeCode);
		
		mapValue.put("ALL","全部");
		
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			
			if(StringUtils.isNotEmpty(SystemCodeEntity.getExtattr1())){
				mapValue.put(SystemCodeEntity.getExtattr1(), SystemCodeEntity.getCodeName());
			}
		}
		
		
		return mapValue;
	}
	
	
	boolean containStr(String code){
		String str = "STORAGE_PRICE_SUBJECT";
		int index = str.indexOf(code);
		return index<0?false:true;
	} 
	
	
	/**
	 * 枚举值
	 * @return
	 */
	@DataProvider
	public Map<String, String> getEnumList3(String typeCode) {
		
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> tmscodels = systemCodeService.findEnumList(typeCode);
		for (SystemCodeEntity SystemCodeEntity : tmscodels) {
			
			mapValue.put(SystemCodeEntity.getCode(), SystemCodeEntity.getCodeName());
		}
		
		return mapValue;
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
	
	@DataProvider
	public Map<String,String> getIstB(){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		mapValue.put("1", "B2B");
		mapValue.put("0", "B2C");
		return mapValue;
	}
	
	@DataProvider
	public Map<String,String> getCarrierId(String typeCode){
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<SystemCodeEntity> codeList = systemCodeService.findEnumList(typeCode);
		for (SystemCodeEntity SystemCodeVo : codeList) {	
			mapValue.put(SystemCodeVo.getCode(), SystemCodeVo.getExtattr1());
		}		
		return mapValue;
	}
}
