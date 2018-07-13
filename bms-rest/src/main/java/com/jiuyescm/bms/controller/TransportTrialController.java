package com.jiuyescm.bms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyescm.bms.base.calcu.vo.CalcuResultVo;
import com.jiuyescm.bms.common.constants.WechatEnquiryConstant;
import com.jiuyescm.bms.rest.RequestVo;
import com.jiuyescm.bms.wechat.enquiry.api.ITransportTrialService;
import com.jiuyescm.bms.wechat.enquiry.constant.WechatMessageConstant;
import com.jiuyescm.bms.wechat.enquiry.utils.FormatUtil;
import com.jiuyescm.bms.wechat.enquiry.utils.ReqParamVlidateUtil;
import com.jiuyescm.bms.wechat.enquiry.vo.TransportTrialVo;
import com.jiuyescm.framework.restful.support.controller.BaseController;
import com.jiuyescm.framework.restful.support.exception.InvalidRequestException;
import com.jiuyescm.framework.restful.support.response.Response;

/**
 * 运输微信试算
 * @author yangss
 *
 */
@RestController
@RequestMapping(value = { "/wechat/{v}" }, produces = { "application/json;charset=UTF-8" })
public class TransportTrialController extends BaseController{

	private static final Logger logger = LoggerFactory.getLogger(TransportTrialController.class.getName());
	
	@Autowired
	private ITransportTrialService transportTrialService;
	
	/**
	 * 城际
	 * @return
	 */
	@RequestMapping(value = "/trials/cj",method = RequestMethod.POST)
	@ResponseBody
	public Response<TransportTrialVo> trialCj(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkTrialCjReqParam(requestVo)) {
			throw new InvalidRequestException("INVALID_TRIAL_CJ",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<TransportTrialVo> res = Response.createResponse(null);
		try {
			CalcuResultVo calculResutlVo = transportTrialService.trialCJ(requestVo.getParams());
			if ("succ".equals(calculResutlVo.getSuccess())) {
				TransportTrialVo trialVo = new TransportTrialVo();
				trialVo.setPrice(FormatUtil.formatPrice(calculResutlVo.getPrice()));
				trialVo.setUnitPrice(FormatUtil.formatPrice(calculResutlVo.getUnitPrice()));
				res.setItems(trialVo);
			}else {
				res.setCode(WechatMessageConstant.FAIL);
				res.setMessage(calculResutlVo.getMsg());
			}
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 同城
	 * @return
	 */
	@RequestMapping(value = "/trials/tc",method = RequestMethod.POST)
	@ResponseBody
	public Response<TransportTrialVo> trialTc(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkTrialTcReqParam(requestVo)) {
			throw new InvalidRequestException("INVALID_TRIAL_TC",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<TransportTrialVo> res = Response.createResponse(null);
		try {
			CalcuResultVo calculResutlVo = transportTrialService.trialTC(requestVo.getParams());
			if ("succ".equals(calculResutlVo.getSuccess())) {
				TransportTrialVo trialVo = new TransportTrialVo();
				trialVo.setPrice(FormatUtil.formatPrice(calculResutlVo.getPrice()));
				trialVo.setUnitPrice(FormatUtil.formatPrice(calculResutlVo.getUnitPrice()));
				res.setItems(trialVo);
			}else {
				res.setCode(WechatMessageConstant.FAIL);
				res.setMessage(calculResutlVo.getMsg());
			}
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 电商专列
	 * @return
	 */
	@RequestMapping(value = "/trials/dszl",method = RequestMethod.POST)
	@ResponseBody
	public Response<TransportTrialVo> trialDszl(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkTrialDszlReqParam(requestVo)) {
			throw new InvalidRequestException("INVALID_TRIAL_DSZL",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<TransportTrialVo> res = Response.createResponse(null);
		try {
			CalcuResultVo calculResutlVo = transportTrialService.trialDSZL(requestVo.getParams());
			if ("succ".equals(calculResutlVo.getSuccess())) {
				TransportTrialVo trialVo = new TransportTrialVo();
				trialVo.setPrice(FormatUtil.formatPrice(calculResutlVo.getPrice()));
				trialVo.setUnitPrice(FormatUtil.formatPrice(calculResutlVo.getUnitPrice()));
				res.setItems(trialVo);
			}else {
				res.setCode(WechatMessageConstant.FAIL);
				res.setMessage(calculResutlVo.getMsg());
			}
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
	
	/**
	 * 航鲜达
	 * @return
	 */
	@RequestMapping(value = "/trials/hxd",method = RequestMethod.POST)
	@ResponseBody
	public Response<TransportTrialVo> trialHxd(@RequestBody(required=true) RequestVo requestVo) {
		//入参校验
		if (!ReqParamVlidateUtil.checkTrialHxdReqParam(requestVo)) {
			throw new InvalidRequestException("INVALID_TRIAL_HXD",WechatEnquiryConstant.ENQUIRY_PARAM_ERR_MSG);
		}
		Response<TransportTrialVo> res = Response.createResponse(null);
		try {
			CalcuResultVo calculResutlVo = transportTrialService.trialHXD(requestVo.getParams());
			if ("succ".equals(calculResutlVo.getSuccess())) {
				TransportTrialVo trialVo = new TransportTrialVo();
				trialVo.setPrice(FormatUtil.formatPrice(calculResutlVo.getPrice()));
				trialVo.setUnitPrice(FormatUtil.formatPrice(calculResutlVo.getUnitPrice()));
				res.setItems(trialVo);
			}else {
				res.setCode(WechatMessageConstant.FAIL);
				res.setMessage(calculResutlVo.getMsg());
			}
		} catch (Exception e) {
			logger.error(WechatEnquiryConstant.SYSTEM_ERR_MSG, e);
			res.setCode(WechatMessageConstant.FAIL);
			res.setMessage(WechatEnquiryConstant.SYSTEM_ERR_MSG);
		}
		return res;
	}
}
