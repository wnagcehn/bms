package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.List;
import java.util.Map;

import com.jiuyescm.bms.wechat.enquiry.vo.AirPortVo;

public interface IAirPortEnquiryService {

	List<AirPortVo> queryAirPort(Map<String, String> reqParam);
}
