package com.jiuyescm.bms.consumer.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.jiuyescm.bms.asyn.service.IBmsFileAsynTaskService;
import com.jiuyescm.bms.asyn.vo.BmsFileAsynTaskVo;
import com.jiuyescm.mdm.customer.api.ICustomerService;
import com.jiuyescm.mdm.customer.api.IPubMaterialInfoService;
import com.jiuyescm.mdm.customer.vo.CustomerVo;
import com.jiuyescm.mdm.customer.vo.PubMaterialInfoVo;
import com.jiuyescm.mdm.warehouse.api.IWarehouseService;
import com.jiuyescm.mdm.warehouse.vo.WarehouseVo;

@Component("bmsMaterialImportTaskCommon")
public class BmsMaterialImportTask {
	
	private static final Logger logger = LoggerFactory.getLogger(BmsMaterialImportTask.class);
	
	@Autowired private IBmsFileAsynTaskService bmsFileAsynTaskService;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private ICustomerService customerService;
	@Autowired private IPubMaterialInfoService pubMaterialInfoService;
	
	public void setTaskProcess(String taskId,Integer process){
		BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskId, process,null, null, null, null, null, null);
		bmsFileAsynTaskService.update(updateEntity);
	}
	
	public void setTaskStatus(String taskId,Integer process,String status){
		BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskId, process,status, null, null, null, null, null);
		bmsFileAsynTaskService.update(updateEntity);
	}
	
	public void setTaskStatus(String taskId,Integer process,String status,String msg){
		BmsFileAsynTaskVo updateEntity = new BmsFileAsynTaskVo(taskId, process,status, null, null, null, null, msg);
		bmsFileAsynTaskService.update(updateEntity);
	}
	
	/**
	 * 表头校验
	 * @param headColumn
	 * @param str
	 * @return
	 */
	public boolean checkTitle(Map<String, String> headColumn, String[] str) {
		if(headColumn == null){
			return false;
		}
		if(headColumn.size() < str.length){
			 return false;
		}
		for (String s : str) {
			if(!headColumn.containsKey(s)){
				return false;
			}
		} 
		return true;
	}
	
	/**
	 * 查询仓库名称-仓库映射
	 * @return
	 */
	public Map<String, WarehouseVo> queryAllWarehouse(){
		List<WarehouseVo> list = warehouseService.queryAllWarehouse();
		Map<String, WarehouseVo> whMap = new HashMap<String, WarehouseVo>();
		for (WarehouseVo vo : list) {
			if(!StringUtils.isBlank(vo.getWarehousename())){
				whMap.put(vo.getWarehousename(), vo);
			}
		}
		return whMap;
	}
	
	/**
	 * 查询商家全称-商家映射
	 * @return
	 */
	public Map<String, CustomerVo> queryAllCustomer(){
		List<CustomerVo> list = customerService.queryAll();
		Map<String, CustomerVo> custMap = new HashMap<String, CustomerVo>();
		for (CustomerVo vo : list) {
			if(!StringUtils.isBlank(vo.getCustomername())){
				custMap.put(vo.getCustomername(), vo);
			}
		}
		return custMap;
	}
	
	 /**
     * 查询商家ID-商家映射
     * @return
     */
    public Map<String, CustomerVo> queryAllCustomerId(){
        List<CustomerVo> list = customerService.queryAll();
        Map<String, CustomerVo> custMap = new HashMap<String, CustomerVo>();
        for (CustomerVo vo : list) {
            if(!StringUtils.isBlank(vo.getCustomerid())){
                custMap.put(vo.getCustomerid(), vo);
            }
        }
        return custMap;
    }
	
	/**
	 * 查询耗材编码-耗材映射
	 * @return
	 */
	public Map<String,PubMaterialInfoVo> queryAllMaterial(){
		Map<String,Object> condition=Maps.newHashMap();
		List<PubMaterialInfoVo> tmscodels = pubMaterialInfoService.queryList(condition);
		Map<String,PubMaterialInfoVo> map=Maps.newLinkedHashMap();
		for(PubMaterialInfoVo materialVo:tmscodels){
			if(!StringUtils.isBlank(materialVo.getBarcode())){
				map.put(materialVo.getBarcode().trim(),materialVo);
			}
		}
		return map;
	}
	
}
