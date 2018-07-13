package com.jiuyescm.bms.quotation.transport.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.bstek.dorado.annotation.DataProvider;
import com.jiuyescm.bms.base.address.entity.PubAddressEntity;
import com.jiuyescm.bms.base.address.service.IPubAddressService;
import com.jiuyescm.bms.common.log.service.IBmsErrorLogInfoService;

@Controller("transportCityController")
public class TransportCityController {

	@Resource 
	private IPubAddressService pubAddressService;
	
	
	/**
	 * 查询省份对应的市
	 * @param parameter
	 * @return
	 */
	@DataProvider
	public Map<String, String> queryAllCity(Map<String, Object> parameter) {
		Map<String, String> mapValue = new LinkedHashMap<String, String>();
		List<PubAddressEntity> provinceList = pubAddressService.queryAllCityForEnum(parameter);
		if (provinceList != null && provinceList.size()>0) {
			for (PubAddressEntity pubAddress : provinceList){
				mapValue.put(String.valueOf(pubAddress.getId()), pubAddress.getAreaName());
			}
		} 
		return mapValue;
		
	}
}
