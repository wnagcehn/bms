package com.jiuyescm.bms.foreign.wechat.enquiry.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyescm.bms.base.airport.entity.PubAirportEntity;
import com.jiuyescm.bms.base.airport.repository.IPubAirportRepository;
import com.jiuyescm.bms.wechat.enquiry.api.IAirPortEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.vo.AirPortVo;

@Service("airPortEnquiryService")
public class AirPortEnquiryServiceImpl implements IAirPortEnquiryService{

	@Autowired
	private IPubAirportRepository pubAirportRepository;
	
	@Override
	public List<AirPortVo> queryAirPort(Map<String, String> reqParam) {
		List<AirPortVo> airPortList = null;
		String fromProvince = reqParam.get("fromProvince");
		String fromCity = reqParam.get("fromCity");
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("province", fromProvince);
		condition.put("city", fromCity);
		List<PubAirportEntity> airportList = pubAirportRepository.query(condition);
		AirPortVo airPortVo = null;
		if (null != airportList && airportList.size() > 0) {
			airPortList = new ArrayList<AirPortVo>();
			for (PubAirportEntity airportEntity : airportList) {
				airPortVo = new AirPortVo(airportEntity.getAirportName());
				airPortList.add(airPortVo);
			}
		}
		return airPortList;
	}

}
