package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.wechat.enquiry.vo.InitResponseVO;
import com.jiuyescm.bms.wechat.enquiry.vo.ProvinceCityVo;


/**
 * 省市查询接口
 * @author yangss
 */
public interface IProvinceCityEnquiryService {
	
	InitResponseVO queryInit(String provinceName, String cityName);
	
	List<ProvinceCityVo> queryFromCitys(String bizType);
	
	List<Map<String, Object>> queryToCityCJ(Map<String, String> params);
	
	List<Map<String, Object>> queryToCityTC(Map<String, String> params);
	
}
