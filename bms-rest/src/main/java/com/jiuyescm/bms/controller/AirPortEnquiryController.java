package com.jiuyescm.bms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyescm.bms.common.constants.WechatEnquiryConstant;
import com.jiuyescm.bms.rest.RequestVo;
import com.jiuyescm.bms.wechat.enquiry.api.IAirPortEnquiryService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;
import com.jiuyescm.bms.wechat.enquiry.utils.ReqParamVlidateUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.AirPortVo;
import com.jiuyescm.framework.restful.support.controller.BaseController;
import com.jiuyescm.framework.restful.support.exception.InvalidRequestException;
import com.jiuyescm.framework.restful.support.response.Response;

/**
 * 机场查询接口
 * @author yangss
 *
 */
@RestController
@RequestMapping(value = { "/wechat/{v}" }, produces = { "application/json;charset=UTF-8" })
public class AirPortEnquiryController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(AirPortEnquiryController.class.getName());
	
	@Autowired
	private IAirPortEnquiryService airPortEnquiryService;
	
	/**
	 * 机场
	 * @return
	 */
	@RequestMapping(value = "/airports",method = RequestMethod.POST)
	@ResponseBody
	public Response<List<AirPortVo>> queryAirPort(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkProvinceCityParam(requestVo)) {
			throw new InvalidRequestException("INVALID_QUO_TC_REGION",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<List<AirPortVo>> res = Response.createResponse(null);
		try {
			res.setItems(airPortEnquiryService.queryAirPort(requestVo.getParams()));
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
}
