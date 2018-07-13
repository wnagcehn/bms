package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.pub.origincity.entity.PubOriginCityElecWarehouseEntity;
import com.jiuyescm.bms.pub.origincity.repository.IPubOriginCityElecWarehouseRepository;
import com.jiuyescm.bms.wechat.enquiry.api.IElecWarehouseEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.vo.ElecWarehouseVo;
import com.jiuyescm.bms.wechat.enquiry.vo.WarehouseVo;

@Service("elecWarehouseService")
public class ElecWarehouseEnquiryServiceImpl implements IElecWarehouseEnquiryService{

	@Autowired
	private IPubOriginCityElecWarehouseRepository originCityElecWarehouseRepository;
	
	/**
	 * 查询电商名称
	 */
	@Override
	public List<ElecWarehouseVo> queryElecName(Map<String, String> reqParam) {
		List<ElecWarehouseVo> eleList = new ArrayList<>();
		
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("provinceName", fromProvince);
		param.put("cityName", fromCity);
		List<PubOriginCityElecWarehouseEntity> ewarehouseList = originCityElecWarehouseRepository.queryElecName(param);
		if (null != ewarehouseList && ewarehouseList.size() > 0) {
			ElecWarehouseVo elecWarehouseVo = null;
			for (PubOriginCityElecWarehouseEntity entity : ewarehouseList) {
				elecWarehouseVo = new ElecWarehouseVo(entity.getElecBizCode(),
						entity.getElecBizName(), 
						entity.getLogo());
				eleList.add(elecWarehouseVo);
			}
		}
		return eleList;
	}

	/**
	 * 查询电商仓库名称
	 */
	@Override
	public List<WarehouseVo> queryElecWarehouse(Map<String, String> reqParam) {
		List<WarehouseVo> eleList = new ArrayList<>();
		
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		String eleBizName = reqParam.get("eleBizName");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("provinceName", fromProvince);
		param.put("cityName", fromCity);
		param.put("elecBizName", eleBizName);
		List<PubOriginCityElecWarehouseEntity> ewarehouseList = originCityElecWarehouseRepository.query(param);
		if (null != ewarehouseList && ewarehouseList.size() > 0) {
			WarehouseVo warehouseVo = null;
			for (PubOriginCityElecWarehouseEntity entity : ewarehouseList) {
				warehouseVo = new WarehouseVo(entity.getWarehouseCode(), entity.getWarehouseName());
				eleList.add(warehouseVo);
			}
		}
		return eleList;
	}

}
