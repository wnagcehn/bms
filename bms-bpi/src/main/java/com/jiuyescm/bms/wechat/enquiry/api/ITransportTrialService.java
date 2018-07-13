package com.jiuyescm.bms.wechat.enquiry.api;

import java.util.Map;

import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;

/**
 * 运输微信试算接口
 * @author yangss
 *
 */
public interface ITransportTrialService {

	//试算-城际
	CalcuResultVo trialCJ(Map<String, String> params);
	
	//试算-同城
	CalcuResultVo trialTC(Map<String, String> params);
	
	//试算-电商专列
	CalcuResultVo trialDSZL(Map<String, String> params);
	
	//试算-航鲜达
	CalcuResultVo trialHXD(Map<String, String> params);
}
