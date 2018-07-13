package com.jiuyescm.bms.base.system;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyescm.bms.base.dictionary.entity.SystemCodeEntity;
import com.jiuyescm.bms.base.dictionary.service.ISystemCodeService;
import com.jiuyescm.exception.BizException;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

public class BaseController {

	@Resource
	private ISystemCodeService systemCodeService;
	@Autowired
	private IWarehouseService warehouseService;
	
	/**
	 * 获取应收业务数据导出的文件路径
	 * @return
	 */
	public String getBizReceiveExportPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_RECEIVE_BIZ");
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 获取应付业务数据导出的文件路径
	 * @return
	 */
	public String getBizPayExportPath(){
		SystemCodeEntity systemCodeEntity = getSystemCode("GLOABL_PARAM", "EXPORT_PAY_BIZ");
		return systemCodeEntity.getExtattr1();
	}
	
	/**
	 * 获取系统参数对象
	 * @param typeCode
	 * @param code
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public SystemCodeEntity getSystemCode(String typeCode, String code){
		if (StringUtils.isNotEmpty(typeCode) && StringUtils.isNotEmpty(code)) {
			SystemCodeEntity systemCodeEntity = systemCodeService.getSystemCode(typeCode, code);
			if(systemCodeEntity == null){
				throw new BizException("请在系统参数中配置文件上传路径,参数" + typeCode + "," + code);
			}
			return systemCodeEntity;
		}
		return null;
	}
	
	/**
	 * 获取温度类型
	 * @return
	 */
	public Map<String,String> getTemperature(){
		Map<String,String> map = new LinkedHashMap<String,String>();
		List<SystemCodeEntity> systemCodeList = systemCodeService.findEnumList("TEMPERATURE_TYPE");
		if(systemCodeList != null && systemCodeList.size() > 0){
			for(SystemCodeEntity entity : systemCodeList){
				map.put(entity.getCode(), entity.getCodeName());
			}
		}
		return map;
	}
	
	/**
	 * 获取仓库
	 * @return
	 */
	public Map<String,String> getWarehouse(){
		Map<String,String> map = new LinkedHashMap<String,String>();
		//仓库信息
		List<WarehouseVo> warehouseVos = warehouseService.queryAllWarehouse();
		for (WarehouseVo warehouseVo : warehouseVos) {
			map.put(warehouseVo.getWarehouseid(), warehouseVo.getWarehousename());
		}
		return map;
	}
	
	
}
